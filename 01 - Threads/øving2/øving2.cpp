//
// Created by Sivert Utne on 19/02/2020.
//



#include "Workers.cpp"



/**
 * <br>Example task of what the different worker threads will be running in parallell.
 *
 * @param taskName Name of the task
 * @param color Console color for the task to easier tell the tasks apart.
 * @param difficulty from 0 to 9 - how long the task will take to execute in order to illustrate how different tasks
 * will run in parallell when the tasks take different amounts of time.
 */
void task( string taskName , string color , int difficulty )
{
	cout << color + taskName + "        STARTING"  RESET  " in Thread: " << this_thread::get_id() << endl;
	for(int prosent = 1 ; prosent < 10 ; prosent++)
	{
		this_thread::sleep_for(std::chrono::milliseconds(difficulty * 10));
		cout << color + taskName + " " + to_string(prosent * 10) + "%"  RESET  "\n";
	}
	cout << color + taskName + " 100% - FINISHED" RESET "\n";
}



int øving2_main()
{
	cout << "MAIN - " << this_thread::get_id() << endl;


	Workers worker_threads(4);
	Workers event_loop(1);


	worker_threads.start();
	event_loop.start();


	worker_threads.post_timeout([]
	                            {
		                            cout << MAGENTA "WORKER THREADS TIMEOUT " RESET << this_thread::get_id();
	                            } ,
	                            5000);

	worker_threads.post([]
	                    { task("A" , BOLDCYAN , 9); });
	worker_threads.post([]
	                    { task("B" , BOLDYELLOW , 4); });
	event_loop.post([]
	                { task("C" , BOLDBLUE , 2); });
	event_loop.post([]
	                { task("D" , BOLDGREEN , 2); });

	event_loop.post_timeout([]
	                        {
		                        cout << MAGENTA "EVENT LOOP TIMEOUT - " RESET << this_thread::get_id();
	                        } , 5000);


	cout << WHITE "\n            MAIN DONE - Waiting for threads\n" RESET << endl;

	worker_threads.stop();
	event_loop.stop();

	return 0;
}