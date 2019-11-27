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

//			System.out.println(ipAddress);
			//write ipAddress to client
			
			switch (status)
			{
			case "status: 200":
				String username = fromClient.readLine();
				System.out.println("new user name request/join " + username);
				break;
				
			case "status: 202":
				System.out.println("general message");
				//ServerMain.bt;
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
			
			fromClient.close();
			toClient.close();
		}
		catch (java.io.IOException ioe) 
		{
			System.err.println(ioe);
		}
		finally
		{
			// close streams and socket
			try
			{
			if (fromClient != null)
				fromClient.close();
			if (toClient != null)
				toClient.close();
			}
			catch (IOException ioe)
			{
				System.err.println(ioe);
			}
		}
	}
}

//BufferedReader  fromClient = null;
//BufferedWriter toClient = null;
//InetAddress host = null;
//try 
//{
//	/**
//	 * get the input and output streams associated with the socket.
//	 */
//	fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
//	toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
//
//	String status = fromClient.readLine();
//	String datestamp = fromClient.readLine();
//
//	
//	switch (status)
//	{
//	case "status: 200":
//		String username = fromClient.readLine();
//		System.out.println("new user name request/join " + username);
//		break;
//		
//	case "status: 202":
//		System.out.println("general message");
//		
//		break;
//		
//	case "status: 203":
//		System.out.println("private message");
//		break;
//	case "status: 300":
//		
//		break;
//		
//	default:
//		System.err.println("Something went wrong");
//	}
//	
//	toClient.write("You made it. You said\n" + status + "\r\n");
//	toClient.write(datestamp + "\r\n");
//	toClient.flush();
//	
//	fromClient.close();
//	toClient.close();
//	}
//
//catch (IOException ioe) 
//{
//	System.err.println(ioe);
//}
//
//finally 
//{
//	// close streams and socket
//	if (fromClient != null)
//		fromClient.close();
//	if (toClient != null)
//		toClient.close();
//}