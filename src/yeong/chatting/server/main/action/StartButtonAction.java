package yeong.chatting.server.main.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputControl;
import yeong.chatting.client.base.action.ActionInfo;
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
		
		if(toggle.getText().equals("서버 시작")) {
			t.start();
			setText("서버 중지");
			appendLog("서버를 시작합니다" + "\n");
		} else {
			t.close();
			setText("서버 시작");
			appendLog("서버를 중지합니다"+ "\n");
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
			Date now = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String time = sdf.format(now);
			log.appendText("["+time +"] " + str);
		});
	}
	
	public TextArea getLog() {
		return log;
	}
}
