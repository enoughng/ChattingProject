package yeong.chatting.client.base.action;import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * @FileName  : RegistryAction.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : 회원가입에 대한 요청 클래스
 */
public class FormAction implements CommonAction {

	@Override
	public void action(ActionInfo info) {
		addForm(info);
	}

	private void addForm(ActionInfo action) {
		try {
			FXMLLoader loader = new FXMLLoader(action.getURL());
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			Stage stage = new Stage();

			stage.setScene(scene);
			if(action.getAbsoluteDestination().indexOf(getClass().getResource(CommonPathAddress.RegistryLayout).toString())!=-1) {
				stage.initOwner(action.getPrimaryStage());
				stage.setTitle("회원가입 창");
			}
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}
	
	 
//	public static void addForm(URL url) throws IOException {
//		FXMLLoader loader = new FXMLLoader(url);
//		Parent root = (Parent)loader.load();
//		Scene scene = new Scene(root);
//		Stage stage = new Stage();
//		stage.setScene(scene);
//		stage.show();
//	}
 }
