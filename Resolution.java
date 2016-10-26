import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Resolution {
	private Dictionary dico = null;
	private QueryParser qPsr = null;
	
	public Resolution(Dictionary dico, QueryParser qPsr) {
		super();
		this.dico = dico;
		this.qPsr = qPsr;
	}
	
	/*public ArrayList<String> execute(){
		ArrayList<String> results = new ArrayList<>();
		ArrayList<Integer> results_int = new ArrayList<>();
		ArrayList<String[]> list_p_o = qPsr.getList_p_o();
		ArrayList<Integer[]> list_p_o_int = new ArrayList<>();
		for(String[] p_o : list_p_o){
			list_p_o_int.add(new Integer[]{dico.getKey(p_o[0]), dico.getKey(p_o[1])});
		}
		HashMap<Integer, Node> nodes = dico.getNodes();
		int nodes_size = nodes.size();
		ArrayList<Integer> list_s_traited = new ArrayList<>();
		for(Map.Entry<Integer, Node> entry : nodes.entrySet()) {
		    Node n = entry.getValue();
		    int id = n.getId(); 
			if(!list_s_traited.contains(id)){
				
			}
		}
		return results;
	}*/
	
	public boolean node_contain_p_o(Node n, ArrayList<Integer[]> list_p_o_int){
		boolean bool = true;
		for(Integer[] p_o_int : list_p_o_int){
			
		}
		return bool;
	}
}
