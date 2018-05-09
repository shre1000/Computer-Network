import java.io.*;
import java.net.*;
import java.util.*;

final class HttpRequest implements Runnable{

Socket s ;
final static String CRLF = "\r\n";

public HttpRequest(Socket t) throws Exception
{
this.s = t;
}

public void run() {
try
{
 processrequest();
}
catch(Exception e)
{
System.out.println(e);
}
}

private void processrequest() throws Exception{

BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
DataOutputStream os = new DataOutputStream(s.getOutputStream());

String requestLine = br.readLine();
System.out.println();
System.out.println(requestLine);

String headerLine = null;
while((headerLine = br.readLine()).length()!=0)
{
System.out.println(headerLine);
}

StringTokenizer tokens= new StringTokenizer(requestLine);
tokens.nextToken();
String fileName = tokens.nextToken();

fileName = "."+ fileName;

FileInputStream fis = null;

boolean fileExists = true;

try
{
fis = new FileInputStream(fileName);
}
catch(FileNotFoundException e)
{
	fileExists = false;
}

String statusLine = null;
String contentTypeLine = null;
String entityBody = null;

if(fileExists)
{
	statusLine = "HTTP/1.1 200 OK" + CRLF;
    contentTypeLine = "Content-type:" + contentType(fileName) + ";" + "charset=ISO-8859-1" + CRLF;
}
else
{
	statusLine = "HTTP/1.1 404 Not Found" + CRLF;
	contentTypeLine = "Content-type:" + contentType(fileName) + CRLF;
    entityBody = "<HTML>" + "<HEAD><TITLE>Not Found</TITLE></HEAD>"+ "<BODY>Not Found</BODY></HTML>";
}

os.writeBytes(statusLine);
os.writeBytes(contentTypeLine);
os.writeBytes(CRLF);


if(fileExists)
{
	sendBytes(fis,os);
	fis.close();
}
else{
	os.writeBytes(entityBody);
	os.flush();
}

os.close();
br.close();
s.close();

}

private static void sendBytes(FileInputStream fis,DataOutputStream os)throws Exception
{
	byte[] buffer = new byte[1024];
	int bytes = 0;
	
	while((bytes=fis.read(buffer))!= -1)
	{
		os.write(buffer, 0, bytes);
		os.flush();
	}
}

private static String contentType(String fileName){
	if(fileName.endsWith(".htm")|| fileName.endsWith(".html"))
	{
		return "text/html";
    }
	if(fileName.endsWith(".jpeg")|| fileName.endsWith(".jpg"))
	{
		return "image/jpeg";
    }
	if(fileName.endsWith(".gif"))
	{
		return "image/gif";
    }
return "application/octet-stream";
}

}

