package hw7;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MapNodeTest {
	
	
	@Test
	void testConstructor() {
		// test null args
		assertThrows(IllegalArgumentException.class, () -> {new MapNode("","",1.0,null);});
		assertThrows(IllegalArgumentException.class, () -> {new MapNode("","",null,1.0);});
		assertThrows(IllegalArgumentException.class, () -> {new MapNode("",null,1.0,1.0);});
		new MapNode(null,"",1.0,2.0);
	}
	
	@Test
	void testGetters() {
		MapNode test = new MapNode(null,"",1.0,2.0);
		assertTrue(test.getName()==null);
		assertTrue(test.getID().equals(""));
		assertTrue(test.getX()==1.0);
		assertTrue(test.getY()==2.0);
	}
	
	@Test
	void testEquals() {
		Object a = new Object();
		MapNode test1 = new MapNode("name","id",1.0,2.0);
		MapNode test2 = new MapNode("name2","id",2.0,1.0);
		assertFalse(test1.equals(a));
		assertTrue(test1.equals(test2) && test2.equals(test1));
		test2 = new MapNode("name","id2",2.0,1.0);
		assertFalse(test1.equals(test2) && test2.equals(test1));
	}
	
	@Test
	void testHashCode() {
		MapNode test1 = new MapNode("name","id",1.0,2.0);
		MapNode test2 = new MapNode("name2","id",2.0,1.0);
		assertTrue(test1.hashCode()==test2.hashCode());
		test2 = new MapNode("name","id2",2.0,1.0);
		assertFalse(test1.hashCode()==test2.hashCode());
	}
	
	@Test
	void testCompareTo() {
		MapNode test1 = new MapNode("name","id",1.0,2.0);
		MapNode test2 = new MapNode("name2","id",2.0,1.0);
		assertTrue(test1.compareTo(test2)<0);
		assertTrue(test2.compareTo(test1)>0);
		test2 = new MapNode("name","id",2.0,1.0);
		assertTrue(test1.compareTo(test2)==0);
		test1 = new MapNode(null,"id",2.0,1.0);
		assertTrue(test1.compareTo(test2)<0);
		assertTrue(test2.compareTo(test1)>0);
		test2 = new MapNode(null,"id",2.0,1.0);
		assertTrue(test1.compareTo(test2)==0);
	}
	
	@Test
	void testDirection() {
		MapNode origin = new MapNode(null,"",0.0,0.0);
		MapNode point;
		String[] directions = {"East","NorthEast","North","NorthWest","West","SouthWest","South","SouthEast","East"};
		double val = 22.5;
		int i = 1;
		while (i < 9) {
			double rads = Math.toRadians(val);
			point = new MapNode(null,"",Math.cos(rads-0.0001),-1*(Math.sin(rads-0.0001)));
			assertTrue(MapNode.direction(origin,point).equals(directions[i-1]));
			point = new MapNode(null,"",Math.cos(rads+0.0001),-1*Math.sin(rads+0.0001));
			assertTrue(MapNode.direction(origin,point).equals(directions[i]));
			val = val + 45;
			i = i + 1;
			System.out.println();
		}
	}
}