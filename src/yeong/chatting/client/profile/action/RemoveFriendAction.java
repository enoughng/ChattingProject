package yeong.chatting.client.profile.action;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.ProtocolType;

public class RemoveFriendAction implements CommonAction {
	
	@Override
	public void action(ActionInfo info) {
		
		
		String id = (String)info.getUserDatas()[0];
		Alert alert = new Alert(AlertType.WARNING, "정말 삭제하시겠습니까?");
		alert.setTitle("경고");
		alert.setHeaderText(id + "님을 친구목록에서 삭제합니다.");
		ButtonType bt1 = new ButtonType("삭제");
		ButtonType bt2 = new ButtonType("취소");
		alert.getButtonTypes().setAll(bt1,bt2);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get()==bt1) {
			ProtocolType requestProtocol = ProtocolType.REQUEST_REMOVE_FRIEND;
			Message request = new Message(requestProtocol,new Member(ClientInfo.currentMember));
			request.setTo(new Member(id,""));
			try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
			return;
		} 
		Alert alert2 = new Alert(AlertType.INFORMATION, "삭제를 취소하셨습니다.");
		ButtonType bt3 = new ButtonType("확인");
		alert2.getButtonTypes().setAll(bt3);
		alert2.setTitle("알림");
		alert2.setHeaderText("사용자 요청");
		alert2.show();
		
		
		
		
//		Message request = new Message
		
	}
}
