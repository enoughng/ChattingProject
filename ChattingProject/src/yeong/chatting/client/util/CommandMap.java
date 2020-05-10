package yeong.chatting.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import yeong.chatting.client.action.CommonAction;
import yeong.chatting.util.Log;

public class CommandMap {
	private static Map<String,CommonAction> commandMap = new HashMap<>();

	static {
		Properties properties = new Properties();
		try {
//			properties.load(new FileInputStream(ClientMain.class.getResource("properties/ClientAction.properties").toString()));
			properties.load(new FileInputStream("src/yeong/chatting/client/properties/ClientAction.properties"));
			Enumeration keys =  properties.keys();
			while(keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				Class<?> clazz;
				try {
					clazz = Class.forName(properties.getProperty(key));
					CommonAction instance = (CommonAction)clazz.newInstance();
					commandMap.put(key, instance);
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
					Log.e(e.toString());
				}
			}
			Log.i("설정파일이 성공적으로 로딩되었습니다.");	
		} catch (IOException e) {
			Log.e(e.toString());
		}
	}
	
	public static Map<String,CommonAction> getCommandMap() {
		return commandMap;
	}
	

}
