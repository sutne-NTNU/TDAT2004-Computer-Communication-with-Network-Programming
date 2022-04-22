package Oppgave2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class BankAccountDAO
{
	private EntityManagerFactory emf;



	public BankAccountDAO(EntityManagerFactory emf)
	{
		this.emf = emf;
	}



	/**
	 * @param act a new BankAccount to be inserted into the database, must have a new AccountNumber
	 */
	public void createAccount(BankAccount act)
	{
		EntityManager em = getEntitymanager();
		try
		{
			em.getTransaction().begin();
			em.persist(act);
			em.getTransaction().commit();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	private void closeEntityManager(EntityManager em)
	{
		if(em != null && em.isOpen()) em.close();
	}



	private EntityManager getEntitymanager( )
	{
		return emf.createEntityManager();
	}



	public void updateAccount(BankAccount act)
	{
		EntityManager em = getEntitymanager();
		try
		{
			em.getTransaction().begin();
			em.merge(act);
			em.getTransaction().commit();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	public List <BankAccount> getAccounts( )
	{
		EntityManager em = getEntitymanager();
		try
		{
			Query q = em.createQuery("SELECT OBJECT(o) FROM BankAccount o");
			return q.getResultList();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	public List <BankAccount> getAccountsOver(double amount)
	{
		EntityManager em = getEntitymanager();
		try
		{
			Query q = em.createQuery("SELECT OBJECT(b) FROM BankAccount b WHERE b.balance >= :amount");
			q.setParameter("amount" , amount);
			return q.getResultList();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	public BankAccount getAccount(int accountNumber)
	{
		EntityManager em = getEntitymanager();
		try
		{
			return em.find(BankAccount.class , accountNumber);
		}
		finally
		{
			closeEntityManager(em);
		}
	}
}