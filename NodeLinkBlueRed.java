import java.util.ArrayList;


public class NodeLinkBlueRed {
	private ArrayList<Integer> list_id = null;
	private ArrayList<RelationLinkBlueRed> relsBlue = null;
	private ArrayList<RelationLinkBlueRed> relsRed = null;
	private RelationLinkBlueRed fatherRelBlue = null;

	public void addId(int id){
		if(list_id == null){
			list_id = new ArrayList<>();
		}
		list_id.add(id);
	}
	
	public void addRelBlue(RelationLinkBlueRed rel){
		if(relsBlue == null){
			relsBlue = new ArrayList<RelationLinkBlueRed>();
		}
		relsBlue.add(rel);
	}
	
	public void addRelRed(RelationLinkBlueRed rel){
		if(relsRed == null){
			relsRed = new ArrayList<RelationLinkBlueRed>();
		}
		relsRed.add(rel);
	}
	
	public ArrayList<Integer> getList_Id() {
		return list_id;
	}

	public ArrayList<RelationLinkBlueRed> getRelsBlue() {
		return relsBlue;
	}
	
	public ArrayList<RelationLinkBlueRed> getRelsRed() {
		return relsRed;
	}

	public RelationLinkBlueRed getFatherRelBlue() {
		return fatherRelBlue;
	}

	public void setFatherRelBlue(RelationLinkBlueRed fatherRelBlue) {
		this.fatherRelBlue = fatherRelBlue;
	}
}
