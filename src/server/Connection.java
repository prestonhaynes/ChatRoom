package server;
/**
 * This is the separate thread that services each
 * incoming echo client request.
 *
 * @author Greg Gagne 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;

public class Connection implements Runnable
{
	private Socket client;
	private static Handler handler = new Handler();
	
	public Connection(Socket client) {
		this.client = client;
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run()
	{
		BufferedReader  fromClient = null;
		BufferedWriter toClient = null;
		InetAddress host = null;
		
		try 
		{
			/**
			 * get the input and output streams associated with the socket.
			 */
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			String status = fromClient.readLine();
			String datestamp = fromClient.readLine();

			
			switch (status)
			{
			case "status: 200":
				String username = fromClient.readLine();
				for (ChatUser cu : ServerMain.socketConnections)
				{
					if (cu.getUsername() == username)
					{
						badUsername(username);
						break;
					}
				}
				System.out.println("new user name request/join " + username);
				break;
				
			case "status: 202":
				System.out.println("general message");
				ServerMain.bt.addMessage("");
				break;
				
			case "status: 203":
				System.out.println("private message");
				break;
			case "status: 300":
				
				break;
				
			default:
				System.err.println("Something went wrong");
			}
			
			toClient.write("You made it. You said\n" + status + "\r\n");
			toClient.write(datestamp + "\r\n");
			toClient.flush();
			
		}
		catch (java.io.IOException ioe) 
		{
			System.err.println(ioe);
		}
	}

	private void badUsername(String username) throws IOException 
	{
		// TODO Auto-generated method stub
		
		
		BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		toClient.write("status: 401" + "\r\n");
		toClient.write("date: " + new Date().toGMTString() + "\r\n");
		toClient.write("\r\n\r\n");	
	}
}

