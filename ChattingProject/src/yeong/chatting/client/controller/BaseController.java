package yeong.chatting.client.controller;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.util.CommandMap;
import yeong.chatting.client.util.ThreadUtil;
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
	
	protected ObjectInputStream ois;
	protected ObjectOutputStream oos;
	private Map<String,CommonAction> commandMap;
	private CommonAction action;
	
	public static Member currnetMember;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initStream();
		initCommand();
	}
	
	protected void action(ActionInfo info) {
		if(info == null) return;
		action = commandMap.get(info.getCommand());
		action.action(info);
	}

	private void initStream() {
		ois = ThreadUtil.getOis();
		oos = ThreadUtil.getOos();
	}
	
	private void initCommand() {
		commandMap = CommandMap.getCommandMap();
	}
}
