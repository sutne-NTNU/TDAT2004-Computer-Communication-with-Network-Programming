//
// Created by Sivert Utne on 19/02/2020.
//

#include "Workers.h"
#include <iostream>



using namespace std;



Workers::Workers( int nrOfThreads )
{
	threads.reserve(nrOfThreads);
	moreTasksWillCome = true;
}



/**
 * Creates the reserved number of threads and starts their execution of tasks, if no tasks are available they
 * will wait for tasks to be added.
 */
void Workers::start()
{
	int capacity = threads.capacity();
	for(int i = 0 ; i < capacity ; i++)
	{
		threads.emplace_back([&]
		                     {
			                     cout << "Thread Created - " << this_thread::get_id() << endl;
			                     while(true)
			                     {
				                     function<void()> task;
				                     {
					                     unique_lock<mutex> lock(mtx);
					                     cv.wait(lock , [&]
					                     { return !tasks.empty() || !moreTasksWillCome; });
					                     if(tasks.empty() && !moreTasksWillCome) return;
					                     task = *tasks.begin();
					                     tasks.pop_front();
				                     }
				                     task();
			                     }
		                     });
	}
}



/**
 * Add a function for the workers to execute
 *
 * @param func - Task for the threads to complete
 */
void Workers::post( const function<void()> &task )
{
	unique_lock<mutex> lock(mtx);
	tasks.emplace_back(task);
	cv.notify_one();
}



/**
 * Performs the given function after the given timeout.
 *
 * @param func Task to be done
 * @param ms Timeout in milliseconds before the task will get executed
 */
void Workers::post_timeout( const function<void()> &task , int timeout_ms )
{
	task();
	cout << " - Posting\n";
	post([&task , timeout_ms]
	     {
		     task();
		     cout << " - Waiting to execute\n";
		     this_thread::sleep_for(chrono::milliseconds(timeout_ms));
		     task();
		     cout << " - EXECUTED\n";
	     });
}



/**
 * Signals all worker threads that no more tasks will be posted, then waits for them to join.
 */
void Workers::stop()
{
	{
		unique_lock<mutex> lock(mtx);
		moreTasksWillCome = false;
		cv.notify_all();
	}
	for(auto &t : threads)
	{
		t.join();
	}
};