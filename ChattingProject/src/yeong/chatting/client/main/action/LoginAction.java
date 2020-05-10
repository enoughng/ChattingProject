package yeong.chatting.client.main.action;

import javafx.stage.Window;
import yeong.chatting.client.action.CommonAction;
import yeong.chatting.util.Log;

/**
 * @FileName  : LoginAction.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 로그인 작업에 대한 요청
 */
public class LoginAction implements CommonAction {
		@Override
		public void formAction(Window primaryStage, String url) {
			Log.i(getClass(),"LoginAction 실행");
		}
}
