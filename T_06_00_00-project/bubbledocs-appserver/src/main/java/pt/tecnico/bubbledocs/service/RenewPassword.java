package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;


public class RenewPassword extends BubbleDocsService {
	
	private String userToken;
	private Utilizador user;
	
	public RenewPassword (String userToken){
		this.userToken = userToken;
	}
	
	@Override
    protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		user = bd.getUtilizadorByUserToken(userToken);
		user.setPassLocal(false);
		bd.restartUserSession(userToken);
		
	}
}