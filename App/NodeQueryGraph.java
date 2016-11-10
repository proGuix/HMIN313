import java.util.ArrayList;


public class NodeQueryGraph {
	private Integer id = -1;
	private String var = null;
	private ArrayList<Relation> rels = null;
	
	public NodeQueryGraph(int id) {
		this.id = id;
	}

	public void addRel(Relation rel){
		if(rels == null){
			rels = new ArrayList<Relation>();
		}
		rels.add(rel);
	}
	
	public Integer getId() {
		return id;
	}

	public ArrayList<Relation> getRels() {
		return rels;
	}
	
	
}
