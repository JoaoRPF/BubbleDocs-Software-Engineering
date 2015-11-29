package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

import pt.tecnico.bubbledocs.exception.InvalidCellId;

public abstract class Conteudo extends Conteudo_Base {
  
    private  String test;
    public static String nomeFuncao1;
    public static String args1;
    public static String argg1;
    public static String argg2;
    public static int[] cellID;
    public static int literal1;
    public static String[] argumentos1;

    public void delete(){
    	this.setCelula(null);
    	
    }
    
    public static Conteudo parseConteudo(FolhaCalculo f,String conteudo) {
        if (conteudo.contains("(")) { // E uma funcao
            String funcao = conteudo.substring(1); // remove 
            String nomeFuncao = parseNomeFuncao(funcao);
            String argumento = parseArgumentoFuncao(funcao);
            if (conteudo.contains(",")){
            	return parseFuncaoBinaria(f,nomeFuncao, argumento);
            }
            
            else if(conteudo.contains(":")){
            	return parseFuncaoIntervalo(f,nomeFuncao,argumento);
            	
            }
           
            else{
            	return null;
           
            }
        }    
        else if (conteudo.contains("=")) {  // e uma referencia
        	return parseReferencia(f,conteudo.substring(1));
        }
        else {
            return parseLiteral(conteudo);
        }
    }
    
    private static String parseNomeFuncao(String funcao) {
        nomeFuncao1 = funcao.substring(0, funcao.indexOf("("));
        return funcao.substring(0, funcao.indexOf("("));
    }
    
    private static String parseArgumentoFuncao(String funcao) {
    	args1 = funcao.substring(funcao.indexOf("(") + 1, funcao.indexOf(")"));
    	return funcao.substring(funcao.indexOf("(") + 1, funcao.indexOf(")"));
    }
    
    public static FuncaoBinaria parseFuncaoBinaria(FolhaCalculo f,String nomeFuncao, String argumento) {
        String[] argumentos = argumento.split(",");
        argumentos1 = argumentos;
        Argumento arg1 = parseArgumento(f,argumentos[0]);
        Argumento arg2 = parseArgumento(f,argumentos[1]);
        
        		switch(nomeFuncao) {
        
        			case "MUL":
        				return new Mul(arg1,arg2,nomeFuncao);
        			case "DIV":
        				return new Div(arg1,arg2,nomeFuncao);
        			case "SUB":
        				return new Sub(arg1,arg2,nomeFuncao);
        			case "ADD":
        				return new Add(arg1,arg2, nomeFuncao);
         		}
        
        return new FuncaoBinaria(arg1, arg2, nomeFuncao);
    }
    
    public static FuncaoIntervalo parseFuncaoIntervalo(FolhaCalculo f,String nomeFuncao, String argumento) {
        String[] argumentos = argumento.split(":");
        String arg1 = argumentos[0];
        String arg2 = argumentos[1];
 
		//System.out.println("NOMEFUNCAO = " + nomeFuncao);
		Argumento argumento1 = parseReferencia(f, arg1);
		Argumento argumento2 = parseReferencia(f, arg2);
       
        Interval intervalo = new Interval (f, (Referencia)argumento1, (Referencia)argumento2);
        		//System.out.println("NOME FUNCAO NO SWITCH : " + nomeFuncao);
        		switch(nomeFuncao) {
        		
        			case "PRD":
        				return new Prd(intervalo,nomeFuncao);
        			case "AVG":
        				return new Avg(intervalo,nomeFuncao);
        		}
        
        return new FuncaoIntervalo(intervalo, nomeFuncao);
    }
    
    public static Argumento parseArgumento(FolhaCalculo f,String argumento) {
    		if (argumento.contains(";")) {
    			return parseReferencia(f,argumento);
    		}
    		return parseLiteral(argumento);
    	}
    
    public static Literal parseLiteral(String literal) {
        return new Literal(literal);
    }
    
    public static  Referencia parseReferencia(FolhaCalculo f,String referencia) {
		int[] vec = parseCelula(referencia);
		return new Referencia(f,vec);
	}
    
    public static int[] parseCelula(String endereco) {

    	try {
    		String[] args = endereco.split(";");
    		int[] vec = {Integer.parseInt(args[0]), Integer.parseInt(args[1])};
    		cellID=vec;
    		return vec;
    	}catch(NumberFormatException ex) {
    		throw new InvalidCellId(endereco);
    	}
    }
  
    /*===============================================================================================================================*/
    //getters e setters
    
    public String getNomeFuncao() {
        return nomeFuncao1;
    }
    
    public String[] getArgs() {
        return argumentos1;
    }
    
    public int[] getCellID() {
        return cellID;
    }
    
    
    public int getLiteral() {
        return literal1;
    }
    
    public  String getStringContent(){
    	return this.test;
    }
    
    public  void setStringContent(String s){
    	test = s;
    }
    
    public abstract Integer getContent();
    
    /*===============================================================================================================================*/
   
    public  String importFromXML(Element element) {
    	setStringContent(element.getAttributeValue("value"));
		return test;
						
    }
    
    public Element exportToXML(){
    	Element element = new Element("conteudo");
    	element.setAttribute("folhaId",  Integer.toString(this.getCelula().getFolhacalculo().getIdentificador()));
    	element.setAttribute("celulaLinha", Integer.toString(this.getCelula().getLinha()));
    	element.setAttribute("celulaColuna", Integer.toString(this.getCelula().getColuna()));
    	element.setAttribute("value", getStringContent());
    	element.setAttribute("type", this.getClass().toString());
  
        return element;
    }
   

}

