package pt.tecnico.bubbledocs.domain;

//import java.util.List;

public class Prd extends Prd_Base {

	private int fim=1;
	
	public Prd( Argumento intervalo, String nomeFuncao) {
		init(intervalo,nomeFuncao);
	}
	
	public Integer compute(){

		if(this.getArg().getvalue() == null)
			return null;
		else{
			for(Celula c :this.getArg().getIntervalo()){
				if(c.getConteudo() == null){
					return null;
				}
				else{
					
					int cont = c.getConteudo().getContent();
					fim*=cont;
				}
			}
			return fim;
		}
	}
}


