package pt.tecnico.bubbledocs.service.integration;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.exception.RemoteInvocationException;
import pt.tecnico.bubbledocs.exception.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.remote.StoreRemoteServices;

public class ExportDocumentIntegrator extends BubbleDocsIntegrator {
	private byte[] docXML;
	private int docID;
	private String token;
	
	public byte[] getDocXML() {
    	return this.docXML;
    }

	public ExportDocumentIntegrator(String userToken, int docId) {

    	this.token = userToken;
    	docID = docId;
    }
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
    	BubbleDocs bubble = FenixFramework.getDomainRoot().getBubbledocs();
    	FolhaCalculo folha = bubble.getFolhaById(this.docID);
    	
		ExportDocument exportDocument = new ExportDocument(this.token,  this.docID);
		exportDocument.execute();
		this.docXML=exportDocument.getDocXML();
		try {
            //Instanciar serviço remoto
            StoreRemoteServices store = new StoreRemoteServices();
            store.storeDocument(bubble.getUtilizadorByUserToken(token).getUsername(), folha.getNome(), this.docXML);
   	 	}catch(RemoteInvocationException e2){
   	 		throw new UnavailableServiceException(folha.getNome(), "export");
        }
	}

}
