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
import java.util.Date;

public class Connection implements Runnable
{
	private BufferedReader  fromClient = null;
	private BufferedWriter toClient = null;
//	private static Handler handler = new Handler();
	
	public Connection(Socket client) throws IOException {
		fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
		toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	}

    /**
     * This method runs in a separate thread.
     */	
	public void run()
	{		
		try 
		{
			/**
			 * get the input and output streams associated with the socket.
			 */
			
			String status = fromClient.readLine();
			String datestamp = fromClient.readLine();

			
			switch (status)
			{
			case "status: 200":
				boolean usernameTaken = false;
				String username = fromClient.readLine();
				for (ChatUser cu : ServerMain.socketConnections)
				{
					if (cu.getUsername().equalsIgnoreCase(username))
					{
						usernameTaken = true;
						ServerMain.socketConnections.remove(ServerMain.socketConnections.size() - 1);
						badUsername(username);

					}
				}	
				
				if (usernameTaken)
					break;
				System.out.println("oof");
				ServerMain.socketConnections.get(ServerMain.socketConnections.size() - 1).setUsername(username);
				
				toClient.write("status: 201" + "\r\n");
				toClient.write(datestamp + "\r\n");
				ServerMain.bt.addMessage(username + " has joined the chat");
				toClient.flush();
					
				
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
			
			for (ChatUser cu : ServerMain.socketConnections)
			{
				System.out.println(cu.getUsername());
			}

		}
		catch (java.io.IOException ioe) 
		{
			System.err.println(ioe);
		}
	}

	private void badUsername(String username) throws IOException 
	{
		// TODO Auto-generated method stub
		
		System.out.println("User tried joining with used username.");
		
		//BufferedWriter toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
		toClient.write("status: 401" + "\r\n");
		toClient.write("date: " + new Date().toGMTString() + "\r\n");
		toClient.write("\r\n\r\n");
		toClient.close();
	}
}

