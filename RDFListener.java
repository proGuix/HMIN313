
import org.openrdf.model.Statement;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class RDFListener extends RDFHandlerBase {
	
    private Dictionary dico = new Dictionary();
	
    public void handleStatement(Statement st) {
	
	dico.add(st.getSubject().toString(), st.getPredicate().toString(), st.getObject().toString());
	System.out.println(st.getSubject() + "," + st.getPredicate() + "," + st.getObject());
    }

    public Dictionary getDico() {
	return dico;
    }
}