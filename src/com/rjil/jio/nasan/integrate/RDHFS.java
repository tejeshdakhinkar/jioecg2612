package com.rjil.jio.nasan.integrate;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;


public class RDHFS {
	
	HttpURLConnection conn = null;
	DataOutputStream dos = null;
	String twoHyphens = "--";
	String boundary = "*****";
	static int serverResponseCode =0;

	public static void sendtoFileUpload(String filelocation,String jioid) throws ClientProtocolException, IOException 
	{
		System.out.println(jioid);
		System.out.println(filelocation);
		String upLoadServerUri = "http://files.healthhub.net.in/patients/records/upload?authId="+URLEncoder.encode("nikeshtest")+"&authToken="+URLEncoder.encode("o8OH5ZEiORIONRmVJB7eFA")+"&jioId="+URLEncoder.encode(jioid);
		uploadFile(filelocation,upLoadServerUri);
	}
	
public static int uploadFile(String sourceFileUri,String upLoadServerUri) throws ClientProtocolException,
	IOException 
	{
		String fileName = sourceFileUri;
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		File sourceFile = new File(fileName);
		String ufile = "";
		System.out.println("Nikesh File name: "+sourceFile);
		if (!sourceFile.isFile()) {
			
			return 0;
		
		} else {
			try {
				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(upLoadServerUri);
				System.out.println("url --->" + upLoadServerUri);
				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("file", sourceFile.toString());
		
				dos = new DataOutputStream(conn.getOutputStream());
				System.out.println(fileName);
				String[] bfilename = fileName.split("//");
				ufile = bfilename[6];
				System.out.println(ufile);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=file ;filename="
						+ ufile + lineEnd);
				dos.writeBytes(lineEnd);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
				while (bytesRead > 0) 
				{
		
					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
		
				}
		
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		
				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();
				System.out.println("Server Status--->"+serverResponseCode);
				if (serverResponseCode == 200) 
				{
					System.out.println("Report Uploaded successfully");
				}
		
				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();
			} catch (MalformedURLException e) {
				// TODO: handle exception
				e.printStackTrace();
		
		
			} catch (Exception e) {
		
				e.printStackTrace();
		
			}
			return serverResponseCode;
}

}

public static String buildJSON()  
{
	JSONObject job = new JSONObject();
	job.put("token", "METROPOLIS@#@!$$$$&&^%$%");
	job.put("jioid", "10013");
	job.put("sendingentity", "Metropolis");
	job.put("healthhubtoken", "LWF8_lX4Wjsqqe__s0dv8g");
	job.put("auth_id","10003");
	return job.toString();
}




}
