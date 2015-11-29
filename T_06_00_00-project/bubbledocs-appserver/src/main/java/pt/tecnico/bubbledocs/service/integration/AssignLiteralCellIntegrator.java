package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {
	 	private String result;
	    private String token;
	    private int idFolha;
	    private String cellId;
	    private String literal;
	
	 public String getResult() {
	        return this.result;
	    }

	
	public AssignLiteralCellIntegrator(String accessUsername, int docId, String cellId, String literal) {
		// add code here	
	    	this.token=accessUsername;
	    	this.idFolha=docId;
	    	this.cellId=cellId;
	    	this.literal=literal;
	   	
	    }
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		AssignLiteralCell assignLiteralCell = new AssignLiteralCell (this.token,this.idFolha,this.cellId,this.literal);
    	assignLiteralCell.execute();
    	this.result=assignLiteralCell.getResult();
	}

}
