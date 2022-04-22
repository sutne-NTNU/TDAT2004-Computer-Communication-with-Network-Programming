package Oppgave2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class WebServer
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = new ServerSocket();
		//To run on port 80 you must run this file as root user,
		//or by using "sudo java Oppgave2.WebServer" in Terminal from /src
		serverSocket.bind(new InetSocketAddress("192.168.0.196" , 80));

		System.out.println(new Date() + " - Server is running on ip: " + serverSocket.getInetAddress().getHostAddress() + "    Port: " + serverSocket.getLocalPort());
		while(true)
		{
			Socket clientSocket = serverSocket.accept();
			System.out.println(new Date() + " - New connection: " + clientSocket.getInetAddress().getHostName() + ":" + clientSocket.getPort());
			new Thread(new ConnectionHandler(clientSocket)).start();
		}
	}
}





class ConnectionHandler implements Runnable
{
	private final Socket clientSocket;



	public ConnectionHandler(Socket clientSocket)
	{
		this.clientSocket = clientSocket;
	}



	@Override
	public void run( )
	{

		try(BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) ;
		    PrintWriter out = new PrintWriter(clientSocket.getOutputStream() , true))
		{

			StringBuilder response = new StringBuilder();
			response.append("HTTP/1.0 200 OK\n");
			response.append("Content-Type: text/html; charset=utf-8\n");
			response.append("\n"); //blank line to seperate header from body
			response.append("<html>\n");
			response.append("<title>Oving 4 - Oppgave 2</title>\n");
			response.append("<body>\n");
			response.append("<h1>Welcome to Oving 4 - Oppgave 2</h1>\n");
			response.append("<h3>Du koblet deg til: ").append(new Date()).append("</h3>\n");
			response.append("Headeren din var:\n");
			response.append("<ul>\n");

			String line;
			while(!(line = in.readLine()).equals(""))
			{
				//add each line from clients header to a list item
				response.append("<li>").append(line).append("</li>\n");
			}

			response.append("</ul>\n");
			response.append("</body>\n");
			response.append("</html>");

			out.println(response);
		}
		catch(IOException ioe)
		{
			System.err.println("Server error : " + ioe);
		}
	}
}