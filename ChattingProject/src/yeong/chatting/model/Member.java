package yeong.chatting.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8515944973869516702L;
	private String id;
	private String password;
	private String  name;
	private String email;
	
	public Member(String id, String password) {
		this.id = (id);
		this.password =(password);
	}
	public Member(String id, String password, String name) {
		this(id,password);
		this.name = (name);
	}
	public Member(String id, String password, String name, String email) {
		this(id, password, name);
		this.email = (email);
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "Member [id=" + id + ", name=" + name + "]";
	}
	
	
	
	
}
