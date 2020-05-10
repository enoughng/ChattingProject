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
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : Ŭ���̾�Ʈ�� ��� ��Ʈ�ѷ��� Base�� ��ӹ޾� ����ϴµ�, �̋� �������� ��Ʈ���� action�� ���� Ŭ������ ������ �����ִ� Map�� �������ִ�.
 * �̸� ����� ��Ʈ�ѷ����� ����� action �޼��带 �����ϰ� �ְ� �� action �޼���� properties������ �̿��� Map���� String������ �����Ͽ� �����ٰ� action�޼��带 ����ϰԵȴ�.
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
