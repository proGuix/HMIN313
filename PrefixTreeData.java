import java.util.ArrayList;
import java.util.HashMap;


public class PrefixTreeData {
    private GraphData grData = null;
    private Node nodeRoot = new Node();
	
    public PrefixTreeData(GraphData grData){
	this.grData = grData;
    }
	
    public void create(){
	HashMap<Integer, Node> nodesGrData = grData.getNodes();
	for(Integer key : nodesGrData.keySet()){
	    Node node = nodesGrData.get(key);
	    ArrayList<Relation> rels = node.getRels();
	    if(rels != null){
		for(Relation rel : rels){
		    addVertexEdgesLab(node, rel);
		}
	    }
	}
    }
	
    public void addVertexEdgesLab(Node subj, Relation rel){
	Integer idSubj = subj.getId();
	ArrayList<Integer> preds = rel.getPreds();
	merge(idSubj, preds);
    }
	
    public void merge(int idSubj, ArrayList<Integer> preds){
	Node nodeCur = nodeRoot;
	for(int pred : preds){
	    Relation rel = null;
	    ArrayList<Relation> rels = nodeCur.getRels();
	    if(rels != null){
		for(Relation r : rels){
		    if(pred == r.getPreds().get(0)){
			rel = r;
			break;
		    }
		}
	    }
	    Node nextNode = null;
	    if(rel != null){
		nextNode = rel.getChild();
		nextNode.addId(idSubj);
	    }
	    else{
		nextNode = new Node();
		nextNode.addId(idSubj);
		rel = new Relation(nodeCur, nextNode);
		rel.add(pred);
	    }
	    nodeCur = nextNode;
	}
    }

    public ArrayList<Integer> ensNodeWithPreds(int id, ArrayList<Integer> preds){
	Node nodeCur = nodeRoot;
	int i = 0; 
	boolean bool = true;
	while(i < preds.size() && bool){
	    Relation rel = null;
	    ArrayList<Relation> rels = nodeCur.getRels();
	    if(rels != null){
		for(Relation r : rels){
		    if(preds.get(i) == r.getPreds().get(0)){
			rel = r;
			break;
		    }
		}
	    }
	    Node nextNode = null;
	    if(rel != null){
		nextNode = rel.getChild();
		nodeCur = nextNode;
		i++;
	    }
	    else{
		bool = false;
	    }
	}
	if(bool){
	    return nodeCur.getList_Id();
	}
	else{
	    return new ArrayList<Integer>();
	}
    }
}
