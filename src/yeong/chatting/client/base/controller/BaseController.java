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
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : Ŭ���̾�Ʈ�� ��� ��Ʈ�ѷ��� Base�� ��ӹ޾� ����ϴµ�, �̋� �������� ��Ʈ���� action�� ���� Ŭ������ ������ �����ִ� Map�� �������ִ�.
 * �̸� ����� ��Ʈ�ѷ����� ����� action �޼��带 �����ϰ� �ְ� �� action �޼���� properties������ �̿��� Map���� String������ �����Ͽ� �����ٰ� action�޼��带 ����ϰԵȴ�.
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

