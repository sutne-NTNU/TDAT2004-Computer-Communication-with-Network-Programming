package Oppgave4;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Oppgave4_Main
{
	public static void main(String[] args) throws Exception
	{
		BankAccountDAO_4 facade = null;
		EntityManagerFactory emf = null;

		try
		{
			emf = Persistence.createEntityManagerFactory("Oving6_4");
			facade = new BankAccountDAO_4(emf);

			System.out.println("\n\nAccounts from Database: ");
			List <BankAccount_4> list = facade.getAccounts();
			for(BankAccount_4 b : list)
			{
				System.out.println("\t" + b);
			}

			BankAccount_4 act = list.get(0);
//			act.deposit(3000);
//			facade.updateAccount(act);

			//transferring from one account to another
			BankAccount_4 fromAccount = facade.getAccount(act.getAccountNumber());
			BankAccount_4 toAccount = list.get(1);

			System.out.println("\n\nBefore transfer:" + fromAccount.getBalance() + " -> " + toAccount.getBalance());

			fromAccount.transfer(toAccount , 1234);
			facade.updateAccount(fromAccount);
			//thread.Sleep to induce an error on the accounts when running two main() at the same time
			Thread.sleep(10000);
			//the result of this is that after the two have finsihed, the balance on the "to" account has only received one trensfer
			facade.updateAccount(toAccount);

			//verifying that the balance has been updated
			fromAccount = facade.getAccount(fromAccount.getAccountNumber());
			toAccount = facade.getAccount(toAccount.getAccountNumber());
			System.out.println("After transfer:" + fromAccount.getBalance() + " -> " + toAccount.getBalance());
		}
		finally
		{
			emf.close();
		}
	}
}