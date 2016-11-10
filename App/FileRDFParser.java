import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileRDFParser {    	
    private Dictionary dico = new Dictionary();
    private String file = null;

    public FileRDFParser(String file){
	this.file = file;
    }

    public void parse(){
	BufferedReader br;
        String line;
	String[] splitLine;
        try{
            br = new BufferedReader(new FileReader(new File(file)));
	    int cpt = 1;
            while((line = br.readLine()) != null){
		splitLine = line.split("\t");
		String s1 = splitLine[0];
		String s2 = splitLine[1];
		String s3 = splitLine[2].substring(0, splitLine[2].length()-2);
		System.out.println("Treatment triplet " + cpt);
		cpt++;
		dico.add(s1, s2, s3);
	    }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Dictionary getDico() {
	return dico;
    }
}