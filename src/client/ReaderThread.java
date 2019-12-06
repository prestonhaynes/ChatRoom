package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
public class ReaderThread implements Runnable
{
	private Socket socket;
	
	public ReaderThread(Socket s)
	{
		socket = s;
	}
	
    @Override
	public void run() 
    {
        BufferedReader fromServer = null;
    	try 
        {
            // get the input stream from the socket
    		System.out.println("not yet");
    		fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    		System.out.println("created");
    		while(true) 
    		{
	            // read from the socket
	    		String status = fromServer.readLine();
	    		if (status != null){
					String headerDate = fromServer.readLine();
					switch (status)
					{
						case "status: 201":
							System.out.println("Successful join");
							break;
						case "status: 301":
							System.out.println(fromServer.readLine());
							break;
						case "status: 401":
							System.out.println("Username already taken");
							System.exit(0);
							break;
						case "status: 404":
							System.out.println("Username "+ fromServer.readLine().split(".")[1] +" does not exist");
							break;
						default:
							System.err.println("Something goes wrong!");
					}
				}
	             /**
	              * ok, data has now arrived. Display it in the text area,
	              * and resume listening from the socket.
	              */
    		}
        }
        catch (java.io.IOException ioe) 
        { 
        	System.err.println(ioe);
        }
    }
}