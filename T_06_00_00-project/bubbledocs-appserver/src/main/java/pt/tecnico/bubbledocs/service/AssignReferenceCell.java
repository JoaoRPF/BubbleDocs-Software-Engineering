package pt.tecnico.bubbledocs.service;


import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;

public class AssignReferenceCell extends BubbleDocsService {
    private String result;
    private String tokenUser;
	private int docId;
	private String cellId;
	private String reference;

    public AssignReferenceCell(String tokenUser, int docId, String cellId, String reference) {
    	this.tokenUser=tokenUser;
    	this.docId=docId;
    	this.cellId=cellId;
    	this.reference=reference;
    }

    @Override
    protected void dispatch() throws BubbleDocsException {
    	BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
    	bd.validaIdCelula(cellId);
    	bd.presentInSession(tokenUser);
    	bd.spreadSheetExists(docId);
    	bd.permitedUser(docId, tokenUser);
    	this.result=bd.insereReferenciaSucesso(docId, cellId, reference, tokenUser);
    }

    public final String getResult() {
        return result;
    }
}
