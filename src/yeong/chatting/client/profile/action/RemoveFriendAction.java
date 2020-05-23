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
		Alert alert = new Alert(AlertType.WARNING, "���� �����Ͻðڽ��ϱ�?");
		alert.setTitle("���");
		alert.setHeaderText(id + "���� ģ����Ͽ��� �����մϴ�.");
		ButtonType bt1 = new ButtonType("����");
		ButtonType bt2 = new ButtonType("���");
		alert.getButtonTypes().setAll(bt1,bt2);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get()==bt1) {
			ProtocolType requestProtocol = ProtocolType.REQUEST_REMOVE_FRIEND;
			Message request = new Message(requestProtocol,new Member(ClientInfo.currentMember));
			request.setTo(new Member(id,""));
			try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
			return;
		} 
		Alert alert2 = new Alert(AlertType.INFORMATION, "������ ����ϼ̽��ϴ�.");
		ButtonType bt3 = new ButtonType("Ȯ��");
		alert2.getButtonTypes().setAll(bt3);
		alert2.setTitle("�˸�");
		alert2.setHeaderText("����� ��û");
		alert2.show();
		
		
		
		
//		Message request = new Message
		
	}
}
