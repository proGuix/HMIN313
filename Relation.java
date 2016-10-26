import java.util.ArrayList;


public class Relation {
	private ArrayList<Integer> preds = new ArrayList<Integer>();	
	private Node father = null;
	private Node child = null;
	
	public Relation(Node father, Node child){
		this.father = father;
		this.child = child;
	}
	
	public void add(int id){
		preds.add(id);
	}

	public ArrayList<Integer> getPreds() {
		return preds;
	}

	public Node getChild() {
		return child;
	}

	public Node getFather() {
		return father;
	}
}
