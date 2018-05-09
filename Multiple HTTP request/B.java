import java.io.*;
import java.net.*;
import java.util.*;

public class B implements Runnable{
	Socket s2;
	final static String CLRF = "\r\n";
	
		public B(Socket k){
			this.s2 = k;
		}

		
	
	public void run() {
	try{
			processRequest();
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	private void processRequest() throws Exception{
		
		BufferedReader CTP = new BufferedReader(new InputStreamReader(s2.getInputStream()));//client to proxy
		String requestLine = null;
		String headerLine = "";
		
	
			requestLine = CTP.readLine();//reading request line
			String hostLine = CTP.readLine();//reading host line
			
			StringTokenizer tokens = new StringTokenizer(hostLine);
			tokens.nextToken();
			String hostname = tokens.nextToken();
			System.out.println(hostname);//getting host name
			
			requestLine=requestLine+CLRF+ hostLine + CLRF;//combining request line and host line
			while((hostLine = CTP.readLine()).length()!=0){
				headerLine = headerLine+hostLine+CLRF;		//adding next header lines
			}
	
		String finalrequestLine = requestLine+headerLine+ CLRF;//getting final request line to send to server
		
		
		OutputStream PTCStream = null;//proxy to client
		InputStream STPStream = null;//server to proxy
		OutputStream PTSStream = null;//proxy to server
		Socket proxy = null;
		
			proxy = new Socket(hostname,80);//creating socket for proxy as client
			
			PTCStream = s2.getOutputStream();
			STPStream = proxy.getInputStream();
			PTSStream = proxy.getOutputStream();
			DataOutputStream PTS = new DataOutputStream(PTSStream);
			PTS.writeBytes(finalrequestLine);//sending request to server
			PTS.flush();
		
		
		
		byte[] buffer = new byte[1024];
		
		int con = 0; // to ensure status line only has status
		String statusLine="";
		String responseheader="";
		
			DataInputStream STP = new DataInputStream(STPStream);
			String responseline = STP.readLine();//reading status line
			
			while(responseline.length()!=0){
				if(con == 0){
					statusLine = responseline;//setting status line
					con = 1;
				}else{
					responseheader = responseheader+ responseline +CLRF;// for reading header lines
				}
				
			responseline = STP.readLine();
			}
		
		String finalresponseline = statusLine + CLRF;
		finalresponseline = finalresponseline +responseheader;//combining status and header lines
		finalresponseline = finalresponseline +CLRF;//getting final response line from server
		
			DataOutputStream PTC = new DataOutputStream(PTCStream);
			PTC.writeBytes(finalresponseline);//writing response line
			int bytes =0;
			while(true) {
				 bytes = STPStream.read(buffer);//reading data
	  			 	if(bytes>0){
	  			 		PTCStream.write(buffer);//writing data
	   			 		PTCStream.flush();
	  			 	}
	  			 	else{
	  			 		break;
	  			 	}
	  			
	        
	  		 }
			
		
		//closing streams and socket
			
			PTSStream.close();
			STPStream.close();
			CTP.close();
			PTCStream.close();
			proxy.close();
			s2.close();
			
		
	}
	
}
