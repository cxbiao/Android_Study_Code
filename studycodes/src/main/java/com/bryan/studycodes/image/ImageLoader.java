package com.bryan.studycodes.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by bryan on 2015-11-29.
 * 图片加载 可以是newwork，file,provider,asssets
 * "http://www.baidu.com/ffd.jpg";
 * "file:///mnt/sdcard/abc.jpg";
 * "content://.....";
 * "assets://tangyan.jpg";
 */
public class ImageLoader  {

    private static final String TAG = "ImageLoader";

    public static final int MESSAGE_POST_RESULT=1;

    private static final int CPU_COUNT=Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE=CPU_COUNT+1;
    private static final int MAXIMUM_POOL_SIZE=CPU_COUNT*2+1;
    private static final long KEEP_ALIVE=10L;

    private static final long DISK_CACHE_SIZE=1024*1024*50;   //磁盘缓存最大50M
    private static final int IO_BUFFER_SIZE=8*1024;
    private static final int DISK_CACHE_INDEX=0;
    private boolean mIsDiskLruCacheCreated=false;
    private static  ImageLoader instance;

    private static final ThreadFactory sThreadFactory=new ThreadFactory() {
        private final AtomicInteger mCount=new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r,"ImageLoader#"+mCount.getAndIncrement());
        }
    };

    public static final Executor THREAD_POOL_EXECUTOR=new ThreadPoolExecutor(
            CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),sThreadFactory
    );

    private Handler mMainHandler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result= (LoaderResult) msg.obj;
            ImageView imageView=result.imageView;
            String url= (String) imageView.getTag();
            if(url.equals(result.url)){
                imageView.setImageBitmap(result.bitmap);
            }else{
                Log.w(TAG, "set image bitmamp,but url has changed,ignored!");
            }
        }
    };


    private Context mContext;
    private ImageResizer mImageResizer=new ImageResizer();
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;

    private ImageLoader(Context context){
        mContext=context.getApplicationContext();
        int maxMemory= (int) (Runtime.getRuntime().maxMemory()/1024);  //统一KB单位
        int cacheSize=maxMemory/8;
        mMemoryCache=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight()/1024;
            }
        };

        File diskCacheDir=getDiskCacheDir(mContext,"bitmap");
        if(!diskCacheDir.exists()){
            diskCacheDir.mkdirs();
        }
        if(getUsesSpace(diskCacheDir)>DISK_CACHE_SIZE){
            try {
                mDiskLruCache=mDiskLruCache.open(diskCacheDir,1,1,DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  static ImageLoader getInstance(Context context){
        if(instance==null){
            synchronized (ImageLoader.class){
                if(instance==null) instance=new ImageLoader(context);
            }
        }
        return  instance;
    }



    private void addBitmapToMemCache(String key,Bitmap bitmap){
        if(getBitmapFromMemCache(key)==null){
            mMemoryCache.put(key,bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    public void bindBitmap(String uri,ImageView imageView,int resId){
        ImageSize imageSize = getImageViewWidth(imageView);
        int reqWidth = imageSize.width;
        int reqHeight = imageSize.height;
        bindBitmap(uri, imageView, resId, reqWidth, reqHeight);
    }

    public void bindBitmap(final String uri, final ImageView imageView, int resId,final int reqWidth, final int reqHeight){
        imageView.setTag(uri);
        Bitmap bitmap=loadBitmapFromMemCache(uri);
        if(bitmap!=null){
            Log.d(TAG, "loadBitmapFromMemCache,uri:" + uri);
            imageView.setImageBitmap(bitmap);
            return;
        }
        imageView.setImageResource(resId);
        Runnable loadBitmapTask=new Runnable() {
            @Override
            public void run() {
                 Bitmap bitmap=loadBitmap(uri,reqWidth,reqHeight);
                if(bitmap!=null){
                    LoaderResult result=new LoaderResult(imageView,uri,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    public Bitmap loadBitmap(String uri,int reqWidth,int reqHeight){
        Bitmap bitmap=loadBitmapFromMemCache(uri);
        if (bitmap!=null){
            Log.d(TAG, "loadBitmapFromMemCache,uri:" + uri);
            return bitmap;
        }
        boolean isNetImg=uri.startsWith("http");
        try {
            if(!isNetImg){
                Log.d(TAG, "loadBitmapFromLocal,uri:" + uri);
                return loadBitmapFromLocal(uri,reqWidth,reqHeight);
            }
            bitmap=loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if(bitmap!=null){
                Log.d(TAG, "loadBitmapFromDisk,uri:"+uri);
                return bitmap;
            }
            bitmap=loadBitmapFromHttp(uri,reqWidth,reqHeight);
            Log.d(TAG, "loadBitmapFromHttp,uri:"+uri);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(bitmap==null && !mIsDiskLruCacheCreated && isNetImg){
            Log.w(TAG, "encounter error,DiskLruCache is not created.");
            bitmap=downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromMemCache(String url){
        String key=hashKey(url);
        Bitmap bitmap=getBitmapFromMemCache(key);
        return bitmap;
    }

    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int reqHeight)throws IOException{
        if(Looper.myLooper()==Looper.getMainLooper()){
            throw  new RuntimeException("can not visit network from UI Thread.");
        }
        if(mDiskLruCache==null){
            return null;
        }
        String key=hashKey(url);
        DiskLruCache.Editor editor=mDiskLruCache.edit(key);
        if(editor!=null){
            OutputStream outputStream=editor.newOutputStream(DISK_CACHE_INDEX);
            if(downloadUrlToStream(url,outputStream)){
                editor.commit();
            }else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,int reqHeight) throws IOException{
        if(Looper.myLooper()==Looper.getMainLooper()){
            Log.w(TAG, "load bitmap from UI Thread,it's  not recommended!" );
        }
        if(mDiskLruCache==null){
            return null;
        }
        Bitmap bitmap=null;
        String key=hashKey(url);
        DiskLruCache.Snapshot snapShot=mDiskLruCache.get(key);
        if (snapShot!=null){
            FileInputStream fis= (FileInputStream) snapShot.getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fd=fis.getFD();
            bitmap=getDecodeBitmap(url,fd,reqWidth,reqHeight);
        }
        return bitmap;
    }

    private Bitmap loadBitmapFromLocal(String uri,int reqWidth,int reqHeight )  throws IOException{
        if(Looper.myLooper()==Looper.getMainLooper()){
            Log.w(TAG, "load bitmap from UI Thread,it's  not recommended!" );
        }
        Bitmap bitmap=null;
        String key=hashKey(uri);
        String file=null;
        FileInputStream fis=null;
        FileDescriptor fd=null;
        if(uri.startsWith("file")){
            file=uri.substring(uri.indexOf("/")+2);
            fis=new FileInputStream(new File(file));
            fd=fis.getFD();
        }else if(uri.startsWith("content")){
            file=getRealPath(mContext,Uri.parse(uri));
            fis=new FileInputStream(new File(file));
            fd=fis.getFD();
        }else if(uri.startsWith("assets")){
             fis=new FileInputStream(getAssetFile(uri));
             fd=fis.getFD();

        }else {
            return null;
        }
        bitmap=getDecodeBitmap(uri,fd,reqWidth,reqHeight);
        return bitmap;
    }


    private File getAssetFile(String uri) throws IOException{
        String file=uri.substring(uri.indexOf("/") + 2);
        String key=hashKey(uri);
        File cache=new File(mContext.getExternalCacheDir()+"/"+key);
        if(cache.exists()){
            return cache;
        }
        InputStream is=mContext.getAssets().open(file);
        byte[] buf=new byte[1024];
        int len;
        BufferedInputStream bis=new BufferedInputStream(is);
        BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(cache));
        while ((len=bis.read(buf))!=-1){
            bos.write(buf,0,len);
        }
        bos.close();
        bis.close();
        return cache;


    }

    private Bitmap getDecodeBitmap(String uri,FileDescriptor fd,int reqWidth,int reqHeight ) throws IOException{
        Bitmap bitmap=null;
        String key=hashKey(uri);
        bitmap=mImageResizer.decodeSampleBitmapFromFileDes(fd, reqWidth, reqHeight);
        if(bitmap!=null){
            addBitmapToMemCache(key,bitmap);
        }
        return bitmap;
    }

    public boolean downloadUrlToStream(String urlString,OutputStream outputStream){
        HttpURLConnection urlConnection=null;
        BufferedOutputStream out=null;
        BufferedInputStream in=null;
        try {
            URL url=new URL(urlString);
            urlConnection= (HttpURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            out=new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            int b;
            while ((b=in.read())!=-1){
                out.write(b);
            }
            return true;

        }catch (Exception e){
            Log.e(TAG, "downloadBitmap faild."+e);
        }finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            Util.closeQuietly(out);
            Util.closeQuietly(in);
        }
        return false;

    }

    private Bitmap downloadBitmapFromUrl(String urlString){
        Bitmap bitmap=null;
        HttpURLConnection urlConnectioon=null;
        BufferedInputStream in=null;
        try {
            URL  url=new URL(urlString);
            urlConnectioon= (HttpURLConnection) url.openConnection();
            in=new BufferedInputStream(urlConnectioon.getInputStream(),IO_BUFFER_SIZE);
            bitmap= BitmapFactory.decodeStream(in);

        }catch (Exception e){
            Log.e(TAG, "Error in downloadBitmap:"+e);

        }finally {
            if(urlConnectioon!=null){
                urlConnectioon.disconnect();
            }
           Util.closeQuietly(in);
        }
        return bitmap;

    }


    private String hashKey(String url){
        String cacheKey="";
        MessageDigest digest= null;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey=bytesToHex(digest.digest());

        } catch (Exception e) {
           cacheKey=String.valueOf(url.hashCode());
        }
       return cacheKey;
    }

    private String bytesToHex(byte[] bytes){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i <bytes.length ; i++) {
            String hex=Integer.toHexString(0xFF & bytes[i]);
            if (hex.length()==1){
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();

    }

    public File getDiskCacheDir(Context context,String uniqueName){
        String cachePath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            cachePath=context.getExternalCacheDir().getAbsolutePath();
        }else {
            cachePath=context.getCacheDir().getAbsolutePath();

        }
        return new File(cachePath+File.separator+uniqueName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private  long getUsesSpace(File path){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.GINGERBREAD){
            return path.getUsableSpace();
        }
        StatFs stats=new StatFs(path.getAbsolutePath());
        return (long)stats.getBlockSize()*(long)stats.getAvailableBlocks();
    }


    /**
     * 根据ImageView获得适当的压缩的宽和高
     *
     * @param imageView
     * @return
     */
    private ImageSize getImageViewWidth(ImageView imageView)
    {
        ImageSize imageSize = new ImageSize();
        final DisplayMetrics displayMetrics = imageView.getContext()
                .getResources().getDisplayMetrics();
        final LayoutParams params = imageView.getLayoutParams();

        int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getWidth(); // Get actual image width
        if (width <= 0)
            width = params.width; // Get layout width parameter
        if (width <= 0)
            width = getImageViewFieldValue(imageView, "mMaxWidth"); // Check
        // maxWidth
        // parameter
        if (width <= 0)
            width = displayMetrics.widthPixels;
        int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView.getHeight(); // Get actual image height
        if (height <= 0)
            height = params.height; // Get layout height parameter
        if (height <= 0)
            height = getImageViewFieldValue(imageView, "mMaxHeight"); // Check
        // maxHeight
        // parameter
        if (height <= 0)
            height = displayMetrics.heightPixels;
        imageSize.width = width;
        imageSize.height = height;
        return imageSize;

    }

    /**
     * 反射获得ImageView设置的最大宽度和高度
     *
     * @param object
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object object, String fieldName)
    {
        int value = 0;
        try
        {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = (Integer) field.get(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
            {
                value = fieldValue;

                Log.e("TAG", value + "");
            }
        } catch (Exception e)
        {
        }
        return value;
    }

    public static String getRealPath(Context context,Uri uri){
        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return "";

    }


    private static class ImageSize
    {
        int width;
        int height;
    }

    private static class LoaderResult {
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String url, Bitmap bitmap) {
            this.imageView = imageView;
            this.url = url;
            this.bitmap = bitmap;
        }
    }


}
