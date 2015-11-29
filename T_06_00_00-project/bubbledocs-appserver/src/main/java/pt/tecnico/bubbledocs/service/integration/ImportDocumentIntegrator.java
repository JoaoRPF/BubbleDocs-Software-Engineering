package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.ImportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ImportDocumentIntegrator extends BubbleDocsIntegrator {

	private String userToken;
	private String id;
	
	public ImportDocumentIntegrator(String userToken, String id){
		this.userToken = userToken;
		this.id  = id;
    }

	/***INVERTER ORDEM??
	 	LOCAL PRIMEIRO E REMOTE DEPOIS?
	 * **/
    @Override
    protected void dispatch() throws BubbleDocsException {
    	GetUsername4TokenService tokenService  = new GetUsername4TokenService(this.userToken);
    	tokenService.execute();
    	String username = tokenService.getUsername();
    	StoreRemoteServices store = new StoreRemoteServices();
    	byte[] folhaBytes = null;
    	try{
    		folhaBytes = store.loadDocument(username, id);
    	}catch(RemoteInvocationException e2){
       	 	throw new UnavailableServiceException(username, "export");
        }
    	ImportDocument importDoc = new ImportDocument(username, folhaBytes);
    	importDoc.execute();
    }
}
