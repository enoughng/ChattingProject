package yeong.chatting.client.main.action;

import javafx.application.Platform;
import javafx.stage.Window;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.util.Log;

public class ExitAction implements CommonAction{
	@Override
	public void action(ActionInfo info) {
		Platform.exit();
	}
}
