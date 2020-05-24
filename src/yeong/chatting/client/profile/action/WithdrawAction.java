package yeong.chatting.client.profile.action;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.Member;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class WithdrawAction implements  CommonAction{
	@Override
	public void action(ActionInfo info) {
		
		Alert alert = new Alert(AlertType.WARNING, "���� �����ϽÁٽ��ϱ�?");
		alert.setContentText("���̵� �����ϸ� ������ �� �����ϴ� ");
		ButtonType bt1 = new ButtonType("Ȯ��");
		ButtonType bt2 = new ButtonType("���");
		alert.setTitle("���");
		alert.getButtonTypes().setAll(bt1,bt2);
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get()==bt1) {
			Message message = new Message(ProtocolType.REQUEST_DELETE_ACCOUNT, new Member(ClientInfo.currentMember));
			try {
				oos.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Alert alert2 = new Alert(AlertType.INFORMATION, "������ ��ҵǾ����ϴ�.");
			alert.setTitle("�˸�");
			alert.setHeaderText("����� ���");
			alert2.showAndWait();
		}
	}
}
