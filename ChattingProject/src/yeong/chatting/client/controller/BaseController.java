package yeong.chatting.client.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.util.CommandMap;
import yeong.chatting.client.util.ThreadUtil;
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
	
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	protected Map<String,CommonAction> commandMap;
	private CommonAction action;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initStream();
		initCommand();
		Log.i("map size : " + commandMap.size());
	}
	
	
	protected void action(String command) {
		if(command == null) return;
		action = commandMap.get(command);
		action.action();
	}
	
	protected void formAction(String command, Control con, String destination) {
		if(command == null) return;
		action = commandMap.get(command);
		action.formAction(con.getScene().getWindow(), destination);

	}

	private void initStream() {
		ois = ThreadUtil.getOis();
		oos = ThreadUtil.getOos();
	}
	
	private void initCommand() {
		commandMap = CommandMap.getCommandMap();
	}
}
