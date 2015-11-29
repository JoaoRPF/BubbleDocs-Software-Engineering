package pt.tecnico.bubbledocs.domain;

public class FuncaoIntervalo extends FuncaoIntervalo_Base {
    
    public FuncaoIntervalo(Interval intervalo, String nomeFuncao) {
    	
        super();
        this.setArg(intervalo);
        this.setFuncaoIntervalo(nomeFuncao);
        //System.out.println("NOME FUNCAO NO CONSTR : " + nomeFuncao);
    }
    
    public FuncaoIntervalo() {
        super();
       
    }
    
    public void delete(){
    	super.delete();
    	if(this.getArg() != null)
    		this.getArg().delete();
    	
    	this.setArg(null);
    	this.deleteDomainObject();
    }
    
    public void init(Argumento intervalo, String nomeFuncao) {
    	this.setArg(intervalo);
        this.setFuncaoIntervalo(nomeFuncao);
       // System.out.println("NOME DA FUNCAO " + nomeFuncao);
        
    }
    
    public Integer compute(){
		return null;
	}

	@Override
	public Integer getContent() {
		// TODO Auto-generated method stub
		return this.compute();
	}
	
	
}
