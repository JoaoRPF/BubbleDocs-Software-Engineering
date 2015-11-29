package pt.tecnico.bubbledocs.domain;

public class Add extends Add_Base {
    
    public Add(Argumento arg1, Argumento arg2, String nomeFuncao) {
        init(arg1, arg2, nomeFuncao);
    }
    
    public Integer compute(){
    	
    	if(this.getArgumento().getvalue() == null || this.getArgumento2().getvalue()== null)
			return null;
		else

			return this.getArgumento().getvalue()+this.getArgumento2().getvalue();

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
    

