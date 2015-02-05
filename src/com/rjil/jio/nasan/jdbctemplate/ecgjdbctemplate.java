package com.rjil.jio.nasan.jdbctemplate;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.codehaus.*;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.rjil.jio.nasan.pojo.device_registration;
import com.rjil.jio.nasan.pojo.ecg;
import com.rjil.jio.nasan.pojo.clinic_registration;
import com.rjil.jio.nasan.pojo.server;
import com.rjil.jio.nasan.pojo.verifylogin_pojo;
import com.rjil.jio.nasan.rowmapper.device_registrationmapper;
import com.rjil.jio.nasan.rowmapper.ecgmapper;
import com.rjil.jio.nasan.rowmapper.clinic_registrationmapper;
import com.rjil.jio.nasan.rowmapper.serverrowmapper;
import com.rjil.jio.nasan.rowmapper.verifyloginmapper;

@Configurable
public class ecgjdbctemplate {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplateObject;

	public void setJdbcTemplateObject(JdbcTemplate jdbcTemplateObject) {
		this.jdbcTemplateObject = jdbcTemplateObject;
	}

	public void setDataSource(DataSource dataSource) {
		// TODO Auto-generated method stub
		this.dataSource = dataSource;
	}

	public org.codehaus.jettison.json.JSONObject loginverify(String device_id,
			String clinic_id, String password, String user_type)
			throws JSONException {
		org.codehaus.jettison.json.JSONObject response = new org.codehaus.jettison.json.JSONObject();
		int success = 0, id, verify, new_clinic_id = 0;
		String device_status = "";
		String sql12 = "", sql11 = "";
		Integer cusertype = Integer.valueOf(user_type);

		// System.out.println(sql12);
		clinic_registration health = new clinic_registration();
		device_registration health1 = new device_registration();
		String organisation_name = "";
		try {
			System.out.println(password);
			System.out.println("id" + device_id);
			System.out.println(user_type);

			sql12 = "select * from clinic_registration where clinic_id = ? and password = ? and user_type = ?";
			/*
			 * sql12=
			 * "insert into device_registration values('ril1000','Reliance','1234567890','testdevice','active')"
			 * ; jdbcTemplateObject.update(sql12);
			 */
			System.out.println(sql12);
			try {
				health = jdbcTemplateObject.queryForObject(sql12, new Object[] {
						clinic_id, password, cusertype },
						new clinic_registrationmapper());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String sql23 = "select * from clinic_registration where clinic_id = '"
					+ clinic_id
					+ "' and password = '"
					+ password
					+ "' and user_type = '" + user_type + "'";
			System.out.println(health.getOrganisation_name());

			if (health.getClinic_id() != null) {
				sql11 = "select * from device_registration where clinic_id=? and device_id=?";
				String sql112 = "select * from device_registration where clinic_id='"
						+ clinic_id + "' and device_id='" + device_id + "'";
				System.out.println(sql112);
				try {
					health1 = jdbcTemplateObject.queryForObject(sql11,
							new Object[] { clinic_id, device_id },
							new device_registrationmapper());
				} catch (Exception e) {
					// e.printStackTrace();
				}
				if (health1.getDevice_id() != null) {
					if (health1.getDevice_status().equals("1")) {
						// success = 1;
						response.put("status", "success");
						response.put("Organisation Name",
								health.getOrganisation_name());
						response.put("imei", "Active");
					} else {
						response.put("status", "fail");
						response.put("Error Code", "EC");
						response.put("imei", "Device Not Active");
					}

				} else {
					response.put("status", "fail");
					response.put("Error Code", "EC");
					response.put("Message", "Device is not registered");
				}
			} else {
				response.put("status", "fail");
				response.put("Error Code", "EC");
				response.put("Message", "Clinic is not registered");
			}

		} catch (DataAccessException ex) {
			// ex.printStackTrace();

		}
		// System.out.println(response.toString());
		return response;
	}

	public int insertdata(Integer clinic_id, String patient_id,
			String patient_name, String age, String sex, String contact_number,
			String ecg_link, String jio_id, Timestamp ecgtest_date,
			Timestamp serverupload_time, String user_type) {
		org.json.simple.JSONObject response = new org.json.simple.JSONObject();
		String sqlget = "Select * from jioecg_upload where clinic_id=? and patient_name=? and ecgtest_date=?";
		System.out.println(sqlget);
		Timestamp newecgtest_date = null;
		verifylogin_pojo health2 = new verifylogin_pojo();
		int count = 0;
		try {

			health2 = jdbcTemplateObject.queryForObject(sqlget, new Object[] {
					clinic_id, patient_name, ecgtest_date },
					new verifyloginmapper());
			newecgtest_date = health2.getEcgtest_date();

			System.out.println(newecgtest_date);

		} catch (DataAccessException ex) {
			ex.printStackTrace();
		}
		System.out.println(health2.getClinic_id());
		if (health2.getPatient_name() != null && health2.getClinic_id() != 0) {
			System.out.println("Hii");
			if (newecgtest_date.toString().equalsIgnoreCase(
					ecgtest_date.toString())) {
				response.put("status", "fail");
				System.out.println(response.toString());
			} else {
				String sql = "insert into jioecg_upload(clinic_id,patient_name,patient_id,sex,age,jio_id,contact_number,linkdownload,ecgtest_date,serverupload_time,user_type) values(?,?,?,?,?,?,?,?,?,?,?)";

				try {
					System.out.println("started1" + contact_number);
					count = jdbcTemplateObject.update(sql, clinic_id,
							patient_name, patient_id, sex, age, jio_id,
							contact_number, ecg_link, ecgtest_date,
							serverupload_time, user_type);
					System.out.println(count);
					System.out.println("done");
				} catch (DataAccessException ex) {
					ex.printStackTrace();
					// TODO Auto-generated method stub
				}

			}
		} else {
			String sql = "insert into jioecg_upload(clinic_id,patient_name,patient_id,sex,age,jio_id,contact_number,linkdownload,ecgtest_date,serverupload_time,user_type) values(?,?,?,?,?,?,?,?,?,?,?)";

			try {
				System.out.println("started" + contact_number);
				count = jdbcTemplateObject.update(sql, clinic_id, patient_name,
						patient_id, sex, age, jio_id, contact_number, ecg_link,
						ecgtest_date, serverupload_time, user_type);
				System.out.println(count);
				System.out.println("done");
			} catch (DataAccessException ex) {
				ex.printStackTrace();
				// TODO Auto-generated method stub
			}
		}
		return count;
	}

	public server servertype(String id) throws JSONException {
		org.codehaus.jettison.json.JSONObject response = new org.codehaus.jettison.json.JSONObject();
		{
			String sql = "Select * from server_active where id= ? ";
			server sr = new server();
			try {
				sr = jdbcTemplateObject.queryForObject(sql,
						new Object[] { id }, new serverrowmapper());
			} catch (DataAccessException ex) {
				ex.printStackTrace();
			}

			return sr;
		}
	}

	public int register(String clinic_id, String password,
			String organisation_name, String organisation_head,
			String organisation_address, String city, Integer contact_no,
			Integer alternate_contact_no, String email_id,
			String alternate_email_id, String user_type) {

		String sql11 = "Select * from clinic_registration where clinic_id=? and organisation_name=? and password=?";
		clinic_registration reg = null;
		int result1 = 0;
		String oldclinic_id = "";
		String oldOrganisation_name = "";
		
		/*BigInteger phone_no = BigInteger.valueOf(contact_no);
		Integer alt_phone_no = Integer.valueOf(alternate_contact_no);*/
		
		try {
			reg = jdbcTemplateObject.queryForObject(sql11, new Object[] {
					clinic_id, organisation_name, password },
					new clinic_registrationmapper());
			oldclinic_id = reg.getClinic_id();
			oldOrganisation_name = reg.getOrganisation_name();
			System.out.println(reg.getClinic_id());
		} catch (Exception e) {// e.printStackTrace();

		}
		int result = 0;
		if (oldclinic_id.trim().equals("")) {
			try {
				String sql = "insert into clinic_registration(clinic_id,organisation_name,organisation_head,organisation_address,organisation_city,contact_number,alternate_contact_number,email_id,alternate_email_id,password,user_type) values(?,?,?,?,?,?,?,?,?,?,?)";

				result = jdbcTemplateObject.update(sql, clinic_id,
						organisation_name, organisation_head,
						organisation_address, city, contact_no,
						alternate_contact_no, email_id, alternate_email_id,
						password, user_type);
			}

			catch (Exception e) {
				e.printStackTrace();
				result = 2;
			}
		}
		/*
		 * } else result=0;
		 */
		return result;
	}

	public int devicereg(String device_id, String device_name,
			String device_status, String clinic_id, String organisation_name) {
		// TODO Auto-generated method stub

		String sql11 = "Select * from device_registration where clinic_id=? and device_id=?";
		device_registration reg = null;
		int result1 = 0;
		String oldclinic_id = "";
		try {
			reg = jdbcTemplateObject.queryForObject(sql11, new Object[] {
					clinic_id, device_id }, new device_registrationmapper());
			oldclinic_id = reg.getDevice_id();
			System.out.println(reg.getClinic_id());
		} catch (Exception e) {// e.printStackTrace();

		}

		if (oldclinic_id.trim().equals("")) {
			String sql12 = "insert into device_registration(clinic_id,organisation_name,device_id,device_name,device_status) values(?,?,?,?,?)";

			result1 = jdbcTemplateObject.update(sql12, clinic_id,
					organisation_name, device_id, device_name, device_status);
		} else
			result1 = 0;

		return result1;
	}

	public org.codehaus.jettison.json.JSONObject webverify(String clinic_id,
			String password) throws JSONException {
		String sql = "select * from clinic_registration where clinic_id=? and password=?";
		String sql1 = "select * from clinic_registration where clinic_id=?";
		org.codehaus.jettison.json.JSONObject data = new org.codehaus.jettison.json.JSONObject();
		clinic_registration clinicinfo = new clinic_registration();

		try {
			if (password.equalsIgnoreCase("")) {
				clinicinfo = jdbcTemplateObject.queryForObject(sql1,
						new Object[] { clinic_id },
						new clinic_registrationmapper());
				data.put("email_id", clinicinfo.getEmail_id());
				data.put("password", clinicinfo.getPassword());
			} else {
				clinicinfo = jdbcTemplateObject.queryForObject(sql,
						new Object[] { clinic_id, password },
						new clinic_registrationmapper());

				System.out.println(clinicinfo.getOrganisation_name());
				data.put("status", "success");
				data.put("clinic_id", clinicinfo.getClinic_id());
				data.put("organisation_name", clinicinfo.getOrganisation_name());
				data.put("organisation_head", clinicinfo.getOrganisation_head());
				data.put("organisation_address",
						clinicinfo.getOrganisation_address());
				data.put("organisation_city", clinicinfo.getOrganisation_city());
				data.put("contact_number", clinicinfo.getContact_number());
				data.put("alternative_contact_number",
						clinicinfo.getAlternate_contact_number());
				data.put("email_id", clinicinfo.getEmail_id());
				data.put("alternate_email_id",
						clinicinfo.getAlternate_email_id());
				data.put("user_type", clinicinfo.getUser_type());
			}
		} catch (Exception e) {
			// e.printStackTrace();
			data.put("status", "Failed");
		}

		return data;
	}

	public List<Map<String, Object>> clinicdata(String clinic_id,String user_type)
			throws JSONException {
		org.codehaus.jettison.json.JSONObject ecgdata = new org.codehaus.jettison.json.JSONObject();
		JSONArray ja = new JSONArray();
		verifylogin_pojo data = new verifylogin_pojo();
		String sql="";
		if(user_type.equals("1"))
		{
			sql = "select * from jioecg_upload";
		}else
		{
			sql = "select * from jioecg_upload where clinic_id=?";
		}
		List<String> datalist = new ArrayList<String>();
		List<Map<String, Object>> clinic_data = null;
		try {
			if(user_type.equals("1"))
			{
				clinic_data = jdbcTemplateObject.queryForList(sql);
			}else
			{
				clinic_data = jdbcTemplateObject.queryForList(sql,
						new Object[] { clinic_id });
			}
			
			ecgdata.put("status", "success");

			int i = 0;
			if (clinic_data != null && !clinic_data.isEmpty()) {
				for (Map<String, Object> clinical_data : clinic_data) {
					for (Iterator<Map.Entry<String, Object>> it = clinical_data
							.entrySet().iterator(); it.hasNext();) {
						Map.Entry<String, Object> entry = it.next();
						String key = entry.getKey();
						Object value = entry.getValue();
						// System.out.println(key + " = " + value);
						ecgdata.put(key, value);
						// System.out.println(ecgdata.toString());
					}
					// System.out.println();
					// ja.put(ecgdata);
					ja.put(i, ecgdata);
					datalist.add(ecgdata.toString());
					// System.out.println("ja"+ja.toString());
					i++;
				}
			}

			/*
			 * Iterator<String> iterator = datalist.iterator(); while
			 * (iterator.hasNext()) {
			 * //System.out.println("list"+iterator.next()); }
			 */

			/*
			 * ecgdata.put("id",data.getId()); ecgdata.put("clinic_id",
			 * data.getClinic_id()); ecgdata.put("patient_name",
			 * data.getPatient_name());
			 * ecgdata.put("patient_id",data.getPatient_id());
			 * ecgdata.put("sex", data.getSex()); ecgdata.put("age",
			 * data.getJio_id()); ecgdata.put("contact_number",
			 * data.getContact_number()); ecgdata.put("linkdownload",
			 * data.getLinkdownload()); ecgdata.put("ecgtest_date",
			 * data.getEcgtest_date()); ecgdata.put("serverupload_time",
			 * data.getServerupload_time()); ecgdata.put("user_type",
			 * data.getUser_type());
			 */

		} catch (Exception e) {
			e.printStackTrace();
			ecgdata.put("status", "Failed");
		}
		System.out.println("End");
		// return (ArrayList<String>) datalist;
		return clinic_data;
	}

	public List<Map<String, Object>> devicedata(String clinic_id)
			throws JSONException {
		org.codehaus.jettison.json.JSONObject ecgdata = new org.codehaus.jettison.json.JSONObject();
		JSONArray ja = new JSONArray();
		verifylogin_pojo data = new verifylogin_pojo();
		System.out.println(clinic_id);
		String sql = "select * from device_registration where clinic_id=?";
		List<String> datalist = new ArrayList<String>();
		List<Map<String, Object>> devicedata = null;
		try {
			devicedata = jdbcTemplateObject.queryForList(sql,
					new Object[] { clinic_id });
			//System.out.println("present");
			ecgdata.put("status", "success");
			int i = 0;
			if (devicedata != null && !devicedata.isEmpty()) {
				for (Map<String, Object> device_data : devicedata) {
					for (Iterator<Map.Entry<String, Object>> it = device_data
							.entrySet().iterator(); it.hasNext();) {
						Map.Entry<String, Object> entry = it.next();
						String key = entry.getKey();
						Object value = entry.getValue();
						//System.out.println(key + " = " + value);
						ecgdata.put(key, value);
						// System.out.println(ecgdata.toString());
					}
					// System.out.println();
					// ja.put(ecgdata);
					ja.put(i, ecgdata);
					datalist.add(ecgdata.toString());
					// System.out.println("ja"+ja.toString());
					i++;
				}
			}

			/*
			 * Iterator<String> iterator = datalist.iterator(); while
			 * (iterator.hasNext()) {
			 * //System.out.println("list"+iterator.next()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			ecgdata.put("status", "Failed");
		}
		//System.out.println("End");
		// return (ArrayList<String>) datalist;
		return devicedata;
	}

	public org.codehaus.jettison.json.JSONObject getEmailId(String clinic_id) {

		return null;
	}

	public String getlink(String patient_id) {

		String link = "";
		verifylogin_pojo ecg = new verifylogin_pojo();

		String sql = "select * from jioecg_upload where patient_id=?";
		try {
			ecg = jdbcTemplateObject.queryForObject(sql,
					new Object[] { patient_id }, new verifyloginmapper());
		} catch (Exception e) {
			e.printStackTrace();
		}
		link = ecg.getLinkdownload();
		return link;
	}

	public List<Map<String, Object>> regclinic() throws JSONException 
	{
		org.codehaus.jettison.json.JSONObject ecgdata = new org.codehaus.jettison.json.JSONObject();
		JSONArray ja = new JSONArray();
		verifylogin_pojo data = new verifylogin_pojo();
		//System.out.println(clinic_id);
		String sql = "select * from clinic_registration where user_type=0";
		List<String> datalist = new ArrayList<String>();
		List<Map<String, Object>> devicedata = null;
		try {
			devicedata = jdbcTemplateObject.queryForList(sql);
			//System.out.println("present");
			ecgdata.put("status", "success");
			int i = 0;
			if (devicedata != null && !devicedata.isEmpty()) {
				for (Map<String, Object> device_data : devicedata) {
					for (Iterator<Map.Entry<String, Object>> it = device_data
							.entrySet().iterator(); it.hasNext();) {
						Map.Entry<String, Object> entry = it.next();
						String key = entry.getKey();
						Object value = entry.getValue();
						//System.out.println(key + " = " + value);
						ecgdata.put(key, value);
						// System.out.println(ecgdata.toString());
					}
					// System.out.println();
					// ja.put(ecgdata);
					ja.put(i, ecgdata);
					datalist.add(ecgdata.toString());
					// System.out.println("ja"+ja.toString());
					i++;
				}
			}

			/*
			 * Iterator<String> iterator = datalist.iterator(); while
			 * (iterator.hasNext()) {
			 * //System.out.println("list"+iterator.next()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			ecgdata.put("status", "Failed");
		}
		//System.out.println("End");
		// return (ArrayList<String>) datalist;
		return devicedata;
	}

	public int getDeviceStatus( String clinic_id,String device_id, String device_status) 
	{
		String sql="";
		if(device_status.equalsIgnoreCase("Disabled"))
			sql="update device_registration set device_status='1' where device_id=? and clinic_id=?";
		else
			sql="update device_registration set device_status='0' where device_id=? and clinic_id=?";
		
		int success=jdbcTemplateObject.update(sql,new Object[] { device_id,clinic_id });
		System.out.println(success);
		return success;
	}

	public int updateClinicInfo(
			String clinic_id, String password, String organisation_name,
			String organisation_head, String organisation_address, String city,
			int contact_no, int alternate_contact_no, String email_id,
			String alternate_email_id) 
	{
		String sql="",sql11="",sql22="";
		int sql_success=0,success=0;
		int sql22_success;
		sql="update clinic_registration set password=?,organisation_name=?,organisation_head=?,organisation_address=?,organisation_city=?,contact_number=?,alternate_contact_number=?,email_id=?,alternate_email_id=? where clinic_id=?";
		sql22="update device_registration set organisation_name=? where clinic_id=?";
		
		sql22_success=jdbcTemplateObject.update(sql22,new Object[]{organisation_name,clinic_id});
		sql_success=jdbcTemplateObject.update(sql,new Object[] { password,organisation_name,organisation_head,organisation_address,city,contact_no,alternate_contact_no,email_id,alternate_email_id,clinic_id});
		System.out.println(sql_success+"  "+sql22_success);
		if(sql_success==1 && sql22_success==2)
			success=1;
		System.out.println(success);
		return success;
	}
}
