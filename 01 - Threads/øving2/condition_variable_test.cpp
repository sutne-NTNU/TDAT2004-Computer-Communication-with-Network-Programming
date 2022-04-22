//
// Created by Sivert Utne on 24/02/2020.
//

#include <iostream>
#include <vector>
#include <thread>
#include <mutex>
#include <condition_variable>





using namespace std;


mutex mtx;
condition_variable cv;
int saldo = 0;



bool amount_availbale( int amount )
{
	string y = "\033[32m YES \033[0m";
	string n = "\033[31m no \033[0m";
	cout << "Checking if " << amount << " is available: " << (saldo - amount >= 0 ? y : n) << endl;
	return saldo - amount >= 0;
}



void withdraw( int amount )
{
	unique_lock<mutex> lock(mtx);
	cv.wait(lock , [&amount]
	{ return amount_availbale(amount); });
	saldo -= amount;
	cout << BLUE << "   withdrew " << amount << " new saldo: " << saldo << RESET << endl;
}



void add( int amount )
{
	unique_lock<mutex> lock(mtx);
	saldo += amount;
	cout << GREEN << "\nAdding Funds " << amount << " new saldo " << saldo << RESET << endl;
	cv.notify_all();
}

void stop(){
	cv.notify_all();
}


int condition_variable_test_main()
{
	int sleep_time = 2;
	vector<thread> threads;
	threads.emplace_back(withdraw , 3);
	threads.emplace_back(withdraw , 1);
	threads.emplace_back(withdraw , 5);
	threads.emplace_back(withdraw , 2);
	threads.emplace_back(withdraw , 4);

	this_thread::sleep_for(chrono::seconds(sleep_time));
	threads.emplace_back(add , 1);

	this_thread::sleep_for(chrono::seconds(sleep_time));
	threads.emplace_back(add , 7);

	this_thread::sleep_for(chrono::seconds(sleep_time));
	threads.emplace_back(add , 2);

	stop();

	for(auto &t : threads) t.join();
	cout << "DONE" << endl;
	return 0;
}