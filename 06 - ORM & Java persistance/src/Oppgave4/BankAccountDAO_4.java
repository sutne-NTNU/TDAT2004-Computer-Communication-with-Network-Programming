package Oppgave4;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

public class BankAccountDAO_4
{
	private EntityManagerFactory emf;



	public BankAccountDAO_4(EntityManagerFactory emf)
	{
		this.emf = emf;
	}



	/**
	 * @param act a new BankAccount to be inserted into the database, must have a new AccountNumber
	 */
	public void createAccount(BankAccount_4 act)
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



	public void updateAccount(BankAccount_4 act)
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



	public List <BankAccount_4> getAccounts( )
	{
		EntityManager em = getEntitymanager();
		try
		{
			Query q = em.createQuery("SELECT OBJECT(o) FROM BankAccount_4 o");
			return q.getResultList();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	public List <BankAccount_4> getAccountsOver(double amount)
	{
		EntityManager em = getEntitymanager();
		try
		{
			Query q = em.createQuery("SELECT OBJECT(b) FROM BankAccount_4 b WHERE b.balance >= :amount");
			q.setParameter("amount" , amount);
			return q.getResultList();
		}
		finally
		{
			closeEntityManager(em);
		}
	}



	public BankAccount_4 getAccount(int accountNumber)
	{
		EntityManager em = getEntitymanager();
		try
		{
			return em.find(BankAccount_4.class , accountNumber);
		}
		finally
		{
			closeEntityManager(em);
		}
	}
}