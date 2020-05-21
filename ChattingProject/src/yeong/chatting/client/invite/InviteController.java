package yeong.chatting.client.invite;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.model.Member;
import yeong.chatting.util.Log;

public class InviteController extends BaseController {
	
	@FXML ListView<Member> waitingList;
	@FXML Button invite;
	
	private ObservableList<Member> memberList;
	
	private static InviteController con;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		initWaitingList();
		con = this;
		Log.i(getClass(), "initial »£√‚" + con);
		
	}
	
	
	
	@FXML
	private void click() {
		ClientInfo.currentMember.setPlace(Place.ChattingRoom);
		ActionInfo info = new ActionInfo("Invite",invite); 
		info.setUserDatas(waitingList.getSelectionModel().getSelectedItem());
		action(info);
	}
	@FXML
	private void cancel() {
		ClientInfo.currentMember.setPlace(Place.ChattingRoom);
//		Stage stage = (Stage)cancel.getScene().getWindow();
		stage.close();
	}
	
	
	private void initWaitingList() {
		memberList = FXCollections.observableArrayList();
		waitingList.setItems(memberList);
	}
	
	public void setListView(ObservableList<Member> memberList) {
		Log.i(getClass(), memberList.toString());
		this.memberList.setAll(memberList);
	}
	
	public static InviteController getCon() {
		return con;
	}
	
}
