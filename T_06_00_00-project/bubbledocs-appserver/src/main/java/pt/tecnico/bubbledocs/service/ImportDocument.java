package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class ImportDocument extends BubbleDocsService {

	private String userToken;
	private String username;
	private String name;
	private String email;
	private int id;
	private byte[] folhaImport;
	
	public ImportDocument(String username, byte[] folha){
		this.username = username;
		this.folhaImport  = folha;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	bd.recoverFromBackup(folhaImport, username);	
    }
}
