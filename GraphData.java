import java.util.HashMap;


public class GraphData {
	private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
	private HashMap<Integer, Relation> rels = new HashMap<Integer, Relation>();
	
	public HashMap<Integer, Node> getNodes() {
		return nodes;
	}
	public HashMap<Integer, Relation> getRels() {
		return rels;
	}	
}
