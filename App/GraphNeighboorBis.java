import java.util.ArrayList;
import java.util.HashMap;
import java.lang.Math;

public class GraphNeighboorBis {

    private HashMap<Integer, Row> rows = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> linkedLists = new HashMap<>();
    
    public HashMap<Integer, Row> getRows(){
	return rows;
    }
    
    public HashMap<Integer, ArrayList<Integer>> getLinkedLists(){
	return linkedLists;
    }    

    public ArrayList<Integer> neighbs(ArrayList<Integer> preds){
	ArrayList<Integer> result = new ArrayList<Integer>();
	int size_preds = preds.size();
	for(int i = 0; i < size_preds; i++){
	    if(linkedLists.get(preds.get(i)) == null){
		return result;
	    }
	}
	if(size_preds == 1){
	    return groupNeighbs(linkedLists.get(preds.get(0)));
	}
	int[][] is = new int[size_preds][2];
	for(int i = 0; i < size_preds; i++){
	    is[i][0] = 0;
	    is[i][1] = linkedLists.get(preds.get(i)).size();
	}
	boolean bool0 = true;
	int i = 0;
	while(bool0){
	    boolean bool1 = true;
	    while(is[i][0] < is[i][1] && is[i+1][0] < is[i+1][1] && bool1){
		int comp = compare(preds.get(i), is[i][0], preds.get(i+1), is[i+1][0]);
		if(comp == -1){
		    is[i][0]++;
		    i--;
		    bool1 = false;
		}
		if(comp == 0){
		    if(i == size_preds - 2){
			result.add(i+1);
			is[i+1][0]++;
		    }
		    else{
			i++;
			bool1 = false;
		    }		  
		}
		if(comp == 1){
		    is[i+1][0]++;
		}
	    }
	    if(is[i][0] == is[i][1] || is[i+1][0] == is[i+1][1]){
		bool0 = false;
	    }
	}
	return result;	
    }

    public ArrayList<Integer> groupNeighbs(ArrayList<Integer> index_match){
	ArrayList<Integer> rs = new ArrayList<>();
	for(Integer i : index_match){
	    ArrayList<Integer> nbs = rows.get(i).getNeighbs();
	    rs = union(rs, nbs);
	}
	return rs;
    }

    public ArrayList<Integer> union(ArrayList<Integer> gr1, ArrayList<Integer> gr2){
	ArrayList<Integer> grResult = new ArrayList<>();
	for(Integer i : gr1){
	    grResult.add(i);
	}
	for(int i : gr2){
	    if(!grResult.contains(i)){
		grResult.add(i);
	    }
	}
	return grResult;
    }

    public int compare(int p1, int i1, int p2, int i2){
	Row r1 = rows.get(linkedLists.get(p1).get(i1));
	Row r2 = rows.get(linkedLists.get(p2).get(i2));
	if(r1.getBegin() < r2.getBegin() && r1.getEnd() > r2.getEnd()){
	    return 0;
	}
	else if(r1.getBegin() < r2.getBegin() && r1.getEnd() < r2.getEnd()){
	    return -1;
	}
	else{
	    return 1;
	}
    }

    public String toString(){
	String str = "";
	for(int i = 0; i < rows.size(); i++){
	    str += i + " " + rows.get(i) + "\n";
	}
	str += "\n";
	for(int key : linkedLists.keySet()){
	    ArrayList<Integer> lL = linkedLists.get(key);
	    str += key + " ";
	    for(int j = 0; j < lL.size(); j++){
		int index = lL.get(j);
		if(j < lL.size() - 1){
		    str += index + ",";
		}
		else{
		    str += index;
		}
	    }
	    str += "\n";	 
	}
	return str;
    }

    public void addPredInLinkList(int pred, int index){
	ArrayList<Integer> lL = linkedLists.get(pred);
	if(lL == null){
	    lL = new ArrayList<>();
	    lL.add(index);
	    linkedLists.put(pred, lL);
	}
	else{
	    lL.add(index);
	}
    }

    public void setEndBottomToUp(int cpt, int nb){
	int i = rows.size()-1;
	int j = 0;
	while(i > -1 && j < nb){
	    Row row = rows.get(i);
	    if(row.getEnd() == -1){
		row.setEnd(cpt++);	      
		j++;
	    }
	    i--;
	}
    }

    public int sizePrefixShared(ArrayList<Integer> predsBefore, ArrayList<Integer> preds){
	int size_max = Math.min(predsBefore.size(), preds.size());
	boolean bool = true;
	int i = 0;
	while(i < size_max && bool){
	    int p1 = predsBefore.get(i);
	    int p2 = preds.get(i);
	    if(p1 != p2){
		bool = false;
	    }
	    else{
		i++;
	    }
	}
	if(bool){
	    return size_max;
	}
	else{
	    return i;
	}
    }

    public Row getRowByPreds(ArrayList<Integer> preds){
	Row row = null;
	for(int i = 0; i < rows.size(); i++){
	    Row r = rows.get(i);
	    ArrayList<Integer> ps = r.getPreds();
	    if(ps != null){
		if(equalsPreds(preds, ps)){
		    row = r;
		    break;
		}
	    }
	}
	return row;
    }

    public boolean equalsPreds(ArrayList<Integer> preds1, ArrayList<Integer> preds2){
	int size1 = preds1.size();
	int size2 = preds2.size();
	if(size1 == size2){
	    boolean bool = true;
	    int cpt = 0;
	    while(cpt < size1 && bool){
		
		if(preds1.get(cpt).equals(preds2.get(cpt))){
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

    public class Row {
	private int begin = 0;
	private int end = 0;
	private ArrayList<Integer> preds = null;
	private ArrayList<Integer> neighbs = null;

	public Row(int begin, int end, ArrayList<Integer> preds, ArrayList<Integer> neighbs){
	    this.begin = begin;
	    this.end = end;
	    if(preds != null){
		this.preds = new ArrayList<>();
		for(Integer p : preds){
		    this.preds.add(p);
		}
	    }
	    if(neighbs != null){
		this.neighbs = new ArrayList<>();
		for(Integer nb : neighbs){
		    this.neighbs.add(nb);
		}
	    }
	}

	public String toString(){
	    String str = "";
	    str += begin + " " + end + " ";
	    if(preds == null){
		str += "null ";
	    }
	    else{
		for(int i = 0; i < preds.size(); i++){
		    int p = preds.get(i);
		    if(i < preds.size()-1){
			str += p + ",";
		    }
		    else{
			str += p;
		    }
		}
		str += " ";
	    }	    
	    if(neighbs == null){
		str += "null";
	    }
	    else{
		for(int i = 0; i < neighbs.size(); i++){
		    int n = neighbs.get(i);	     	     
		    if(i < neighbs.size()-1){
			str += n + ",";
		    }
		    else{
			str += n;
		    }
		}	
	    }	    
	    return str;
	}

	public int getBegin(){
	    return begin;
	}

	public int getEnd(){
	    return end;
	}

	public void setEnd(int end){
	    this.end = end;
	}

	public ArrayList<Integer> getPreds(){
	    return preds;
	}

	public ArrayList<Integer> getNeighbs(){
	    return neighbs;
	}
    }
}
