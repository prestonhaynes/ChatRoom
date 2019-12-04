package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.Vector;

public class BroadcastThread implements Runnable
{
	Date date = new Date();
	Vector<String> messageVector;
	public BroadcastThread()
	{
		messageVector = new Vector<String>();
	}
    public void run() 
    {
    	
    	BufferedWriter toClient = null;
        while (true) 
        {
            // sleep for 1/10th of a second
            try { Thread.sleep(100); } catch (InterruptedException ignore) { }
            
            
            if (messageVector.size() > 0)
            {
            	System.out.println("Broadcasting");
	            for (String s : messageVector)
	            {
	            	System.out.println(s);
	            	if (s != null)
		            	for (ChatUser cu : ServerMain.socketConnections)
		            	{
		            		try 
		            		{
		            			toClient = new BufferedWriter(new OutputStreamWriter(cu.getSocket().getOutputStream()));
		            			toClient.write("status: 301" + "\r\n");
		            			toClient.write("date: " + date.toGMTString() + "\r\n");
								toClient.write(s + "\r\n");
								toClient.write("\r\n\r\n");
								toClient.flush();
							} 
		            		catch (SocketException e)
		            		{
		            			System.err.println(cu.getUsername() + " no longer is connected");
		            			ServerMain.socketConnections.remove(cu);
		            			try 
		            			{
									toClient.close();
								} 
		            			catch (IOException e1) 
		            			{
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
		            		}
		            		catch (IOException e) 
		            		{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
		            	}
	            }
	            messageVector.clear();
	            messageVector.trimToSize();
            }
            /**
             * check if there are any messages in the Vector. If so, remove them
             * and broadcast the messages to the chatroom
             */
        }
    }
	public void addMessage(String string) 
	{
		// TODO Auto-generated method stub
		messageVector.add(string);
	}
	
	public void sendPrivateMessage(String fromUsername, String toUsername, String message)
	{
		Socket toSocket = null;
		for (ChatUser cu: ServerMain.socketConnections)
		{
			if (cu.getUsername() == toUsername)
			{
				toSocket = cu.getSocket();
			}
		}
		
		if (toSocket != null)
		{
			try
			{
				BufferedWriter toClient = null;
				toClient = new BufferedWriter(new OutputStreamWriter(toSocket.getOutputStream()));
				toClient.write("status: 203" + "\r\n");
				toClient.write("date: " + date.toGMTString() + "\r\n");
				toClient.write(message + "\r\n");
				toClient.write("\r\n\r\n");
				toClient.flush();
			}
			catch (IOException ioe)
			{
				System.err.println(ioe);
			}
		}
		else
		{
			System.err.println("user does not exist");
			
			Socket fromSocket = null;
			for (ChatUser cu: ServerMain.socketConnections)
			{
				if (cu.getUsername() == fromUsername)
				{
					fromSocket = cu.getSocket();
				}
			}
			
			try
			{
				BufferedWriter fromClient = new BufferedWriter(new OutputStreamWriter(fromSocket.getOutputStream()));
				
				fromClient.write("status: 404" + "\r\n");
				fromClient.write("date: " +date.toGMTString() + "\r\n");
				fromClient.write("from" + fromUsername +  "\r\n");
				fromClient.write("\r\n\r\n");
				fromClient.flush();
			}
			catch (IOException ioe)
			{
				System.err.println(ioe);
			}
		}
	}
} 