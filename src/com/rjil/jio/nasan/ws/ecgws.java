package com.rjil.jio.nasan.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.gson.Gson;
import com.rjil.jio.nasan.forgotpwd.ForgotPassword;
import com.rjil.jio.nasan.integrate.RDHFS;
import com.rjil.jio.nasan.jdbctemplate.ecgjdbctemplate;
import com.rjil.jio.nasan.pojo.ecg;
import com.rjil.jio.nasan.pojo.server;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/jioecg/")
public class ecgws implements Runnable {
	ApplicationContext context = new ClassPathXmlApplicationContext(
			"/com/rjil/jio/nasan/ds/ecgbean.xml");
	ecgjdbctemplate ecgjdbc = (ecgjdbctemplate) context.getBean("ecg");

	@POST
	@Path("/verifylogin")
	@Consumes("application/json")
	@Produces("application/json")
	public String insertData(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("password") String password,
			@QueryParam("device_id") String device_id,
			@QueryParam("user_type") String user_type) throws IOException,
			JSONException, ParseException {

		JSONObject job = new JSONObject();
		JSONObject data = new JSONObject();
		JSONObject response = new JSONObject();
		JSONObject success = new JSONObject();

		data.put("device_id", device_id);
		data.put("clinic_id", clinic_id);
		data.put("password", password);
		data.put("user_type", user_type);

		// Reflection using Google Library
		Gson gs = new Gson();
		ecg ecgdata = gs.fromJson(data.toString(), ecg.class);

		System.out.println(clinic_id);

		if ((device_id.trim().equalsIgnoreCase(""))
				|| (clinic_id.trim().equalsIgnoreCase(null))
				|| (password.trim().equalsIgnoreCase(null))
				|| (user_type.trim().equalsIgnoreCase(null))) {
			response.put("status:", "fail");
			response.put("Error code:", "EC101");
			response.put("message:", "Insufficient Parameter");
			job = response;
			System.out.println(response.toString());
		} else {
			success = (JSONObject) ecgjdbc.loginverify(ecgdata.getDevice_id(),
					ecgdata.getClinic_id(), ecgdata.getPassword(),
					ecgdata.getUser_type());

			/*
			 * if (success.get("status").toString().equalsIgnoreCase("fail")) {
			 * response.put("status", "fail"); response.put("Error code",
			 * "EC102"); response.put("message", "User doesn't exist");
			 * response.put("imei", "Not Active"); job = response; } else {
			 */
			job = success; // Sucess is constructed as json from loginverify
							// method
			// }
		}
		System.out.println(job.toString());
		return job.toString();
	}

	String response = "";
	public static String FILE_DESTINATION = "";
	String empiip = "http://";
	String jioid;

