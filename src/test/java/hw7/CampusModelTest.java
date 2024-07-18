package hw7;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Iterator;

public class CampusModelTest {
	
	void testFindPath(CampusModel model) {
		// tests illegal arguments
		assertThrows(IllegalArgumentException.class, () -> {model.findPath("",null);});
		assertThrows(IllegalArgumentException.class, () -> {model.findPath(null,"");});
		assertThrows(IllegalArgumentException.class, () -> {model.findPath("EMPAC","");});
		assertThrows(IllegalArgumentException.class, () -> {model.findPath("","EMPAC");});
		// ensures function works with legal arguments
		model.findPath("EMPAC", "EMPAC");
	}
	
	void testGetBuildings(CampusModel model) {
		MapNode curr;
		MapNode prev;
		Iterator<MapNode> itr = model.getBuildings();
		curr = itr.next();
		// ensures buildings are in lexicographical order
		while (itr.hasNext()) {
			prev = curr;
			curr = itr.next();
			assertTrue(curr.compareTo(prev)>=0);
		}
	}
	
	void testGetName(CampusModel model) {
		String validName = "EMPAC";
		String validID = "76";
		String invalid = "aaaaaa";
		assertTrue(model.getName(validName).equals("EMPAC"));
		assertTrue(model.getName(validID).equals("EMPAC"));
		assertThrows(IllegalArgumentException.class, () -> {model.getName(invalid);});
	}
	
	@Test
	void testObjectCorrectness() throws IOException {
		CampusModel model = new CampusModel();
		testGetName(model);
		testGetBuildings(model);
		testFindPath(model);
	}
}