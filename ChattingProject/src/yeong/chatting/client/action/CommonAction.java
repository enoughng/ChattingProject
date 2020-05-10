package yeong.chatting.client.action;

import javafx.stage.Window;

public interface CommonAction {
	public default void action() {}
	public default void formAction(Window stage, String URL) {}

}
