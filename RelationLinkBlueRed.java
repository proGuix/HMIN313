

public class RelationLinkBlueRed {	
	private Integer pred = -1;
	private NodeLinkBlueRed father = null;
	private NodeLinkBlueRed child = null;
	
	public RelationLinkBlueRed(NodeLinkBlueRed father, NodeLinkBlueRed child){
		this.father = father;
		this.child = child;
	}
	
	public void addPred(int pred){
		this.pred = pred;
	}

	public Integer getPred() {
		return pred;
	}

	public NodeLinkBlueRed getChild() {
		return child;
	}

	public NodeLinkBlueRed getFather() {
		return father;
	}
}
