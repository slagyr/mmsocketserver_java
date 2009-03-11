//- Copyright ©2009 Micah Martin.  All Rights Reserved
//- MMSocketServer and all included source files are distributed under terms of the GNU LGPL.

package socketserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class SocketService
{
	private ServerSocket serverSocket = null;
	private Thread serviceThread = null;
	private boolean running = false;
	private SocketServer server = null;
	private final LinkedList<Thread> threads = new LinkedList<Thread>();

  public SocketService(int port, SocketServer server) throws Exception
  {
    this(port, server, InetAddress.getLocalHost());
  }

	public SocketService(int port, SocketServer server, InetAddress host) throws Exception
	{
		this.server = server;
		serverSocket = new ServerSocket(port, 0, host);
		serviceThread = new Thread(
		   new Runnable()
		   {
			   public void run()
			   {
				   serviceThread();
			   }
		   }
		);
		serviceThread.start();
	}

  public boolean isRunning()
  {
    return running && !serverSocket.isClosed();
  }

	public void close() throws Exception
	{
		waitForServiceThreadToStart();
		running = false;
		serverSocket.close();
		serviceThread.join();
		waitForServerThreads();
	}

	private void waitForServiceThreadToStart()
	{
		while(!running) Thread.yield();
	}

	private void serviceThread()
	{
		running = true;
		while(running)
		{
			try
			{
				Socket s = serverSocket.accept();        
				startServerThread(s);
			}
			catch(IOException e)
			{
      //okay
			}
		}
	}

	private void startServerThread(Socket s)
	{
		Thread serverThread = new Thread(new ServerRunner(s));
		synchronized(threads)
		{
			threads.add(serverThread);
		}
		serverThread.start();
	}

	private void waitForServerThreads() throws InterruptedException
	{
		while(threads.size() > 0)
		{
			Thread t;
			synchronized(threads)
			{
				t = threads.getFirst();
			}
			t.join();
		}
	}

	private class ServerRunner implements Runnable
	{
		private Socket socket;

		ServerRunner(Socket s)
		{
			socket = s;
		}

		public void run()
		{
			try
			{
				server.serve(socket);
				synchronized(threads)
				{
					threads.remove(Thread.currentThread());
				}
			}
			catch(Exception e)
			{
        //okay
			}
		}
	}
}
