package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator{
    private int sheetId;  // id of the new sheet
	 private String userToken;
	 private String name;
	 private int rows;
	 private int columns;
	 
	 public int getSheetId() {
	        return sheetId;
	    }
	 
	 public CreateSpreadSheetIntegrator(String userToken, String name, int rows, int columns) {
	    	this.userToken=userToken;
	    	this.name=name;
	    	this.rows=rows;
	    	this.columns=columns;
	 }
	@Override
	protected void dispatch() throws BubbleDocsException {
		// TODO Auto-generated method stub
		CreateSpreadSheet createSpreadSheet = new CreateSpreadSheet (userToken,name,rows,columns);
        createSpreadSheet.execute();
        this.sheetId=createSpreadSheet.getSheetId();
	}

}
