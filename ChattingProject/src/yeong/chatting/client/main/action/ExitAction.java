package yeong.chatting.client.main.action;

import javafx.application.Platform;
import javafx.stage.Window;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.util.Log;

public class ExitAction implements CommonAction{
	@Override
	public void action() {
		Log.i(getClass(),"ExitAction ½ÇÇà");
		Platform.exit();
	}
}
