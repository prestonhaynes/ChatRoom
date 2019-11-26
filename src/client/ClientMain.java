package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		
		BufferedReader networkBin = null;
		BufferedWriter networkBout = null;		
		Socket sock = null;

		try 
		{
			// open socket and buffered read and write
			sock = new Socket(args[0],7331);
			networkBout = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
			networkBin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			
			// Send hostname to get server
			networkBout.write("Status: 200\r\n");
			networkBout.write("Date: UTC\r\n");
			networkBout.flush();
			
			// Read response from server and display it to user
			String line;
			while ( (line = networkBin.readLine()) != null)
				System.out.println(line);
			networkBout.close();
		}
		catch (UnknownHostException uhe) 
		{
			System.err.println("Unknown server: ");
		}
	}

}
