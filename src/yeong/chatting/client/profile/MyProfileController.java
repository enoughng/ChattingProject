package yeong.chatting.client.profile;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.model.ChattingProfile;
import yeong.chatting.util.Log;

public class MyProfileController extends BaseController{
		
	@FXML TextField nickname;
	@FXML TextField id;
	@FXML TextArea introduce;
	@FXML Button editBtn;
	@FXML Button withdrawBtn;
	@FXML Button cancelBtn;
	
	public static MyProfileController con;
	
	private StringProperty strName = new SimpleStringProperty();
	private StringProperty strId = new SimpleStringProperty();
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		initLabelSetting();
		con = this;
	}
	
	@FXML
	private void edit() {
		ActionInfo editAction = new ActionInfo("EditProfile", editBtn);
		editAction.setCons(nickname, id, introduce, editBtn);
		action(editAction);
	}
	
	@FXML
	private void withdraw() {
		ActionInfo withdrawAction = new ActionInfo("EditProfile", editBtn);
		action(withdrawAction);
	}
	
	@FXML
	private void cancel() {
		Stage s = (Stage)cancelBtn.getScene().getWindow();
		s.close();
	}
	
	
	/**
	 * 동적으로 변환시켜줌
	 */
	public void setProfile(ChattingProfile cp) {
		strName.set(cp.getNickname());
		strId.set(cp.getId());
		introduce.setText("");
		if(cp.getIntroduce()!=null)
			introduce.appendText(cp.getIntroduce());
	}
	
	/**
	 * 외부에서 이 객체 접근
	 * @return
	 */
	public static MyProfileController getProfileController() {
		return con;
	}
	
	private void initLabelSetting() {
		id.setEditable(false);
		nickname.setEditable(false);
		introduce.setEditable(false);
		nickname.textProperty().bind(strName);
		id.textProperty().bind(strId);
	}
	
	public void closeMyProfileController() {
		Stage s = (Stage)id.getScene().getWindow();
		s.close();
	}
	
}

