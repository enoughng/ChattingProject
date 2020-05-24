package yeong.chatting.client.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.main.ClientMain;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
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
		
	public static URL getResource(String common) {
		return ClientMain.class.getResource(common);
	}
	
	
}
