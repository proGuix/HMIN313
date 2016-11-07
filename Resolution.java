import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Resolution {
    private HashMap<Integer, String> dico = null;
    private GraphData grData = null;
    private PrefixTreeData prefTrData = null;
    private QueryGraph qGr = null;
    private NeighIndexBis nghInd = null;
	
    public Resolution(HashMap<Integer, String> dico, GraphData grData, PrefixTreeData prefTrData, QueryGraph qGr, NeighIndexBis nghInd) {
	this.dico = dico;
	this.grData = grData;
	this.prefTrData = prefTrData;
	this.qGr = qGr;
	this.nghInd = nghInd;
	
    }

    public ArrayList<Integer> indexNFS(ArrayList<Integer> index, ArrayList<Integer> sortPreds, HashMap<Integer, Node> nodesQG){
	ArrayList<Integer> indexCopy = new ArrayList<>();
	for(int i : index){
	    indexCopy.add(i);
	}
	ArrayList<Integer> indexNFS = new ArrayList<>();
	indexNFS.add(indexCopy.get(0));
	for(int p : sortPreds){
	    for(int i = 1; i < indexCopy.size(); i++){
		if(nodesQG.get(indexCopy.get(i)).getRelFath().getPreds().contains(p)){
		    indexNFS.add(indexCopy.get(i));
		    indexCopy.remove(i);
		    i--;
		}
	    }
	}
	return indexNFS;
    }

    public ArrayList<Integer> sortPreds(ArrayList<Integer> preds){	
	HashMap<Integer,Integer> nbOccPreds = grData.getNbOccPreds();
	ArrayList<Integer> ps = new ArrayList<>();
	for(int p : preds){
	    ps.add(p);
	}
	ArrayList<Integer> psSort = new ArrayList<>();
	for(int i = 0; i < preds.size(); i++){
	    int min = minOccPred(ps, nbOccPreds);
	    psSort.add(min);
	}
	return psSort;
    }

    public int minOccPred(ArrayList<Integer> preds, HashMap<Integer,Integer> nbOccPreds){
	int minInd = 0;
	for(int i = 1; i < preds.size(); i++){
	    int n1 = nbOccPreds.get(preds.get(minInd));
	    int n2 = nbOccPreds.get(preds.get(i));
	    if(n1 > n2){
		minInd = i;
	    }
	}
	int min = preds.get(minInd);
	preds.remove(minInd);
	return min;
    }
	
    public ArrayList<String> execute(){
	ArrayList<String> results = new ArrayList<>();
	HashMap<Integer, Node> nodesQG = qGr.getNodes();
	ArrayList<Integer> indexNS = qGr.getNodesSort();

	ArrayList<Integer> srtPrds = sortPreds(qGr.getPreds());
        ArrayList<Integer> indexNodeFinalSort = indexNFS(indexNS, srtPrds, nodesQG);

	Node nodeQGInit = nodesQG.get(indexNodeFinalSort.get(1));
	ArrayList<Integer> predsQGInit = nodeQGInit.getRelFath().getPreds();

	ArrayList<Integer> indexEnsInit = prefTrData.ensNodeWithPreds(predsQGInit);
	
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
