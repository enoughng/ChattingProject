package yeong.chatting.client.util;

import java.net.URL;

import javafx.stage.Stage;
import yeong.chatting.client.action.GoAction;

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
