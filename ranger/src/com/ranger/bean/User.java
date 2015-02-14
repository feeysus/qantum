package com.ranger.bean;


import java.io.Serializable;

/**
 * 
 * 
 * @author XX
 * 
 */

public class User implements Serializable {
  
	private static final long serialVersionUID = 1L;

	private Integer id;

	private String login_name;

	private String login_pwd;
 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin_name() {
		return login_name;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public String getLogin_pwd() {
		return login_pwd;
	}

	public void setLogin_pwd(String login_pwd) {
		this.login_pwd = login_pwd;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getAdcd() {
		return adcd_id;
	}

	public void setAdcd(String adcd) {
		this.adcd_id = adcd;
	}

	private Integer role_id;

	private String user_name;

	private String adcd_id;
 
}
