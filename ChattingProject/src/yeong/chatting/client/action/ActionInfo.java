package yeong.chatting.client.action;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;
import yeong.chatting.client.util.CommandMap;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;

public class ActionInfo {
	
	private static Map<String,CommonAction> commandMap;
	
	static {
		commandMap = CommandMap.getCommandMap();
	}
	
	private Member currentMember;
	private String command;
	private Control con;
	private String destination;
	private CommonAction ca;
	
	private Control[] cons;
	/**
	 * @param command : properties�� ����� Aciton�̸�
	 * @param con : �ش� ��Ʈ�ѷ�
	 */
	public ActionInfo(String command, Control con) {
		this.command = command;
		this.con = con;
		destination=null;
		initAction(command);
	}
	
	/**
	 * @param command : properties�� ����� Aciton�̸�
	 * @param destination : String URL
	 */
	public ActionInfo(String command, Control con, String destination) {
		this(command,con);
		this.destination=destination;
	}
	
	
	public void setCons(Control... cons) {
		this.cons = cons;
	}
	
	public Control[] getCons() {
		return cons;
	}
	
	
	/** �ش� �׼� ���� ���� */
	public String getCommand() {
		return command;
	}
	/** ȭ����ȯ �� �� ���⸦ ���� primaryStage */
	public Stage getPrimaryStage() {
		return (Stage)con.getScene().getWindow();
	}
	
	/** ȭ����ȯ�̳� �����⸦ ���� ���� ��� */
	public String getAbsoluteDestination() {
		return getClass().getResource(destination).toExternalForm();
	}
	
	/** ȭ����ȯ�̳� �����⸦ ����  ����η� ���� URL */
	public URL getURL() {
		return getClass().getResource(destination);
	}
	
	public void setCurrentMember(Member currentMember) {
		this.currentMember = currentMember;
	}
	
	public Member getCurrentMember() {
		return currentMember;
	}
	
	
	public void goAction(String destination) {
		this.command = "Go";
		this.destination = destination;
		initAction(command);
		ca.action(this);
	}
	
	public void closeAction() {
		getPrimaryStage().close();
	}
	
	
	private void initAction(String command) {
		ca = (CommonAction)commandMap.get(command);
	}
	
	
}
