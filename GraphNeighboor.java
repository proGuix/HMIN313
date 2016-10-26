import java.util.ArrayList;
import java.util.HashMap;


public class GraphNeighboor {
	private HashMap<Integer, ArrayList<NodeLinkBlueRed>> mapPredNode = new HashMap<>();
	private NodeLinkBlueRed nodeRoot = new NodeLinkBlueRed();
	
	public HashMap<Integer, ArrayList<NodeLinkBlueRed>> getMapPredNode() {
		return mapPredNode;
	}
	
	public NodeLinkBlueRed getNodeRoot() {
		return nodeRoot;
	}	
	
	public boolean contains(ArrayList<NodeLinkBlueRed> list, NodeLinkBlueRed node){
		for(NodeLinkBlueRed node0 : list){
			if(equals(node0, node)){
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(NodeLinkBlueRed node1, NodeLinkBlueRed node2){
		ArrayList<Integer> listPredsNode1 = ListPredEdgeOfNodeToArray(node1);
		ArrayList<Integer> listPredsNode2 = ListPredEdgeOfNodeToArray(node2);
		int size1 = listPredsNode1.size();
		int size2 = listPredsNode2.size();
		if(size1 == size2){
			boolean bool = true;
			int cpt = 0;
			while(cpt < size1 && bool){
				if(listPredsNode1.get(cpt) == listPredsNode2.get(cpt)){
					cpt++;
				}
				else{
					bool = false;
				}
			}
			return bool;
		}
		return false;
	}
	
	public boolean contains(NodeLinkBlueRed node1, NodeLinkBlueRed node2){
		ArrayList<Integer> listPredsNode1 = ListPredEdgeOfNodeToArray(node1);
		ArrayList<Integer> listPredsNode2 = ListPredEdgeOfNodeToArray(node2);
		int size1 = listPredsNode1.size();
		int size2 = listPredsNode2.size();
		if(size1 >= size2){
			int cpt1 = 0, cpt2 = 0;
			boolean bool = true;
			while(cpt1 < size1 && cpt2 < size2 && bool){
				if(listPredsNode1.get(cpt1) == listPredsNode2.get(cpt2)){
					cpt1++;
					cpt2++;
				}
				else if(listPredsNode1.get(cpt1) < listPredsNode2.get(cpt2)){
					cpt1++;
				}
				else{
					bool = false;
				}
			}
			return bool;
		}
		return false;
	}
	
	public ArrayList<Integer> ListPredEdgeOfNodeToArray(NodeLinkBlueRed node){
		ArrayList<Integer> result = new ArrayList<>();
		NodeLinkBlueRed nodeCourant = node;
		while(nodeCourant.getList_Id() != null){
			RelationLinkBlueRed relFath = node.getFatherRelBlue();
			int pred = relFath.getPred();
			result.add(0, pred);
			nodeCourant = relFath.getFather();
		}
		return result;
	}
}
