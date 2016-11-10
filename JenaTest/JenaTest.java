import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;


public class JenaTest {

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		Model model = ModelFactory.createDefaultModel();
		String pathToOntology = "500K.rdf";
		InputStream in = FileManager.get().open(pathToOntology);
		Long start = System.currentTimeMillis();
		Model inf = model.read(in, "", "N3");
		System.out.println("Import time : " + (System.currentTimeMillis() - start));
		
		String file = "Q_1_includes.queryset";
		ArrayList<String> query0 = new ArrayList<>();
		String line;
		int cpt = 0;
		try (
		     InputStream fis = new FileInputStream(file);
		     InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		     BufferedReader br = new BufferedReader(isr);
		     ) {
			while ((line = br.readLine()) != null) {		   
			    if(line.length() >= 6 && line.substring(0, 6).toUpperCase().equals("SELECT")){
			    	cpt++;
			    	if(cpt > 1){
			    		JenaTest.execute(query0, inf);
			    	}
			    	query0 = new ArrayList<>();
			    	System.out.println(line);
			    	query0.add(line);
			    }
			    if(line.length() > 0 && line.charAt(0) == '\t'){				
					String[] fields = line.split(" ");
					System.out.println(fields[1]);
					System.out.println(fields[2]);
					query0.add(fields[1]);
					query0.add(fields[2]);
			    }
			}
		} catch (FileNotFoundException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		JenaTest.execute(query0, inf);
	}
	
	public static void execute(ArrayList<String> query0, Model inf){
		String q = query0.get(0) + "?v0 ";
	    for(int i = 1; i < query0.size(); i += 2){
	    	q += query0.get(i) + " ";
	    	q += query0.get(i+1) + " . ";
	    }
	    q += "}";
		
		Query query = QueryFactory.create(q);

		Long start = System.currentTimeMillis();

		QueryExecution qexec = QueryExecutionFactory.create(query, inf);

		System.out.println("Query pre-processing time : " + (System.currentTimeMillis() - start));

		/**
		 * 
		 * Execute Query and print result
		 * 
		 */
		start = System.currentTimeMillis();

		try {

			ResultSet rs = qexec.execSelect();

			ResultSetFormatter.out(System.out, rs, query);

		} finally {

			qexec.close();
		}

		System.out.println("Query + Display time : " + (System.currentTimeMillis() - start));
	}
}
