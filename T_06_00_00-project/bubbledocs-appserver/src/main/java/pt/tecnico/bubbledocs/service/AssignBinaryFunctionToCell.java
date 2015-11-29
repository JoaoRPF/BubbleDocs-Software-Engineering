package pt.tecnico.bubbledocs.service;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.domain.Conteudo;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

import java.util.*;

public class AssignBinaryFunctionToCell extends BubbleDocsService {
	
	private String cellid;
	private int folhaID;
	private String usertoken;
	private String content;
	private String result;
	
	public AssignBinaryFunctionToCell(String cellID, int folhaID, String userToken, String conteudo) {
		// add code here
		this.cellid = cellID;
		this.folhaID = folhaID;
		this.usertoken = userToken;
		this.content = conteudo;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	bd.validaIdCelula(this.cellid);
    	bd.presentInSession(this.usertoken); 	
    	bd.spreadSheetExists(this.folhaID);
    	bd.permitedUser(folhaID, usertoken);
		this.result= bd.insereFuncBinariaSucesso(folhaID, cellid, content, usertoken);
	}
	
	public String getResult() {
		return this.result;
	}
}
