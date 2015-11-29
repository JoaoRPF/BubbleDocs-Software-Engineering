package pt.tecnico.bubbledocs.exception;

	public class InvalidIntervalType extends BubbleDocsException {

		/**
		 */
		private static final long serialVersionUID = 1L;

		private String funcao;

		public InvalidIntervalType(String funcaoIntervalo) {
			funcao = funcaoIntervalo;
		}
		
		public String getUserName() {
			return funcao;
		} 

	}


