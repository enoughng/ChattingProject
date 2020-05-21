package yeong.chatting.client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.ClientThread;
import yeong.chatting.client.util.ThreadUtil;
import yeong.chatting.model.Message;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;
import yeong.chatting.util.ProtocolType;

public class ClientMain extends Application{

	
	@Override
	public void init() throws Exception {
		super.init();
		try { 
			Class.forName("yeong.chatting.client.util.ThreadUtil");
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(CommonPathAddress.LoginLayout));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);


		primaryStage.setScene(scene);
		primaryStage.setTitle("로그인 화면");
		primaryStage.show();
		
		Thread thread = new Thread(new ClientThread(ThreadUtil.getOis(),ThreadUtil.getOos(),primaryStage));
		thread.setDaemon(true);
		thread.start();
	}


	@Override
	public void stop() throws Exception {
		if(ClientInfo.currentRoom != null) {
			Message exitRequest = new Message(ProtocolType.REQUEST_EXITROOM, ClientInfo.currentMember, ClientInfo.currentRoom);
			ThreadUtil.getOos().writeObject(exitRequest);
			Thread.sleep(1000);
			Message logoutRequest = new Message(ProtocolType.REQUEST_LOGOUT, ClientInfo.currentMember);
			ThreadUtil.getOos().writeObject(logoutRequest);				
		}  else if(ClientInfo.currentMember != null){
			Log.i("currentMember : " + ClientInfo.currentMember);
			Message logoutRequest = new Message(ProtocolType.REQUEST_LOGOUT, ClientInfo.currentMember);
			ThreadUtil.getOos().writeObject(logoutRequest);			
		}
		super.stop();
	}
	/**
	 * chatProject 메인 실행
	 */
	public static void main(String[] args) {
		
		launch(args);
	}
}
