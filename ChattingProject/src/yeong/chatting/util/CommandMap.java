package yeong.chatting.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import yeong.chatting.server.action.CommonAction;

public class CommandMap {
	
	private static Map<String,CommonAction> commandMap = new HashMap<>();
	
	static {
		Properties pro = new Properties();
		try {
			pro.load(new FileInputStream("ChattingProject/src/yeong/chatting/server/properties/ServerAction.properties"));
			Enumeration<Object> keys = pro.keys();
			while(keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				Class<?> clazz = Class.forName(pro.getProperty(key));
				CommonAction instance = (CommonAction)clazz.newInstance();
				commandMap.put(key, instance);
			}
		} catch (FileNotFoundException e) {
			Log.e("CommandMap : " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("CommandMap : " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e("CommandMap : " + e.getMessage());			
			e.printStackTrace();
		} catch (InstantiationException e) {
			Log.e("CommandMap : " + e.getMessage());			
		} catch (IllegalAccessException e) {
			Log.e("CommandMap : " + e.getMessage());			
		}
	}
	
	
	
	public static Map<String,CommonAction> getCommandMap() {
		return commandMap;
	}
	

}
