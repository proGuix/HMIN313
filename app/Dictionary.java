import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;


public class Dictionary {
    private int sizeDico = 0;
    private HashMap<Integer, String> dico = new HashMap<Integer, String>();
    private ArrayList<ArrayList<Integer>> adj_list = new ArrayList<ArrayList<Integer>>();
	
    private GraphData grData = new GraphData();
    private HashMap<Integer, Node> nodes = grData.getNodes();
    private HashMap<Integer, Relation> rels = grData.getRels();
    private HashMap<Integer, ArrayList<Integer>> index_preds = grData.getIndexPreds();
    private HashMap<Integer, Integer> nb_occ_preds = grData.getNbOccPreds();
    private int sizeRels = 0; 
	
    public void add(String v1, String v2, String v3){
	int k1 = getKey(v1);
	if(k1 == -1){
	    k1 = sizeDico;
	    dico.put(k1, v1);
	    nodes.put(k1, new Node(k1));
	    sizeDico++;
	}
	int k2 = getKey(v2);
	if(k2 == -1){
	    k2 = sizeDico;
	    dico.put(k2, v2);
	    index_preds.put(k2, new ArrayList<Integer>());
	    nb_occ_preds.put(k2, 0);
	    sizeDico++;
	}
	int k3 = getKey(v3);
	if(k3 == -1){
	    k3 = sizeDico;
	    dico.put(k3, v3);
	    nodes.put(k3, new Node(k3));
	    sizeDico++;
	}
	Node n1 = nodes.get(k1);
	Node n2 = nodes.get(k3);
		
	boolean bool = true;
	for(Map.Entry<Integer, Relation> entry : rels.entrySet()) {
	    Relation r1 = entry.getValue();
	    int id1 = r1.getFather().getId();
	    int id2 = r1.getChild().getId();
	    ArrayList<Integer> preds = r1.getPreds();
	    if(id1 == k1 && id2 == k3){
		if(!preds.contains(k2)){ 		
		    insertPred(k2, preds);
		    Node node_Fath = nodes.get(id1);
		    ArrayList<Relation> rs = node_Fath.getRels();
		    rs.remove(r1);
		    insertRel(r1, node_Fath);
		}
		bool = false;
		break;
	    }
	}
	if(bool){
	    Relation r = new Relation(n1, n2);
	    r.add(k2);
	    insertRel(r, n1);
	    rels.put(sizeRels, r);
	    sizeRels++;
	}
    }

    public void insertRel(Relation r, Node n){
	ArrayList<Relation> rels = n.getRels();
	if(rels == null){
	    n.addRel(r);
	    return;
	}
	int i = 0;
	boolean bool = true;
	while(i < rels.size() && bool){
	    if(compare(r, rels.get(i)) <= 0){
		bool = false;
	    }
	    else{
		i++;
	    }
	}
	if(!bool){
	    rels.add(i, r);
	}
	else{
	    rels.add(r);
	}
    }

    public int compare(Relation r1, Relation r2){
	ArrayList<Integer> preds1 = r1.getPreds();
	ArrayList<Integer> preds2 = r2.getPreds();
	int size1 = preds1.size();
	int size2 = preds2.size();
	int i = 0;
	boolean bool = true;
	while(i < Math.min(size1, size2) && bool){
	    if(preds1.get(i) != preds2.get(i)){
		bool = false;
	    }
	    else{
		i++;
	    }
	}
	if(bool){
	    if(size1 > size2){
		return -1;
	    }
	    if(size1 < size2){
		return 1;
	    }
	    else{
		return 0;
	    }
	}
	else{
	    if(preds1.get(i) > preds2.get(i)){
		return 1;
	    }
	    else{
		return -1;
	    }
	}
    }
	
    public void insertPred(int k2, ArrayList<Integer> preds){
	int i = 0;
	boolean bool = true;
	while(i < preds.size() && bool){
	    if(k2 <= preds.get(i)){
		bool = false;
	    }
	    else{
		i++;
	    }
	}
	if(!bool){
	    preds.add(i, k2);
	}
	else{
	    preds.add(k2);
	}
    }
	
    public Integer getKey(String val){
	for(Integer key : dico.keySet()){
	    if(dico.get(key).equals(val)){
		return key;
	    }
	}
	return -1;
    }
	
    public void save(){
	File f = new File (PathFile.dico_file);
	f.delete();
	try {
	    FileWriter fw = new FileWriter (f, true);
	    for(int i = 0; i < sizeDico; i++){
		fw.write (i + "," + dico.get(i) + "\n");
	    }
	    fw.close();
	} catch (IOException exception) {
	}
		
	File g = new File (PathFile.relations_file);
	g.delete();
	try {
	    FileWriter gw = new FileWriter (g, true);
	    for(int i = 0; i < sizeRels; i++){
		if(i != 0){
		    gw.write ("\n");
		}
		Relation r = rels.get(i);
		ArrayList<Integer> preds = r.getPreds();
		String str = "[" + dico.get(preds.get(0));
		for(int j = 1; j < preds.size(); j++){
		    str += "," + dico.get(preds.get(j));
					
		}
		str += "]";
		//				gw.write (dico.get(r.getFather().getId()) + "," + str + "," + dico.get(r.getChild().getId()));
		gw.write (r.getFather().getId() + "," + r.getPreds() + "," + r.getChild().getId());
	    }
	    gw.close();
	} catch (IOException exception) {
	}
    }
	
    public GraphData getGrData() {
	return grData;
    }

    public HashMap<Integer, String> getDico() {
	return dico;
    }

    public HashMap<Integer, Relation> getRels() {
	return rels;
    }

    public HashMap<Integer, Node> getNodes() {
	return nodes;
    }
}
