package yeong.chatting.client.action;import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

/**
 * @FileName  : RegistryAction.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @�ۼ���      : Yeong
 * @�����̷� :
 * @���α׷� ���� : ȸ�����Կ� ���� ��û Ŭ����
 */
public class FormAction implements CommonAction {

	@Override
	public void formAction(Window primaryStage, String url) {
		Log.i(getClass(),"RegistryAction ����");
		addForm(primaryStage, url);
	}

	private void addForm(Window primaryStage, String url) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
			Parent regiForm = (Parent)loader.load();
			Scene scene = new Scene(regiForm);
			Stage stage = new Stage();

			stage.setScene(scene);
			if(url.equals(CommonPathAddress.RegistryLayout)) {
				stage.initOwner(primaryStage);
				stage.initModality(Modality.APPLICATION_MODAL);
			}
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(getClass(), e);
		}
	}
}
