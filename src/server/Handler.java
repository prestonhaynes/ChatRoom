package server;
/**
 * Handler class containing the logic for echoing results back
 * to the client. 
 *
 * @author Greg Gagne 
 * Edited by:
 * @author Preston Haynes
 */

import java.io.*;
import java.net.*;
import java.util.stream.Collectors;


public class Handler 
{
	public static final int BUFFER_SIZE = 256;
	
	/**
	 * this method is invoked by a separate thread
	 */
	public void process(Socket client) throws java.io.IOException {
		BufferedReader  fromClient = null;
		BufferedWriter toClient = null;
		InetAddress host = null;
		try {
			/**
			 * get the input and output streams associated with the socket.
			 */
			fromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
			toClient = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

			String status = fromClient.readLine();
			String datestamp = fromClient.readLine();

//			System.out.println(ipAddress);
			//write ipAddress to client
			toClient.write("You made it. You said\n" + status + "\n");
			toClient.write(datestamp + "\r\n");
			toClient.flush();
			
			fromClient.close();
			toClient.close();
   		}
		
		catch (IOException ioe) 
		{
			System.err.println(ioe);
		}
		
		finally 
		{
			// close streams and socket
			if (fromClient != null)
				fromClient.close();
			if (toClient != null)
				toClient.close();
		}
	}
}
