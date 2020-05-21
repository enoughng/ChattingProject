package yeong.chatting.model;

import java.io.Serializable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import yeong.chatting.client.util.Place;

public class Member implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8515944973869516702L;
	private String id;
	private String password;
	private String name;
	private String email;
	private String isLogin;
	
	private Place place;
	
	
	
	
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
	
	
	
	public String getLogin() {
		return isLogin;
	}

	public void setLogin(String isLogin) {
		this.isLogin = isLogin;
	}
	
	public Place isWaitingRoom() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
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
	
	
	public Place getPlace() {
		return place;
	}
	@Override
	public String toString() {
		return name + "(" + id +")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Member) {
			Member m = (Member) obj;
			if(id.equals(m.id)) {
				return true;				
			}
		}
		return false;
	}
	
	
}
