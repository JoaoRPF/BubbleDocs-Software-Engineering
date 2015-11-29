package pt.tecnico.bubbledocs.service;

import java.util.Set;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Sessao;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class GetUsername4TokenService extends BubbleDocsService {
	
	private String userToken;
	private String username;
	
	public GetUsername4TokenService (String userToken){
		this.userToken=userToken;
	}
		
	public String getUsername (){
		return this.username;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {

    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		this.username= bd.getUsernameByUserToken(this.userToken);
		
	}
}