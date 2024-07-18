package hw7;


/**
 * MapNode represents a node on the RPI campus map.
 * MapNodes have a name, an ID, a x coordinate, and
 * a y coordinate. MapNode is immutable.
 */
public class MapNode implements Comparable<MapNode> {
	
	/*
	 * Representation Invariant:
	 * 		id, x, y != null
	 * 
	 * 
	 * Abstraction function:
	 * 		name -> the name of the MapNode. if name==null, MapNode has no name.
	 * 		id -> the ID of the MapNode
	 * 		x -> the x coordinate of the MapNode
	 * 		y -> the y coordinate of the MapNode
	 */
	
	private String name;
	private String id;
	private Double x;
	private Double y;
	
	/**
	 * @param: ID      The ID of the MapNode
	 * @param: xCoord  The X coordinate of the MapNode
	 * @param: yCoord  The Y coordinate of the MapNode
	 * @modifies: id, x, y
	 * @effects:  name = Name, id = ID, x = xCoord, y = yCoord
	 * @throws:  IllegalArgumentException if ID or xCoord or yCoord are null
	 * @returns: A new MapNode object
	 */
	MapNode(String Name, String ID, Double xCoord, Double yCoord) {
		if (ID==null||xCoord==null||yCoord==null) {
			throw new IllegalArgumentException("Arguments must be non null");
		}
		name = Name;
		id = ID;
		x = xCoord;
		y = yCoord;
	}
	
	/**
	 * @param: none
	 * @modifies: none
	 * @effects:  none
	 * @throws: none
	 * @returns: the name of this MapNode if it exists, else null
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param: none
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: the ID of this MapNode
	 */
	public String getID() {return id;}
	
	/**
	 * @param: none
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: the x coordinate of this MapNode
	 */
	public Double getX() {return x;}
	
	/**
	 * @param: none
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: the y coordinate of this building
	 */
	public Double getY() {return y;}
	
	/**
	 * @param: a  the object to compare this object to.
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: true if a is a MapNode and the ID of a
	 * 			      is equal to this object's ID
	 */
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof MapNode)) return false;
		else return id.equals(((MapNode)a).getID());
	}
	
	/**
	 * @param: none
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: the hash code of this object's ID.
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	/**
	 * @param: a   the MapNode to be compared to
	 * @modifies: none
	 * @effects:  none
	 * @throws:  none
	 * @returns: compares objects by name if available, else id.
	 */
	@Override
	public int compareTo(MapNode a) {
		if (name==null && a.getName()==null) {
			return 0;
		}
		if (name==null) {
			return -1;
		}
		if (a.getName()==null) {
			return 1;
		}
		return name.compareTo(a.getName());
	}
	
	/**
     * @requires none
     * @param source  The source MapNode
     * @param dest    The destination MapNode
     * @modifies none
     * @effects none
     * @returns The direction to traverse from source to dest based on the angle between
     *          the two nodes.
     * @throws IllegalArgumentException if source or dest are null
     */
	public static String direction(MapNode source, MapNode dest) {
		if (source==null||dest==null) throw new IllegalArgumentException("Arguments must be non null");
		double angle = Math.toDegrees(Math.atan2(-1*(dest.getY()-source.getY()),dest.getX()-source.getX()));
		if (angle < 0) {
			angle = 180 + (180 + angle);
		}
		if (angle < 337.5) {
			if (angle < 292.5) {
				if (angle < 247.5) {
					if (angle < 202.5) {
						if (angle < 157.5) {
							if (angle < 112.5) {
								if (angle < 67.5) {
									if (angle < 22.5) {
										return "East";
									}
									else return "NorthEast";
								}
								else return "North";
							}
							else return "NorthWest";
						}
						else return "West";
					}
					else return "SouthWest";
				}
				else return "South";
			}
			else return "SouthEast";
		}
		else return "East";
	}
}