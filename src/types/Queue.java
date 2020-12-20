package types;

public class Queue { // first-in, first-out
	private QueueLink head = null, tail = null;

	public synchronized Object pop() {
		while (head == null) {
			try {
				wait(); // block until we get an item
			} catch (InterruptedException e) {
			}
		}
		Object o = head.obj; // pop the first item
		head = head.next;
		if (head == null) {
			tail = null;
		}
		return o;
	}

	public synchronized void push(Object o) {
		if (head == null) {
			head = tail = new QueueLink(o);
		} else {
			tail.next = new QueueLink(o);
			tail = tail.next;
		}
		notify(); // wake up blocked threads
	}
}
