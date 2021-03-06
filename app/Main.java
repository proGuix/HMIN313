import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.FileWriter;

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
	boolean bool = true;
	String file = null;

	File folder = new File(PathFile.dataset_folder);
	ArrayList<String> listFiles = Main.listFilesForFolder(folder);

	HashMap<Integer, String> filesHM = new HashMap<>();
	for(int i = 0; i < listFiles.size(); i++){
	    filesHM.put(i+1, listFiles.get(i));
	}

	while(bool){
	    System.out.println("\nChoose a dataset in repertory \"../test/dataset/\" : ");	    
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

	FileRDFParser fRDFPsr = new FileRDFParser(PathFile.dataset_folder + file);
	System.out.println("Parse .rdf file, create dictionary and create graph data");
	long startTime0   = System.currentTimeMillis();

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

	long endTime0   = System.currentTimeMillis();
	long totalTime0 = endTime0 - startTime0;
	System.out.println("Time import dataset " + totalTime0 + " ms");
	
	folder = new File(PathFile.queries_folder);
	listFiles = Main.listFilesForFolder(folder);

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

	    QueryParser qPsr = new QueryParser(dico, PathFile.queries_folder + file);
	    System.out.println("Parse queries");
	    qPsr.parseBis();

	    FileWriter fwR = null;
	    FileWriter fwT = null;
	    try{
		File writeResult = new File(PathFile.result_and_time_folder + file + "_results.csv");
		File writeTime = new File(PathFile.result_and_time_folder + file + "_times.csv");		
		writeResult.delete();        
		writeResult.createNewFile();
		writeTime.delete();        
		writeTime.createNewFile();
		fwR = new FileWriter(PathFile.result_and_time_folder + file + "_results.csv");	
		fwT = new FileWriter(PathFile.result_and_time_folder + file + "_times.csv");
	    } catch(IOException e){}

	    int nbQueries = qPsr.getNbQuery();
	    for(int i = 0; i < nbQueries; i++){
		System.out.println("\nCreate query graph");
		long startTime = System.currentTimeMillis();
		qPsr.createGraphBis(i);
		QueryGraph qGr = qPsr.getQueryGraph();
		ArrayList<String> result = new ArrayList<>();
		if(qGr == null){
		    System.out.println("Execute query over data");
		    System.out.println("Dictionary doesn't understand query");
		}
		else{
		    Resolution res = new Resolution(dicoHM, grData, prefTrData, qGr, nghInd);
		    System.out.println("Execute query over data");
		    result = res.execute();
		    System.out.println("Results of query");
		    for(String str : result){
			System.out.println(str);
		    }
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
		
		    fwT.write(totalTime + "\n");
		} catch(IOException e){}
	    }
	    try{
		fwR.close();
		fwT.close();
	    } catch(IOException e){}    
	}
    }
}
