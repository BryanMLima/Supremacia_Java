package Game;


import br.ufsc.inf.leobr.cliente.Jogada;

public class Lance implements Jogada{
	private static final long serialVersionUID = -7534910343227345409L;
	private String estado;
	private int player;	
	private String defensor;
	private String nomeAtacante;
	private String atacante;
	private String vencedor = "";
	private String tropaRemovida;
	private String tropaAdicionada;
	private String nacaoEscolhida = "";
	private int rodadasMaxDeterminadas = 100;
	private String tipo;
	private int qtdProduto;	
	private int baixasDefensor;
	private int dadosDefensor;
	private int baixasAtacante;
	private int dadosAtacante;
	private boolean isSurrender;
	
	public Lance() {
		
	}
	
	public String getNomeAtacante() {
		return nomeAtacante;
	}
	public void setNomeAtacante(String nomeAtacante) {
		this.nomeAtacante = nomeAtacante;
	}
	public boolean isSurrender() {
		return isSurrender;
	}
	public void setSurrender(boolean isSurrender) {
		this.isSurrender = isSurrender;
	}
	public String getVencedor() {
		return vencedor;
	}
	public void setVencedor(String vencedor) {
		this.vencedor = vencedor;
	}
	
	public String getNacaoEscolhida() {
		return nacaoEscolhida;
	}
	public int getBaixasDefensor() {
		return baixasDefensor;
	}
	public void setBaixasDefensor(int baixasDefensor) {
		this.baixasDefensor = baixasDefensor;
	}
	public int getDadosDefensor() {
		return dadosDefensor;
	}
	public void setDadosDefensor(int dadosDefensor) {
		this.dadosDefensor = dadosDefensor;
	}
	public int getBaixasAtacante() {
		return baixasAtacante;
	}
	public void setBaixasAtacante(int baixasAtacante) {
		this.baixasAtacante = baixasAtacante;
	}
	public int getDadosAtacante() {
		return dadosAtacante;
	}
	public void setDadosAtacante(int dadosAtacante) {
		this.dadosAtacante = dadosAtacante;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getPlayer() {
		return player;
	}
	public void setPlayer(int player) {
		this.player = player;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getQtdProduto() {
		return qtdProduto;
	}
	public void setQtdProduto(int qtdProduto) {
		this.qtdProduto = qtdProduto;
	}
	public void setNacaoEscolhida(String nacaoEscolhida) {
		this.nacaoEscolhida = nacaoEscolhida;
	}
	public int getRodadasMaxDeterminadas() {
		return rodadasMaxDeterminadas;
	}
	public void setRodadasMaxDeterminadas(int rodadasMaxDeterminadas) {
		this.rodadasMaxDeterminadas = rodadasMaxDeterminadas;
	}
	
	public String getTropaRemovida() {
		return tropaRemovida;
	}
	public void setTropaRemovida(String tropaRemovida) {
		this.tropaRemovida = tropaRemovida;
	}
	public String getTropaAdicionada() {
		return tropaAdicionada;
	}
	public void setTropaAdicionada(String tropaAdicionada) {
		this.tropaAdicionada = tropaAdicionada;
	}
	public String getAtacante() {
		return atacante;
	}
	public void setAtacante(String atacante) {
		this.atacante = atacante;
	}
	public String getDefensor() {
		return defensor;
	}
	public void setDefensor(String defensor) {
		this.defensor = defensor;
	}
	
}
