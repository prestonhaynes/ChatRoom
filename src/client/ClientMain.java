package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
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
			
			// Send hostname to get server
			toServer.write("status: 200\r\n");
			toServer.write("date: " + date.toGMTString() +"\r\n");
			toServer.write("carlos" + "\r\n");
			toServer.write("\r\n\r\n");
			
			toServer.flush();
			
			// Read response from server and display it to user
			String line;
//			while ( (line = fromServer.readLine()) != null)
//				System.out.println(line);
//			exec.execute(rt);
			toServer.close();
			fromServer.close();
		}
		catch (UnknownHostException uhe) 
		{
			System.err.println("Unknown server: ");
		}
	}

}
