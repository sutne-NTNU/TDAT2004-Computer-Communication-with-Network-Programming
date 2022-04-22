//
// Created by Sivert Utne on 31/01/2020.
//

#include <iostream>
#include "LinkList.h"



using namespace std;



int linkList_main()
{
	LinkList liste;
	liste.addSorted( 5 , 1 );
	liste.addSorted( 6 , 2 );
	liste.addSorted( 3 , 3 );
	liste.addSorted( 8 , 4 );
	liste.addSorted( 7 , 5 );
	liste.addSorted( 4 , 6 );
	liste.addSorted( 2 , 7 );
	liste.addSorted( 9 , 8 );
	liste.addSorted( 6 , 9 );
	liste.print();

	return 0;
}



LinkList::LinkList()
{
	head = nullptr;
	curr = nullptr;
	prev = nullptr;
}

//add a new node to start av list
bool LinkList::empty()
{
	return head == nullptr;
}


//add a Node sorted by the number
void LinkList::addSorted( int tall , int threadNr )
{
	auto newNode = new Node;
	newNode->neste = nullptr;
	newNode->tall = tall;
	newNode->threadNR = threadNr;

	if( head == nullptr )
	{
//		cout << "placing head: " << newNode->tall << endl;
		head = newNode;
		return;
	}
	if( head->tall >= newNode->tall )
	{
//		cout << "replacing head " << head->tall << " with " << newNode->tall << endl;
		newNode->neste = head;
		head = newNode;
		return;
	}
	curr = head;
	while( curr->neste != nullptr && curr->tall < newNode->tall )
	{
		prev = curr;
		curr = curr->neste;
	}
	if( curr->tall >= newNode->tall )
	{
//		cout << "inserting: " << newNode->tall << " between: " << prev->tall << " and: " << curr->tall << endl;
		newNode->neste = curr;
		prev->neste = newNode;
		return;
	}
	curr->neste = newNode;
//	cout << "appending: " << newNode->tall << endl;
}


//add a new node to start av list
void LinkList::push( int tall , int threadNr)
{
	auto n = new Node;
	n->neste = head;
	n->tall = tall;
	n->threadNR = threadNr;
	head = n;
}


//add a new node to back of list
void LinkList::append( int tall , int threadNr)
{
	auto n = new Node;
	n->tall = tall;
	n->neste = nullptr;
	n->threadNR = threadNr;
	tail->neste = n;
}

//retrieve the first item from list and remove it
auto LinkList::pop(){
	auto n = new Node;
	n->tall = head->tall;
	n->neste = head->neste;
	n->threadNR = head->threadNR;
	head = head->neste;
	return n;
}


//prints the list
void LinkList::print()
{
	if( head == nullptr )
	{
		cout << "Listen er tom" << endl;
		return;
	}
	curr = head;
	if(curr->threadNR == 0){
		while( curr != nullptr )
		{
			cout << curr->tall << endl;
			curr = curr->neste;
		}
		return;
	}
	while( curr != nullptr )
	{
		cout << "Thread: " << curr->threadNR << " found prime: " << curr->tall << endl;
		curr = curr->neste;
	}
}



