import java.io.*;
import java.net.*;
import java.util.*;

public class PingClientFor3A {
	public static void main(String args[])throws Exception{
		if(args.length!=2){
			System.out.println("two arguments required: host and port");
			return;
		}
		
		String hostname= args[0];
		int port = Integer.parseInt(args[1]);
		
		DatagramSocket clientsocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(hostname);
		byte[] sendData= new byte[1024];
		byte[] receivedata = new byte[1024];
	
       //for sending 10 pings to server
		for(int i = 0 ; i<10; i++){

             long timestamp = System.currentTimeMillis();//get timestamp
             
             String SendMessage = "Ping "+ i + " " + timestamp + "\r\n";
             System.out.println();
             System.out.print("send_message is " +SendMessage);//print request message
             
             sendData= SendMessage.getBytes();
             DatagramPacket sendpacket= new DatagramPacket(sendData,sendData.length,IPAddress, port);
             clientsocket.send(sendpacket);	
             System.out.println("request send");
		     
		     try{
	         clientsocket.setSoTimeout(1000);//waiting for 1 sec to receive response
	         
		     DatagramPacket recivepacket = new DatagramPacket(receivedata, receivedata.length);
		     clientsocket.receive(recivepacket);
		     
		     long receivedtimestamp = System.currentTimeMillis();//get response message arrival time to calculate round trip delay
		     
		     long delay = receivedtimestamp-timestamp;//calculate round trip delay
             printdata(recivepacket,delay);//print response message
             
		     }//end of try
		     
		     catch(Exception e){
			 System.out.println(e);
		     }//end of catch
		
		}//end of for 
		
	clientsocket.close();

	}//end of main method
	
	//for printing response
	private  static void printdata(DatagramPacket recivepacket, long delay)throws Exception{
		byte[] buf = recivepacket.getData();
		ByteArrayInputStream bais  = new ByteArrayInputStream(buf);
		InputStreamReader isr = new InputStreamReader(bais);
		BufferedReader br = new BufferedReader(isr);
		String Line = br.readLine();
		System.out.println("Received from: "+ recivepacket.getAddress().getHostAddress()+":"+ new String(Line)+ " round trip time is "+ delay);
		}

}//end of class

