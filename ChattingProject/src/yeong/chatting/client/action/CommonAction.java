package yeong.chatting.client.action;

import java.io.ObjectOutputStream;

import javafx.stage.Window;
import yeong.chatting.client.util.ThreadUtil;

public interface CommonAction {
	ObjectOutputStream oos = ThreadUtil.getOos();
	public void action(ActionInfo info);
}
