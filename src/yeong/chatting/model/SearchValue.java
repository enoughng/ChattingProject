package yeong.chatting.model;

import java.io.Serializable;

public class SearchValue implements Serializable {
	private String email;
	private String name;
	private String id;
	private String password;
	
	public SearchValue() {
		// TODO Auto-generated constructor stub
	}
	
	public SearchValue(SearchValue sv) {
		this.email = sv.email;
		this.name = sv.name;
		this.id = sv.id;
		this.password = sv.password;
	}
	
	public SearchValue(String email, String name) {
		this.email = email;
		this.name = name;
	}
	
	public SearchValue(String email, String name, String id) {
		this.email = email;
		this.name = name;
		this.id = id;
	}




	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
