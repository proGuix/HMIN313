import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class NeighIndexBis {
    private GraphData grData = null;
    private HashMap<Integer, GraphNeighboorBis> grNeighbs = new HashMap<>();
	
    public NeighIndexBis(GraphData grData){
	this.grData = grData;
    }

    public void create(){
	HashMap<Integer, Node> nodesGrData = grData.getNodes();
	Set<Integer> keys = nodesGrData.keySet();
	for(Integer key : keys){
	    Node node = nodesGrData.get(key);
	    ArrayList<Relation> rels = node.getRels();
	    if(rels != null){
		GraphNeighboorBis grNbs = new GraphNeighboorBis();
		HashMap<Integer, GraphNeighboorBis.Row> rows = grNbs.getRows();
		int cpt = 1;
		rows.put(0, grNbs.new Row(cpt++, -1, null, null));
		System.out.println("node = " + node.getId());
		for(int i = 0; i < rels.size(); i++){
		    Relation rel = rels.get(i);
		    int o = rel.getChild().getId();
		    ArrayList<Integer> preds = rel.getPreds();
		    if(i == 0){
			for(int j = 0; j < preds.size(); j++){
			    System.out.print(preds.get(j) + " ");
			    ArrayList<Integer> ps = new ArrayList<>();
			    for(int k = 0; k <= j; k++){
				ps.add(preds.get(k));
			    }
			    ArrayList<Integer> os = new ArrayList<>();
			    os.add(o);
			    if(j < preds.size()-1){
				rows.put(rows.size(), grNbs.new Row(cpt++, -1, ps, os));
			    }
			    else{
				rows.put(rows.size(), grNbs.new Row(cpt++, cpt++, ps, os));
			    }
			    grNbs.addPredInLinkList(preds.get(j), rows.size()-1);
			}
		    }
		    if(i > 0){
			ArrayList<Integer> predsBefore = rels.get(i-1).getPreds();
			int sizePrefSh = grNbs.sizePrefixShared(predsBefore, preds);
			int sizeRm = 0, sizeAdd = preds.size() - sizePrefSh;
			if(sizeAdd == 0){
			    sizeRm = predsBefore.size() - sizePrefSh;
			}
			else{
			    sizeRm = predsBefore.size() - sizePrefSh - 1;
			}
			grNbs.setEndBottomToUp(cpt, sizeRm);
			for(int j = 0; j < preds.size(); j++){
			    System.out.print(preds.get(j) + " ");
			    if(j < sizePrefSh){
				ArrayList<Integer> ps = new ArrayList<>();
				for(int k = 0; k <= j; k++){
				    ps.add(preds.get(k));
				}
				GraphNeighboorBis.Row row = grNbs.getRowByPreds(ps);
				row.getNeighbs().add(o);
			    }
			    else{
				ArrayList<Integer> ps = new ArrayList<>();
				for(int k = 0; k <= j; k++){
				    ps.add(preds.get(k));
				}
				ArrayList<Integer> os = new ArrayList<>();
				os.add(o);
				if(j < preds.size()-1){
				    rows.put(rows.size(), grNbs.new Row(cpt++, -1, ps, os));
				}
				else{
				    rows.put(rows.size(), grNbs.new Row(cpt++, cpt++, ps, os));
				}
				grNbs.addPredInLinkList(preds.get(j), rows.size()-1);
			    }
			}
		    }
		    if(i == rels.size()-1){
			grNbs.setEndBottomToUp(cpt, preds.size());
		    }
		    System.out.println();
		}
		grNeighbs.put(node.getId(), grNbs);
		System.out.println();
	       	System.out.println(grNbs);
		ArrayList<Integer> ps = new ArrayList<Integer>();
		ps.add(2567);
		ArrayList<Integer> nbs = neighbs(node.getId(), ps);
		for(int nb : nbs){
		    System.out.print(nb + " ");		    
		}
		System.out.println();
		System.out.println();
	    }
	}
    }

    public ArrayList<Integer> neighbs(int n, ArrayList<Integer> preds){
	return grNeighbs.get(n).neighbs(preds);
    }

    public HashMap<Integer, GraphNeighboorBis> getGrNeighbs(){
	return grNeighbs;
    }
}
