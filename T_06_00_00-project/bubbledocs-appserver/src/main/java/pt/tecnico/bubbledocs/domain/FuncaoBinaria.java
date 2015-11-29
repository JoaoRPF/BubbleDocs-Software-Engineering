package pt.tecnico.bubbledocs.domain;

public class FuncaoBinaria extends FuncaoBinaria_Base {
    
    public FuncaoBinaria(Argumento arg1, Argumento arg2, String nomeFuncao) {
        super();
        this.setArgumento(arg1);
        this.setArgumento2(arg2);
        this.setFuncao(nomeFuncao);
    }
    
    public FuncaoBinaria() {
        super();
       
    }
    
    public void delete(){
    	super.delete();
    	if(this.getArgumento() != null)
    		this.getArgumento().delete();
    	if(this.getArgumento2() != null)
    		this.getArgumento2().delete();
    	this.setArgumento(null);
    	this.setArgumento2(null);
    	//this.setCelula(null);
    	this.deleteDomainObject();
    }
    
    public void init(Argumento arg1, Argumento arg2, String nomeFuncao) {
        //super();
        this.setArgumento(arg1);
        this.setArgumento2(arg2);
        this.setFuncao(nomeFuncao);
    }

	@Override
	public Integer getContent() {
		// TODO Auto-generated method stub
		return null;
	}    

}
