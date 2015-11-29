package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.exception.UnknownBubbleDocsUserException;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.DeleteUser;
import pt.tecnico.bubbledocs.service.GetUserInfoService;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class DeleteUserIntegrator extends BubbleDocsIntegrator {
	private String toDeleteUsername;
	private String userToken;
	private String [] user;
	
    public DeleteUserIntegrator(String userToken, String toDeleteUsername) {
	// add code here
    	this.toDeleteUsername=toDeleteUsername;
    	this.userToken=userToken;
    }
    
    @Override
	public void execute() throws BubbleDocsException {
    	try{
    		GetUserInfoService info = new GetUserInfoService(this.toDeleteUsername);
    		info.execute();
    		user= info.getUserInfo();
    	}catch(UnknownBubbleDocsUserException e){}
    	
		dispatch();
		IDRemoteServices remote = new IDRemoteServices();
    	try{
    		remote.removeUser(this.toDeleteUsername);
    	}
    	catch(RemoteInvocationException remoteException){
    		
    		CreateUser createPerson = new CreateUser (this.userToken,this.toDeleteUsername,user[0],user[1]);
            createPerson.execute();
    		throw new  UnavailableServiceException("Serviço remoto falhou","");

    	}
		
	}
    
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		DeleteUser deleteUser = new DeleteUser(this.userToken,  this.toDeleteUsername);
    	deleteUser.execute();
	}
}
