package com.rjil.jio.nasan.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rjil.jio.nasan.pojo.device_registration;

	public class device_registrationmapper implements RowMapper<device_registration>
	{
		device_registration reg=new device_registration();
		public device_registration mapRow(ResultSet rs, int rownum) throws SQLException
		{
			
			reg.setDevice_id(rs.getString("device_id"));
			reg.setDevice_name(rs.getString("device_name"));
			reg.setDevice_status(rs.getString("device_status"));
			reg.setClinic_id(rs.getString("clinic_id"));
			reg.setOrganisation_name(rs.getString("organisation_name"));
			
			return reg;
}
	}
