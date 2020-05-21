package yeong.chatting.client.base.action;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class GoAction implements CommonAction {
	@Override
	public void action(ActionInfo info) {
		go(info);
	}
	


	private void go(ActionInfo info) {
		try {
			FXMLLoader loader = new FXMLLoader(info.getURL());
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			
			
			Stage primaryStage = (Stage)info.getPrimaryStage();
			if(info.getURL().toString().indexOf(CommonPathAddress.LoginLayout) != -1) {
				primaryStage.setTitle("�α��� ȭ��");				
			} else if(info.getURL().toString().indexOf("CreateRoomLayout.fxml") != -1) {
//				CreateRoomController con = loader.getController();
//				con.setStage(info.getPrimaryStage());
				info.getPrimaryStage().setTitle("ä�ù� �����");
			}
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}
	
	public static void staticGo(Stage stage, URL url) {
		FXMLLoader loader = new FXMLLoader(url);
		Parent root;
		
		try {
			
			root = (Parent)loader.load();
			Scene scene = new Scene(root);
			if(url.toString().indexOf("WaitingRoomLayout.fxml") != -1) {
				try {
				stage.setTitle("���� : " + ClientInfo.currentMember.getName());
				} catch(NullPointerException e) {
					
				}
			} else if(url.toString().indexOf("CreateRoomLayout.fxml") != -1) {
//				CreateRoomController con = loader.getController();
//				con.setStage(stage);
				stage.setTitle("ä�ù� �����");
			} else if(url.toString().indexOf(CommonPathAddress.ChattingRoomLayout) != -1) {
				stage.setTitle(ClientInfo.currentRoom.getRoom_num()+ " �� ä�ù�" + " : " + ClientInfo.currentMember.getName() + "��");
			}
			
			
			stage.setScene(scene);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

}
