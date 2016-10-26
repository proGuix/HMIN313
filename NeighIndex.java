import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class NeighIndex {
	private GraphData grData = null;
	private HashMap<Integer, GraphNeighboor> mapGrNeigh = new HashMap<>();
	
	public NeighIndex(GraphData grData){
		this.grData = grData;
	}
	
	public void create(){
		HashMap<Integer, Node> nodesGrData = grData.getNodes();
		Set<Integer> keys = nodesGrData.keySet();
		for(Integer key : keys){
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
		if(!mapGrNeigh.containsKey(idSubj)){
			mapGrNeigh.put(idSubj, new GraphNeighboor());
		}
		ArrayList<Integer> preds = rel.getPreds();
		int idObj = rel.getChild().getId();
		merge(idSubj, idObj, preds);
	}
	
	public void merge(int idSubj, int idObj, ArrayList<Integer> preds){
		GraphNeighboor gr = mapGrNeigh.get(idSubj);
		NodeLinkBlueRed root = gr.getNodeRoot();
		NodeLinkBlueRed nodeLBR = root;
		int lengthBlue = 0;
		for(Integer pred : preds){
			lengthBlue++;
			RelationLinkBlueRed relLBR = null;
			ArrayList<RelationLinkBlueRed> rels = nodeLBR.getRelsBlue();
			if(rels != null){
				for(RelationLinkBlueRed rel : rels){
					if(pred == rel.getPred()){
						relLBR = rel;
						break;
					}
				}
			}
			NodeLinkBlueRed nextNodeLBR = null;
			if(relLBR != null){
				nextNodeLBR = relLBR.getChild();
				nextNodeLBR.addId(idObj);
			}
			else{
				nextNodeLBR = new NodeLinkBlueRed();
				nextNodeLBR.addId(idObj);
				relLBR = new RelationLinkBlueRed(nodeLBR, nextNodeLBR);
				nextNodeLBR.setFatherRelBlue(relLBR);
				relLBR.addPred(pred);
				constructLinkRed(gr, nextNodeLBR, pred);
			}
			nodeLBR = nextNodeLBR;
		}
	}
	
	public void constructLinkRed(GraphNeighboor gr, NodeLinkBlueRed node, int pred){
		HashMap<Integer, ArrayList<NodeLinkBlueRed>> map = gr.getMapPredNode();
		ArrayList<NodeLinkBlueRed> list_begin = map.get(pred);
		if(list_begin == null){
			ArrayList<NodeLinkBlueRed> list = new ArrayList<>();
			list.add(node);
			map.put(pred, list);
		}
		else{
			int cpt = 0;
			int size_init = list_begin.size();
			for(int i = 0; i < list_begin.size(); i++){
				NodeLinkBlueRed node_begin = list_begin.get(i);
				if(gr.contains(node, node_begin)){
					if(!gr.contains(list_begin, node)){
						list_begin.set(i, node);
					}
					else{
						list_begin.remove(i);
						i--;
					}
					node.addRelRed(new RelationLinkBlueRed(node, node_begin));
				}
				if(gr.contains(node_begin, node)){
					constructLinkRedBis(gr, node_begin, node);
				}
				if(!gr.contains(node, node_begin) && !gr.contains(node_begin, node)){
					cpt++;
				}
			}
			if(cpt == size_init){
				list_begin.add(node);
			}
		}
	}
	
	public void constructLinkRedBis(GraphNeighboor gr, NodeLinkBlueRed node_Fath, NodeLinkBlueRed new_node){
		ArrayList<RelationLinkBlueRed> relsRed = node_Fath.getRelsRed();
		if(relsRed == null){
			node_Fath.addRelRed(new RelationLinkBlueRed(node_Fath, new_node));
		}
		else{
			int cpt = 0;
			int size = relsRed.size();
			for(int i = 0; i < relsRed.size(); i++){
				RelationLinkBlueRed rel = relsRed.get(i);
				NodeLinkBlueRed node_Child = rel.getChild();
				if(gr.contains(new_node, node_Child)){
					new_node.addRelRed(new RelationLinkBlueRed(new_node, node_Child));
					relsRed.set(i, new RelationLinkBlueRed(node_Fath, new_node));
				}
				if(gr.contains(node_Child, new_node)){
					constructLinkRedBis(gr, node_Child, new_node);
				}
				if(!gr.contains(new_node, node_Child) && !gr.contains(node_Child, new_node)){
					cpt++;
				}
			}
			if(cpt == size){
				node_Fath.addRelRed(new RelationLinkBlueRed(node_Fath, new_node));
			}
		}
	}
}
