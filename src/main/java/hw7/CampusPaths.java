package hw7;

import hw6.QueueItem;
import java.io.IOException;
import java.util.Scanner;
import java.util.Iterator;

public class CampusPaths {
	
	// CampusPaths is NOT an ADT
	
	private static CampusModel model;
	
	/**
	 * @param: in1  The source input string (name/id)
	 * @param: in2  The destination input string (name/id)
	 * @modifies: System.out
	 * @effects:  If path exists between buildings associated with in1 and in2,
	 * 			  will print:
	 * 				Path from [in1 name] to [in2 name]:
	 * 					Walk [direction to subdest] to [subdest]
	 * 					...
	 *					Total distance: [total distance(3 decimals)] pixel units.\n
	 * 			  If no path exists, will print:
	 * 				There is no path from [in1 name] to [in2 name].\n
	 * 			  If input string in1/in2 is not associated with campus building,
	 * 			  will print:
	 * 				Unknown building: [in1/in2]
	 * @throws:  none
	 * @returns: none
	 */
	private static void displayPath(String in1, String in2) {
		// view
		String source = "";
		String dest = "";
		boolean notFound = false;
		// checks if inputs are valid / retrieves building names
		try {
			source = model.getName(in1);
		}
		catch (IllegalArgumentException e) {
			System.out.print("Unknown building: ["+in1+"]\n");
			notFound = true;
		}
		try {
			dest = model.getName(in2);
		}
		catch (IllegalArgumentException e) {
			if (!in2.equals(in1)) {
				System.out.print("Unknown building: ["+in2+"]\n");
				notFound = true;
			}
		}
		if (notFound) return;
		// calls path finder in model
		Iterator<QueueItem<MapNode>> itr = model.findPath(source, dest);
		// case: no valid path
		if (!itr.hasNext()) {
			System.out.print("There is no path from "+source+" to "+dest+".\n");
			return;
		}
		System.out.print("Path from "+source+" to "+dest+":\n");
		QueueItem<MapNode> curr = itr.next();
		// prints directions
		while (itr.hasNext()) {
			curr = itr.next();
			String direction = MapNode.direction(curr.parent,curr.node);
			String subDest;
			// subdest is intersection
			if (curr.node.getName()==null) {
				subDest = "Intersection "+curr.node.getID();
			}
			// subdest is building
			else subDest = curr.node.getName();
			System.out.print("\tWalk "+direction+" to ("+subDest+")\n");
		}
		// prints total distance
		String dist = String.format("%.3f", curr.pathWeight);
		System.out.print("Total distance: "+dist+" pixel units.\n");
	}
	
	/**
	 * @param: none
	 * @modifies: System.out
	 * @effects:  Prints all campus buildings in lexicographical order
	 * 			  with a new line between each building
	 * @throws:  none
	 * @returns: none
	 */
	private static void displayBuildings() {
		// view
		Iterator<MapNode> itr = model.getBuildings();
		MapNode curr;
		while (itr.hasNext()) {
			curr = itr.next();
			System.out.println(curr.getName()+","+curr.getID());
		}
	}
	
	/**
	 * @param: none
	 * @modifies: System.out
	 * @effects:  Prints all possible commands
	 * @throws:  none
	 * @returns: none
	 */
	private static void displayCommands() {
		// view
		System.out.println("b lists all buildings");
		System.out.println("r prints directions for the shortest route between any two buildings");
		System.out.println("q quits the program");
		System.out.println("m prints a menu of all commands");
	}
	
	/**
	 * @param: args  the command line arguments of the program
	 * @modifies: model, System.out
	 * @effects:  model: initializes model as a new CampusModel object
	 * 			  System.out: prints to System.out based on user input.
	 * @throws:  IOException if CampusModel construction fails
	 * @returns: none
	 */
	public static void main(String[] args) throws IOException {
		// controller
		model = new CampusModel();
		Scanner reader = new Scanner(System.in);
		String command = "";
		String in1;
		String in2;
		while (true) {
			command = reader.nextLine();
			if (command.equals("q")) break;
			else if (command.equals("m")) {
				// view
				displayCommands();
			}
			//controller
			else if (command.equals("r")) {
				// view
				System.out.print("First building id/name, followed by Enter: ");
				// controller
				in1 = reader.nextLine();
				// view
				System.out.print("Second building id/name, followed by Enter: ");
				// controller
				in2 = reader.nextLine();
				// view
				displayPath(in1,in2);
			}
			//controller
			else if (command.equals("b")) {
				// view
				displayBuildings();
			}
			else System.out.println("Unknown option");
		}
		reader.close();
	}
}