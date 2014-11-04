import java.net.*;
import java.util.Scanner;
import java.io.*;

public class TCPClient
{
	public InetSocketAddress serverAddress = null; 
	public Socket clientSocket    = null;
	public ObjectOutputStream out = null;
	public ObjectInputStream in   = null;
	public boolean threadAlive = true;
	public String clientName;
	
	public TCPClient(InetSocketAddress serverAddress)
	{
		this.serverAddress = serverAddress;
	}
	
	public void start() 
	{
		try
		{
			 this.clientSocket = new Socket();
		     this.clientSocket.connect(this.serverAddress);
		     
		     /* Initializing the streams */
		     out = new ObjectOutputStream(clientSocket.getOutputStream());
			 out.flush();
			 in = new ObjectInputStream(clientSocket.getInputStream());
			 
			 /* This section prompts user for name*/
			 Scanner sc = new Scanner(System.in);
		     System.out.println("Choose a name please.");
		     clientName = sc.nextLine().trim();
		     System.out.println("Hello "+clientName+", welcome to the chat room!");
			 
		     new Thread(new listeningClient()).start();
			 
			 while(true)
			 {
				 Scanner scanner = new Scanner(System.in);
				 String message = scanner.nextLine().trim();
				 out.writeObject(clientName+" says: "+message);
			 }			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(clientSocket != null)
			{	
				try
				{
					clientSocket.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public class listeningClient implements Runnable
	{
		public void run()
		{
			while(threadAlive)
			 {
				 try {
					System.out.println(in.readObject());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			 }
	    }
	}
	
	public static void main(String args[])
	{
		int port = 3000;
	    InetSocketAddress serverAddress = new InetSocketAddress(port);
	    
		TCPClient client = new TCPClient(serverAddress);
		client.start();
	}
}   
