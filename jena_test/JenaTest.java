import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.util.FileManager;


public class JenaTest {

    public static ArrayList<String> listFilesForFolder(File folder) {
	ArrayList<String> files = new ArrayList<>();
	for (File fileEntry : folder.listFiles()) {
	    if (!fileEntry.isDirectory()) {		
		files.add(fileEntry.getName());
	    }
	}
	return files;
    }
	
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

	boolean bool = true;
	String file = null;

	File folder = new File("../test/dataset");
	ArrayList<String> listFiles = JenaTest.listFilesForFolder(folder);

	HashMap<Integer, String> filesHM = new HashMap<>();
	for(int i = 0; i < listFiles.size(); i++){
	    filesHM.put(i+1, listFiles.get(i));
	}

	while(bool){
	    System.out.println("\nChoose a dataset in repertory \"../test/dataset\" : ");	    
	    for(int i = 0; i < listFiles.size(); i++){
		int j = i+1;
		System.out.println(" " + j + " - " + listFiles.get(i));
	    }
	    System.out.print("\nTape between 1 and " + listFiles.size() + " : ");
	    Scanner sc = new Scanner(System.in);
	    String str = sc.nextLine();
	    int strToInt = 0;
	    try {
		strToInt = Integer.parseInt(str);
		if(strToInt >= 1 && strToInt <= listFiles.size()){
		    file = filesHM.get(strToInt);
		    bool = false;
		}
	    } catch (NumberFormatException e) {
		System.out.println("Wrong number");
	    }	    
	}
		
	Model model = ModelFactory.createDefaultModel();
	String pathToOntology = "../test/dataset/" + file;
	InputStream in = FileManager.get().open(pathToOntology);
	long start = System.currentTimeMillis();
	Model inf = model.read(in, "", "N3");
	long end = System.currentTimeMillis();
	long time0 = end - start;
	System.out.println("Import time : " + time0);
		
		
	folder = new File("../test/queries/");
	listFiles = JenaTest.listFilesForFolder(folder);

	while(true){
	    bool = true;
	    file = null;
	    filesHM = new HashMap<>();
	    for(int i = 0; i < listFiles.size(); i++){
		filesHM.put(i+1, listFiles.get(i));
	    }
	    while(bool){
		System.out.println("\nChoose a file of queries in repertory \"../test/queries/\" : ");
		for(int i = 0; i < listFiles.size(); i++){
		    int j = i+1;
		    System.out.println(" " + j + " - " + listFiles.get(i));
		}
		System.out.print("\nTape between 1 and " + listFiles.size() + " : ");
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine();
		int strToInt = 0;
		try {
		    strToInt = Integer.parseInt(str);
		    if(strToInt >= 1 && strToInt <= listFiles.size()){
			file = filesHM.get(strToInt);
			bool = false;
		    }
		} catch (NumberFormatException e) {
		    System.out.println("Wrong number");
		}
	    }
	    String pathQuery =  "../test/queries/" + file;
	    ArrayList<ArrayList<String>> queries = new ArrayList<>();
	    String line;
	    try (
		 InputStream fis = new FileInputStream(pathQuery);
		 InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		 BufferedReader br = new BufferedReader(isr);
		 ) {
		    while ((line = br.readLine()) != null) {		   
			if(line.length() >= 6 && line.substring(0, 6).toUpperCase().equals("SELECT")){
			    ArrayList<String> query = new ArrayList<>();
			    query.add(line);
			    queries.add(query);
			}
			if(line.length() > 0 && line.charAt(0) == '\t'){				
			    String[] fields = line.split(" ");
			    queries.get(queries.size() - 1).add(fields[1]);
			    queries.get(queries.size() - 1).add(fields[2]);
			}
		    }
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
			
	    FileWriter fwR = null;
	    FileWriter fwT = null;
	    try{
		File writeResult = new File(file + "_results.csv");
		File writeTime = new File(file + "_times.csv");		
		writeResult.delete();        
		writeResult.createNewFile();
		writeTime.delete();        
		writeTime.createNewFile();
		fwR = new FileWriter(file + "_results.csv");	
		fwT = new FileWriter(file + "_times.csv");
	    } catch(IOException e){}
			
	    for(int k = 0; k < queries.size(); k++){			
		String q = queries.get(k).get(0);
		for(int i = 1; i < queries.get(k).size(); i += 2){
		    q += "?v0 " + queries.get(k).get(i) + " ";
		    q += queries.get(k).get(i+1) + " . ";
		}
		q += "}";
				
		Query query = QueryFactory.create(q);
	
		start = System.currentTimeMillis();
	
		QueryExecution qexec = QueryExecutionFactory.create(query, inf);
	
		end = System.currentTimeMillis();
		long time1 = end - start;
				
		System.out.println("Query pre-processing time : " + time1);
	
	
		ArrayList<String> result = new ArrayList<>();
				
		start = System.currentTimeMillis();
	
		try {
	
		    ResultSet rs = qexec.execSelect();
		    while(rs.hasNext()){
			QuerySolution qS = rs.next();
			RDFNode rdfN = qS.get("v0");
			String res = rdfN.toString();
			System.out.println(res);
			result.add(res);
		    }
		    //ResultSetFormatter.out(System.out, rs, query);
	
		} finally {
	
		    qexec.close();
		}
	
		end = System.currentTimeMillis();
		long time2 = end - start;
		System.out.println("Query + Display time : " + time2);
				
		try{
		    for(int j = 0; j < result.size(); j++) {
			if(j == 0){
			    fwR.write(result.get(j));
			}
			else{
			    fwR.write("," + result.get(j));
			}
		    }
		    if(result.size() == 0){
			fwR.write("NO_RESULT");
		    }
		    fwR.write("\n");
				
		    fwT.write((time1+time2) + "\n");
		} catch(IOException e){}
	    }
	    try{
		fwR.close();
		fwT.close();
	    } catch(IOException e){}
	}
    }
}
