import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;


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
		NeighIndex nghInd = new NeighIndex(grData);
		nghInd.create();
		System.out.println("LOLILOL");
	}
}
