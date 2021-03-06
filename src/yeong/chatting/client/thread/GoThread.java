package yeong.chatting.client.thread;

import java.net.URL;

import javafx.application.Platform;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.GoAction;

public class GoThread implements Runnable{
	
	private Stage stage;
	private URL url;
	
	public GoThread(Stage stage, URL url) {
		this.stage = stage;
		this.url = url;
	}
	
	@Override
	public void run() {
		GoAction.staticGo(stage, url);
	}
	
	
}
