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
	    		String s = fromServer.readLine();
	    		if (s != null)
	    		{
	    			System.out.println(s);
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