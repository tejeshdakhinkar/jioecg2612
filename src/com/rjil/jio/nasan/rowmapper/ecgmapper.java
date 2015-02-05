package com.rjil.jio.nasan.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rjil.jio.nasan.pojo.ecg;

public class ecgmapper implements RowMapper<ecg>
{
	ecg health=new ecg();

	public ecg mapRow(ResultSet rs, int rownum) throws SQLException {
		
		health.setClinic_id(rs.getString("clinic_id"));
		health.setDevice_id(rs.getString("device_id"));
		health.setPassword(rs.getString("password"));
		health.setClinic_name(rs.getString("clinic_name"));
		health.setDevice_status(rs.getString("device_status"));
		health.setOrganisation_name(rs.getString("organisation_name"));
		health.setOrganisation_head(rs.getString("organisation_head"));
		health.setOrganisation_address(rs.getString("organisation_address"));
		health.setContact_number(rs.getString("Contact_number"));
		health.setAlternate_contact_number(rs.getString("alternate_contact_number"));
		health.setEmail_id(rs.getString("email_id"));
		health.setAlternate_email_id(rs.getString("alternate_email_id"));
		health.setUser_type(rs.getString("user_type"));
		
		return health;
	}
	
	

}
