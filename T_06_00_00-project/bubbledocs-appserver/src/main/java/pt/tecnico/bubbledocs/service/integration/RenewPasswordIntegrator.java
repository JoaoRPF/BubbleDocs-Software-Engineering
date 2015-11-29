package pt.tecnico.bubbledocs.service.integration;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Utilizador;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.RenewPassword;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {
	private String userToken;
	private Utilizador user;
	private String username;



	
	public RenewPasswordIntegrator (String userToken){
		this.userToken = userToken;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		user = bd.getUtilizadorByUserToken(userToken);
		username = user.getUsername();
		IDRemoteServices remote = new IDRemoteServices();
		try {
			remote.renewPassword(username);
			
    	}
		catch (RemoteInvocationException rie) {
    		throw new UnavailableServiceException("Serviço remoto falhou","");
		}	
		RenewPassword renewPassword = new RenewPassword(userToken);
    	renewPassword.execute();
	}

}
