package yeong.chatting.client.profile;

import javafx.scene.control.TextArea;
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

public class ProfileController extends BaseController{

	@FXML Label nickname;
	@FXML Label id;
	@FXML TextArea introduce;
	@FXML Button add;
	@FXML Button cancelBtn;
	
	public static ProfileController con;
	
	private StringProperty strName;
	private StringProperty strId;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		initLabelSetting();
		con = this;
	}
	
	@FXML
	private void addFriend() {
		ActionInfo addAction = new ActionInfo("AddFriend", add);
		action(addAction);
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
	public static ProfileController getProfileController() {
		return con;
	}
	
	private void initLabelSetting() {
		strName = new SimpleStringProperty();
		strId = new SimpleStringProperty();
		nickname.textProperty().bind(strName);
		id.textProperty().bind(strId);
	}
	//	stage.initStyle(StageStyle.UNDECORATED;
}
