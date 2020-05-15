package yeong.chatting.client.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import yeong.chatting.client.controller.BaseController;
import yeong.chatting.client.waitingroom.WaitingRoomController;
import yeong.chatting.model.Member;
import yeong.chatting.util.Log;

public class ClientInfo {
//	public static ArrayList<Member> memberList = new ArrayList<>();
	public static Member currentMember;
	
	public static Control[] getController(URL url) throws IOException {
		FXMLLoader loader = new FXMLLoader(url);
		loader.load();
		BaseController con = (BaseController) loader.getController();
		return con.getControls();
	}
}
