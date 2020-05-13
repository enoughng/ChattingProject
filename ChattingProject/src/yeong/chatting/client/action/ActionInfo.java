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
	 * @param command : properties에 저장된 Aciton이름
	 * @param con : 해당 컨트롤러
	 */
	public ActionInfo(String command, Control con) {
		this.command = command;
		this.con = con;
		destination=null;
		initAction(command);
	}
	
	/**
	 * @param command : properties에 저장된 Aciton이름
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
	
	
	/** 해당 액션 실행 변수 */
	public String getCommand() {
		return command;
	}
	/** 화면전환 및 폼 띄우기를 위한 primaryStage */
	public Stage getPrimaryStage() {
		return (Stage)con.getScene().getWindow();
	}
	
	/** 화면전환이나 폼띄우기를 위한 절대 경로 */
	public String getAbsoluteDestination() {
		return getClass().getResource(destination).toExternalForm();
	}
	
	/** 화면전환이나 폼띄우기를 위한  상대경로로 만든 URL */
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