	@POST
	@Path("/multi")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String uploadFile(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition file,
			@QueryParam("clinic_id") Integer clinic_id,
			@QueryParam("patient_name") String patient_name,
			@QueryParam("patient_id") String patient_id,
			@QueryParam("sex") String sex, @QueryParam("age") String age,
			@QueryParam("jio_id") String jio_id,
			@QueryParam("contact_number") String contact_number,
			@QueryParam("ecgtest_date") String ecgtest_date,
			@QueryParam("user_type") String user_type) throws IOException,
			JSONException {
		System.out.println("file" + file);
		System.out.println(contact_number);
		org.json.simple.JSONObject jj = new org.json.simple.JSONObject();
		String id = UUID.randomUUID().toString();
		String ecg_link = "";
		ecg_link = "http://hdmstaging.jiocloud.com/NasanG/" + id + ".jpeg";
		// String serverupload_time="2014-01-01 23:59:59";
		java.util.Date date = new java.util.Date();
		Timestamp serverupload_time = new Timestamp(date.getTime());
		Timestamp ecgDate = null;
		jioid = jio_id;
		try {
			ecgDate = new Timestamp(new SimpleDateFormat("yyyy-MM-ddhh:mm:ss")
					.parse(ecgtest_date).getTime());
		} catch (Exception e) {
		}
		int count = ecgjdbc.insertdata(clinic_id, patient_id, patient_name,
				age, sex, contact_number, ecg_link, jio_id, ecgDate,
				serverupload_time, user_type);

		if (jio_id != null) {

			Thread tt = new Thread(this, "Integrate File Upload Server");
			tt.start();
		}
		System.out.println("count" + ecg_link);
		java.io.File imgfile = null;
		if (count == 1) {
			/*
			 * if(srtype.equals("localhost")) { imgfile=new
			 * java.io.File(FILE_DESTINATION); } if(srtype.equals("production"))
			 * { imgfile=new java.io.File(ecg_link); // C://Program
			 * Files//Apache Software Foundation//Tomcat 7.0//webapps//NasanG//
			 * } //java.io.File imgfile=new java.io.File(FILE_DESTINATION);
			 * if(imgfile.exists()) { jj.put("status",
			 * "file uploaded successfully"); System.out.println(jj.toString());
			 * } else jj.put("fail", "file not uploaded successfully");
			 */

			server ss = ecgjdbc.servertype("1");
			String srtype = ss.getServer();
			String flocation = ss.getFile_location();
			System.out.println(srtype);
			if (srtype.equals("localhost")) {
				FILE_DESTINATION = flocation + id + ".jpeg";
			}
			if (srtype.equals("production")) {
				FILE_DESTINATION = flocation + id + ".jpeg";
				// C://Program Files//Apache Software Foundation//Tomcat
				// 7.0//webapps//NasanG//
			}

			System.out.println(FILE_DESTINATION);
			File f = new File(FILE_DESTINATION);
			OutputStream outputStream = new FileOutputStream(f);
			int size = 0;
			byte[] bytes = new byte[1024];
			while ((size = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, size);
			}
			outputStream.flush();
			outputStream.close();
			jj.put("Success", "file uploaded successfully");
		} else
			jj.put("fail", "file not uploaded successfully");
		return jj.toJSONString();

	}

