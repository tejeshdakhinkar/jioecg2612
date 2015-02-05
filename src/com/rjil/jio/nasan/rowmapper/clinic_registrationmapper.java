package com.rjil.jio.nasan.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rjil.jio.nasan.pojo.clinic_registration;

public class clinic_registrationmapper implements RowMapper<clinic_registration>
{
	clinic_registration reg=new clinic_registration();
	public clinic_registration mapRow(ResultSet rs, int rownum) throws SQLException
	{
		
		
		reg.setClinic_id(rs.getString("clinic_id"));
		reg.setOrganisation_name(rs.getString("organisation_name"));
		reg.setOrganisation_head(rs.getString("organisation_head"));
		reg.setOrganisation_address(rs.getString("organisation_address"));
		reg.setOrganisation_city(rs.getString("organisation_city"));
		reg.setContact_number(rs.getString("contact_number"));
		reg.setAlternate_contact_number(rs.getString("alternate_contact_number"));
		reg.setEmail_id(rs.getString("email_id"));
		reg.setAlternate_email_id(rs.getString("alternate_email_id"));
		reg.setPassword(rs.getString("password"));
		reg.setUser_type(rs.getString("user_type"));
		return reg;
	}
	

}
