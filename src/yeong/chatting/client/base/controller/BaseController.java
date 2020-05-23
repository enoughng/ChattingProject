package yeong.chatting.client.base.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.thread.ClientThread;
import yeong.chatting.client.thread.ThreadUtil;
import yeong.chatting.client.util.CommandMap;
import yeong.chatting.client.util.MyEvent;
import yeong.chatting.model.Member;
import yeong.chatting.util.Log;

/**
 * @FileName  : BaseController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 클라이언트의 모든 컨트롤러는 Base를 상속받아 사용하는데, 이떄 서버와의 스트림과 action에 대한 클래스의 정보를 갖고있는 Map을 가지고있다.
 * 이를 상속한 컨트롤러에서 사용할 action 메서드를 정의하고 있고 이 action 메서드는 properties파일을 이용한 Map에서 String값으로 구별하여 가져다가 action메서드를 사용하게된다.
 * 
 */
public class BaseController  implements Initializable{

	protected Map<String,CommonAction> commandMap;
	protected Stage stage;
	private CommonAction action;
	
	protected MyEvent event;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCommand();
		event = new MyEvent();
		initEvent();
	}

	protected final void action(ActionInfo info) {
		if(info == null) return;
		action = commandMap.get(info.getCommand());
		action.action(info);
	}
	protected final void initCommand() {
		commandMap = CommandMap.getCommandMap();
	}

	public static void goAction(Control con, String url) {
		if(url == null) return;
		Map<String,CommonAction> map = CommandMap.getCommandMap();
		map.get(url).action(new ActionInfo("Go",con,url));
	}	
	
	protected void setStage(Stage stage) {
		this.stage = stage;
	}
	
	protected void initEvent() {}

}

