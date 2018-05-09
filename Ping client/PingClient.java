import java.io.*;
import java.net.*;
import java.util.*;

public class PingClient{
	public static int i=-1; 

	public static void main(String args[])throws Exception{
		if(args.length!=2){
			System.out.println("two arguments required: host and port");
			return;
		}
		
		String hostname= args[0];
		int port = Integer.parseInt(args[1]);
		
		DatagramSocket clientsocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName(hostname);
		
		byte[] receivedata = new byte[1024];
		
		Timer t = new Timer();
	    t.schedule(new TimerTask(){
		    //TimerTask schedules task and run method is override
	    	public void run(){
	    		
			i = i+1;
			
			long timestamp = (System.currentTimeMillis());
			
	        String SendMessage = "Ping "+  i + " " + timestamp + "\r\n";
	        System.out.println();
	        System.out.print("send_message is " +SendMessage); //printing request message from client
	        
	        byte[] sendData= new byte[1024];
	        sendData= SendMessage.getBytes();
	        DatagramPacket sendpacket= new DatagramPacket(sendData,sendData.length,IPAddress, port);
	         
	        try{
	        clientsocket.send(sendpacket);	
		    System.out.println("request sent");
		    
	     	clientsocket.setSoTimeout(1000);//client wait for 1 sec for response from server 
	     	
	     	DatagramPacket recivepacket = new DatagramPacket(receivedata, receivedata.length);
	     	clientsocket.receive(recivepacket);
	     	
	     	long receivedtimestamp = System.currentTimeMillis();//get received response message's time stamp to calculate round trip delay
	     	long delay = receivedtimestamp-timestamp;//calculate round trip delay
	     	
	     	//printing data of response received. instead of using printdata method separately i m doing it here
	        byte[] buf = recivepacket.getData();
	        ByteArrayInputStream bais  = new ByteArrayInputStream(buf);
	        InputStreamReader isr = new InputStreamReader(bais);
	        BufferedReader br = new BufferedReader(isr);
	        String Line = br.readLine();
	        System.out.println("Received from: "+ recivepacket.getAddress().getHostAddress()+":"+ new String(Line)+ "and round trip time is :"+ delay);
	        
	        //to cancel task scheduling after 10 pings if last message ends in try
	        if(i==9){
	           t.cancel();
	        }
	     		
	        }//end of try
	     	
	        catch(Exception e){
	     	System.out.println(e);
	     	
	     	//to cancel task scheduling after 10 pings if last message ends in catch
	     	if(i==9){
	     	 t.cancel();
	     	}
	     	
	     	}//end of catch
	        
		}//end of run
	    	
	},0,1*1000L);//end of t.schedule
	
}//end of main method
}// end of class
