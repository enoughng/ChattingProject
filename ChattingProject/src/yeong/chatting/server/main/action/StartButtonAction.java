package yeong.chatting.server.main.action;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.server.action.CommonAction;
import yeong.chatting.server.thread.ServerThread;
import yeong.chatting.util.Log;

public class StartButtonAction implements CommonAction {
	
	ServerThread t = new ServerThread();
	Button toggle;
	TextArea log;
	@Override
	public void action(ActionInfo info) {
		
		log = (TextArea)info.getCons()[0];
		toggle = (Button)info.getCons()[1];
		
		Log.i(toggle.getText());
		
		if(toggle.getText().equals("���� ����")) {
			t.start();
			setText("���� ����");
			appendLog("[���� ����]" + "\n");
		} else {
			t.close();
			setText("���� ����");
			appendLog("[���� ����]"+ "\n");
		}
	}
	
	private void setText(String str) {
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				toggle.setText(str);
			}
		});
	}
	
	private void appendLog(String str) {
		Platform.runLater(()-> {
			log.appendText(str);
		});
	}
}
