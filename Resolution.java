import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Resolution {
    private HashMap<Integer, String> dico = null;
    private GraphData grData = null;
    private QueryGraph qGr = null;
    private NeighIndexBis nghInd = null;
	
    public Resolution(HashMap<Integer, String> dico, GraphData grData, QueryGraph qGr, NeighIndexBis nghInd) {
	this.dico = dico;
	this.grData = grData;
	this.qGr = qGr;
	this.nghInd = nghInd;
	
    }    
	
    public ArrayList<String> execute(){
	ArrayList<String> results = new ArrayList<>();
	HashMap<Integer, Node> nodesQG = qGr.getNodes();
	ArrayList<Integer> indexNS = qGr.getNodesSort();
	ArrayList<Integer> indexNodeFinalSort = indexNS;
	Node nodeQGInit = nodesQG.get(indexNodeFinalSort.get(1));
	ArrayList<Integer> predsQGInit = nodeQGInit.getRelFath().getPreds();
	HashMap<Integer, ArrayList<Integer>> indexPreds = grData.getIndexPreds();
	ArrayList<Integer> indexEnsInit = indexPreds.get(predsQGInit.get(0));
	HashMap<Integer, Node> nodesData = grData.getNodes();
	for(int i : indexEnsInit){
	    Node nodeMatch = nodesData.get(i);
	    int id = nodeMatch.getId();
	    if(nghInd.neighbs(id, predsQGInit).contains(nodeQGInit.getId())){
		int j = 2;
		boolean bool = true;
		while(j < indexNodeFinalSort.size() && bool){
		    Node nodeQGNext = nodesQG.get(indexNodeFinalSort.get(j));
		    if(nghInd.neighbs(id, nodeQGNext.getRelFath().getPreds()).contains(nodeQGNext.getId())){
			j++;
		    }
		    else{
			bool = false;
		    }
		}
		if(bool){
		    results.add(dico.get(id));
		}
	    }
	}
	return results;
    }       
}
