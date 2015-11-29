package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class CreateUserIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String username;
	private String name;
	private String email;
	
	public CreateUserIntegrator(String userToken, String username, String name ,String email){
		this.userToken = userToken;
		this.username = username;
		this.email = email;
		this.name = name;
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		dispatch();
		IDRemoteServices remote = new IDRemoteServices();
		try{
			remote.createUser(username, email);
		}
		catch(RemoteInvocationException remoteException){
			DeleteUser deleteUser = new DeleteUser(userToken, username);
			deleteUser.execute();
			throw new  UnavailableServiceException("Serviço remoto falhou","");
		}
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		CreateUser createUser = new CreateUser(userToken, username, name, email);
		createUser.execute();
	}

}
