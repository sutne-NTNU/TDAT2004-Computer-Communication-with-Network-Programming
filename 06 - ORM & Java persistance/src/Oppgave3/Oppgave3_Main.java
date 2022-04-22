package Oppgave3;

import Oppgave2.BankAccount;
import Oppgave2.BankAccountDAO;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Oppgave3_Main
{
	public static void main(String[] args) throws Exception
	{
		BankAccountDAO facade = null;
		EntityManagerFactory emf = null;

		try
		{
			emf = Persistence.createEntityManagerFactory("Oving6");
			facade = new BankAccountDAO(emf);

			System.out.println("\n\nAccounts from Database: ");
			List <BankAccount> list = facade.getAccounts();
			for(BankAccount b : list)
			{
				System.out.println("\t" + b);
			}

			BankAccount act = list.get(0);
//			act.deposit(3000);
//			facade.updateAccount(act);

			//transferring from one account to another
			BankAccount fromAccount = facade.getAccount(act.getAccountNumber());
			BankAccount toAccount = list.get(1);

			System.out.println("\n\nBefore transfer:   FromAccount: " + fromAccount.getBalance() + "     ToAccount: " + toAccount.getBalance());

			fromAccount.transfer(toAccount , 1000);
			facade.updateAccount(fromAccount);
			//thread.Sleep to induce an error on the accounts when running two main() at the same time
			Thread.sleep(10000);
			//the result of this is that after the two have finsihed, the balance on the "to" account has only received one trensfer
			facade.updateAccount(toAccount);

			//verifying that the balance has been updated
			fromAccount = facade.getAccount(fromAccount.getAccountNumber());
			toAccount = facade.getAccount(toAccount.getAccountNumber());
			System.out.println("After transfer:   FromAccount: " + fromAccount.getBalance() + "     ToAccount: " + toAccount.getBalance());
		}
		finally
		{
			emf.close();
		}
	}
}