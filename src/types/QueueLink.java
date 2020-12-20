package types;

class QueueLink { // a linked list node for our Queue
	public QueueLink next;
	public Object obj;

	public QueueLink(Object o) {
		obj = o;
		next = null;
	}
}