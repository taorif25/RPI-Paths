package hw6;

import org.junit.jupiter.api.Test;

public class QueueItemTest {
	
	@Test
	public void testQueueItem() {
		// tests the QueueItem class used in findPath()
		QueueItem<String> a = new QueueItem<String>("a","b",0.0,1.0);
		assert(a.edgeWeight == 0.0);
		assert(a.pathWeight == 1.0);
		assert(a.node.equals(new String("a")));
		assert(a.parent.equals(new String("b")));
		QueueItem<String> b = new QueueItem<String>("a","c",5.0,6.0);
		assert(a.equals(b) && b.equals(a));
		assert(a.hashCode() == new String("a").hashCode());
		assert(a.compareTo(b) < 0 && b.compareTo(a) > 0);
		b.pathWeight = 1.0;
		assert(a.compareTo(b)==0);
		assert(!a.equals(new Object()));
	}
	
}