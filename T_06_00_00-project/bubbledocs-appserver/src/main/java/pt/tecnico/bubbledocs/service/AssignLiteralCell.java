package pt.tecnico.bubbledocs.service;


import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;


// add needed import declarations

public class AssignLiteralCell extends BubbleDocsService {
    private String result;
    private String token;
    private int idFolha;
    private String cellId;
    private String literal;
   // private String nomeUser;
    
    public AssignLiteralCell(String accessUsername, int docId, String cellId, String literal) {
	// add code here	
    	this.token=accessUsername;
    	this.idFolha=docId;
    	this.cellId=cellId;
    	this.literal=literal;
   	
    }

    @Override
    protected void dispatch() throws BubbleDocsException {  	    	  	
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();	
    	bd.literalvalido(this.literal);
    	bd.validaIdCelula(this.cellId);    	
    	bd.presentInSession(this.token); 	
    	bd.spreadSheetExists(this.idFolha);
    	bd.permitedUser(idFolha, token);
    	this.result=bd.insereLiteralSucesso(idFolha, cellId, literal, token);
  	
    }

    public String getResult() {
        return this.result;
    }

}
