package pt.tecnico.bubbledocs.domain;

import java.util.List;

public abstract class Argumento extends Argumento_Base {
    
    public Argumento() {
        super();
    }
    
    public void delete(){
        super.delete();
        //this.setCelula(null);
        this.setFuncaoBinaria(null);
        this.setFuncaoBinaria2(null);
        //this.setFuncaoIntervalo(null);
        this.deleteDomainObject();
    }


 public  abstract Integer getvalue();
    
    public Integer getContent() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public List<Celula> getIntervalo(){
        return null;
    }
    
    public String getContentIntervalo(){
        return null;
    }
    


}
