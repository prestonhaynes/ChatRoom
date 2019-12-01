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

		try 
		{
			// open socket and buffered read and write
			sock = new Socket(args[0],7331);
			toServer = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			Date date = new Date();
			rt = new ReaderThread(sock);
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			
			// Send username request
			toServer.write("status: 200\r\n");
			toServer.write("date: " + date.toGMTString() +"\r\n");
			toServer.write("peter" + "\r\n");
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
	}

}
