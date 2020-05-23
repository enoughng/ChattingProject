package yeong.chatting.client.util;

import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class MyEvent {

	public MyEvent() {}
	
	/**
	 * con1���� �׼��� �Ͼ�� con2�� ��Ŀ���� ���ߴ� �޼ҵ�
	 * @param con1  �ش� �ؽ�Ʈ�ʵ�
	 * @param con2 requestFocus�� ��Ʈ��
	 */
	public <T extends TextField, T2 extends Control> void  nextTextField(T con1, T2 con2) {
		
		con1.setOnAction(event -> {
			con2.requestFocus();
		});
	}
	/**
	 * Textfield con1���� �׼��� �Ͼ�� Button con2�� �߻��ϴ� �̺�Ʈ����
	 */
	public <T extends ButtonBase> void fireEvent(TextField con1, T con2) {
		con1.setOnAction(event -> {
			con2.fire();
		});
	}
	
	/**
	 * �ð������� listView�� ���� ����ȭ�� �Ѵ�.
	 */
//	public <T> void initListViewEventAndDataSetting(ListView<T> view, ObservableList<T> List) {
//		
//	}
	
	
}
