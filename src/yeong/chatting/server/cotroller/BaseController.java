package yeong.chatting.server.cotroller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.server.action.CommonAction;
import yeong.chatting.server.thread.ServerThread;
import yeong.chatting.util.CommandMap;
import yeong.chatting.util.Log;

public class BaseController implements Initializable{
	
	private Map<String,CommonAction> commandMap = new HashMap<>();
	protected ServerThread st;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCommand();
		
		st = new ServerThread();
	}
	
	
	
	
	protected void action(ActionInfo info) {
		CommonAction action = (CommonAction)commandMap.get(info.getCommand());
		action.action(info);
	}
	
	private void initCommand() {
		commandMap = CommandMap.getCommandMap();
	}
}
