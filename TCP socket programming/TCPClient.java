package for_client;

import java.io.*;
import java.util.*;
import java.net.*;

public class TCPClient{
	public static void main(String[] args) throws Exception {
		
	   System.out.println("Enter the name of the file :");
       Scanner scanner = new Scanner(System.in);
       String file_name = scanner.nextLine();
        
       Socket clientsocket = new Socket("localhost", 6789);
        
        
       ObjectOutputStream oos2 = new ObjectOutputStream(clientsocket.getOutputStream());
       
       FileInputStream fis = new FileInputStream(file_name);
       BufferedInputStream bis = new BufferedInputStream(fis);
        
       OutputStream os = clientsocket.getOutputStream();
      
       oos2.writeObject(file_name);
        
        
       byte [] buffer = new byte[1000];
       int b = 0;
        
       while((b = bis.read(buffer, 0, buffer.length)) !=-1)
        {           
        os.write(buffer, 0, buffer.length);
        
        }
        os.flush();
        bis.close();
        clientsocket.close();

}
}



