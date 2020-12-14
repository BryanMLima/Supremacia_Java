package Rede;


import javax.swing.JOptionPane;

import Game.Lance;
import Game.SetupGui;
import Game.Tabuleiro;
import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;

public class NetGames implements OuvidorProxy {
	
	private static final long serialVersionUID = 1L;
	protected Proxy proxy;
	protected boolean conectado = false;
	private SetupGui gui;
	private Tabuleiro t;
	private String nomeADV = "";
	private String nomeJOG = "";
	
	public NetGames() {
		super();
		this.proxy = Proxy.getInstance();
		proxy.addOuvinte(this);
	}
	
	public void addObserver(SetupGui gui) {
		this.gui = gui;
	}
	public void setTabuleiro(Tabuleiro t) {
		this.t = t;
	}
	public String conectar(String servidor, String nome) {
			try {
				proxy.conectar(servidor, nome);
			} catch (JahConectadoException e) {
				e.printStackTrace();
				return "Voce ja esta conectado";
			} catch (NaoPossivelConectarException e) {
				e.printStackTrace();
				return "Nao foi possivel conectar";
			} catch (ArquivoMultiplayerException e) {
				e.printStackTrace();
				return "Voce esqueceu o arquivo de propriedades";
			}
			this.definirConectado(true);
			return "Sucesso: conectado a Netgames Server";
	}

	public String desconectar() {
			try {
				proxy.desconectar();
			} catch (NaoConectadoException e) {
				e.printStackTrace();
				return "Não foi possivel desconectar";
			}
			this.definirConectado(false);
			return "Desconectado com sucesso";
	}

	public void iniciarPartida() {
		System.out.println("iniciar");
		try {
			proxy.iniciarPartida(2);
			System.out.println("iniciou");
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		}
		
	}
	public String getNomeJogador() {
		return nomeJOG;
	}
	public String getNomeAdversario() {
		return nomeADV;
	}
	@Override
	public void iniciarNovaPartida(Integer posicao) {
		this.nomeJOG = proxy.getNomeJogador();
		int indiceAdversario = 1;
		if (posicao.equals(1)) indiceAdversario = 2;
		String adversario = proxy.obterNomeAdversario(indiceAdversario);
		System.out.println("IniciarNetGames");
		this.nomeADV = adversario;
		gui.iniciarByNetGames(posicao);
	}

	@Override
	public void finalizarPartidaComErro(String message) {
		this.t.finalizarJogo();
	}

	@Override
	public void receberMensagem(String msg) {
	}

	@Override
	public void receberJogada(Jogada jogada) {
		this.t.ReceberJogada((Lance) jogada);
	}

	@Override
	public void tratarConexaoPerdida() {
		this.t.finalizarJogo();
		
	}

	@Override
	public void tratarPartidaNaoIniciada(String message) {
		JOptionPane.showMessageDialog(null, "message");
	}
	
	public void enviarJogada(Jogada lance) {
		try {
			proxy.enviaJogada(lance);
		} catch (NaoJogandoException e) {
			e.printStackTrace();
		}
	}
	
	public boolean informarConectado() {
		return conectado;
	}

	public void definirConectado(boolean valor) {
		conectado = valor;
	}
	
	public void encerrarPartida() {
		try {
			proxy.finalizarPartida();
		} catch (NaoConectadoException e) {
			e.printStackTrace();
		} catch (NaoJogandoException e) {
			e.printStackTrace();
		}
	}


}
