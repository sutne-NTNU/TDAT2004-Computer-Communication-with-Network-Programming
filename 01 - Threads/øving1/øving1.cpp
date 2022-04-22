//
// Created by Sivert Utne on 06/02/2020.
//


#include <iostream>
#include <thread>
#include <mutex>
#include <vector>

/*
 *        BOTH
 */

using namespace std;
using Clock = chrono::high_resolution_clock;
vector<unsigned int> primeVector;
mutex resultDoor;
atomic<int> counter;



/**
 * Checks to see if start value is below limit in order to check for the "rule-breaking" primes, primarily 1 and 2
 * and also changes the start value to an odd number as there is no need to check even numbers above 2.
 * @param start int
 * @return int - new start value
 */
int validateStart( int start )
{
	int newStart = start;
	if(newStart <= 3)
	{
		if(newStart < 3)
		{
			counter++;
			primeVector.emplace_back(2);
		}
		counter++;
		primeVector.emplace_back(3);
		newStart = 5;
	}
	if(newStart % 2 == 0) newStart++;
	return newStart;
}



/**
 * Given the number is above 3, this method will check relevant numbers to see of the number is prime,
 * returns false if the number is ever divisible by anything.
 * @param number must be above 3
 * @return boolean
 */
bool isPrime( int number )
{
	if(number % 3 == 0) return false;
	for(int i = 5 ; (i * i) <= number ; i += 6)
	{
		if(number % i == 0 || number % (i + 2) == 0)
		{
			return false;
		}
	}
	return true;
}



/**
 * When a thread finishes this method is used to format the string of what of how many primes the thread found out of
 * the numbers it checked, and also adds the number off primes the thread found to the total number for the program.
 * @param threadId
 * @param numbersChecked - Amount of numbers this thread has checked
 * @param primesFound - Number of primes this thread found
 */
void threadFinished( int threadId , int numbersChecked , int primesFound )
{
	counter.fetch_add(primesFound);
	string print = (string("    THREAD ")
	                + to_string(threadId)
	                + GREEN
	                + string(" FINISHED: ")
	                + YELLOW
	                + to_string(primesFound)
	                + RESET
	                + string(" / ")
	                + to_string(numbersChecked)
	                + string(" primes"));
	resultDoor.lock();
	{
		cout << print << endl;
	}
	resultDoor.unlock();
}



/**
 * First sorts the vector of primes that were found, then prints them one by one.
 */
void printPrimes()
{
	sort(primeVector.begin() , primeVector.end());
	for(auto i : primeVector)
	{
		cout << to_string(i) +  " ";;
	}
	cout << endl;
	primeVector.clear();
}



/**
 * Simply prints out a string with the final number of primes found and the execution time for the program.
 *
 * @param time - Number of milliseconds it took for all threads to finish
 */
void printResult( int time )
{
	cout << "}\n" YELLOW << counter << RESET " primes found in " BOLDBLUE << time << RESET  " ms" << endl;
//	printPrimes();
}



/**
 * Takes a prime number and uses a mutex lock to prevent threads from colliding when adding their prime to the
 * primeVector.
 * @param prime - int
 */
void addPrimeToVector( int prime )
{
	resultDoor.lock();
	{
		primeVector.emplace_back(prime);
	}
	resultDoor.unlock();
}



/*
 *        FIXED ASSIGNEMENT
 */






/**
 * This is run by the threads. <p>
 * the thread will start on its personal start value, then use the 'jump' to skiå to its next value to check.
 * @param start int - This specific threads startvalue
 * @param end int - The overall end value
 * @param threadNr int
 * @param jump int - The numbers to skip for this thread to not collide with the other threads
 */
void findPrimes( int start , int end , int threadNr , int jump )
{
	cout << "    THREAD " + to_string(threadNr) + RED " STARTING " RESET << endl;
	int checked = 0 , found = 0;
	for(int i = start ; i <= end ; i += (jump))
	{
//		cout << "        " << to_string(threadNr) + " checking " + to_string(i) << endl;
		checked++;
		if(isPrime(i))
		{
			found++;
			addPrimeToVector(i);
		}
	}
	threadFinished(threadNr , checked , found);
}



/**
 * This version essentially parts the given interval from start to end into equal parts for each thread, but it does
 * so in a way that gives each thread roughly the same amount of high(slow) and low(quick) numbers to check by using
 * the jump value.
 * @param start
 * @param end
 * @param numThreads
 */
