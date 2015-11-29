package pt.tecnico.bubbledocs.service.integration;

import pt.tecnico.bubbledocs.exception.BubbleDocsException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignRangeFunctionToCell;

public class AssignIntervalFunctionServiceIntegrator extends BubbleDocsIntegrator{
	private String result;
	private Integer resultFinal;
    private String userToken;
    private int idFolha;
    private String idCelula;
    private String funcaoIntervalo;

 public String getResult() {
        return this.result;
    }
 
 
 

public AssignIntervalFunctionServiceIntegrator(String idCelula, String funcaoIntervalo, int idFolha, String userToken) {
	// add code here	
    	this.userToken=userToken;
    	this.idFolha=idFolha;
    	this.idCelula=idCelula;
    	this.funcaoIntervalo=funcaoIntervalo;
   	
    }
@Override
protected void dispatch() throws BubbleDocsException {
	// TODO Auto-generated method stub
	
	AssignRangeFunctionToCell assignIntervalCell = new AssignRangeFunctionToCell (this.idCelula,this.funcaoIntervalo,this.idFolha, this.userToken);
	assignIntervalCell.execute();
	this.result=assignIntervalCell.getResult();
	this.resultFinal=assignIntervalCell.getResultFinal();
		
	}




public Integer getResultFinal() {
	return this.resultFinal;
}




public void setResultFinal(Integer resultFinal) {
	this.resultFinal = resultFinal;
}

}
