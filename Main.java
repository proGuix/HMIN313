import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Main {
    public static ArrayList<String> listFilesForFolder(File folder) {
	ArrayList<String> files = new ArrayList<>();
	for (File fileEntry : folder.listFiles()) {
	    if (!fileEntry.isDirectory()) {		
		files.add(fileEntry.getName());
	    }
	}
	return files;
    }
    
    public static void main(String args[]) throws FileNotFoundException {
		
	/*	Reader reader = new FileReader(PathFile.source_file);

		RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		RDFListener rdfListener = new RDFListener();
		rdfParser.setRDFHandler(rdfListener);
		try {
		rdfParser.parse(reader, "");
		} catch (Exception e) {

		}

		try {
		reader.close();
		} catch (IOException e) {
		}

	
		
		Dictionary dico = rdfListener.getDico();*/

	boolean bool = true;
	String file = null;
	while(bool){
	    System.out.println("\nChoose a dataset in repertory \"testsuite/dataset\" : ");
	    System.out.println(" 1 - 100K.rdf");
	    System.out.println(" 2 - 500K.rdf");
	    System.out.print("\nTape 1 or 2 : ");
	    Scanner sc = new Scanner(System.in);
	    String str = sc.nextLine();
	    switch(str){
	    case "1":
		file = "100K.rdf";
		bool = false;
		break;
	    case "2":
		file = "100K.rdf";
		bool = false;
		break;
	    }
	}

	FileRDFParser fRDFPsr = new FileRDFParser("testsuite/dataset/" + file);
	System.out.println("Parse .rdf file, create dictionary and create graph data");
	fRDFPsr.parse();
	
	Dictionary dico = fRDFPsr.getDico();
	//dico.save();
	GraphData grData = dico.getGrData();
	grData.createIndex();
	grData.initNbOccPreds();
	PrefixTreeData prefTrData = new PrefixTreeData(grData);	
	System.out.println("Create prefix tree from data");
	prefTrData.create();
	NeighIndexBis nghInd = new NeighIndexBis(grData);
	System.out.println("Create FP-TREE from all subjets of data");
	nghInd.create();
	HashMap<Integer, String> dicoHM = dico.getDico();

	
	File folder = new File("testsuite/queries/");
	ArrayList<String> listFiles = Main.listFilesForFolder(folder);

	while(true){
	    bool = true;
	    file = null;
	    HashMap<Integer, String> filesHM = new HashMap<>();
	    for(int i = 0; i < listFiles.size(); i++){
		filesHM.put(i+1, listFiles.get(i));
	    }
	    while(bool){
		System.out.println("\nChoose a file of queries in repertory \"testsuite/queries/\" : ");
		for(int i = 0; i < listFiles.size(); i++){
		    System.out.println(i+1 + " - " + listFiles.get(i));
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

	    QueryParser qPsr = new QueryParser(dico, "testsuite/queries/" + file);
	    System.out.println("Parse queries");
	    qPsr.parseBis();

	    FileWriter fwR = null;
	    FileWriter fwT = null;
	    try{
		File writeResult = new File("testsuite/queries_result_and_time/" + file + "_results.csv");
		File writeTime = new File("testsuite/queries_result_and_time/" + file + "_times.csv");		
		writeResult.delete();        
		writeResult.createNewFile();
		writeTime.delete();        
		writeTime.createNewFile();
		fwR = new FileWriter("testsuite/queries_result_and_time/" + file + "_results.csv");	
		fwT = new FileWriter("testsuite/queries_result_and_time/" + file + "_times.csv");
	    } catch(IOException e){}

	    int nbQueries = qPsr.getNbQuery();
	    for(int i = 0; i < nbQueries; i++){
		System.out.println("\nCreate query graph");
		long startTime = System.currentTimeMillis();
		qPsr.createGraphBis(i);
		QueryGraph qGr = qPsr.getQueryGraph();
		Resolution res = new Resolution(dicoHM, grData, prefTrData, qGr, nghInd);
		System.out.println("Execute query over data");
		ArrayList<String> result = res.execute();
		System.out.println("Results of query");
		for(String str : result){
		    System.out.println(str);
		}

		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time execute query " + totalTime + " ms");
		
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
		
		    fwT.write(totalTime + " ms\n");
		} catch(IOException e){}
	    }
	    try{
		fwR.close();
		fwT.close();
	    } catch(IOException e){}    
	}
    }
}
