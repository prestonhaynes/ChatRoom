package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServerMain 
{
	private static final Executor exec = Executors.newCachedThreadPool();
	public static BroadcastThread bt;
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Server is started");
		
		ArrayList<Socket> socketConnections = new ArrayList<Socket>();
		
		ServerSocket sock = null;
		try
		{
			bt = new BroadcastThread();
			sock = new ServerSocket(7331);
			
			while (true)
			{
				Socket clientSocket = sock.accept();
				Runnable task = new Connection(clientSocket);
				socketConnections.add(clientSocket);
				System.out.println(clientSocket.getLocalSocketAddress());
				System.out.println("Made connection");
				exec.execute(task);

				System.out.println(socketConnections.size());
				for (Socket s : socketConnections)
				{
					System.out.println(s.getInetAddress().toString());
				}
			}
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
		finally
		{
//			if (sock != null)
//				sock.close();
		}
	}

}
