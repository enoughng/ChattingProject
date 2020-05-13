package yeong.chatting.client.waitingroom.action;

import javafx.stage.Window;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.util.Log;

public class LogoutAction implements CommonAction{
	
	@Override
	public void action(ActionInfo info) {
		Log.i(getClass(),"formAction ½ÇÇà");
	}
}
