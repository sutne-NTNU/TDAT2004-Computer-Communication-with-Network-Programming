package Oppgave2;

import javax.persistence.*;
import java.io.Serializable;

@Entity

public class BankAccount implements Serializable
{
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private int accountNumber;
	private double balance;
	private String owner;



	public BankAccount( )
	{
	}



	public BankAccount( String owner)
	{
		this.balance = 0;
		this.owner = owner;
	}



	public int getAccountNumber( )
	{
		return accountNumber;
	}



	public void setAccountNumber(int accountNumber)
	{
		this.accountNumber = accountNumber;
	}



	public String getOwner( )
	{
		return owner;
	}



	public void setOwner(String owner)
	{
		this.owner = owner;
	}



	public double getBalance( )
	{
		return balance;
	}



	public void setBalance(double balance)
	{
		this.balance = balance;
	}



	public void transfer(BankAccount toAccount , double amount)
	{
		if(amount < 0) return;
		toAccount.deposit(this.withdraw(amount));
	}



	public double withdraw(double amount)
	{
		if(amount < 0 || balance < amount) return 0;
		balance -= amount;
		return amount;
	}



	public void deposit(double amount)
	{
		if(amount < 0) return;
		balance += amount;
	}



	public String toString( )
	{
		return "AccountNr: " + accountNumber + ", Owner: " + owner + ", Balance: " + balance + "kr";
	}
}
