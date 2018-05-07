package for_server;

import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String[] args) throws Exception{
		
		ServerSocket WelcomeSocket = new ServerSocket(6789);
		
		while (true) {
			
            Socket connectionsocket = WelcomeSocket.accept();
            
            
            ObjectInputStream ois2 = new ObjectInputStream(connectionsocket.getInputStream());
            Object o = ois2.readObject();
            File file = new File(o.toString());
           
            InputStream in = connectionsocket.getInputStream();
       
            OutputStream os = new FileOutputStream(file);
            
            byte [] buffer = new byte[1000];
            int b = 0;
            
            while((b = in.read(buffer, 0, buffer.length))!= -1 )
            {
                os.write(buffer, 0, b);
            }
            
           System.out.println("File transfer is done successfully.");
   
 
           connectionsocket.close();

        }

}
}