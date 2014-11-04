import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class TCPServer {
	
	public ServerSocket listenSocket = null;
    public InetSocketAddress address = null;
    public ArrayList<Service> servicelist = new ArrayList<Service>();
    
    public TCPServer(InetSocketAddress address)
    {
    	    this.address = address;
    }
        
    public void start()
    {
        try 
        {	
        	this.listenSocket = new ServerSocket();
        	this.listenSocket.bind(address);
        	
        	System.out.println("TCP Server started!");
        	
        	while(true)
        	{
        			Socket serviceSocket = listenSocket.accept();
        			
        			Service newService = new Service(serviceSocket);
        			servicelist.add(newService);
        			
        			Thread newServiceThread = new Thread(newService);
        			newServiceThread.start();
        	} 
        }
        	
        catch (Exception e) 
        {	
			e.printStackTrace();
		}
        	
    }
        
	private class Service implements Runnable
	{
		Socket serviceSocket = null;
	    ObjectInputStream in = null;
		ObjectOutputStream out = null;
		
		private Service(Socket serviceSocket)
		{					
			try
			{		
				this.serviceSocket = serviceSocket;
				this.in = new ObjectInputStream(this.serviceSocket.getInputStream());
				this.out = new ObjectOutputStream(this.serviceSocket.getOutputStream());
				this.out.flush();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			try
			{
				while(true)
				{
					/* Receive message from the client */
					String message = (String)in.readObject();
					System.out.println("Server Received:" + message);
						
					for(Service serviceX : servicelist)
					{
						serviceX.out.writeObject(message);
						serviceX.out.flush();
					}
					
				}
			}
			
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			finally
			{
				if(serviceSocket != null)
					
				try
				{
					serviceSocket.close();
				}
				
				catch(Exception e)
				{
						e.printStackTrace();
				}	
			}
			
		 }
	 }
	
	 public static void main(String args[]) 
	 {
		    int port = 3000;
		    InetSocketAddress serverAddress = new InetSocketAddress(port);
		    
		    TCPServer server = new TCPServer(serverAddress);
		    server.start();
	 }
}	
		
