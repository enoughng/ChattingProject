package yeong.chatting.client.main.action;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import yeong.chatting.client.action.ActionInfo;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.client.util.ClientThread;
import yeong.chatting.client.util.ThreadUtil;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

/**
 * @FileName  : LoginAction.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 08.(레이아웃) -> 2020. 5. 12.
 * @작성자      : Yeong
 * @변경이력 : 수신은 ClientThread Class로 따로 빼놓았다. (본 클래스에서는 송신만 담당한다.)
 * @프로그램 설명 : 로그인 작업에 대한 요청 
 */
public class LoginAction implements CommonAction {

	@Override
	public void action(ActionInfo info) {
		checkInfo(info); // 
	}
	private void checkInfo(ActionInfo info) {
		
		TextInputControl idTf = (TextField)info.getCons()[0];
		TextInputControl pwPf = (PasswordField)info.getCons()[1];
		String inputId = idTf.getText(); 
		String inputPw = pwPf.getText();
		
		Message m = new Message.mBuilder(ProtocolType.REQUEST_LOGIN, new Member(inputId, inputPw))
				.build();
		
		try {
			oos.writeObject(m);
			Log.i(getClass(),"로그인 요청이 성공적으로 이루어졌습니다.");
		} catch (IOException e) {
			Log.e(getClass(), e);
		}
	}
	
	
	
}
