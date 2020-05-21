package yeong.chatting.client.waitingroom.action;

import java.io.IOException;
import java.util.Vector;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.main.ClientMain;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;


/**
 * 실수했음 바꾸는건 ClientThread에서 하는일이지 여기에서하는일이 아님
 * 모든 Action은 Output만 날려줄것.
 * @author Yeong
 *
 */
public class RequestCurrentMemberAction implements CommonAction{

	@Override
	public void action(ActionInfo info) {
		Message request = new Message(ProtocolType.REQUEST_WAITINGROOM_MEMBER, info.getCurrentMember());
		try {
			oos.writeObject(request);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i(getClass(),"액션 실행됨");
	}
}
