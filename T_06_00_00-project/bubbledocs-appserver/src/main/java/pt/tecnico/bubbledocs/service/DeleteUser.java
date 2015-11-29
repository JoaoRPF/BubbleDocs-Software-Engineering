package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.*;
// add needed import declarations

public class DeleteUser extends BubbleDocsService {
	private String toDeleteUsername;
	private String userToken;
	private Utilizador user;
	
    public DeleteUser(String userToken, String toDeleteUsername) {
	// add code here
    	this.toDeleteUsername=toDeleteUsername;
    	this.userToken=userToken;
    }
    
    @Override 
    protected void dispatch() throws BubbleDocsException {  //update agora a cena remota esta no integrator, aqui apenas manipula as cenas localmente
    	BubbleDocs bd  = FenixFramework.getDomainRoot().getBubbledocs();
    	bd.checkIfIsRoot(userToken);
    	bd.presentInSession(userToken);
    	this.user= bd.getUtilizadorByUserToken(this.userToken);
    	this.user = bd.getUtilizadorByUsername(toDeleteUsername);
    	bd.removeUtilizador(this.user); 
		bd.restartUserSession(userToken);
    } 
}


