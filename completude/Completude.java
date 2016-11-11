import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;


public class Completude {
    private int sizeDico = 0;
    private HashMap<Integer, String> dico = new HashMap<Integer, String>();
    private ArrayList<Integer> ens = new ArrayList<>();
    private ArrayList<Integer> ensBase = new ArrayList<>();
    private ArrayList<Integer> doublons = new ArrayList<>();
    private sizeBase = 0;
    private size = 0;
    private int cpt = 0;

    public int min(ArrayList<Integer> ens){
	int i = 0;
	for(int j = 1; j < ens.size(); j++){
	    int min = ens.get(i);
	    int n = ens.get(j);
	    if(min > n){
		i = j;
	    }
	}
	int min = ens.get(i);
	ens.remove(i);
	return min;
    }

    public String toString(){
	float tx1 = 100*((float)cpt/(float)sizeBase);
	float tx2 = 100*((float)doublons.size()/(float)size);
	String str = "Rate of completeness : " + tx1 + "\n";
	str += "Rate of doublons : " + tx2 + "\n";
	return str;
    }

			
    public void algo_completude(){
	int i = 0, j = 0;
	while(i < ensBase.size() && j < ens.size()){
	    int n1 = ensBase.get(i);
	    int n2 = ens.get(j);
	    if(j > 0){
		if(ens.get(j - 1) == n2){
		    if(!doublons.contains(n2)){
			doublons.add(n2);
		    }
		}
	    }
	    if(n1 == n2){
		i++; j++;
		cpt++;
	    }
	    if(n1 < n2){
		i++;
	    }
	    if(n1 > n2){
		j++;
	    }	    
	}
    }

    public int addInDico(String word){
	int key = getKey(word);
	if(key == -1){
	    dico.put(sizeDico, word);
	    sizeDico++;
	}
	return key;
    }

    public Integer getKey(String val){
	for(Integer key : dico.keySet()){
	    if(dico.get(key).equals(val)){
		return key;
	    }
	}
	return -1;
    }      

    public static void main(String[] args){
	Completude compl = new Completude();
	
    }
}
