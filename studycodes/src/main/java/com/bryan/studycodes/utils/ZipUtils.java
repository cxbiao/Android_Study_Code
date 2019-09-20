package com.bryan.studycodes.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    /** ZipInputStream  遍历整个zip包
     *
     * @param src  源文件
     * @param destDir 解压目录
     */
    public static void unZip(String src,String destDir){
        try {
            ZipInputStream zi=new ZipInputStream(new FileInputStream(src));
            ZipEntry zipEntry;
            while ((zipEntry=zi.getNextEntry())!=null){
                if(zipEntry.isDirectory()){
                    System.out.println(zipEntry.getName()+"is directory");
                    new File(destDir,zipEntry.getName()).mkdirs();
                }else{
                    System.out.println(zipEntry.getName()+",size:"+zipEntry.getSize()+",compressSize:"+zipEntry.getCompressedSize());

                    FileOutputStream fos=new FileOutputStream(new File(destDir,zipEntry.getName()));

                    byte[] buffer=new byte[1024];
                    int len;
                    while ((len=zi.read(buffer))!=-1){
                        fos.write(buffer,0,len);
                    }
                    fos.close();
                }

            }
            zi.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /** ZipFile 单独对某个文件解压，不用遍历
     *
     * @param src  源zip文件
     * @param file  要解压的文件
     * @param destDir 解压目录
     */
    public static void unZipFile(String src,String file,String destDir){
        try{
            ZipFile zf=new ZipFile(src);
            ZipEntry zipEntry=zf.getEntry(file);
            if(zipEntry==null){
                System.err.println(file+" can't find!");
                return;
            }
            System.out.println(zipEntry.getName()+",size:"+zipEntry.getSize()+",compressSize:"+zipEntry.getCompressedSize());

            InputStream zi= zf.getInputStream(zipEntry);

           FileOutputStream fos=new FileOutputStream(new File(destDir,zipEntry.getName()));

           byte[] buffer=new byte[1024];
           int len;
           while ((len=zi.read(buffer))!=-1){
               fos.write(buffer,0,len);
           }
           fos.close();
           zf.close();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