void FindPrimesWithThreads( int start , int end , int numThreads )
{
	cout << "\n\n\n" << BOLDGREEN + to_string(numThreads) + RESET " Threads\n{" << endl;
	counter = 0;
	auto startTime = Clock::now();
	{
		int jump = numThreads * 2;
		start = validateStart(start);

		thread threads[numThreads];
		for(int i = 0 ; i < numThreads ; i++)
		{
			int threadId = i + 1;
			threads[i] = thread(findPrimes , start , end , threadId , jump);
			start += 2;
		}
		for(auto &t : threads) t.join();
	}
	auto endTime = Clock::now();

	printResult(chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count());
}



/*
 *        DYNAMIC ASSIGNEMENT
 */




atomic<int> nextInLine;



void findPrimes_improved( int end , int threadNr )
{
	int remaining = end - nextInLine < 0 ? 0 : end - nextInLine;
	cout << "    THREAD " + to_string(threadNr) + RED  " STARTING "  RESET  " - "
	        + (remaining > 0 ? (to_string(remaining) + " remaining") : "0 remaining - " RED "FINISHED"  RESET)
	     << endl;
	if(remaining == 0)return;

	int checking , checked = 0 , found = 0;
	while((checking = nextInLine.fetch_add(2)) <= end)
	{
		checked++;
//		cout << "        " << to_string(threadNr) + " checking " + to_string(checking) << endl;
		if(isPrime(checking))
		{
			found++;
			addPrimeToVector(checking);
		}
	}
	threadFinished(threadNr , checked , found);
}



/**
 * This version dynamically gives the threads a number to check as they become available. <p>
 * This means that thread 1 will continue checking primes alone until Thread 2 starts, and no threads will finish
 * until the entire interval is checked, unlike the previous version where thread1 usually finishes long before the
 * other threads, or even before the start.
 *
 * @param start lowest number that will be checked
 * @param end highest number that will be checked
 * @param numThreads number of threads to execute
 */
void FindPrimesWithThreads_improved( int start , int end , int numThreads )
{
	cout << "\n\n\n" << BOLDGREEN << numThreads << RESET << " Threads\n{" << endl;
	counter = 0 , nextInLine = start;


	auto startTime = Clock::now();
	{
		nextInLine = validateStart(start);

		thread threads[numThreads];
		for(int i = 0 ; i < numThreads ; i++)
		{
			int threadId = i + 1;
			if(nextInLine < end)
			{
				threads[i] = thread(findPrimes_improved , end , threadId);
			}
		}

		for(auto &t : threads)
		{
			if(t.joinable())
			{
				t.join();
			}
		}
	}
	auto endTime = Clock::now();

	printResult(chrono::duration_cast<chrono::milliseconds>(endTime - startTime).count());
}









/*
 * RUNNING CODE
 */








/*
 * Used previously in an attempt to prevent an annoyance of overhead lag of about 400-500ms on the first run with
 * more than one thread, but it has since stopped working.
 */
void dummy()
{
	{
		thread threads[2];
		for(auto &t : threads)
		{
			t = thread(validateStart , 123);
		}
		for(auto &t : threads) t.join();
	}
}



/**
 * Takes in the relevant values then starts the two different versions of this program to solve.
 * @param start int - lower limit of prime calculation
 * @param end int - upper limit of prime calculation
 * @param fromNrOfThreads int - the program will start with this many threads
 * @param toNrOfThreads int - the program will run from start + 1 until it reaches this number of threads
 */
void run( int start , int end , int fromNrOfThreads , int toNrOfThreads )
{
	dummy();

	cout << BOLDBLUE << "\n\n\n\nFIXED ASSIGNEMENT OF NUMBERS " << RESET << endl;;
	for(int i = fromNrOfThreads ; i <= toNrOfThreads ; i++)
	{
		FindPrimesWithThreads(start , end , i);
	}

	cout << BOLDBLUE << "\n\n\n\n\nDYNAMIC ASSIGNEMENT OF NUMBERS\n\n\n" << RESET << endl;
	for(int i = fromNrOfThreads ; i <= toNrOfThreads ; i++)
	{
		FindPrimesWithThreads_improved(start , end , i);
	}
}



/*
	Finn alle primtall mellom to gitte tall ved hjelp av et gitt antall tråder.
	Skriv til slutt ut en sortert liste av alle primtall som er funnet
    Pass på at de ulike trådene får omtrent like mye arbeid
*/
int øving1_main()
{
	int MILLION = 1000000;
	int MAXINT = 1410065408;

	int START = (MAXINT - 1 * MILLION) , END = (MAXINT);

	int startThreadNr = 1;
	int endThreadNr = 6;

	cout << "checking from " << START << " to " << END
	     << "\nwith " << startThreadNr << " to " << endThreadNr << " threads" << endl;

	run(START , END , startThreadNr , endThreadNr);

	return 0;
}
