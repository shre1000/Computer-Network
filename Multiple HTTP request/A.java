import java.io.*;
import java.net.*;

public class A {
public static void main(String args[]) throws IOException{
	int port = 10000;
	ServerSocket s = new ServerSocket(port);
	while(true){
		Socket s1 = s.accept();
		
		try{
			B b = new B(s1);
			Thread t = new Thread(b);
			t.start();
		}
		catch(Exception e){
			System.out.println(e);
			
		}
		
	}
	
}
}
