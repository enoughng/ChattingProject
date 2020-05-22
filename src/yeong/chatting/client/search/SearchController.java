package yeong.chatting.client.search;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import yeong.chatting.client.base.action.ActionInfo;
import yeong.chatting.client.base.controller.BaseController;
import yeong.chatting.util.Log;

public class SearchController extends BaseController{
	
	@FXML TextField idNickname;
	@FXML TextField idEmail;
	@FXML Button idSearch;

	@FXML TextField pwNickname;
	@FXML TextField pwEmail;
	@FXML TextField pwID;
	@FXML Button pwSearch;
	
	private static SearchController con;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);
		con = this;
	}
	
	@FXML 
	private void checkID() {
		ActionInfo idAction = new ActionInfo("IDSearch", idSearch);
		idAction.setCons(idNickname, idEmail);
		action(idAction);
	}
	
	@FXML
	private void checkPW() {
		ActionInfo pwAction = new ActionInfo("PWSearch", pwSearch);
		pwAction.setCons(pwNickname, pwEmail, pwID);
		action(pwAction);
	}
	
	public static SearchController getCon() {
		return con;
	}
	
	public Stage getSearchControllerStage() {
		Stage s = (Stage)idSearch.getScene().getWindow();
		return s;
		
	}
}
