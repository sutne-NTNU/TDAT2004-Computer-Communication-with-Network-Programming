package Oppgave2;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Oppgave2_Main
{
	public static void main(String[] args) throws Exception
	{
		BankAccountDAO facade = null;
		EntityManagerFactory emf = null;

		try
		{
			emf = Persistence.createEntityManagerFactory("Oving6");
			facade = new BankAccountDAO(emf);

			//Creating accounts
			BankAccount ba = new BankAccount("Sivert Utne");
			facade.createAccount(ba);
			BankAccount ba2 = new BankAccount("Ola Nordmann");
			facade.createAccount(ba2);

			System.out.println("\n\nAccounts from Database: ");
			List <BankAccount> list = facade.getAccounts();
			for(BankAccount b : list)
			{
				System.out.println("\t" + b);
			}

			//depositing to an account
			BankAccount account = list.get(0);
			account.deposit(2000);
			facade.updateAccount(account);

			//verifying that the balance has been updated
			account = facade.getAccount(account.getAccountNumber());
			System.out.println("\n\nAfter deposit of 2000: " + account);

			//retrieving accounts over a certain balance
			double amount = 1000;
			list = facade.getAccountsOver(amount);
			System.out.println("\n\nAccounts with a balance over " + amount + ": ");
			for(BankAccount b : list)
			{
				System.out.println("\t" + b);
			}

			//Changing owner of one of these accounts
			account = list.get(0);
			account.setOwner("New Owner");
			facade.updateAccount(account);

			//verifying that the owner has been changed
			account = facade.getAccount(account.getAccountNumber());
			System.out.println("\n\nAfter changing owner: " + account);
		}
		finally
		{
			emf.close();
		}
	}
}