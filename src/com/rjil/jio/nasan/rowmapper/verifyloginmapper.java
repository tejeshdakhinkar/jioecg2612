package com.rjil.jio.nasan.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;


import com.rjil.jio.nasan.pojo.verifylogin_pojo;

public class verifyloginmapper implements RowMapper<verifylogin_pojo>
{
 
	verifylogin_pojo health=new verifylogin_pojo();
	
	public verifylogin_pojo mapRow(ResultSet rs, int rownum)
			throws SQLException {
		
		health.setId(rs.getInt("id"));
		health.setClinic_id(rs.getInt("clinic_id"));
		health.setAge(rs.getString("age"));
		health.setPatient_id(rs.getString("patient_id"));
		health.setPatient_name(rs.getString("patient_name"));
		health.setJio_id(rs.getString("jio_id"));
		health.setAge(rs.getString("age"));
		health.setContact_number(rs.getString("contact_number"));
		health.setLinkdownload(rs.getString("linkdownload"));
		health.setEcgtest_date(rs.getTimestamp("ecgtest_date"));
		health.setServerupload_time(rs.getTimestamp("serverupload_time"));
		health.setUser_type(rs.getString("user_type"));
		
		return health;
	}
	

	/*public ecg mapRow(ResultSet rs, int rownum) throws SQLException {
		
		health.setClinic_id(rs.getInt("clinic_id"));
		health.setImei_no(rs.getString("imei_no"));
		health.setUsername(rs.getString("username"));
		health.setPassword(rs.getString("password"));
		health.setClinic_name(rs.getString("clinic_name"));
		health.setDevice_status(rs.getString("device_status"));
		//health.setUser_type(rs.getInt("user_type"));
		health.setAge(rs.getString("age"));
		health.setPatient_id(rs.getString("patient_id"));
		health.setPatient_name(rs.getString("patient_name"));
		health.setJio_id(rs.getString("jio_id"));
		health.setAge(rs.getString("age"));
		health.setContact_number(rs.getString("contact_number"));
		health.setLinkdownload(rs.getString("linkdownload"));
		health.setEcgtest_date(rs.getTimestamp("ecgtest_date"));
		health.setServerupload_time(rs.getTimestamp("serverupload_time"));
		
		return health;
	}
	
*/	

}
