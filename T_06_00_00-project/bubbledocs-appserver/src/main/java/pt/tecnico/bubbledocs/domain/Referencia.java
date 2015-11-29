package pt.tecnico.bubbledocs.domain;

import java.util.Set;

public class Referencia extends Referencia_Base {
    private FolhaCalculo folha;

    public Referencia(FolhaCalculo f,int [] vec) {
        super();
       this.setLinha(vec[0]);
       this.setColuna(vec[1]);
       this.folha=f;

    }
    
    public void delete(){
    	this.setCelula(null);
    	this.setCelula2(null);
    	this.setFuncaoBinaria(null);
    	this.setFuncaoBinaria2(null);
    	this.deleteDomainObject();
    }
    
    
    public Integer calcula(){
        Set <Celula> cell = this.folha.getCelulaSet();
        for(Celula c : cell){
            if(c.getLinha()==this.getLinha() && c.getColuna()==this.getColuna() )
                if(c.getConteudo()==null)
                    return null;
                else{
                                       
                    
                    return c.getConteudo().getContent();
                }
        }
        return null;
        
        
    }
    
    @Override
    public Integer getvalue() {
        return getContent();
    }
    
    @Override
    public Integer getContent() {
        return calcula();
    }
    



    
}
