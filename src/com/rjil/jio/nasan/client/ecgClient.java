package com.rjil.jio.nasan.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ecgClient {
	public static void main(String[] args) throws JSONException,
			ClientProtocolException, IOException {
		JSONObject json = new JSONObject();
		
		json.put("username", "aks");
		json.put("password", "qwerty");
		json.put("imei_no", "12345678");
		json.put("user_type", "1");
		/*json.put("clinic_name", "24");
		json.put("device_status", "female");
*/
		/*json.put("clinic_id", "ril1000");
		json.put("password", "111111");
		json.put("device_id", "860070023435169");
		json.put("user_type", "1");*/
		System.out.println(json);

		try {
			System.out.println("Teju is smart");
			HttpPost post = new HttpPost(
					"http://hdmstaging.jiocloud.com/jioecg/rest/jioecg/verifylogin");
			post.setHeader("Content-type", "application/json");
			post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			System.out.println("Kamine chal");
			HttpResponse httpresponse = client.execute(post);
			System.out.println(httpresponse);
			HttpEntity entity = httpresponse.getEntity();
			System.out.println(entity);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
