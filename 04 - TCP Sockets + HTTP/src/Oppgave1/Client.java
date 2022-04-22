package Oppgave1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class Client
{
	private static Scanner scanner = new Scanner(System.in);



	public static void main(String[] args) throws IOException
	{
		Socket serverSocket = new Socket("192.168.0.196" , 5000);
		BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		PrintWriter out = new PrintWriter(serverSocket.getOutputStream() , true);

		System.out.println(in.readLine()); //connected to server
		do
		{
			System.out.print("Server: " + in.readLine()); //Number 1:
			out.println(sendNumber(scanner.nextLine()));
			System.out.print("Server: " + in.readLine()); // + / -
			out.println(sendOperator(scanner.nextLine()));
			System.out.print("Server: " + in.readLine()); //Number 2:
			out.println(sendNumber(scanner.nextLine()));
			System.out.println("Server: " + in.readLine()); //result
			System.out.println("type \"y\" to do another.");
		}
		while(scanner.nextLine().equalsIgnoreCase("y"));

		in.close();
		out.close();
		serverSocket.close();
	}



	static int sendNumber(String str)
	{
		boolean number = false;
		int i = -1;
		while(!number)
		{
			try
			{
				i = Integer.parseInt(str);
				number = true;
			}
			catch(NumberFormatException nfe)
			{
				System.out.print("Thats not a number! try again: ");
				str = scanner.nextLine();
			}
		}
		return i;
	}



	static String sendOperator(String str)
	{
		while(!(str.equals("+") || str.equals("-")))
		{
			System.out.print("Thats not a legal operator! Use \"+\" or \"-\": ");
			str = scanner.nextLine();
		}
		return str;
	}
}
