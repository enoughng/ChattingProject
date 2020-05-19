package yeong.chatting.server.thread;

public class EequalsEx {
	
	public static void main(String[] args) {
		
		A a1 = new A("°¡");
		A a2 = new A("°¡");
		System.out.println(a1.equals(a2));
	}
}

class A {
	
	public String aa;
	
	public A(String aa) {
		this.aa = aa;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof A) {
			A a = (A) obj;
			if(aa.equals(a.aa)) {
				return true;
			}
		} 
		return false;
	}

}

