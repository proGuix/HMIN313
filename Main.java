import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import java.util.ArrayList;
import java.util.HashMap;


public class Main {
    public static void main(String args[]) throws FileNotFoundException {
		
	Reader reader = new FileReader(PathFile.source_file);

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
		
	Dictionary dico = rdfListener.getDico();
	dico.save();
	GraphData grData = dico.getGrData();
	grData.createIndex();
	grData.initNbOccPreds();
	PrefixTreeData prefTrData = new PrefixTreeData(grData);	
	prefTrData.create();
	NeighIndexBis nghInd = new NeighIndexBis(grData);
	nghInd.create();
	HashMap<Integer, String> dicoHM = dico.getDico();

	long startTime = System.currentTimeMillis();

	QueryParser qPsr = new QueryParser(dico);
	qPsr.parse();
	qPsr.createGraph();
	QueryGraph qGr = qPsr.getQueryGraph();
	Resolution res = new Resolution(dicoHM, grData, qGr, nghInd);
	ArrayList<String> result = res.execute();
	for(String str : result){
	    System.out.println(str);
	}

	long endTime   = System.currentTimeMillis();
	long totalTime = endTime - startTime;
	System.out.println("Time execute query " + totalTime + " ms");
    }
}
