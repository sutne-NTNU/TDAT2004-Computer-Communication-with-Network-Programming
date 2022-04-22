package Oppgave1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress("192.168.0.196" , 5000));
		System.out.println("Server is running on ip: " + serverSocket.getInetAddress().getHostAddress() + "    Port: " + serverSocket.getLocalPort());

		while(true)
		{
			Socket clientSocket = serverSocket.accept();
			new Thread(new ClientHandler(clientSocket)).start();
		}
	}
}





class ClientHandler implements Runnable
{
	Socket clientSocket;



	ClientHandler(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}



	@Override
	public void run( )
	{

		if(clientSocket == null) return;
		try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream() , true) ;)
		{
			out.println("You have been connected to the server!");
//			while(true)
			{
				out.println("First number: ");
				int n1 = Integer.parseInt(in.readLine());
				out.println("Add \"+\", Subtract \"-\", Multiply \"*\" or  Divide \"/\": ");
				String operator = in.readLine();
				out.println("Second number: ");
				int n2 = Integer.parseInt(in.readLine());

				switch (operator)
				{
					case "+":
						out.println(n1 + " + " + n2 + " = " + (n1 + n2));
						break;
					case "-":
						out.println(n1 + " - " + n2 + " = " + (n1 - n2));
						break;
					case "*":
						out.println(n1 + " * " + n2 + " = " + (n1 * n2));
						break;
					case "/":
						out.println(n1 + " / " + n2 + " = " + (1.0 * n1 / n2));
						break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
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
