package yeong.chatting.util;

/**
 * @FileName  : Log.java
 * @Project     : ChatProject
 * @Date         : 2020. 5. 8.
 * @작성자      : Yeong
 * @변경이력 :
 * @프로그램 설명 : Log를 쉽게 찍기 위한 클래스이다.
 */
public class Log {
	
	private Log() {}
	
	public static void i(String msg) {
		System.out.println(msg);
	}
	
	public static void i(Object msg) {
		System.out.println(msg.toString());
	}
	
	public static void i(Class<?> clazz, String msg) {
		System.out.println(clazz.getSimpleName() + " : " + msg);
	}
	
	public static void i(Class<?> clazz, Object msg) {
		System.out.println(clazz.getSimpleName() + " : " + msg.toString());
	}
	
	public static void e(String msg) {
		System.err.println(msg);
	}
	
	public static void e(Class<?> clazz, String msg) {
		System.err.println(clazz.getSimpleName() + " : " + msg);
	}
	
	public static void e(Class<?> clazz, Exception e) {
		System.err.println(clazz.getSimpleName() + " : ");
		e.printStackTrace();
		System.exit(0);
	}
	public static void e(Class<?> clazz, String msg, Exception e) {
		System.err.println(clazz.getSimpleName() + " : " + msg +"("+ e.getMessage() +")");
		System.exit(0);
	}
}
