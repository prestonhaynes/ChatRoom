package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClientMain {

	private static final Executor exec = Executors.newCachedThreadPool();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		BufferedReader fromServer = null;
		BufferedWriter toServer = null;		
		Socket sock = null;
		ReaderThread rt = null;
		String serverAddress = args[0];
		String username = args[1];
		Date date = new Date();
		if (username.length() > 100)
		{
			System.out.println("Username is too long. It should be under 100 characters");
			System.exit(0);
		}

	
		try 
		{
			// open socket and buffered read and write
			sock = new Socket(serverAddress,7331);
			toServer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			
			rt = new ReaderThread(sock);
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			// Send username request
			toServer.write("status: 200\r\n");
			toServer.write("date: " + date.toGMTString() +"\r\n");
			toServer.write(username + "\r\n");
			toServer.write("\r\n\r\n");
			
			toServer.flush();
			
			// Read response from server and display it to user
			String line;
			exec.execute(rt);
		}
		catch (UnknownHostException uhe) 
		{
			System.err.println("Unknown server: ");
		}

		while (true)
		{
			// read line from scanner
			Scanner myObj = new Scanner(System.in);  
			System.out.println("Enter message");
			String message = myObj.nextLine(); 
			// Check if first word is "/pm"
			if( message.split(".").length > 0 && message.split(".")[0].contains("/pm") == true){
				// if yes, parse out username and message and send message to server as private message to user
				String arr[] = message.split("\\s+");
				String private_message = arr[0];
				String private_name = arr[1];
				toServer.write("status: 203\r\n");
				toServer.write("date: " + date.toGMTString() +"\r\n");
				toServer.write("from:" + username + "\r\n");
				toServer.write("to:" + private_name + "\r\n");
				toServer.write(message);
				toServer.write("\r\n\r\n");
			}
			// if no, parse out username and message and send message to server as general message to user
			else{
				toServer.write("status: 202\r\n");
				toServer.write("date: " + date.toGMTString() +"\r\n");
				toServer.write("from:" + username + "\r\n");
				toServer.write(message);
				toServer.write("\r\n\r\n");

			}
		    
			
			
		}
		
	}

}
