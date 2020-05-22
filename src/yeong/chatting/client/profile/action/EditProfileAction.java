package yeong.chatting.client.profile.action;

import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.model.ChattingProfile;
import yeong.chatting.model.Message;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class EditProfileAction implements CommonAction {
	
	TextField id;
	TextField name;
	TextArea introduce;
	boolean cancel = false;
	@Override
	public void action(ActionInfo info) {
		

		name = (TextField)info.getCons()[0];
		id = (TextField)info.getCons()[1];
		introduce = (TextArea)info.getCons()[2];
		Button edit = (Button)info.getCons()[3];
		
		String strId = id.getText();
		String strName = name.getText();
		String strIntroduce = introduce.getText();
		
		if(edit.getText().equals("������ ����")) {
			name.textProperty().unbind();
			name.setEditable(true);
			introduce.setEditable(true);			
			edit.setText("������ ���� �Ϸ�");
		} else if (edit.getText().equals("������ ���� �Ϸ�")){
			
			blankAlert(name); // �����Ѱ� ����˻�
			blankAlert(introduce); // 
			if(cancel) return;

			name.setEditable(false);
			introduce.setEditable(false);
			
			edit.setText("������ ����");
			
			Message request = new Message(ProtocolType.REQUEST_PROFILE_EDIT);
			request.setFrom(ClientInfo.currentMember);
			request.setProfile(new ChattingProfile(strName, strId, strIntroduce));
			if(ClientInfo.currentRoom != null) request.setrInfo(ClientInfo.currentRoom);
			try { oos.writeObject(request); } catch (IOException e) { e.printStackTrace(); }
		}
		
		
	}
	
	
	private void blankAlert(TextInputControl c) {
		if(c.getText().trim().length()==0) {
			cancel=true;
			Alert alert = new Alert(AlertType.ERROR, "��ĭ�� ä���ּ���");
			alert.setTitle("����");
			alert.setHeaderText("����");
			ButtonType bt1 = new ButtonType("Ȯ��");
			alert.getButtonTypes().setAll(bt1);
			alert.showAndWait();
			c.requestFocus();
		} 
	}
}
