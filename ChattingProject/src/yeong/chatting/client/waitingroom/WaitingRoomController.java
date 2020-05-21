package yeong.chatting.client.waitingroom;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.client.util.ClientInfo;
import yeong.chatting.client.util.Place;
import yeong.chatting.model.Member;
import yeong.chatting.model.MemberBeans;
import yeong.chatting.model.RoomInfo;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * @FileName  : WaitingRoomController.java
 * @Project     : ChattingProject
 * @Date         : 2020. 5. 10.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : ���ǿ� ���� Ŭ����
 */
public class WaitingRoomController extends BaseController {
	
	@FXML TableView<RoomInfo> roomList;
	@FXML ListView<Member> memberList;

	@FXML Button createBtn;
	@FXML Button enterBtn;
	@FXML Button logoutBtn;
	
	private ObservableList<Member> members;
	private ObservableList<RoomInfo> roomInfos;
	
	private static WaitingRoomController con;
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			super.initialize(location, resources);
			con = this;
			initListView();
			initTableView();
		}
	
	@FXML
	private void create() {
		ClientInfo.currentMember.setPlace(Place.CreateRoom);
		action(new ActionInfo("Go",createBtn,CommonPathAddress.CreateRoomLayout));
		
	}
	
	@FXML
	private void enter() {
		ClientInfo.currentMember.setPlace(Place.ChattingRoom);
		ActionInfo info = new ActionInfo("EnterRoom", enterBtn, CommonPathAddress.ChattingRoomLayout);
		if(roomList.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR, "�� ���� �������ֽʽÿ�");
			alert.setHeaderText("���� �������ּ��� : ");
			alert.setTitle("����");
			alert.showAndWait();
			return;
		}
		info.setUserDatas(roomList.getSelectionModel().getSelectedItem());
		action(info);
	}
	
	@FXML
	private void logout() {
		ClientInfo.currentMember.setPlace(Place.LoginLayout);
		action(new ActionInfo("Logout",logoutBtn,CommonPathAddress.LoginLayout));
		action(new ActionInfo("Go",logoutBtn,CommonPathAddress.LoginLayout));

	}
	
	//ȭ���� ���������
	@FXML
	public void getMembers() {
		ActionInfo action = new ActionInfo("RequestCurrentMember",memberList);
		action(action);
	}
	
	/**
	 * ��� ����
	 */
	public void setListView(ObservableList<Member> list) {
		members.clear(); 
		members.addAll(list);
	}
	
	/**
	 * �� ���� ����
	 */
	public void setTableView(ObservableList<RoomInfo> list) {
		roomInfos.clear();
		roomInfos.addAll(list);
	}
	
	public static WaitingRoomController getController() {
		return con;
	}
	
	
	/**
	 * ListView�� ObservableList<Member> ����
	 */
	private void initListView() {
		members = FXCollections.observableArrayList();
		memberList.setItems(members);
	}
	
	
	
	/**
	 * TableView�� ObservableList<RoomInfo> ����
	 */
	private void initTableView() {
		roomInfos = FXCollections.observableArrayList();
		roomList.setItems(roomInfos);
		
		TableColumn<RoomInfo, Integer> rIndex = new TableColumn<RoomInfo, Integer>("�� ��ȣ");
		TableColumn<RoomInfo, String> rTitle = new TableColumn<RoomInfo, String>("�� ����");
		TableColumn<RoomInfo, Integer> rMembers = new TableColumn<RoomInfo, Integer>("�ο� ��");
		TableColumn<RoomInfo, String > rHost = new TableColumn<RoomInfo, String>("����");
		
		rIndex.setCellValueFactory(new PropertyValueFactory<RoomInfo, Integer>("room_num"));
		rTitle.setCellValueFactory(new PropertyValueFactory<RoomInfo, String>("room_title"));
		rMembers.setCellValueFactory(new PropertyValueFactory<RoomInfo, Integer>("room_members"));
		rHost.setCellValueFactory(new PropertyValueFactory<RoomInfo, String>("room_host"));
		
		rIndex.prefWidthProperty().bind(roomList.prefWidthProperty().multiply(0.25));
		rTitle.prefWidthProperty().bind(roomList.prefWidthProperty().multiply(0.3));
		rMembers.prefWidthProperty().bind(roomList.prefWidthProperty().multiply(0.25));
		rHost.prefWidthProperty().bind(roomList.prefWidthProperty().multiply(0.2));
		
		roomList.getColumns().setAll(rIndex, rTitle, rMembers, rHost);
		
	}
	
}
