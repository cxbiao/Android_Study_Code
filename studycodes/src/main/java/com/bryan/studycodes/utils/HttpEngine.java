package com.bryan.studycodes.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * urlconnection上传文件

 */
public class HttpEngine {

	public static byte[] stream2ByteArray(InputStream is)  {

		try {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			byte[] buf=new byte[1024];
			int len=0;
			while((len=is.read(buf))!=-1){
                bos.write(buf, 0, len);
            }
			bos.close();
			is.close();
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    public static byte[] getBytesFromUrl(String url){
    	
    	try {
			URL myUrl=new URL(url);
			HttpURLConnection urlConnection=
					(HttpURLConnection) myUrl.openConnection();
			urlConnection.setRequestMethod("GET");
			if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
				InputStream is=urlConnection.getInputStream();
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				byte[] buf=new byte[1024];
				int len=0;
				while((len=is.read(buf))!=-1){
					bos.write(buf, 0, len);
				}
				bos.close();
				is.close();
				return bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
  		return null;
      
   }






	/**
	 * http://m.blog.csdn.net/blog/jianggujin/44964313
	 * 有些 https不需要证书
	 * @param url
	 * @return
	 */
	public static byte[] getBytesFromHttps(String url){

		try {
			//设置SSLContext
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[]{myX509TrustManager}, new SecureRandom());

			URL myUrl=new URL(url);
			HttpsURLConnection urlConnection=
					(HttpsURLConnection) myUrl.openConnection();
			//设置套接工厂
			urlConnection.setSSLSocketFactory(sslcontext.getSocketFactory());
			urlConnection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			urlConnection.setRequestMethod("GET");
			if(urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
				InputStream is=urlConnection.getInputStream();
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				byte[] buf=new byte[1024];
				int len=0;
				while((len=is.read(buf))!=-1){
					bos.write(buf, 0, len);
				}
				bos.close();
				is.close();
				return bos.toByteArray();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static TrustManager myX509TrustManager = new X509TrustManager() {


		@Override
		public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

		}

		@Override
		public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

		}

		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return new java.security.cert.X509Certificate[0];
		}
	};

}
