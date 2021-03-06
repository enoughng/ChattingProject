package yeong.chatting.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import yeong.chatting.client.base.action.CommonAction;
import yeong.chatting.util.CommonPathAddress;
import yeong.chatting.util.Log;

public class CommandMap {
	private static Map<String,CommonAction> commandMap = new HashMap<>();

	static {
		Properties properties = new Properties();
		try {
//			properties.load(new FileInputStream(ClientMain.class.getResource("properties/ClientAction.properties").toString()));
			properties.load(new FileInputStream(CommonPathAddress.clientActionProperties));
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
		} catch (IOException e) {
			Log.e(e.toString());
		}
	}
	
	public static Map<String,CommonAction> getCommandMap() {
		return commandMap;
	}
	

}
