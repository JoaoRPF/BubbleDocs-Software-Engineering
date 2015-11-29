package pt.tecnico.bubbledocs.domain;


public class Mul extends Mul_Base {
    
    public Mul(Argumento arg1, Argumento arg2, String nomeFuncao) {
        init(arg1, arg2, nomeFuncao);
    }
    
    public Integer compute(){
//    	Celula c = this.getArgumento().getCelula();
//    	Celula b = this.getArgumento2().getCelula();

    	
    	if(this.getArgumento().getvalue() == null || this.getArgumento2().getvalue()== null)
			return null;
		else
//			return Integer.parseInt(c.getConteudo().toString())+Integer.parseInt(b.getConteudo().toString());
			return this.getArgumento().getvalue()*this.getArgumento2().getvalue();

	}
    @Override
    public String toString(){
    	Integer a=this.compute();
    	if(a==null)
    		return "#VALUE";
    	else
    		return " "+a;
    }	
}