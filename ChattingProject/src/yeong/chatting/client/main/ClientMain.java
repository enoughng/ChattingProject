package yeong.chatting.client.main;

import java.util.Vector;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.client.util.ClientThread;
import yeong.chatting.client.util.ThreadUtil;
import yeong.chatting.model.Member;
import yeong.chatting.util.CommonPathAddress;

public class ClientMain extends Application{
	
	private static Vector<Member> currentMemberList;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(CommonPathAddress.LoginLayout));
		Parent root = (Parent)loader.load();
		Scene scene = new Scene(root);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		Thread thread = new Thread(new ClientThread(ThreadUtil.getOis(),ThreadUtil.getOos(),primaryStage));
		thread.setDaemon(true);
		thread.start();
		
	}
	
	
	/**
	 * chatProject ����
	 */
	public static void main(String[] args) {
		try {
			Class.forName("yeong.chatting.client.util.ThreadUtil");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		launch(args);
	}
	
	public static void setList(Vector<Member> list) {
		currentMemberList = list;
	}
	
	public static Vector<Member> getList() {
		return currentMemberList;
	}
}
