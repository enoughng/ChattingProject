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
	@FXML Button can;
	
	private ObservableList<Member> memberList;
	
	private static InviteController con;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		initWaitingList();
		con = this;
		
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
		Stage stage = (Stage)can.getScene().getWindow();
		stage.close();
	}
	
	
	private void initWaitingList() {
		memberList = FXCollections.observableArrayList();
		waitingList.setItems(memberList);
		waitingList.setOnMouseClicked(event -> {
			String eventTarget = event.getTarget().toString();
			if(eventTarget.substring(eventTarget.length()-6, eventTarget.length()).contains("'null'")) {
				 waitingList.getSelectionModel().clearSelection();
				 return;
			}
			if(event.getClickCount()==2) {
				invite.fire();
			}
		});
	}
	
	public void setListView(ObservableList<Member> memberList) {
		this.memberList.setAll(memberList);
	}
	
	public static InviteController getCon() {
		return con;
	}
	
}
