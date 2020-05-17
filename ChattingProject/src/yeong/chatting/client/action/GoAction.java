package yeong.chatting.client.action;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import yeong.chatting.client.chattingroom.ChattingRoomController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
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

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}
	
	public static void LoginGo(Stage stage, URL url) {
		FXMLLoader loader = new FXMLLoader(url);
		Parent form;
		try {
			form = (Parent)loader.load();
			Scene scene = new Scene(form);
			Stage primaryStage = stage;
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}

	public static void WaitingRoomGo(Stage stage, URL url) {
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(url);
			Parent regiForm = (Parent)loader.load();
			WaitingRoomController con = loader.getController();
			con.setListView(ClientInfo.waitingRoomMemberList);
			con.setTableView(ClientInfo.waitingRoomList);
			Scene scene = new Scene(regiForm);
			stage.setScene(scene);
			stage.show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void ChattingRoomGo(Stage stage, URL url) {
		FXMLLoader loader;
		try {
			loader = new FXMLLoader(url);
			Parent regiForm = (Parent)loader.load();
			ChattingRoomController con = loader.getController();
			//			con.setListView(ClientInfo.chattingRoomMemberList);
			Scene scene = new Scene(regiForm);
			stage.setScene(scene);
			stage.show();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
