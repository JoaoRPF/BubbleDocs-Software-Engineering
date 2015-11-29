package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.GetSpreadSheetContent;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator{
	private String[][] matriz = null;
	private String token;
    private int idFolha;
    
    public String[][] getMatriz() {
        return this.matriz;
    }
    
    public GetSpreadSheetContentIntegrator(String userToken, int idFolha) {
	    this.token = userToken;
	    this.idFolha = idFolha;
	}

	@Override
	protected void dispatch() throws BubbleDocsException {
		GetSpreadSheetContent spreadSheetContent = new GetSpreadSheetContent (this.token,this.idFolha);
    	spreadSheetContent.execute();
    	this.matriz=spreadSheetContent.getMatriz();
	}

}
