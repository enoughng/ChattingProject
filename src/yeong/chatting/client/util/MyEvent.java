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
	 * con1에서 액션이 일어나면 con2로 포커스를 맞추는 메소드
	 * @param con1  해당 텍스트필드
	 * @param con2 requestFocus할 컨트롤
	 */
	public <T extends TextField, T2 extends Control> void  nextTextField(T con1, T2 con2) {
		
		con1.setOnAction(event -> {
			con2.requestFocus();
		});
	}
	/**
	 * Textfield con1에서 액션이 일어나면 Button con2가 발생하는 이벤트설정
	 */
	public <T extends ButtonBase> void fireEvent(TextField con1, T con2) {
		con1.setOnAction(event -> {
			con2.fire();
		});
	}
	
	/**
	 * 시간남으면 listView에 대한 공통화를 한다.
	 */
//	public <T> void initListViewEventAndDataSetting(ListView<T> view, ObservableList<T> List) {
//		
//	}
	
	
}
