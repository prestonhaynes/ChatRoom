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
			String fromUsername = null;
			String message = null;
			
			switch (status)
			{
			case "status: 200":
				boolean usernameTaken = false;
				String username = fromClient.readLine();
				for (ChatUser cu : ServerMain.socketConnections)
				{
					if (cu.getUsername().equals(username))
					{
						usernameTaken = true;
						ServerMain.socketConnections.remove(ServerMain.socketConnections.size() - 1);
						badUsername(username);

					}
				}	
				
				if (usernameTaken)
					break;
				ServerMain.socketConnections.get(ServerMain.socketConnections.size() - 1).setUsername(username);
				
				toClient.write("status: 201" + "\r\n");
				toClient.write(datestamp + "\r\n");
				toClient.flush();
				
				ServerMain.bt.addMessage(username + " has joined the chat");
				
				System.out.println("new user name request/join " + username);
				break;
				
			case "status: 202":
                System.out.println("general message");
                fromUsername = fromClient.readLine();
                message = fromClient.readLine();
                ServerMain.bt.addMessage(fromUsername + ": " + message);
                
                break;
                
            case "status: 203":
                System.out.println("private message");
                fromUsername = fromClient.readLine().split(".")[1];
                String toUsername = fromClient.readLine().split(".")[1];
                message = fromClient.readLine();
                ServerMain.bt.sendPrivateMessage(fromUsername, toUsername, message);
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

