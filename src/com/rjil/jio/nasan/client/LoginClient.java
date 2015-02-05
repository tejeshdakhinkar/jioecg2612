package com.rjil.jio.nasan.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ws.rs.QueryParam;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONTokener;

public class LoginClient {
	public static void main(String[] args) throws JSONException {
		LoginClient l = new LoginClient();
		l.verify("ril1000", "111111");
		//l.getClinicData("56");
		//l.getPassword("ril1000");
		//l.getDeviceData("ril1000");
		// l.devreg("880070023435164", "nokia", "1", "888", "kkkkk");
		l.clinicreg("RF1226", "111111", "RF", "Raja", "Mumbai", "Mumbai", "1234567890", "222545789", "sdfs@fortis.com", "jjfjjf@fortis.com", "0");
	}

	public void verify(String clinic_id, String passwoord) throws JSONException {
		try {
			String path = "http://localhost:8080/jioecg_2612/rest/jioecg/weblogin?clinic_id="
					+ clinic_id + "&password=" + passwoord;
			HttpPost post = new HttpPost(path);
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));

			String line = br.readLine();
			System.out.println(line);
			HttpEntity entity = httpresponse.getEntity();
			// System.out.println(entity.getContent());

			line = line.replace("{", "");
			line = line.replace("}", "");
			line = line.replaceAll("\"", "");

			JSONObject data = new JSONObject();

			String a[] = new String[2];
			for (String retval : line.split(",", 0)) {
				// System.out.println(retval);
				String a1 = retval;
				int i = 0;
				for (String a2 : a1.split(":", 0)) {
					a[i] = a2;
					i++;
				}
				data.put(a[0], a[1]);

			}

			System.out.println("json");
			System.out.println(data.toString());
			System.out.println("status value"+data.get("organisation_name"));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void devreg(String device_id, String device_name,
			String device_status, String clinic_id, String organisation_name)
			throws JSONException {
		try {
			String path = "http://localhost:8080/jioecg_2612/rest/jioecg/deviceregistration?device_id="
					+ device_id
					+ "&device_name="
					+ device_name
					+ "&device_status="
					+ device_status
					+ "&clinic_id="
					+ clinic_id
					+ "&organisation_name="
					+ organisation_name
					+ "";
			HttpPost post = new HttpPost(path);
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));
			String line = br.readLine();

			HttpEntity entity = httpresponse.getEntity();
			// System.out.println(entity);

			line = line.replace("{", "");
			line = line.replace("}", "");
			line = line.replaceAll("\"", "");

			JSONObject data = new JSONObject();

			String a[] = new String[2];
			for (String retval : line.split(",", 0)) {
				// System.out.println(retval);
				String a1 = retval;
				int i = 0;
				for (String a2 : a1.split(":", 0)) {
					a[i] = a2;
					i++;
				}
				data.put(a[0], a[1]);
			}

