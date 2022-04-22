package Oppgave1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class Server
{
	static final int PORT = 5000;
	static InetAddress clientAddress;
	static DatagramSocket socket;
	static int clientPORT;



	public static void main(String[] args) throws IOException
	{
		socket = new DatagramSocket(PORT);
		System.out.println("Waiting for a Oppgave1.Client...");
		String fromClient = recieve();
		System.out.println("Oppgave1.Client said: " + fromClient);
		send("Hello! Your are connected to the server!");

		do
		{
			send("Number 1: ");
			int a = Integer.parseInt(recieve());
			send("operator: ");
			String operator = recieve();
			send("Number 2: ");
			int b = Integer.parseInt(recieve());
			System.out.println("Oppgave1.Client sent: " + a + operator + b);

			switch (operator)
			{
				case "+":
					send(a + " + " + b + " = " + (a + b));
					break;
				case "-":
					send(a + " - " + b + " = " + (a - b));
					break;
				case "*":
					send(a + " * " + b + " = " + (a * b));
					break;
				case "/":
					send(a + " / " + b + " = " + (1.0 * a / b));
					break;
				default:
					send("An illegal Operator was sent!");
					break;
			}
		}
		while(!recieve().equalsIgnoreCase("stop"));
		System.out.println("Oppgave1.Client stopped");
	}



	static String recieve( ) throws IOException
	{
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf , buf.length);
		socket.receive(packet);
		clientAddress = packet.getAddress();
		clientPORT = packet.getPort();
		return new String(packet.getData() , 0 , packet.getLength());
	}



	static void send(String str) throws IOException
	{
		byte[] message = str.getBytes();
		DatagramPacket packet = new DatagramPacket(message , message.length , clientAddress , clientPORT);
		socket.send(packet);
	}
}

