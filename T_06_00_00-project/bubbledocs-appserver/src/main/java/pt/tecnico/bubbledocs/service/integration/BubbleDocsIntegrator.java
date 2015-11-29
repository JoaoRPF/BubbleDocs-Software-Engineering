package pt.tecnico.bubbledocs.service.integration;

import java.util.Set;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;

public abstract class BubbleDocsIntegrator {

    public  void execute() throws BubbleDocsException {
        dispatch();
    }
    
    static BubbleDocs getBubbleDocs() {
        return FenixFramework.getDomainRoot().getBubbledocs();
    }

    static Utilizador getUtilizador(String personName) throws UnknownBubbleDocsUserException {
    	BubbleDocs bd = getBubbleDocs();
    	Utilizador ufinal=null;
        Set <Utilizador> p = bd.getUtilizadorSet();
        for(Utilizador aux :p){
        	if(aux.getUsername().equals(personName)){
        		ufinal=aux;
        	}
        }
        if (ufinal == null)
            throw new UnknownBubbleDocsUserException(personName);

        return ufinal;
    }
    
    protected abstract void dispatch() throws BubbleDocsException;

}
