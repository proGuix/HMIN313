import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Completude {
    private int sizeDico = 0;
    private HashMap<Integer, String> dico = new HashMap<Integer, String>();
    private ArrayList<String> ensExist = new ArrayList<>();
    private ArrayList<String> ensNExist = new ArrayList<>();
    private ArrayList<String> ensDbls = new ArrayList<>();

    public String toString(){
	float dicoS = (float)dico.size();
	float ensExistS = (float)ensExist.size();
	float ensNExistS = (float)ensNExist.size();
	float ensDblsS = (float)ensDbls.size();

	float tx1 = (100*(ensExistS+ensNExistS))/dicoS;
	float tx2 = (100*ensDblsS)/(ensExistS+ensNExistS);
	String str = "Rate of completeness : " + tx1 + " %\n";
	str += "Rate of doublons : " + tx2 + " %\n";
	//return str;
	return tx1 + "," + tx2 + "\n";
    }

			
    public void algo_completude(ArrayList<String> ens){
	for(String val : ens){
	    if(ensExist.contains(val) && !ensDbls.contains(val)){
		ensDbls.add(val);
	    }
	    if(ensNExist.contains(val) && !ensDbls.contains(val)){
		ensDbls.add(val);
	    }
	    if(!ensDbls.contains(val)){
		int k = getKey(val);
		if(k != -1){
		    ensExist.add(val);
		}
		else{
		    ensNExist.add(val);
		}
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
    
    public HashMap<Integer, String> getDico(){
	return dico;
    }

    public static void main(String[] args){
	ArrayList<Completude> complS = new ArrayList<>();

	String file = "jena_results.csv";
	BufferedReader br;
        String line;
	String[] splitLine;
        try{
            br = new BufferedReader(new FileReader(new File(file)));
            while((line = br.readLine()) != null){
		Completude compl = new Completude();
		splitLine = line.split(",");
		for(String r : splitLine){	       
		    compl.addInDico(r);
		}
		complS.add(compl);
	    }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

	file = "app_results.csv";
	int cpt = 0;
        try{
	    FileWriter fwR = new FileWriter("completude.csv");

            br = new BufferedReader(new FileReader(new File(file)));
            while((line = br.readLine()) != null){
		splitLine = line.split(",");
		ArrayList<String> ens = new ArrayList<>();
		for(String r : splitLine){
		    ens.add(r);
		    //System.out.println(r);
		}
		System.out.println();
		Completude cmpl = complS.get(cpt);
		/*HashMap<Integer, String> dc = cmpl.getDico();
		for(int k : dc.keySet()){
		    System.out.println(dc.get(k));
		}
		System.out.println();*/
		cmpl.algo_completude(ens);
		System.out.println(cmpl);
		fwR.write(cmpl.toString());
		cpt++;
	    }
	    fwR.close();
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
