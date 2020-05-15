package yeong.chatting.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MemberBeans {
	private StringProperty id;
	private StringProperty password;
	private StringProperty email;
	private StringProperty name;

	
	public MemberBeans(Member member) {
		id = new SimpleStringProperty(member.getId());
		password = new SimpleStringProperty(member.getPassword());
		email = new SimpleStringProperty(member.getEmail());
		name  = new SimpleStringProperty(member.getName());
	}
	
	public void setId(String id) {
		this.id.set(id);
	}

	public String getId() {
		return id.get();
	}
	public void setPassword(String Password) {
		this.password.set(Password);
	}

	public String getPassword() {
		return this.password.get();
	}

	public void setEmail(String email) {
		this.email.set(email);
	}

	public String getEmail() {
		return email.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getName() {
		return name.get();
	}


}
