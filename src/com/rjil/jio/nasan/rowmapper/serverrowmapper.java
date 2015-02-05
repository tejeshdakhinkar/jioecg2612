package com.rjil.jio.nasan.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.rjil.jio.nasan.pojo.server;
import com.rjil.jio.nasan.pojo.verifylogin_pojo;

public class serverrowmapper implements RowMapper<server>
{



	public server mapRow(ResultSet rs, int rownum) throws SQLException {
		// TODO Auto-generated method stub
		
		server sr = new server();
		
		sr.setId(rs.getString("id"));
		sr.setServer(rs.getString("server_type"));
		sr.setFile_location(rs.getString("file_location"));
		
		return sr;
	}

}