	public void run() {
		// TODO Auto-generated method stub

		try {

			RDHFS.sendtoFileUpload(FILE_DESTINATION, jioid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * @POST
	 * 
	 * @Path("/registration")
	 * 
	 * @Consumes("application/json")
	 * 
	 * @Produces("application/json") public String
	 * register(@QueryParam("imei_no") String imei_no,@QueryParam("username")
	 * String username,@QueryParam("password") String
	 * password,@QueryParam("clinic_name") String
	 * clinic_name,@QueryParam("device_status") String
	 * device_status,@QueryParam("organisation_name") String
	 * organisation_name,@QueryParam("organisation_name_head") String
	 * organisation_name_head,@QueryParam("organisation_address")String
	 * organisation_address,@QueryParam("contact_no") int
	 * contact_no,@QueryParam("alternate_contact_no") int
	 * alternate_contact_no,@QueryParam("email_id") String
	 * email_id,@QueryParam("user_type") String user_type) {
	 * org.json.simple.JSONObject userreg = new org.json.simple.JSONObject();
	 * System.out.println(username); if((imei_no.trim().equalsIgnoreCase("")) ||
	 * (username.trim().equalsIgnoreCase("")) ||
	 * (password.trim().equalsIgnoreCase("")) ||
	 * (organisation_name.trim().equalsIgnoreCase("")) ||
	 * (organisation_name_head.trim().equalsIgnoreCase("")) ||
	 * (organisation_address.trim().equalsIgnoreCase("")) || (contact_no==0) ||
	 * (alternate_contact_no==0) || email_id.trim().equalsIgnoreCase("")) {
	 * 
	 * userreg.put("status:", "fail"); userreg.put("Error code:", "EC101");
	 * userreg.put("message:", "Insufficient Parameter"); //job=response;
	 * System.out.println(response.toString()); } else { int
	 * result=ecgjdbc.register
	 * (imei_no,username,password,clinic_name,device_status
	 * ,organisation_name,organisation_name_head
	 * ,organisation_address,contact_no,
	 * alternate_contact_no,email_id,user_type); if(result==1) {
	 * 
	 * System.out.println("Registation Successful"); userreg.put("success",
	 * "Registation Successful"); } else {
	 * System.out.println("Registration Failed"); userreg.put("failed",
	 * "Registration Failed"); } } return userreg.toJSONString();
	 * 
	 * }
	 * 
	 * @POST
	 * 
	 * @Path("/devicereg")
	 * 
	 * @Consumes("application/json")
	 * 
	 * @Produces("application/json") public String
	 * devicereg(@QueryParam("device_id")String
	 * device_id,@QueryParam("device_name")String
	 * device_name,@QueryParam("device_status")String
	 * device_status,@QueryParam("clinic_id")String
	 * clinic_id,@QueryParam("organisation_name")String organisation_name) {
	 * org.json.simple.JSONObject devcreg = new org.json.simple.JSONObject();
	 * 
	 * if((device_id.trim().equalsIgnoreCase(""))||(device_name.trim().
	 * equalsIgnoreCase
	 * (""))||(clinic_id.trim().equalsIgnoreCase(""))||(device_status
	 * .trim().equalsIgnoreCase(""))) { devcreg.put("Status", "Failed");
	 * devcreg.put("Error code", "EC101"); devcreg.put("Message",
	 * "Insufficient Parameter"); } else { int
	 * result1=ecgjdbc.devicereg(device_id
	 * ,device_name,device_status,clinic_id,organisation_name); if(result1==1) {
	 * 
	 * System.out.println("Registation Successful"); devcreg.put("success",
	 * "Registration Successful"); } else {
	 * System.out.println("Registration Failed"); devcreg.put("failed",
	 * "Registration Failed"); } } return devcreg.toJSONString();
	 * 
	 * }
	 */

	@POST
	@Path("/clinicregistration")
	@Consumes("application/json")
	@Produces("application/json")
	public String register(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("password") String password,
			@QueryParam("organisation_name") String organisation_name,
			@QueryParam("organisation_head") String organisation_head,
			@QueryParam("organisation_address") String organisation_address,
			@QueryParam("city") String city,
			@QueryParam("contact_no") int contact_no,
			@QueryParam("alternate_contact_no") int alternate_contact_no,
			@QueryParam("email_id") String email_id,
			@QueryParam("alternate_email_id") String alternate_email_id,
			@QueryParam("user_type") String user_type) {
		org.json.simple.JSONObject userreg = new org.json.simple.JSONObject();
		System.out.println(clinic_id);
		System.out.println(contact_no);

		if ((clinic_id.trim().equalsIgnoreCase(""))
				|| (password.trim().equalsIgnoreCase(""))
				|| (organisation_name.trim().equalsIgnoreCase(""))
				|| (organisation_head.trim().equalsIgnoreCase(""))
				|| (organisation_address.trim().equalsIgnoreCase(""))
				|| (city.trim().equalsIgnoreCase("")) || (contact_no == 0)
				|| (email_id.trim().equalsIgnoreCase(""))
				|| (user_type.trim().equalsIgnoreCase(""))) {

			userreg.put("status:", "fail");
			userreg.put("Error code:", "EC101");
			userreg.put("message:", "Insufficient Parameter");
			// job=response;
			System.out.println(response.toString());
		} else {
			int result = ecgjdbc.register(clinic_id, password,
					organisation_name, organisation_head, organisation_address,
					city, contact_no, alternate_contact_no, email_id,
					alternate_email_id, user_type);
			if (result == 1) {

				System.out.println("Registation Sucessful");
				userreg.put("sucess", "Registation Sucessful");
			} else {
				System.out.println("Registration Failed");
				userreg.put("failed", "Registration Failed");
			}
		}
		return userreg.toJSONString();

	}

	@POST
	@Path("/deviceregistration")
	@Consumes("application/json")
	@Produces("application/json")
	public String devregister(@QueryParam("device_id") String device_id,
			@QueryParam("device_name") String device_name,
			@QueryParam("device_status") String device_status,
			@QueryParam("clinic_id") String clinic_id,
			@QueryParam("organisation_name") String organisation_name) {
		org.json.simple.JSONObject userreg = new org.json.simple.JSONObject();
		System.out.println(device_id);
		if ((device_id.trim().equalsIgnoreCase(""))
				|| (device_name.trim().equalsIgnoreCase(""))
				|| (device_status.trim().equalsIgnoreCase(""))
				|| (clinic_id.trim().equalsIgnoreCase(""))
				|| (organisation_name.trim().equalsIgnoreCase(""))) {

			userreg.put("status:", "fail");
			userreg.put("Error code:", "EC101");
			userreg.put("message:", "Insufficient Parameter");
			// job=response;
			System.out.println(response.toString());
		} else {
			int result = ecgjdbc.devicereg(device_id, device_name,
					device_status, clinic_id, organisation_name);// (device_id,device_name,device_status,clinic_id,organisation_name);
			if (result == 1) {

				System.out.println("Registation Sucessful");
				userreg.put("sucess", "Registation Sucessful");
			} else if (result == 2) {

				System.out.println("Registation Failed");
				userreg.put("Message", "Device Already Registered");
				userreg.put("Failed", "Registation Failed");

			} else {
				System.out.println("Registration Failed");
				userreg.put("failed", "Registration Failed");
			}
		}
		return userreg.toJSONString();

	}

	@POST
	@Path("/weblogin")
	@Consumes("application/json")
	@Produces("application/json")
	public String verify(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("password") String password) throws JSONException {
		JSONObject data = new JSONObject();

		data = ecgjdbc.webverify(clinic_id, password);
		System.out.println(data.toString());

		return data.toString();

	}

	@POST
	@Path("/clinicdata")
	@Consumes("application/json")
	@Produces("application/json")
	public String clinicdata(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("user_type") String user_type) throws JSONException {
		JSONObject data1 = new JSONObject();
		JSONArray data = new JSONArray();
		// List<String> list=new ArrayList<String>();
		List<Map<String, Object>> list = ecgjdbc.clinicdata(clinic_id,
				user_type);
		System.out.println("hiiii");
		// System.out.println(data.toString());
		data.put(list);

		// data1=(JSONObject) data.get(0);
		/*
		 * Iterator<String> iterator = list.iterator(); while
		 * (iterator.hasNext()) { System.out.println("list"+iterator.); }
		 */

		// System.out.println(data.get(1).toString());

		if (list != null && !list.isEmpty()) {
			for (Map<String, Object> employee : list) {
				for (Iterator<Map.Entry<String, Object>> it = employee
						.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					System.out.println(key + " = " + value);
					// ecgdata.put(key, value);
					// System.out.println(ecgdata.toString());
				}
			}
		}

		return data.toString();

	}

	@POST
	@Path("/devicedata")
	@Consumes("application/json")
	@Produces("application/json")
	public String devicedata(@QueryParam("clinic_id") String clinic_id)
			throws JSONException {
		JSONObject data1 = new JSONObject();
		JSONArray data = new JSONArray();
		// List<String> list=new ArrayList<String>();
		// System.out.println("hiiiiiiii");
		List<Map<String, Object>> list = null;
		try {
			list = ecgjdbc.devicedata(clinic_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("hiiii");
		// System.out.println(data.toString());
		data.put(list);

		// data1=(JSONObject) data.get(0);
		/*
		 * Iterator<String> iterator = list.iterator(); while
		 * (iterator.hasNext()) { System.out.println("list"+iterator.); }
		 */

		// System.out.println(data.get(1).toString());

		if (list != null && !list.isEmpty()) {
			for (Map<String, Object> employee : list) {
				for (Iterator<Map.Entry<String, Object>> it = employee
						.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					// System.out.println(key + " = " + value);
					// ecgdata.put(key, value);
					// System.out.println(ecgdata.toString());
				}
			}
		}

		return data.toString();

	}

	@POST
	@Path("/forgotpassword")
	@Consumes("application/json")
	@Produces("application/json")
	public String forgotpassword(@QueryParam("clinic_id") String clinic_id)
			throws Exception {
		ForgotPassword fp = new ForgotPassword();
		String password = "", email_id = "";
		JSONObject data = new JSONObject();

		data = ecgjdbc.webverify(clinic_id, password);
		System.out.println(data);
		email_id = (String) data.get("email_id");
		password = (String) data.get("password");
		System.out.println(clinic_id + "  " + password + "  " + email_id);
		fp.sendEmail(email_id, clinic_id, password);
		return clinic_id + password + email_id;

	}

	/*
	 * @POST
	 * 
	 * @Path("/getdata")
	 * 
	 * @Consumes("application/json")
	 * 
	 * @Produces("application/json") public String
	 * getdata(@QueryParam("patient_id") String patient_id) throws Exception {
	 * System.out.println(patient_id); String link=ecgjdbc.getlink(patient_id);
	 * 
	 * return link; }
	 */

	@POST
	@Path("/regclinic")
	@Consumes("application/json")
	@Produces("application/json")
	public String regclinic() throws JSONException {
		JSONObject data1 = new JSONObject();
		JSONArray data = new JSONArray();
		// List<String> list=new ArrayList<String>();
		// System.out.println("hiiiiiiii");
		List<Map<String, Object>> list = null;
		try {
			list = ecgjdbc.regclinic();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("hiiii");
		// System.out.println(data.toString());
		data.put(list);

		// data1=(JSONObject) data.get(0);
		/*
		 * Iterator<String> iterator = list.iterator(); while
		 * (iterator.hasNext()) { System.out.println("list"+iterator.); }
		 */

		// System.out.println(data.get(1).toString());

		if (list != null && !list.isEmpty()) {
			for (Map<String, Object> employee : list) {
				for (Iterator<Map.Entry<String, Object>> it = employee
						.entrySet().iterator(); it.hasNext();) {
					Map.Entry<String, Object> entry = it.next();
					String key = entry.getKey();
					Object value = entry.getValue();
					// System.out.println(key + " = " + value);
					// ecgdata.put(key, value);
					// System.out.println(ecgdata.toString());
				}
			}
		}

		return data.toString();

	}

	@POST
	@Path("/activatedeactivate")
	@Consumes("application/json")
	@Produces("application/json")
	public String activatedeactivate(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("device_id") String device_id,
			@QueryParam("device_status") String device_status) {
		JSONObject data = new JSONObject();
		int success1;
		System.out.println(clinic_id + "   " + device_id + " " + device_status);

		success1 = ecgjdbc.getDeviceStatus(clinic_id, device_id, device_status);
		String success = "";
		success = success + success1;
		return success;
	}

	@POST
	@Path("/updateclinicinformation")
	@Consumes("application/json")
	@Produces("application/json")
	public JSONObject updateInfo(@QueryParam("clinic_id") String clinic_id,
			@QueryParam("password") String password,
			@QueryParam("organisation_name") String organisation_name,
			@QueryParam("organisation_head") String organisation_head,
			@QueryParam("organisation_address") String organisation_address,
			@QueryParam("city") String city,
			@QueryParam("contact_no") int contact_no,
			@QueryParam("alternate_contact_no") int alternate_contact_no,
			@QueryParam("email_id") String email_id,
			@QueryParam("alternate_email_id") String alternate_email_id)
			throws JSONException {
		JSONObject data = new JSONObject();
		int success = 0;
		System.out.println("Entered");
		success = ecgjdbc.updateClinicInfo(clinic_id, password,
				organisation_name, organisation_head, organisation_address,
				city, contact_no, alternate_contact_no, email_id,
				alternate_email_id);

		if (success == 1)
			data.put("status", "Information Updated Successfully");
		else
			data.put("status", "Failed to update Information");
		return data;

	}
}
