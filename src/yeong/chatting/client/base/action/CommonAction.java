package yeong.chatting.client.base.action;

import java.io.ObjectOutputStream;

import javafx.stage.Window;
import yeong.chatting.client.thread.ThreadUtil;

public interface CommonAction {
	ObjectOutputStream oos = ThreadUtil.getOos();
	public void action(ActionInfo info);
}
