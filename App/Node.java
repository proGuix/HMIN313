import java.util.ArrayList;


public class Node {
    public enum Type{
	CST,
	    VAR
	    }
    private Type tp = Type.CST;
    private Integer id = -1;
    private String var_name = null;;
    private ArrayList<Relation> rels = null;
    private Relation relFath = null;
    private ArrayList<Integer> list_id = null;
	
    public Node(){}

    public Node(int id) {
	this.id = id;
    }

    public Node(String name_vr) {
	this.id = id;
	this.tp = tp;
    }

    public void addId(int id){
	if(list_id == null){
	    list_id = new ArrayList<>();
	}
	list_id.add(id);
    }

    public void addRel(Relation rel){
	if(rels == null){
	    rels = new ArrayList<Relation>();
	}
	rels.add(rel);
    }

    public void setRelFath(Relation rel){
	relFath = rel;
    }

    public Relation getRelFath(){
	return relFath;
    }
	
    public Integer getId() {
	return id;
    }

    public ArrayList<Integer> getList_Id() {
	return list_id;
    }

    public ArrayList<Relation> getRels() {
	return rels;
    }

    public Type getTp(){
	return tp;
    }
}