			System.out.println("json");
			System.out.println(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getClinicData(String clinic_id) throws JSONException {
		try {
			String path = "http://localhost:8080/jioecg_2612/rest/jioecg/clinicdata?clinic_id="
					+ clinic_id + "";
			HttpPost post = new HttpPost(path);
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));

			String line = br.readLine();
			System.out.println(line);
			HttpEntity entity = httpresponse.getEntity();
			// System.out.println(entity.getContent());
			Map<String,String> datamap=new TreeMap<String,String>();
			List<Map<String, String>> datalist=new ArrayList<Map<String,String>>();
			
			line = line.replace("{", "");
			//line = line.replace("=", ":");
			line = line.replaceAll("\"", "");
			line = line.replaceAll(" ", "");
			/*
			 * line=line.replaceAll("[", ""); line=line.replaceAll("]", "");
			 */
			line = line.replace("[", "").replace("]", "");
			JSONObject data = new JSONObject();
			//System.out.println("line"+line);
			String a[] = new String[2];
			String a5[] = new String[2];
			for (String retval1 : line.split("}", 1)) 
			{
				String line1 = retval1;
				//System.out.println(line1);
				line1=line1.replace("}", "");
				for (String retval : line1.split(",", 0)) 
				{
					//System.out.println(retval);
					String a1 = retval;
					int i = 0;
					/*a5=a1.split(":"); 
					System.out.println(a5[1]);*/
					for (String a2 : a1.split("=", 0)) 
					{
						a[i] = a2;
						i++;
					}
					data.put(a[0], a[1]);
					System.out.println(a[0]+"    "+a[1]);
					datamap.put(a[0], a[1]);
					//System.out.println(datamap.toString());
				}
				datalist.add(datamap);
			}
			System.out.println("json");
			System.out.println();
			//System.out.println(data.toString());
			String date="",time="",datetime="";
			datetime=(String) data.get("ecgtest_date");
			date=datetime.substring(0, 10);
			time=datetime.substring(10);
			System.out.println("date"+date+"  time"+time);
			
			
			
			if (datalist!=null && !datalist.isEmpty()) 
			{
				for (Map<String, String> employee : datalist) 
				{
					for (Iterator<Entry<String, String>> it = employee.entrySet().iterator(); it.hasNext();) 
					{
						Entry<String, String> entry = it.next();
						String key = entry.getKey();
						Object value = entry.getValue();
						System.out.println(key + " = " + value);
						
						//System.out.println(ecgdata.toString());
					}
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void clinicreg(String clinic_id,String password,String organisation_name,String organisation_head,String organisation_address,String city,String contact_no,String alternate_contact_no,String email_id,String alternate_email_id,String user_type)
			throws JSONException {
		try {
			System.out.println(user_type);
			//String path = "http://localhost:8080/jioecg_2612/rest/jioecg/clinicregistration?clinic_id="+clinic_id+"&organisation_name="+organisation_name+"&organisation_head="+organisation_head+"&organisation_address="+organisation_address+"&city="+city+"&contact_no="+contact_no+"&alternate_contact_no="+alternate_contact_no+"&email_id="+email_id+"&alternate_email_id="+alternate_email_id+"&user_type="+user_type+"";
			String path="http://localhost:8080/jioecg_2612/rest/jioecg/clinicregistration?clinic_id="+clinic_id+"&password="+password+"&organisation_name="+organisation_name+"&organisation_head="+organisation_head+"&organisation_address="+organisation_address+"&city="+city+"&contact_no="+contact_no+"&alternate_contact_no="+alternate_contact_no+"&email_id="+email_id+"&alternate_email_id="+alternate_email_id+"&user_type="+user_type+"";
			System.out.println(path);
			HttpPost post = new HttpPost(path);	
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));
			String line = br.readLine();

			HttpEntity entity = httpresponse.getEntity();
			System.out.println(line);

			line = line.replace("{", "");
			line = line.replace("}", "");
			line = line.replaceAll("\"", "");

			JSONObject data = new JSONObject();

			String a[] = new String[2];
			for (String retval : line.split(",", 0)) {
				// System.out.println(retval);
				String a1 = retval;
				int i = 0;
				for (String a2 : a1.split(":", 0)) {
					a[i] = a2;
					i++;
				}
				data.put(a[0], a[1]);
			}

			System.out.println("json");
			System.out.println(data.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Map<String, String>> getDeviceData(String clinic_id) throws JSONException {
		
		List<Map<String, String>> datalist=new ArrayList<Map<String,String>>();
		try {
			String path = "http://localhost:8080/jioecg_2612/rest/jioecg/devicedata?clinic_id="
					+ clinic_id + "";
			HttpPost post = new HttpPost(path);
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));

			String line = br.readLine();
			System.out.println(line);
			HttpEntity entity = httpresponse.getEntity();
			// System.out.println(entity.getContent());
			Map<String,String> datamap=new TreeMap<String,String>();
			
			line = line.replace("{", "");
			//line = line.replace("=", ":");
			line = line.replaceAll("\"", "");
			line = line.replaceAll(" ", "");
			/*
			 * line=line.replaceAll("[", ""); line=line.replaceAll("]", "");
			 */
			line = line.replace("[", "").replace("]", "");
			JSONObject data = new JSONObject();
			System.out.println("line"+line);
			String a[] = new String[2];
			String a5[] = new String[2];
			for (String retval1 : line.split("}", 1)) 
			{
				String line1 = retval1;
				System.out.println(line1);
				line1=line1.replace("}", "");
				for (String retval : line1.split(",", 0)) 
				{
					//System.out.println(retval);
					String a1 = retval;
					//System.out.println("a1"+a1);
					int i = 0;
					/*a5=a1.split(":"); 
					System.out.println(a5[1]);*/
					for (String a2 : a1.split("=", 0)) 
					{
						a[i] = a2;
						i++;
					}
					data.put(a[0], a[1]);
					//System.out.println(a[0]+"    "+a[1]);
					datamap.put(a[0], a[1]);
				}
				System.out.println();
				datalist.add(datamap);
			}
			System.out.println("json");
			//System.out.println(data.toString());
			//System.out.println(datamap.toString());
			
			
			if (datalist!=null && !datalist.isEmpty()) 
			{
				for (Map<String, String> employee : datalist) 
				{
					for (Iterator<Entry<String, String>> it = employee.entrySet().iterator(); it.hasNext();) 
					{
						Entry<String, String> entry = it.next();
						String key = entry.getKey();
						Object value = entry.getValue();
						System.out.println(key + " = " + value);
						
						//System.out.println(ecgdata.toString());
					}
					System.out.println();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return datalist;

	}
	
	public void getPassword(String clinic_id) throws JSONException {
		try {
			String path = "http://localhost:8080/jioecg_2612/rest/jioecg/forgotpassword?clinic_id="
					+ clinic_id + "";
			HttpPost post = new HttpPost(path);
			// post.setHeader("Content-type", "application/json");
			// post.setEntity(new StringEntity(json.toString(), "UTF-8"));
			DefaultHttpClient client = new DefaultHttpClient();
			HttpResponse httpresponse = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					httpresponse.getEntity().getContent()));

			String line = br.readLine();
			System.out.println(line);
			HttpEntity entity = httpresponse.getEntity();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
