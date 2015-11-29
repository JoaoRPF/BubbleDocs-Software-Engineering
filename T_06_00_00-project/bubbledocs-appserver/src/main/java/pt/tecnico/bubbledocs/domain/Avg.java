package pt.tecnico.bubbledocs.domain;

//import java.util.List;

public class Avg extends Avg_Base {

	private int fim=0;
	private int iteracoes=0;
	public Avg(Argumento intervalo, String nomeFuncao) {
		init(intervalo,nomeFuncao);
		//System.out.println("INTERVALO " +intervalo.getIntervalo());
		//System.out.println("NOME FUNCAO NO AVG : " + nomeFuncao);
		compute();
	}

	public int getResultado(){
		return fim/iteracoes;
	}
	
	@Override
	public Integer compute(){

		for (Celula c : this.getArg().getIntervalo()){
			if (c.getConteudo() == null){
				//System.out.println("#AQUIIII VALUE");
				return null;
			}
			else{
				//System.out.println("pi");
				iteracoes++;
				int cont = c.getConteudo().getContent();
				//System.out.println("imprimir conteudo das cells" + cont);
				fim=fim+cont;

			}

		}
		Integer resultado=fim/iteracoes;
		//this.getArg().setStringContent(String.valueOf(resultado));
		//this.getArg().getCelula().getConteudo().setStringContent(String.valueOf(resultado));
		//System.out.println("RESULTADOOOO " + resultado);
		return resultado;
	}

}


