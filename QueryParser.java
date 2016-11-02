import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;


public class QueryParser {
    private HashMap<String, String> prefix = null;
    private String query = null;
    private String var_result = null;
    private ArrayList<String[]> list_p_o = null;
    private QueryGraph qGr = new QueryGraph();
    private Dictionary dico = null;
	
    public QueryParser(Dictionary dico){
	this.dico = dico;
    }
	
    public void parse(){
	prefix = new HashMap<String, String>();
	list_p_o = new ArrayList<>();
		
	String line;
	try (
	     InputStream fis = new FileInputStream(PathFile.query_file);
	     InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	     BufferedReader br = new BufferedReader(isr);
	     ) {
		while ((line = br.readLine()) != null) {
		    if(line.length() >= 6 && line.substring(0, 6).toUpperCase().equals("PREFIX")){
			int i1 = 7, i2 = line.indexOf(':'), i3 = i2 + 3, i4 = line.indexOf('>');
			String key = line.substring(i1, i2);
			String value = line.substring(i3, i4);
			prefix.put(key, value);
		    }
		    if(line.length() >= 6 && line.substring(0, 6).toUpperCase().equals("SELECT")){
			query = line;
			int s1 = query.indexOf('?') + 1, s2 = 0;
			for(int i = s1; i < query.length(); i++){
			    if(query.charAt(i) == ' '){
				s2 = i; 
				break;
			    }
			}
			var_result = query.substring(s1, s2);
			String clause_where = query.substring(query.indexOf('{') + 1, query.indexOf('}'));
			String[] trpl = clause_where.split(" . ");
			for(int i = 0; i < trpl.length; i++){
			    String[] p_o = new String[2];
			    String[] fields = trpl[i].split(" ");
			    for(int j = 1; j < 3; j++){
				String field = fields[j];
				if(field.indexOf(':') != -1){
				    String[] pref_suf = field.split(":"); 
				    p_o[j-1] = prefix.get(pref_suf[0]) + pref_suf[1];
				}
				else{
				    p_o[j-1] = field.substring(1, field.length());
				}
			    }
			    list_p_o.add(p_o);
			}
		    }
		}
	    } catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
	
    public String getVar_result() {
	return var_result;
    }

    public ArrayList<String[]> getList_p_o() {
	return list_p_o;
    }
	
    public void createGraph(){       
	HashMap<Integer, Node> nodes = qGr.getNodes();
	HashMap<Integer, Relation> rels = qGr.getRels();
	ArrayList<Integer> nSort = qGr.getNodesSort();
	qGr.addNode(new Node(var_result));
	nSort.add(0);
	for(String[] p_o : list_p_o){
	    String s1 = p_o[0];
	    int id1 = -1;
	    if(s1.indexOf('#') != -1){
		id1 = dico.getKey(s1);
	    }
	    String s2 = p_o[1];
	    int id2 = -1;
	    if(s2.indexOf('#') != -1){
		id2 = dico.getKey(s2);
	    }
	    int key = qGr.getKey(id2);
	    if(key == -1){
		Node n = new Node(id2);
		Relation r = new Relation(nodes.get(0), n);
		r.add(id1);
		n.setRelFath(r);
		qGr.addNode(n);
		qGr.addRel(r);
		nSort.add(nodes.size()-1);
	    }
	    else{
		Node n = nodes.get(key);
		Relation r = n.getRelFath();
		ArrayList<Integer> preds = r.getPreds();
		boolean bool = true;
		int i = 0;
		while(i < preds.size() && bool){
		    if(id1 < preds.get(i)){
			preds.add(i, id1);
			bool = false;
		    }
		    else if(preds.get(i) == id1){
			bool = false;
		    }
		    else{
			i++;
		    }
		}
		if(bool){
		    preds.add(id1);
		}
		i = qGr.getIndexSort(key);
		bool = true;
		if(i > 1){
		    while(i > 1 && bool){
			int j = nSort.get(i-1);
			if(preds.size() > nodes.get(j).getRelFath().getPreds().size()){
			    nSort.set(i, j);
			    nSort.set(i-1, key);
			    i--;
			}
			else{
			    bool = false;
			}
		    }
		}
	    }	    
	}
    }

    public QueryGraph getQueryGraph(){
	return qGr;
    }
}
