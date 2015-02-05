package com.rjil.jio.nasan.pojo;

import java.sql.Timestamp;

public class verifylogin_pojo
{
	int id;
	int clinic_id;
	String patient_name;
	String patient_id;
	String jio_id;
	String linkdownload;
	Timestamp ecgtest_date;
	Timestamp serverupload_time;
	String contact_number;
	String sex;
	String age;
	String user_type;
	
		
	public String getUser_type() {
		return user_type;
	}
	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getClinic_id() {
		return clinic_id;
	}
	public void setClinic_id(int clinic_id) {
		this.clinic_id = clinic_id;
	}
	public String getPatient_name() {
		return patient_name;
	}
	public void setPatient_name(String patient_name) {
		this.patient_name = patient_name;
	}
	public String getPatient_id() {
		return patient_id;
	}
	public void setPatient_id(String patient_id) {
		this.patient_id = patient_id;
	}
	public String getJio_id() {
		return jio_id;
	}
	public void setJio_id(String jio_id) {
		this.jio_id = jio_id;
	}
	public String getLinkdownload() {
		return linkdownload;
	}
	public void setLinkdownload(String linkdownload) {
		this.linkdownload = linkdownload;
	}
	public Timestamp getEcgtest_date() {
		return ecgtest_date;
	}
	public void setEcgtest_date(Timestamp ecgtest_date) {
		this.ecgtest_date = ecgtest_date;
	}
	public Timestamp getServerupload_time() {
		return serverupload_time;
	}
	public void setServerupload_time(Timestamp serverupload_time) {
		this.serverupload_time = serverupload_time;
	}
	public String getContact_number() {
		return contact_number;
	}
	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	

}
