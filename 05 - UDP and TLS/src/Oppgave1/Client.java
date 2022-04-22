package Oppgave1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class Client
{
	static final int PORT = 5000;
	static InetAddress address;
	static DatagramSocket socket;
	private static Scanner scanner = new Scanner(System.in);



	public static void main(String[] args) throws IOException
	{
		address = InetAddress.getLocalHost();
		socket = new DatagramSocket();

		String str = "Hello!";
		send(str);
		System.out.println(recieve()); //connected to server

		do
		{
			System.out.print("Oppgave1.Server: " + recieve()); //Number 1:
			send(number(scanner.nextLine()));
			System.out.print("Oppgave1.Server: " + recieve()); // + / -
			send(operator(scanner.nextLine()));
			System.out.print("Oppgave1.Server: " + recieve()); //Number 2:
			send(number(scanner.nextLine()));
			System.out.println("Oppgave1.Server: " + recieve()); //result

			System.out.println("type \"stop\" to end execution, or \"c\" to continue");
			str = scanner.nextLine();
			send(str);
		}
		while(!str.equalsIgnoreCase("stop"));
		System.out.println("Stopping");
		socket.close();
	}



	static String number(String str)
	{
		boolean number = false;
		while(!number)
		{
			try
			{
				Integer.parseInt(str);
				number = true;
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Thats not a number! try again: ");
				str = scanner.nextLine();
			}
		}
		return str;
	}



	static String operator(String str)
	{
		while(!(str.equals("+") || str.equals("-") || str.equals("*") || str.equals("/")))
		{
			System.out.print("Thats not a legal operator! Use \"+\", \"-\", \"*\" or \"/\": ");
			str = scanner.nextLine();
		}
		return str;
	}



	static String recieve( ) throws IOException
	{
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf , buf.length);
		socket.receive(packet);
		return new String(packet.getData() , 0 , packet.getLength());
	}



	static void send(String str) throws IOException
	{
		byte[] message = str.getBytes();
		DatagramPacket packet = new DatagramPacket(message , message.length , address , PORT);
		socket.send(packet);
	}
}
