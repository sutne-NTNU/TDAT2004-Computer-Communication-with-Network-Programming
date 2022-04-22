//
// Created by Sivert Utne on 19/02/2020.
//

#ifndef NETTVERKSPROGRAMMERING_WORKER_H
#define NETTVERKSPROGRAMMERING_WORKER_H

#include <list>
#include <vector>
#include <thread>
#include <condition_variable>
#include <mutex>





class Workers
{
private:
	std::mutex mtx;
	std::condition_variable cv;
	std::vector<std::thread> threads;
	std::list <std::function<void()>> tasks;
	std::atomic<bool> moreTasksWillCome{};

public:
	explicit Workers( int nrOfThreads );
	void start();
	void post(const std::function<void()> &task );
	void post_timeout(const std::function<void()> &task , int ms );
	void stop();
};





#endif //NETTVERKSPROGRAMMERING_WORKER_H
