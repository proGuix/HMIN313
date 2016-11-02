import java.util.ArrayList;
import java.util.HashMap;


public class GraphData {
    private HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
    private HashMap<Integer, Relation> rels = new HashMap<Integer, Relation>();
    private HashMap<Integer, ArrayList<Integer>> index_preds = new HashMap<>();

    public void createIndex(){
	for(int key : nodes.keySet()){
	    Node node = nodes.get(key);
	    ArrayList<Relation> rs = node.getRels();
	    if(rs != null){
		for(Relation r : rs){
		    ArrayList<Integer> ps = r.getPreds();
		    for(int p : ps){
			ArrayList<Integer> index_p = index_preds.get(p);
			int ind = getIndOfKey(index_p, key);
			if(ind == -1){
			    index_p.add(key);
			}
		    }
		}
	    }
	}
    }

    public String toString(){
	String str = "";
	for(int key : index_preds.keySet()){
	    str += key + " | ";
	    ArrayList<Integer> index_p = index_preds.get(key);
	    for(int ind : index_p){
		str += ind + " ";
	    }
	    str += "\n";
	}
	return str;
    }

    public int getIndOfKey(ArrayList<Integer> index_p, int key){
	for(int i = 0; i < index_p.size(); i++){
	    int id = index_p.get(i);
	    if(id == key){
		return i;
	    }
	}
	return -1;
    }
	
    public HashMap<Integer, Node> getNodes() {
	return nodes;
    }
    public HashMap<Integer, Relation> getRels() {
	return rels;
    }	

    public HashMap<Integer, ArrayList<Integer>> getIndexPreds(){
	return index_preds;
    }
}
