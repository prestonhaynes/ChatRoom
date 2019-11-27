package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

public class BroadcastThread implements Runnable
{
	Vector<String> messageVector;
	public BroadcastThread()
	{
		messageVector = new Vector<String>();
	}
    public void run() 
    {
    	BufferedWriter toClient;
        while (true) 
        {
            // sleep for 1/10th of a second
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }

            if (messageVector.size() > 0)
            {
	            for (String s : messageVector)
	            {
	            	for (ChatUser cu : ServerMain.socketConnections)
	            	{
	            		try 
	            		{
	            			toClient = new BufferedWriter(new OutputStreamWriter(cu.getSocket().getOutputStream()));
							toClient.write(s);
							toClient.flush();
						} 
	            		catch (IOException e) 
	            		{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            }
            }
            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
        }
    }
	void addMessage(String string) {
		// TODO Auto-generated method stub
		messageVector.add(string);
	}
} 