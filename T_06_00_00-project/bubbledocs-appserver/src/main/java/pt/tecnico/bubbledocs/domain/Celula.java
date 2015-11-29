package pt.tecnico.bubbledocs.domain;

import java.util.Set;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class Celula extends Celula_Base {
    private String conteudoALL;
	public Celula(){
		super();
	}
    
    public Celula(int linha, int coluna) {
        super();
        this.setLinha(linha);
        this.setColuna(coluna);

    }
    
    public void insereConteudo (Conteudo conteudo){
    	if(this.getConteudo()!=null)
    		this.getConteudo().delete();
    	this.setConteudo(conteudo);
    }
    
    public void delete(){
    	Set<Referencia> refs = this.getReferenciaSet();
    	for(Referencia ref : refs){
    		ref.delete();
    	}
    	Conteudo c = this.getConteudo();
    	c.delete();
    	this.setFolhacalculo(null);
    	this.deleteDomainObject();
    }
    
    
    public void importFromXML(FolhaCalculo f,Element element) {
    	
    	try{
    		setColuna(element.getAttribute("coluna").getIntValue());
    		setLinha(element.getAttribute("linha").getIntValue());
    		this.setContent(element.getAttributeValue("contentValue"));
    		
    		
    	}catch(DataConversionException e){
    		e.printStackTrace();
    	}
    	Element conteudo = element.getChild("conteudo");
    	Conteudo c=Conteudo.parseConteudo(f, this.getContent());
    	c.importFromXML(conteudo);
    	
    	c.setStringContent(getContent());
    	this.insereConteudo(c);
    	this.setConteudo(c);
    	c.setCelula(this);

    }
    
    public Element exportToXML(){
    	
    	Element element = new Element("celula");
    	element.setAttribute("FolhaCalculo", Integer.toString(this.getFolhacalculo().getIdentificador()));
    	element.setAttribute("coluna", Integer.toString(getColuna()));
        element.setAttribute("linha", Integer.toString(getLinha()));
        element.setAttribute("contentValue", this.getContent());
        Conteudo c = this.getConteudo();
        if(c!=null)
        	element.addContent(c.exportToXML());
    
        return element;
    }
    
    public String getContent(){
    	return this.conteudoALL;
    }
    public void setContent(String cont){
    	this.conteudoALL=cont;
    }
    
}
