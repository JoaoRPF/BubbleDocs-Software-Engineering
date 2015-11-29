package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator{
	 	private String result;
	    private String tokenUser;
		private int docId;
		private String cellId;
		private String reference;
	
	public final String getResult() {
        return result;
    }
	
	 public AssignReferenceCellIntegrator (String tokenUser, int docId, String cellId, String reference) {
	    	this.tokenUser=tokenUser;
	    	this.docId=docId;
	    	this.cellId=cellId;
	    	this.reference=reference;
	 }
	 
		@Override
		protected void dispatch() throws BubbleDocsException {
			// TODO Auto-generated method stub
			AssignReferenceCell assignReferenceCell = new AssignReferenceCell( tokenUser,  docId,  cellId,  reference);
	    	assignReferenceCell.execute();
	    	this.result=assignReferenceCell.getResult();
		}
	
}
