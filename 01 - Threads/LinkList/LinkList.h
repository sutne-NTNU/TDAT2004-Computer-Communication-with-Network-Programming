//
// Created by Sivert Utne on 31/01/2020.
//

class LinkList
{
private:
	typedef struct Node
	{
		int tall;
		int threadNR;
		Node*neste;
	} *nodePtr;

	nodePtr head;
	nodePtr curr;
	nodePtr prev;
	nodePtr tail;

public:
	LinkList();
	bool empty();
	void addSorted( int tall , int threadNR );
	void push( int tall , int threadNr);
	void append( int tall , int threadNr);
	auto pop();
	void print();
};




