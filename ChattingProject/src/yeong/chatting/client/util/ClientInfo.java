package yeong.chatting.client.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.Log;

public class ClientInfo {
	
	//���� �α��� �� ���
	public static Member currentMember;
	// ���� ������ ��
	public static ObservableList<Member> waitingRoomMemberList;
	// ���� �� ����
	public static ObservableList<RoomInfo> waitingRoomList;
	//���� ������� ���� ��
	public static RoomInfo currentRoom;
	public static ObservableList<Member> chattingRoomMemberList;
	
//	public static <T extends BaseController> T getController(URL url) {
//		
//		FXMLLoader loader = new FXMLLoader(url);
//		try {
//			loader.load();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.exit(0);
//		}
//		Log.i(loader.getLocation());
//		T con = loader.getController();
//		
//		return con;
//	}
}
