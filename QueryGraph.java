import java.util.ArrayList;
import java.util.HashMap;


public class QueryGraph {
    private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
    private HashMap<Integer, Relation> rels = new HashMap<Integer, Relation>();
    private ArrayList<Integer> nodes_sort = new ArrayList<>();
    private int cptNodes = 0;
    private int cptRels = 0;
	
    public HashMap<Integer, Node> getNodes(){
	return nodes;
    }
    public HashMap<Integer, Relation> getRels() {
	return rels;
    }

    public void addNode(Node n){
	nodes.put(cptNodes++, n);
    }

    public void addRel(Relation r){
	rels.put(cptRels++, r);
    }

    public ArrayList<Integer> getNodesSort() {
	return nodes_sort;
    }

    public int getKey(int o){
	for(Integer key : nodes.keySet()){
	    if(nodes.get(key).getId() == o){
		return key;
	    }
	}
	return -1;
    }

    public int getIndexSort(int key){
	for(int i = 0; i < nodes_sort.size(); i++){
	    if(nodes_sort.get(i) == key){
		return i;
	    }
	}
	return -1;
    }
}
