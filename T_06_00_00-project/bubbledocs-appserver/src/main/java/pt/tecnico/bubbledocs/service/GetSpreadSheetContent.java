package pt.tecnico.bubbledocs.service;


import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.bubbledocs.domain.BubbleDocs;
import pt.tecnico.bubbledocs.exception.BubbleDocsException;


public class GetSpreadSheetContent extends BubbleDocsService {
	
	private String userToken;
    private int idFolha;
    private String[][] matriz = null;
	
	public GetSpreadSheetContent(String userToken, int idFolha) {
	    this.userToken = userToken;
	    this.idFolha = idFolha;
	}
	
	public String[][] getMatriz(){
		return this.matriz;
	}
	
	@Override
	protected void dispatch() throws BubbleDocsException {
		BubbleDocs bd = FenixFramework.getDomainRoot().getBubbledocs();
		bd.presentInSession(userToken);
    	bd.spreadSheetExists(idFolha);
    	bd.permitedUser(idFolha, userToken);
		matriz = bd.getConteudoFolha(idFolha);
	}
	
}