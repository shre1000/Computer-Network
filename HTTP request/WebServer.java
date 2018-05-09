import java.io.*;
import java.net.*;
import java.util.*;

public final class WebServer{
public static void main(String args[]) throws Exception{

int port = 6789;
ServerSocket s1 = new ServerSocket(port);

while(true){
Socket s2 = s1.accept();
HttpRequest request = new HttpRequest(s2);
Thread t = new Thread(request);
t.start();
}

}
}

