package Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import Combat.Tropa;
import Economy.Mercado;
import Land.MarAzulClaro;
import Land.MarAzulEscuro;
import Land.Territorio;
import Rede.NetGames;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Tabuleiro {
	private boolean jogoEmProgresso = false;
	private int numRodadas = 1;
	private int maxRodadas;
	private ArrayList<Territorio> territorios = new ArrayList<>();
	private ArrayList<Territorio> ConfAmSul = new ArrayList<>();
	private ArrayList<Territorio> FedEstAfri = new ArrayList<>();
	private ArrayList<Territorio> LigaNacEuro = new ArrayList<>();
	private ArrayList<Territorio> RepPopChi = new ArrayList<>();
	private ArrayList<Territorio> EUA = new ArrayList<>();
	private ArrayList<Territorio> URSS = new ArrayList<>();
	private Mercado mercado = new Mercado();
	private State estados = new State();
	private String currentState = estados.inicializando;
	private boolean isPlayerOne = false;
	private boolean isMoving = false;
	private boolean isAttacking = false;
	private boolean isConstruindo = false;
	private String vencedor = "";
	private Territorio territorioSelecionado;
	private int maxTropa = 3;
	private Jogador jogadorLocal;
	private Jogador jogadorRemoto;
	private GameGui gameGui;
	private NetGames net;
	private PApplet p;
	PImage mapa;

	public Tabuleiro(PApplet p, NetGames net) {
		this.p = p;
		this.net = net;
	}

	public class State {
		private String inicializando = "inicializando";
		private String esperandoEscolhaNacao = "esperandoEscolhaNacao";
		private String movendotropas = "movendotropas";
		private String comprando = "comprando";
		private String vendendo = "vendendo";
		private String construindo = "construindo";
		private String pagando = "pagando";
		private String atacando = "atacando";
		private String esperandoLance = "esperandoLance";
		private String defendendo = "defendendo";
		private String fim = "fim";

	}

	public PImage getImage(String url) throws Exception {
		BufferedImage image = ImageIO.read(this.getClass().getResourceAsStream(url));
		return new PImage(image);
	}

	public String getCurrentState() {
		return this.currentState;
	}


	// identical use to setup in Processing IDE except for size()
	public void start(Integer integer) {
		try {
			this.mapa = this.getImage("/Economy/supremacymap.png");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setupContinents();
		gameGui = new GameGui(this.p, this, this.mercado);

		if (integer == 1) {
			isPlayerOne = true;
			int rodadas = Integer.parseInt(JOptionPane.showInputDialog("Rodadas máximas"));
			String nacao = showNationsMenu("");
			jogadorLocal = new Jogador(nacao, net.getNomeJogador(), this);
			gameGui.setPlayer(jogadorLocal);
			Lance novoLance = new Lance();
			novoLance.setNacaoEscolhida(nacao);
			novoLance.setRodadasMaxDeterminadas(rodadas);
			net.enviarJogada(novoLance);
			maxRodadas = rodadas;
			setupNovaNacao(nacao, jogadorLocal);
			currentState = estados.esperandoEscolhaNacao;
		} else {
			currentState = estados.esperandoEscolhaNacao;
			isPlayerOne = false;
			JOptionPane.showMessageDialog(null, "Aguardando escolha da nação inimiga");
		}
	}

	public void setupNovaNacao(String nacao, Jogador jogador) {
		ArrayList<Territorio> nacaoEscolhida = new ArrayList<Territorio>();
		switch (nacao) {
		case "Confederação da América do Sul":
			jogador.setColor("verdeClaro");
			nacaoEscolhida = ConfAmSul;
			break;
		case "Federação dos Estados Africanos":
			jogador.setColor("roxo");
			nacaoEscolhida = FedEstAfri;
			break;
		case "Liga das Nações Européias":
			jogador.setColor("laranja");
			nacaoEscolhida = LigaNacEuro;
			break;
		case "República Popular da China":
			jogador.setColor("amarelo");
			nacaoEscolhida = RepPopChi;
			break;
		case "Estados Unidos da América":
			jogador.setColor("verdeEscuro");
			nacaoEscolhida = EUA;
			break;
		case "União das Repúblicas Socialistas Soviéticas":
			jogador.setColor("rosa");
			nacaoEscolhida = URSS;
			break;
		}
		for (Territorio territorio : nacaoEscolhida) {
			Tropa tp = new Tropa();
			tp.setOwner(jogador);
			territorio.addOcupante(tp);

		}
	}

	void setP(PApplet p) {
		this.p = p;
	}

	public String showNationsMenu(String except) {
		JFrame frame = new JFrame();
		frame.setAlwaysOnTop(true);
		ArrayList<String> paises = new ArrayList<String>();
		Object[] options = { "Confederação da América do Sul", "Federação dos Estados Africanos",
				"Liga das Nações Européias", "República Popular da China", "Estados Unidos da América",
				"União das Repúblicas Socialistas Soviéticas" };
		for (int i = 0; i < options.length; i++) {
			if (!options[i].toString().equals(except)) {
				paises.add(options[i].toString());
			}
		}
		options = paises.toArray();
		// ...and passing `frame` instead of `null` as first parameter
		Object selectionObject = JOptionPane.showInputDialog(frame, "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null,
				options, options[0]);
		return selectionObject.toString();
	}

	void setupContinents() {

		// South America
		setupSouthAmerica();
		// Central America
		setupCentralAmerica();
		// North America
		setupNorthAmerica();
		// Mar azul
		setupMarAzulClaro();
		// Mar escuro
		setupMarAzulEscuro();
		// Europa
		setupEurope();
		// Africa
		setupAfrica();
		// Russia
		setupRussia();
		// Oceania
		setupOceania();
		// Middle East
		setupMiddleEast();
		// Asia
		setupAsia();
	}

	// identical use to draw in Prcessing IDE
	public void show() {
		p.image(this.mapa, 0, 0);
		gameGui.show();
		for (int i = 0; i < territorios.size(); i++) {
			territorios.get(i).show();
		}

	}

	public void ReceberJogada(Lance lance) {
		if (currentState.equals(estados.inicializando)) {

		} else if (currentState.equals(estados.esperandoLance)) {
			String estagio = lance.getEstado();
			// estagio 1 e 2
			if (estagio.equals(estados.pagando)) {
				removerTropa(lance.getTropaRemovida());
				// estagio 3
			} else if (estagio.equals(estados.vendendo)) {
				String recurso = lance.getTipo();
				int unidades = lance.getQtdProduto();
				mercado.removerValor(recurso, unidades);
				// estagio 4
			} else if (estagio.equals(estados.atacando)) {
				if (lance.getTipo().equals("bomba")) {
					defenderBomba(procurarTerritorio(lance.getAtacante()), procurarTerritorio(lance.getDefensor()));
				} else {
					defender(procurarTerritorio(lance.getAtacante()), procurarTerritorio(lance.getDefensor()));
					
				} 
				// estagio 5
			} else if (estagio.equals(estados.movendotropas)) {
				Tropa moved = procurarTerritorio(lance.getTropaRemovida()).removeOcupante();
				procurarTerritorio(lance.getTropaAdicionada()).addOcupante(moved);

				// estagio 6
			} else if (estagio.equals(estados.construindo)) {
				Territorio territorio = procurarTerritorio(lance.getTropaAdicionada());
				Tropa t = new Tropa();
				t.setOwner(jogadorRemoto);
				territorio.addOcupante(t);
				// estagio 7
			} else if (estagio.equals(estados.comprando)) {
				String recurso = lance.getTipo();
				int unidades = lance.getQtdProduto();
				mercado.addValor(recurso, unidades);
			} else if (estagio.equals(estados.fim)) {
				if (lance.getVencedor().equals("")) {
					JOptionPane.showMessageDialog(null, "Sua vez!");
					currentState = estados.pagando;
					pagarSalarios();
				} else {
					
					JOptionPane.showMessageDialog(null, "Vencedor da partida é "+lance.getVencedor()+"!!");
					p.exit();
				}

				// esperando defendendo
			} else if (estagio.equals(estados.defendendo)) {
				if (lance.getTipo().equals("kaboom")) {
					Territorio t = procurarTerritorio(lance.getDefensor());
					 t.Kaboom();
					 JOptionPane.showMessageDialog(null, "KABOOOOOOMMMMM");
				} else if (lance.getTipo().equals("laser")) {
					JOptionPane.showMessageDialog(null, "Defendido");
				}else if (lance.getTipo().equals("normal")) {
					if (lance.isSurrender()) {
						Territorio territorio = procurarTerritorio(lance.getDefensor());
						ArrayList<Tropa> novosOcupantes = new ArrayList<Tropa>();
						int newTropas = territorio.getOcupantes().size();
						for (int i = 0; i < newTropas; i++) {
							Tropa tp = new Tropa();
							tp.setOwner(jogadorLocal);
							novosOcupantes.add(tp);
						}
						territorio.setOcupantes(novosOcupantes);
				} else {						
						int dadosDefensor = lance.getDadosDefensor();
						int dadosAtacante = lance.getDadosAtacante();
						int baixasAtacante = lance.getBaixasAtacante();
						int baixasDefensor = lance.getBaixasDefensor();
						JOptionPane.showMessageDialog(null,
								"Valor do seu(s) dado(s): " + dadosAtacante + "\nValor do(s) dado(s) do inimigo: "
										+ dadosDefensor + "\nVocê perdeu " + baixasAtacante + " tropas \nO inimigo perdeu "
										+ baixasDefensor + " tropas");
						
						// remove tropas local
						procurarTerritorio(lance.getAtacante()).removeOcupantes(baixasAtacante);
						// remove tropas remoto
						procurarTerritorio(lance.getDefensor()).removeOcupantes(baixasDefensor);
					}
				}
				currentState = estados.atacando;
			}

		} else if (currentState.equals(estados.esperandoEscolhaNacao)) {
			String nacaoInimiga = lance.getNacaoEscolhida();
			jogadorRemoto = new Jogador(nacaoInimiga, net.getNomeAdversario(), this);
			setupNovaNacao(nacaoInimiga, jogadorRemoto);
			if (isPlayerOne) {
				currentState = estados.pagando;
				pagarSalarios();
			} else {
				maxRodadas = lance.getRodadasMaxDeterminadas();
				String nacao;
				nacao = showNationsMenu(nacaoInimiga);
				jogadorLocal = new Jogador(nacao, net.getNomeJogador(), this);
				setupNovaNacao(nacao, jogadorLocal);
				gameGui.setPlayer(jogadorLocal);
				Lance novoLance = new Lance();
				novoLance.setNacaoEscolhida(nacao);
				net.enviarJogada(novoLance);
				currentState = estados.esperandoLance;
			}
		}
	}

	public void vender() {
		int answer = JOptionPane.showConfirmDialog(null, "Deseja vender alguma unidade de suprimento?");
		if (answer == JOptionPane.YES_OPTION) {
			boolean success = false;
			String[] recursos = { "Petroleo", "Minerio", "Cereal" };
			for (int i = 0; i < recursos.length; i++) {
				success = false;
				do {
					String valor = JOptionPane.showInputDialog(
							"Quantas unidades de " + recursos[i] + " você deseja vender?\n Digite 0 caso não queira");
					if (valor == null || valor.equals("")) {
						JOptionPane.showMessageDialog(null, "Cancelado");
						success = true;
					} else {
						int unidades = Integer.parseInt(valor);
						if (unidades > jogadorLocal.central.getQtdRecurso(recursos[i]) || unidades < 0) {
							JOptionPane.showMessageDialog(null,
									"Numero de unidades informada é maior que unidades do jogador, ou é um número negativo");
						} else {
							int lucro = mercado.getValor(recursos[i]) * unidades;
							mercado.removerValor(recursos[i], unidades);
							jogadorLocal.central.addDinheiro(lucro);
							jogadorLocal.central.removerValor(recursos[i], unidades);
							success = true;
							Lance novoLance = new Lance();
							novoLance.setQtdProduto(unidades);
							novoLance.setTipo(recursos[i]);
							novoLance.setEstado(estados.vendendo);
							net.enviarJogada(novoLance);
						}
					}
				} while (!success);
			}

		}
		currentState = estados.atacando;
		atacar();
	}

	public void construir() {
		int answer = JOptionPane.showConfirmDialog(null, "Deseja construir alguma bomba?");
		if (answer == JOptionPane.YES_OPTION) {
			if (jogadorLocal.isPrimeiraBomba()) {
				answer = JOptionPane.showConfirmDialog(null,
						"O custo para girar o dado é 2.000, mais o valor de 5.000 e uma unidade de Minerio para construção"
								+ " efetiva da bomba. O valor alvo para poder construir é 6. Deseja continuar?");
				if (answer == JOptionPane.YES_OPTION) {
					boolean tentarNovamente = false;
					do {
						tentarNovamente = false;
						// Recurso insuficiente
						if (jogadorLocal.central.getDinheiro() < 7000 && jogadorLocal.central.getQtdMinerio() < 1) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente e/ou Minerio insuficiente");
						} else {
							jogadorLocal.central.removerDinheiro(2000);
							int resultado = (int) (Math.random() * 6) + 1;
							JOptionPane.showMessageDialog(null, "Seu resultado: " + resultado);
							if (resultado == 6) {
								jogadorLocal.central.removerValor("Minerio", 1);
								jogadorLocal.central.removerDinheiro(5000);
								jogadorLocal.central.addBomba(1);
							} else {
								answer = JOptionPane.showConfirmDialog(null,
										"Você não conseguiu construir a bomba, deseja tentar novamente?");
								if (answer == JOptionPane.YES_OPTION) {
									tentarNovamente = true;
								}
							}
						}
					} while (tentarNovamente);
				}
			} else {
				answer = JOptionPane.showConfirmDialog(null,
						"O custo para construir uma bomba é 5.000 e 1 unidade de Minerio. Deseja construir?");
				if (answer == JOptionPane.YES_OPTION) {
					boolean construirNovamente = false;
					do {

						if (jogadorLocal.central.getDinheiro() < 5000 && jogadorLocal.central.getQtdMinerio() < 1) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente e/ou Minerio insuficiente");
						} else {
							jogadorLocal.central.removerValor("Minerio", 1);
							jogadorLocal.central.removerDinheiro(5000);
							jogadorLocal.central.addBomba(1);
							answer = JOptionPane.showConfirmDialog(null, "Deseja construir outra bomba?");
							if (answer == JOptionPane.YES_OPTION) {
								construirNovamente = true;
							}
						}
					} while (construirNovamente);
				}
			}
		}

		answer = JOptionPane.showConfirmDialog(null, "Deseja construir algum laser?");

		if (answer == JOptionPane.YES_OPTION) {
			if (jogadorLocal.isPrimeiroLaser()) {
				answer = JOptionPane.showConfirmDialog(null,
						"O custo para girar o dado é 2.000, mais o valor de 10.000 e duas unidade de Minerio para construção"
								+ " efetiva do laser. O valor alvo para poder construir é 6. Deseja continuar?");
				if (answer == JOptionPane.YES_OPTION) {
					boolean tentarNovamente = false;
					do {
						tentarNovamente = false;
						// Recurso insuficiente
						if (jogadorLocal.central.getDinheiro() < 12000 && jogadorLocal.central.getQtdMinerio() < 1) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente e/ou Minerio insuficiente");
						} else {
							jogadorLocal.central.removerDinheiro(2000);
							int resultado = (int) (Math.random() * 6) + 1;
							JOptionPane.showMessageDialog(null, "Seu resultado: " + resultado);
							if (resultado == 6) {
								jogadorLocal.central.removerValor("Minerio", 2);
								jogadorLocal.central.removerDinheiro(5000);
								jogadorLocal.central.addLaser(1);
							} else {
								answer = JOptionPane.showConfirmDialog(null,
										"Você não conseguiu construir o laser, deseja tentar novamente?");
								if (answer == JOptionPane.YES_OPTION) {
									tentarNovamente = true;
								}
							}
						}
					} while (tentarNovamente);
				}
			} else {
				answer = JOptionPane.showConfirmDialog(null,
						"O custo para construir um laser é 10.000 e 2 unidade de Minerio. Deseja construir?");
				if (answer == JOptionPane.YES_OPTION) {
					boolean construirNovamente = false;
					do {
						if (jogadorLocal.central.getDinheiro() < 10000 && jogadorLocal.central.getQtdMinerio() < 2) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente e/ou Minerio insuficiente");
						} else {
							jogadorLocal.central.removerValor("Minerio", 2);
							jogadorLocal.central.removerDinheiro(5000);
							jogadorLocal.central.addLaser(1);
							answer = JOptionPane.showConfirmDialog(null, "Deseja construir outro laser?");
							if (answer == JOptionPane.YES_OPTION) {
								construirNovamente = true;
							}
						}
					} while (construirNovamente);
				}
			}
		}
		maxTropa = 3;
		answer = JOptionPane.showConfirmDialog(null, "Deseja construir alguma tropa?");
		if (answer == JOptionPane.YES_OPTION) {
			isConstruindo = true;
			JOptionPane.showMessageDialog(null, "Clique no territorio desejado para a criação da tropa:");
		}
	}

	public void comprar() {
		int answer = JOptionPane.showConfirmDialog(null, "Deseja comprar alguma unidade de suprimento?");
		if (answer == JOptionPane.YES_OPTION) {
			boolean success = false;
			String[] recursos = { "Petroleo", "Minerio", "Cereal" };
			for (int i = 0; i < recursos.length; i++) {
				success = false;
				do {
					String valor = JOptionPane.showInputDialog(
							"Quantas unidades de " + recursos[i] + " você deseja comprar?\n Digite 0 caso não queira");
					if (valor == null || valor.equals("")) {
						success = true;
					} else {
						int unidades = Integer.parseInt(valor);
						int preco = mercado.getValor(recursos[i]) * unidades;
						if (preco > jogadorLocal.central.getDinheiro()) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente");
						} else {
							mercado.addValor(recursos[i], unidades);
							jogadorLocal.central.removerDinheiro(preco);
							jogadorLocal.central.addValor(recursos[i], unidades);
							success = true;
							Lance novoLance = new Lance();
							novoLance.setQtdProduto(unidades);
							novoLance.setTipo(recursos[i]);
							novoLance.setEstado(estados.comprando);
							net.enviarJogada(novoLance);
						}
					}
				} while (!success);
			}
		}
		currentState = estados.esperandoLance;
		trocarTurno();
	}

	public void atacar() {
		JOptionPane.showMessageDialog(null,
				"Selecione o territorio origem de ataque e logo em seguida o territorio desejado para atacar");
	}

	public void pagarSalarios() {
		ArrayList<Territorio> territoriosDominados = jogadorLocal.getOwnedTerrain();
		// Salario das unidades
		int answer = JOptionPane.showConfirmDialog(null, "Deseja pagar todos salarios?");
		boolean canPayForAll = false;
		int currentMoney = jogadorLocal.central.getDinheiro();
		int neededMoney = 0;
		for (Territorio territorio : territoriosDominados) {
			neededMoney += territorio.getCusto();
			for (Tropa t : territorio.getOcupantes()) {
				neededMoney += t.getSalario();
			}
		}
		if (neededMoney <= currentMoney) {
			canPayForAll = true;
		}
		
		if (answer == JOptionPane.YES_OPTION && canPayForAll) {
			for (Territorio territorio : territoriosDominados) {
				try {
					Map<String, Integer> map = territorio.getRecursos();
					for (Map.Entry<String, Integer> entry : map.entrySet()) {
						String recurso = entry.getKey();
						int qtdRecurso = entry.getValue();
						int custo = territorio.getCusto();			
						// Remove dinheiro da central
						jogadorLocal.central.removerDinheiro(custo);
						jogadorLocal.central.addValor(recurso, qtdRecurso);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(null, "antes do for tropa");
			for (Territorio territorio : territoriosDominados) {
				for (Tropa t : territorio.getOcupantes()) {
					 jogadorLocal.central.removerDinheiro(t.getSalario());
				}
			}
			JOptionPane.showMessageDialog(null, "Pagou Tudo!");
			
		} else {
			for (Territorio territorio : territoriosDominados) {
				Map<String, Integer> map = territorio.getRecursos();
				for (Map.Entry<String, Integer> entry : map.entrySet()) {
					String recurso = entry.getKey();
					int qtdRecurso = entry.getValue();

					answer = JOptionPane.showConfirmDialog(null,
							"Deseja pagar o salário de " + territorio.getCusto() + " da companhia que produz "
									+ qtdRecurso + " do recurso" + recurso + " do territorio " + territorio.getName()
									+ "?");
					if (answer == JOptionPane.YES_OPTION) {
						int custo = territorio.getCusto();
						if (custo > jogadorLocal.central.getDinheiro()) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente");
						} else {
							// Remove dinheiro da central
							jogadorLocal.central.removerDinheiro(custo);
							jogadorLocal.central.addValor(recurso, qtdRecurso);
						}
					}
				}
			}
			ArrayList<String> tropasRemovidas = new ArrayList<String>();
			// salario das tropas
			
			for (Territorio territorio : territoriosDominados) {
				neededMoney += territorio.getCusto();
				for (Tropa t : territorio.getOcupantes()) {
					answer = JOptionPane.showConfirmDialog(null, "Deseja pagar o salário da tropa no territorio "
							+ territorio.getName() + "?");
					int custo = t.getSalario();
					if (answer == JOptionPane.YES_OPTION && (custo <= jogadorLocal.central.getDinheiro())) {
						// Remove dinheiro da central
						jogadorLocal.central.removerDinheiro(custo);
					} else {
						if (custo > jogadorLocal.central.getDinheiro()) {
							JOptionPane.showMessageDialog(null, "Dinheiro insuficiente");
						}
						Lance novoLance = new Lance();
						novoLance.setTropaRemovida(territorio.getName());
						novoLance.setEstado(estados.pagando);
						net.enviarJogada(novoLance);
						tropasRemovidas.add(territorio.getName());
					}
				}
					
			}
			for (int i = tropasRemovidas.size() - 1; i >= 0; i--) {
				removerTropa(tropasRemovidas.get(i));
			}
		}

		// proxima coisa
		currentState = estados.vendendo;
		vender();
	}

	public void removerTropa(String territorio) {
		procurarTerritorio(territorio).removeOcupante();
	}

	public Territorio procurarTerritorio(String nome) {
		for (Territorio t : territorios) {
			if (t.getName().equals(nome)) {
				return t;
			}
		}
		return null;
	}

	public void click(int x, int y) {
		if (gameGui.getBotaoTurno().isPointInside(x, y)) {
			if (currentState.equals(estados.movendotropas)) {
				isMoving = false;
				currentState = estados.construindo;
				construir();
				if (territorioSelecionado != null) {
					for (String front : territorioSelecionado.getFronteiras()) {
						Territorio colado = procurarTerritorio(front);
						colado.setClickable(false);
					}
					
					territorioSelecionado = null;
				}
			} else if (currentState.equals(estados.atacando)) {
				isAttacking = false;
				currentState = estados.movendotropas;
				if (territorioSelecionado != null) {
					for (String front : territorioSelecionado.getFronteiras()) {
						Territorio colado = procurarTerritorio(front);
						colado.setClickable(false);
					}
					territorioSelecionado = null;
				}
			} else if (currentState.equals(estados.construindo)) {
				currentState = estados.comprando;
				comprar();
			}
		}

		// search for clicked terrain
		for (Territorio territorio : this.territorios) {
			if (territorio.isPointInside(x, y)) {
				// estagio movendo tropas
				if (currentState.equals(estados.movendotropas)) {
					avaliarMovimento(territorio);
				} else if (currentState.equals(estados.construindo)) {
					avaliarConstruir(territorio);
				} else if (currentState.equals(estados.atacando)) {
					avaliarAtaque(territorio);
				}
			}
		}
	}
	public void defenderBomba(Territorio atacante, Territorio defensor) {
		int answer;
		Lance novoLance = new Lance();
		novoLance.setEstado(estados.defendendo);
		if (jogadorLocal.central.getQtdLaserStar() > 0) {
			answer = JOptionPane.showConfirmDialog(null, "Deseja defender: " + defensor.getName()+ " com laser star?");
			if(answer == JOptionPane.YES_OPTION) {
				novoLance.setTipo("laser");
				net.enviarJogada(novoLance);
				jogadorLocal.central.removerLaser(1);
				return;
			}
		}
		novoLance.setTipo("kaboom");
		novoLance.setDefensor(defensor.getName());
		net.enviarJogada(novoLance);
		defensor.Kaboom();
	}
	public void defender(Territorio atacante, Territorio defensor) {
		int answer = JOptionPane.showConfirmDialog(null, "O seu  territorio em " + defensor.getName()
				+ " está sendo atacado. Deseja lutar pelo seu territorio?");
		if (answer == JOptionPane.YES_OPTION) {
			int numDadosDefensor = 1;
			int numDadosAtacante = 1;
			if (jogadorLocal.central.removerConjuntoSuprimentos("atacar")) {
				numDadosDefensor++;
			}
			// maior numero de tropas
			if (defensor.getOcupantes().size() > atacante.getOcupantes().size()) {
				numDadosDefensor++;
			} else {
				numDadosAtacante++;
			}
			// maior numero de lasers
			if (jogadorLocal.central.getQtdLaserStar() > jogadorRemoto.central.getQtdLaserStar()) {
				numDadosDefensor++;
			} else {
				numDadosAtacante++;
			}

			int dadosAtacante = (int) ((numDadosAtacante * Math.random() * 6) + 1);
			int dadosDefensor = (int) ((numDadosDefensor * Math.random() * 6) + 1);
			int baixasDefensor = Math.floorDiv(dadosAtacante, 3);
			int baixasAtacante = Math.floorDiv(dadosDefensor, 3);

			JOptionPane.showMessageDialog(null,
					"Valor do seu(s) dado(s): " + dadosDefensor + "\nValor do(s) dado(s) do inimigo: " + dadosAtacante
							+ "\nVocê perdeu " + baixasDefensor + " tropas \nO inimigo perdeu " + baixasAtacante
							+ " tropas");
			// remove tropas defensor
			defensor.removeOcupantes(baixasDefensor);
			atacante.removeOcupantes(baixasAtacante);

			Lance novoLance = new Lance();
			novoLance.setEstado(estados.defendendo);
			novoLance.setAtacante(atacante.getName());
			novoLance.setBaixasAtacante(baixasAtacante);
			novoLance.setDadosAtacante(dadosAtacante);
			novoLance.setBaixasDefensor(baixasDefensor);
			novoLance.setDadosDefensor(dadosDefensor);
			novoLance.setTipo("normal");
			novoLance.setDefensor(defensor.getName());
			novoLance.setSurrender(false);
			net.enviarJogada(novoLance);

		} else {
			try {
				int numTropas = defensor.getOcupantes().size();

				ArrayList<Tropa> novosOcupantes = new ArrayList<>();

				for (int i = 0; i < numTropas; i++) {
					Tropa tp = new Tropa();
					tp.setOwner(jogadorRemoto);
					novosOcupantes.add(tp);
				}

				defensor.setOcupantes(novosOcupantes);

				Lance novoLance = new Lance();
				novoLance.setEstado(estados.defendendo);
				novoLance.setSurrender(true);
				novoLance.setDefensor(defensor.getName());
				novoLance.setTipo("normal");
				net.enviarJogada(novoLance);
			} catch (Exception e) {
				System.out.println(e);
			}

		}

	}

	private void avaliarAtaque(Territorio territorio) {
		String ownerNome = "";
		if (territorio.estaOcupado()) {
			ownerNome = territorio.getOwner().getNome();
		}
		if (isAttacking) {
			if (territorio.isClickable()) {
				realizarAtaque(territorio, territorioSelecionado);
				isAttacking = false;
				for (String front : territorioSelecionado.getFronteiras()) {
					Territorio colado = procurarTerritorio(front);
					if (colado.estaOcupado()) {
						if (colado.getOwner().getNome().equals(jogadorRemoto.getNome())) {
							colado.setClickable(false);
						}
					}
				}
				territorioSelecionado = null;
			} else if (territorioSelecionado != null) {
				for (String front : territorioSelecionado.getFronteiras()) {
					Territorio colado = procurarTerritorio(front);
					colado.setClickable(false);
				}
				territorioSelecionado = null;
			}
		} else if (ownerNome.equals(jogadorLocal.getNome()) && currentState.equals(estados.atacando)) {
			territorioSelecionado = territorio;
			isAttacking = true;
			for (String front : territorio.getFronteiras()) {
				Territorio colado = procurarTerritorio(front);
				if (colado.estaOcupado()) {
					if (colado.getOwner().getNome().equals(jogadorRemoto.getNome())) {
						colado.setClickable(true);
					}
				}
			}
		}
	}

	private void realizarAtaque(Territorio territorio, Territorio territorioSelecionado2) {
		int answer;
		if (jogadorLocal.central.getQtdBombaAtomica() > 0) {
			answer = JOptionPane.showConfirmDialog(null, "Deseja usar bomba atomica?");
			if (JOptionPane.YES_OPTION == answer) {
				Lance novoLance = new Lance();
				novoLance.setEstado(estados.atacando);
				novoLance.setAtacante(territorioSelecionado2.getName());
				novoLance.setDefensor(territorio.getName());
				novoLance.setNomeAtacante(jogadorLocal.getNome());
				novoLance.setTipo("bomba");
				net.enviarJogada(novoLance);
				currentState = estados.esperandoLance;
				jogadorLocal.central.removerBomba(1);
			}
		} else {
			if (jogadorLocal.central.removerConjuntoSuprimentos("atacar")) {
				Lance novoLance = new Lance();
				novoLance.setEstado(estados.atacando);
				novoLance.setAtacante(territorioSelecionado2.getName());
				novoLance.setDefensor(territorio.getName());
				novoLance.setTipo("normal");
				novoLance.setNomeAtacante(jogadorLocal.getNome());
				net.enviarJogada(novoLance);
				currentState = estados.esperandoLance;
				JOptionPane.showMessageDialog(null, "ATACARRR!!!");
			}
		}

	}

	private void avaliarConstruir(Territorio territorio) {
		if (territorio.estaOcupado()) {
			if (isConstruindo && territorio.getOwner().getNome().equals(jogadorLocal.getNome()) && maxTropa > 0) {
				if (!jogadorLocal.central.removerConjuntoSuprimentos("construir")) {
					JOptionPane.showMessageDialog(null, "Recurso e/ou dinheiro insuficiente");
				} else {
					Tropa tropa = new Tropa();
					tropa.setOwner(jogadorLocal);
					territorio.addOcupante(tropa);
					maxTropa--;
					Lance novoLance = new Lance();
					novoLance.setTropaAdicionada(territorio.getName());
					novoLance.setEstado(estados.construindo);
					net.enviarJogada(novoLance);
				}
			} else if (maxTropa <= 0) {
				JOptionPane.showMessageDialog(null, "Limite de construção de tropas atingido!");
			} else if (!(territorio.getOwner().getNome().equals(jogadorLocal.getNome()))) {
				JOptionPane.showMessageDialog(null, "Territorio selecionado não pertence a você.");
			}
		}
	}

	private void avaliarMovimento(Territorio territorio) {
		String ownerNome = "";
		if (territorio.estaOcupado()) {
			ownerNome = territorio.getOwner().getNome();
		}
		if (!ownerNome.equals(jogadorRemoto.getNome())) {
			if (!isMoving) {
				if (ownerNome.equals(jogadorLocal.getNome())) {
					isMoving = true;
					ArrayList<String> fronteiras = territorio.getFronteiras();
					territorioSelecionado = territorio;
					for (String front : fronteiras) {
						Territorio colado = procurarTerritorio(front);
						if (!colado.estaOcupado()) {
							colado.setClickable(true);
						} else {
							if (!colado.getName().equals(jogadorRemoto.getNome())) {
								colado.setClickable(true);
							}
						}
					}
				}
			} else {
				if (territorio.isClickable()) {
					// moveu
					String tipoTerritorio = territorio.getClass().getName();
					boolean movimentoPermitido = false;
					if (tipoTerritorio.equals("Land.Territorio")) {
						if (jogadorLocal.central.getQtdCereal() >= 1) {
							jogadorLocal.central.removerValor("Cereal", 1);
							movimentoPermitido = true;
						}
					} else {
						if (jogadorLocal.central.getQtdPetroleo() >= 1) {
							jogadorLocal.central.removerValor("Petroleo", 1);
							movimentoPermitido = true;
						}
					}
					if (movimentoPermitido) {
						Lance novoLance = new Lance();
						novoLance.setTropaRemovida(territorioSelecionado.getName());
						novoLance.setTropaAdicionada(territorio.getName());
						novoLance.setEstado(estados.movendotropas);
						net.enviarJogada(novoLance);
						
						Tropa moved = territorioSelecionado.removeOcupante();
						territorio.addOcupante(moved);
					} else {
						JOptionPane.showMessageDialog(null, "Recurso insuficiente!");
					}
					isMoving = false;
					for (String front : territorioSelecionado.getFronteiras()) {
						Territorio colado = procurarTerritorio(front);
						colado.setClickable(false);
					}
					territorioSelecionado = null;
					
				} else if (territorio.equals(territorioSelecionado)) {
					for (String front : territorioSelecionado.getFronteiras()) {
						Territorio colado = procurarTerritorio(front);
						colado.setClickable(false);
					}
					territorioSelecionado = null;
					isMoving = false;
				}
			}
		}

	}

	public boolean isJogoEmProgresso() {
		return jogoEmProgresso;
	}

	public void setJogoEmProgresso(boolean jogoEmProgresso) {
		this.jogoEmProgresso = jogoEmProgresso;
	}

	public int getNumRodadas() {
		return numRodadas;
	}

	public void setNumRodadas(int numRodadas) {
		this.numRodadas = numRodadas;
	}

	public int getMaxRodadas() {
		return maxRodadas;
	}

	public void setMaxRodadas(int maxRodadas) {
		this.maxRodadas = maxRodadas;
	}

	public Jogador getJogadorLocal() {
		return jogadorLocal;
	}

	public Jogador getJogadorRemoto() {
		return jogadorRemoto;
	}

	public ArrayList<Territorio> getTerritorios() {
		return this.territorios;
	}

	public void setupSouthAmerica() {
		ArrayList<String> frontBrazil = new ArrayList<>(
				Arrays.asList("Venezuela", "Peru", "Argentina", "Carribean Sea", "Mid Atlantic", "Baia Santos"));
		ArrayList<String> frontArg = new ArrayList<>(
				Arrays.asList("Brazil", "Peru", "Baia Santos", "Rio de la Plata", "South Pacific", "Lima Bay"));
		ArrayList<String> frontVene = new ArrayList<>(
				Arrays.asList("Brazil", "Peru", "Carribean Sea", "Central America", "Gulf of Panama", "Lima Bay"));
		ArrayList<String> frontPeru = new ArrayList<>(Arrays.asList("Venezuela", "Brazil", "Argentina", "Lima Bay"));

		Map<String, Integer> brazilRecursos = new HashMap<String, Integer>();
		Map<String, Integer> argentinaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> venezuelaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> peruRecursos = new HashMap<String, Integer>();
		brazilRecursos.put("Minerio", 3);
		argentinaRecursos.put("Cereal", 3);
		venezuelaRecursos.put("Petroleo", 3);
		venezuelaRecursos.put("Cereal", 2);
		peruRecursos.put("Minerio", 2);
		peruRecursos.put("Petroleo", 2);

		Territorio brazil = new Territorio(this.p, "Brazil", frontBrazil, new PVector(460, 620), 60, 30, null,
				brazilRecursos);
		Territorio argentina = new Territorio(this.p, "Argentina", frontArg, new PVector(440, 740), 60, 30, null,
				argentinaRecursos);
		Territorio venezuela = new Territorio(this.p, "Venezuela", frontVene, new PVector(330, 580), 60, 30, null,
				venezuelaRecursos);
		Territorio peru = new Territorio(this.p, "Peru", frontPeru, new PVector(380, 670), 60, 30, null, peruRecursos);

		territorios.add(brazil);
		territorios.add(argentina);
		territorios.add(venezuela);
		territorios.add(peru);
		ConfAmSul.add(brazil);
		ConfAmSul.add(argentina);
		ConfAmSul.add(venezuela);
		ConfAmSul.add(peru);

	}

	public void setupCentralAmerica() {

		Map<String, Integer> americaCentralRecursos = new HashMap<String, Integer>();
		americaCentralRecursos.put("Minerio", 3);

		ArrayList<String> frontCentralAmerica = new ArrayList<>(Arrays.asList("Carribean Sea", "Venezuela",
				"Gulf of Panama", "Santa Barbara Passage", "Western USA", "Mid-West USA"));
		Territorio centralAmerica = new Territorio(this.p, "Central America", frontCentralAmerica,
				new PVector(260, 490), 100, 30, null, americaCentralRecursos);
		territorios.add(centralAmerica);
	}

	public void setupNorthAmerica() {
		Map<String, Integer> alaskaRecursos = new HashMap<String, Integer>();
		alaskaRecursos.put("Petroleo", 2);

		ArrayList<String> frontAlaska = new ArrayList<>(
				Arrays.asList("Nothern Canada", "Western Canada", "North Pacific", "Barkley Sound"));
		Territorio alaska = new Territorio(this.p, "Alaska", frontAlaska, new PVector(180, 180), 60, 30, null,
				alaskaRecursos);

		Map<String, Integer> westernRecursos = new HashMap<String, Integer>();
		westernRecursos.put("Petroleo", 3);

		ArrayList<String> frontWesternUSA = new ArrayList<>(Arrays.asList("Mid-West USA", "Western Canada",
				"Santa Barbara Passage", "Barkley Sound", "Central America"));
		Territorio westernUSA = new Territorio(this.p, "Western USA", frontWesternUSA, new PVector(217, 333), 80, 30,
				null, westernRecursos);

		Map<String, Integer> midWestRecursos = new HashMap<String, Integer>();
		midWestRecursos.put("Minerio", 3);
		midWestRecursos.put("Cereal", 3);

		ArrayList<String> frontMidWestUSA = new ArrayList<>(
				Arrays.asList("Western USA", "Western Canada", "Eastern Canada", "Eastern USA", "Central America"));
		Territorio midWestUSA = new Territorio(this.p, "Mid-West USA", frontMidWestUSA, new PVector(333, 297), 90, 30,
				null, midWestRecursos);

		Map<String, Integer> easternRecursos = new HashMap<String, Integer>();
		easternRecursos.put("Minerio", 2);
		easternRecursos.put("Cereal", 2);

		ArrayList<String> frontEasternUSA = new ArrayList<>(Arrays.asList("Mid-West USA", "Gulf of St. Lawrence",
				"Eastern Canada", "Long Island Sound", "Carribean Sea"));
		Territorio easternUSA = new Territorio(this.p, "Eastern USA", frontEasternUSA, new PVector(367, 337), 90, 30,
				null, easternRecursos);

		Map<String, Integer> nothernCanadaRecursos = new HashMap<String, Integer>();
		nothernCanadaRecursos.put("Petroleo", 3);

		ArrayList<String> frontNorthernCanada = new ArrayList<>(
				Arrays.asList("Alaska", "Western Canada", "Greenland", "Hudson Strait"));
		Territorio northernCanada = new Territorio(this.p, "Northern Canada", frontNorthernCanada,
				new PVector(305, 191), 100, 30, null, nothernCanadaRecursos);

		Map<String, Integer> westernCanadaRecursos = new HashMap<String, Integer>();
		westernCanadaRecursos.put("Cereal", 5);
		westernCanadaRecursos.put("Minerio", 2);

		ArrayList<String> frontWesternCanada = new ArrayList<>(Arrays.asList("Alaska", "Northern Canada",
				"Barkley Sound", "Hudson Strait", "Eastern Canada", "Western USA"));
		Territorio westernCanada = new Territorio(this.p, "Western Canada", frontWesternCanada, new PVector(280, 252),
				100, 30, null, westernCanadaRecursos);

		Map<String, Integer> easternCanadaRecursos = new HashMap<String, Integer>();
		easternCanadaRecursos.put("Minerio", 4);

		ArrayList<String> frontEasternCanada = new ArrayList<>(
				Arrays.asList("Mid-West USA", "Eastern USA", "Barkley Sound", "Hudson Strait", "Western Canada"));
		Territorio easternCanada = new Territorio(this.p, "Eastern Canada", frontEasternCanada, new PVector(423, 260),
				100, 30, null, easternCanadaRecursos);

		Map<String, Integer> greenlandRecursos = new HashMap<String, Integer>();
		ArrayList<String> frontGreenland = new ArrayList<>(
				Arrays.asList("North Atlantic", "Northern Canada", "Hudson Strait"));
		Territorio greenland = new Territorio(this.p, "Greenland", frontGreenland, new PVector(500, 140), 100, 30, null,
				greenlandRecursos);
		territorios.add(alaska);
		territorios.add(westernUSA);
		territorios.add(midWestUSA);
		territorios.add(easternUSA);
		territorios.add(northernCanada);
		territorios.add(westernCanada);
		territorios.add(easternCanada);
		territorios.add(greenland);
		EUA.add(alaska);
		EUA.add(westernUSA);
		EUA.add(midWestUSA);
		EUA.add(easternUSA);
	}

	public void setupAfrica() {
		Map<String, Integer> saharaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> egyptRecursos = new HashMap<String, Integer>();
		Map<String, Integer> nigeriaRecursos = new HashMap<String, Integer>();
		saharaRecursos.put("Minerio", 1);
		saharaRecursos.put("Petroleo", 2);

		ArrayList<String> frontSahara = new ArrayList<>(
				Arrays.asList("Mid Atlantic", "Mediterranean Sea", "Egypt", "Nigeria"));
		Territorio sahara = new Territorio(this.p, "Sahara", frontSahara, new PVector(711, 507), 60, 30, null,
				saharaRecursos);

		ArrayList<String> frontEgypt = new ArrayList<>(
				Arrays.asList("Sahara", "Mediterranean Sea", "Red Sea", "Nigeria", "Mozambique"));
		Territorio egypt = new Territorio(this.p, "Egypt", frontEgypt, new PVector(844, 527), 60, 30, null,
				egyptRecursos);

		saharaRecursos.put("Petroleo", 3);

		ArrayList<String> frontNigeria = new ArrayList<>(
				Arrays.asList("Sahara", "Egypt", "Gulf of Guinea", "Zaire", "Mozambique", "Mid Atlantic"));
		Territorio nigeria = new Territorio(this.p, "Nigeria", frontNigeria, new PVector(756, 578), 60, 30, null,
				nigeriaRecursos);

		Map<String, Integer> zaireRecursos = new HashMap<String, Integer>();
		zaireRecursos.put("Petroleo", 2);

		ArrayList<String> frontZaire = new ArrayList<>(
				Arrays.asList("South Africa", "Gulf of Guinea", "Nigeria", "Mozambique", "Cape of Good Hope"));
		Territorio zaire = new Territorio(this.p, "Zaire", frontZaire, new PVector(832, 693), 60, 30, null,
				zaireRecursos);

		Map<String, Integer> mozambiqueRecursos = new HashMap<String, Integer>();
		mozambiqueRecursos.put("Minerio", 2);
		mozambiqueRecursos.put("Cereal", 3);

		ArrayList<String> frontMozambique = new ArrayList<>(
				Arrays.asList("South Africa", "Straits of Malacca", "Nigeria", "Zaire", "Red Sea", "Egypt"));
		Territorio mozambique = new Territorio(this.p, "Mozambique", frontMozambique, new PVector(931, 654), 80, 30,
				null, mozambiqueRecursos);

		Map<String, Integer> southAfricaRecursos = new HashMap<String, Integer>();
		southAfricaRecursos.put("Cereal", 2);
		southAfricaRecursos.put("Minerio", 2);

		ArrayList<String> frontSouthAfrica = new ArrayList<>(
				Arrays.asList("Mozambique", "Straits of Malacca", "Cape of Good Hope", "Zaire"));
		Territorio southAfrica = new Territorio(this.p, "South Africa", frontSouthAfrica, new PVector(866, 772), 80, 30,
				null, southAfricaRecursos);
		territorios.add(sahara);
		territorios.add(egypt);
		territorios.add(nigeria);
		territorios.add(zaire);
		territorios.add(mozambique);
		territorios.add(southAfrica);
		FedEstAfri.add(nigeria);
		FedEstAfri.add(mozambique);
		FedEstAfri.add(zaire);
		FedEstAfri.add(southAfrica);

	}

	public void setupRussia() {
		ArrayList<String> frontKola = new ArrayList<>(Arrays.asList("Barents Sea", "Scandinavia", "Russia", "Siberia"));

		ArrayList<String> frontRussia = new ArrayList<>(Arrays.asList("Baltic Sea", "Scandinavia", "Baltic Sea",
				"Siberia", "Poland", "Romania", "Kazakh", "Buryatsk", "Kola"));

		ArrayList<String> frontKazakh = new ArrayList<>(Arrays.asList("Black Sea", "Manchuria", "Mongolia", "Tibet",
				"Pakistan", "Romania", "Russia", "Buryatsk", "Black Sea"));

		ArrayList<String> frontSiberia = new ArrayList<>(
				Arrays.asList("Barents Sea", "Yakutsk", "Kola", "Russia", "Buryatsk"));

		ArrayList<String> frontBuryatsk = new ArrayList<>(Arrays.asList("Sea of Okhotsk", "Yakutsk", "Sea of Japan",
				"Russia", "Siberia", "Manchuria", "Mongolia", "Kazakh"));

		ArrayList<String> frontYakutsk = new ArrayList<>(
				Arrays.asList("Sea of Okhotsk", "Buryatsk", "North Pacific", "Siberia"));
		Map<String, Integer> kolaRecursos = new HashMap<>();
		Map<String, Integer> russiaRecursos = new HashMap<>();
		Map<String, Integer> kazakhRecursos = new HashMap<>();
		Map<String, Integer> siberiaRecursos = new HashMap<>();
		Map<String, Integer> buryatskRecursos = new HashMap<>();
		Map<String, Integer> yakutskRecursos = new HashMap<>();

		buryatskRecursos.put("Minerio", 2);
		kazakhRecursos.put("Minerio", 3);
		yakutskRecursos.put("Petroleo", 3);
		siberiaRecursos.put("Minerio", 3);
		russiaRecursos.put("Cereal", 3);
		kolaRecursos.put("Petroleo", 3);

		Territorio kola = new Territorio(this.p, "Kola", frontKola, new PVector(1004, 180), 40, 30, null, kolaRecursos);
		Territorio russia = new Territorio(this.p, "Russia", frontRussia, new PVector(989, 247), 60, 30, null,
				russiaRecursos);
		Territorio kazakh = new Territorio(this.p, "Kazakh", frontKazakh, new PVector(1074, 316), 60, 30, null,
				kazakhRecursos);
		Territorio siberia = new Territorio(this.p, "Siberia", frontSiberia, new PVector(1152, 200), 60, 30, null,
				siberiaRecursos);
		Territorio buryatsk = new Territorio(this.p, "Buryatsk", frontBuryatsk, new PVector(1224, 281), 60, 30, null,
				buryatskRecursos);
		Territorio yakutsk = new Territorio(this.p, "Yakutsk", frontYakutsk, new PVector(1314, 171), 60, 30, null,
				yakutskRecursos);

		territorios.add(kola);
		territorios.add(russia);
		territorios.add(kazakh);
		territorios.add(siberia);
		territorios.add(buryatsk);
		territorios.add(yakutsk);
		URSS.add(kola);
		URSS.add(russia);
		URSS.add(kazakh);
		URSS.add(buryatsk);
		URSS.add(siberia);
		URSS.add(yakutsk);

	}

	public void setupEurope() {
		ArrayList<String> frontBritishIsles = new ArrayList<>(Arrays.asList("North Sea"));

		ArrayList<String> frontScandinavia = new ArrayList<>(
				Arrays.asList("North Sea", "Barents Sea", "Baltic Sea", "Kola", "Russia"));
		ArrayList<String> frontPoland = new ArrayList<>(
				Arrays.asList("Eastern Europe", "Greece", "Romania", "Kola", "Russia", "Baltic Sea"));
		ArrayList<String> frontIberia = new ArrayList<>(
				Arrays.asList("Western Europe", "Bay of Biscay", "Mediterranean Sea"));
		ArrayList<String> frontWesternEurope = new ArrayList<>(Arrays.asList("Iberia", "Bay of Biscay",
				"Mediterranean Sea", "North Sea", "Eastern Europe", "Baltic Sea"));
		ArrayList<String> frontEasternEurope = new ArrayList<>(
				Arrays.asList("Poland", "Greece", "Mediterranean Sea", "Western Europe", "Baltic Sea"));
		ArrayList<String> frontGreece = new ArrayList<>(
				Arrays.asList("Poland", "Romania", "Sea of Crete", "Black Sea", "Eastern Europe"));
		ArrayList<String> frontRomania = new ArrayList<>(
				Arrays.asList("Poland", "Greece", "Russia", "Black Sea", "Kazakh"));

		Map<String, Integer> britishIslesRecursos = new HashMap<>();
		Map<String, Integer> scandinaviaRecursos = new HashMap<>();
		Map<String, Integer> polandRecursos = new HashMap<>();
		Map<String, Integer> iberiaRecursos = new HashMap<>();
		Map<String, Integer> westernEuropeRecursos = new HashMap<>();
		Map<String, Integer> easternEuropeRecursos = new HashMap<>();
		Map<String, Integer> greeceRecursos = new HashMap<>();
		Map<String, Integer> romaniaRecursos = new HashMap<>();

		britishIslesRecursos.put("Petroleo", 3);
		easternEuropeRecursos.put("Minerio", 3);
		westernEuropeRecursos.put("Cereal", 3);
		iberiaRecursos.put("Minerio", 2);
		scandinaviaRecursos.put("Petroleo", 2);
		easternEuropeRecursos.put("Cereal", 2);
		greeceRecursos.put("Minerio", 2);
		polandRecursos.put("Cereal", 3);

		Territorio britishIsles = new Territorio(this.p, "British Isles", frontBritishIsles, new PVector(705, 246), 70,
				25, null, britishIslesRecursos);
		Territorio scandinavia = new Territorio(this.p, "Scandinavia", frontScandinavia, new PVector(785, 190), 90, 25,
				null, scandinaviaRecursos);
		Territorio poland = new Territorio(this.p, "Poland", frontPoland, new PVector(859, 262), 60, 25, null,
				polandRecursos);
		Territorio iberia = new Territorio(this.p, "Iberia", frontIberia, new PVector(683, 374), 60, 25, null,
				iberiaRecursos);
		Territorio westernEurope = new Territorio(this.p, "Western Europe", frontWesternEurope, new PVector(740, 323),
				90, 25, null, westernEuropeRecursos);
		Territorio easternEurope = new Territorio(this.p, "Eastern Europe", frontEasternEurope, new PVector(809, 290),
				90, 25, null, easternEuropeRecursos);
		Territorio greece = new Territorio(this.p, "Greece", frontGreece, new PVector(851, 343), 60, 25, null,
				greeceRecursos);
		Territorio romania = new Territorio(this.p, "Romania", frontRomania, new PVector(900, 305), 60, 25, null,
				romaniaRecursos);

		territorios.add(britishIsles);
		territorios.add(scandinavia);
		territorios.add(poland);
		territorios.add(iberia);
		territorios.add(westernEurope);
		territorios.add(easternEurope);
		territorios.add(greece);
		territorios.add(romania);
		LigaNacEuro.add(britishIsles);
		LigaNacEuro.add(scandinavia);
		LigaNacEuro.add(easternEurope);
		LigaNacEuro.add(westernEurope);
		LigaNacEuro.add(iberia);

	}

	public void setupOceania() {
		Map<String, Integer> newZealandRecursos = new HashMap<>();
		newZealandRecursos.put("Cereal", 1);

		ArrayList<String> frontNewZealand = new ArrayList<>(Arrays.asList("Tasman Sea"));
		Territorio newZealand = new Territorio(this.p, "New Zealand", frontNewZealand, new PVector(1390, 853), 70, 25,
				null, newZealandRecursos);
		territorios.add(newZealand);

		Map<String, Integer> easternAustraliaRecursos = new HashMap<>();
		easternAustraliaRecursos.put("Cereal", 3);
		easternAustraliaRecursos.put("Minerio", 5);

		ArrayList<String> frontEasternAustralia = new ArrayList<>(
				Arrays.asList("Tasman Sea", "Great Australian Blight", "Western Australia", "Timor Sea"));
		Territorio easternAustralia = new Territorio(this.p, "Eastern Australia", frontEasternAustralia,
				new PVector(1280, 839), 100, 30, null, easternAustraliaRecursos);
		territorios.add(easternAustralia);

		Map<String, Integer> westernAustraliaRecursos = new HashMap<>();
		easternAustraliaRecursos.put("Minerio", 3);
		easternAustraliaRecursos.put("Petroleo", 2);
		easternAustraliaRecursos.put("Cereal", 2);

		ArrayList<String> frontWesternAustralia = new ArrayList<>(
				Arrays.asList("Tasman Sea", "Great Australian Blight", "Eastern Australia", "Shark Bay", "Timor Sea"));
		Territorio westernAustralia = new Territorio(this.p, "Western Australia", frontWesternAustralia,
				new PVector(1180, 810), 100, 30, null, westernAustraliaRecursos);
		territorios.add(westernAustralia);

		Map<String, Integer> indonesiaRecursos = new HashMap<>();
		easternAustraliaRecursos.put("Petroleo", 3);

		ArrayList<String> frontIndonesia = new ArrayList<>(
				Arrays.asList("Java Sea", "Mid Pacific", "Bay of Bengal", "Timor Sea"));
		Territorio indonesia = new Territorio(this.p, "Indonesia", frontIndonesia, new PVector(1310, 670), 100, 25,
				null, indonesiaRecursos);
		territorios.add(indonesia);
	}

	public void setupMiddleEast() {
		Map<String, Integer> middleEastRecursos = new HashMap<String, Integer>();

		ArrayList<String> frontMiddleEast = new ArrayList<>(
				Arrays.asList("Sea of Crete", "Red Sea", "Arabia", "Iraq", "Turkey"));
		Territorio middleEast = new Territorio(this.p, "Middle East", frontMiddleEast, new PVector(919, 465), 70, 25,
				null, middleEastRecursos);
		territorios.add(middleEast);

		Map<String, Integer> arabiaRecursos = new HashMap<String, Integer>();
		arabiaRecursos.put("Petroleo", 5);

		ArrayList<String> frontArabia = new ArrayList<>(Arrays.asList("Middle East", "Red Sea", "Arabian Sea", "Iraq"));
		Territorio arabia = new Territorio(this.p, "Arabia", frontArabia, new PVector(996, 545), 50, 25, null,
				arabiaRecursos);
		territorios.add(arabia);

		Map<String, Integer> iraqRecursos = new HashMap<String, Integer>();
		iraqRecursos.put("Petroleo", 3);

		ArrayList<String> frontIraq = new ArrayList<>(
				Arrays.asList("Middle East", "Arabia", "Arabian Sea", "Turkey", "Iran"));
		Territorio iraq = new Territorio(this.p, "Iraq", frontIraq, new PVector(1004, 454), 40, 25, null, iraqRecursos);
		territorios.add(iraq);

		Map<String, Integer> iranRecursos = new HashMap<String, Integer>();
		iranRecursos.put("Petroleo", 4);

		ArrayList<String> frontIran = new ArrayList<>(Arrays.asList("Pakistan", "Arabian Sea", "Turkey", "Iraq"));
		Territorio iran = new Territorio(this.p, "Iran", frontIran, new PVector(1056, 444), 40, 25, null, iranRecursos);
		territorios.add(iran);

		Map<String, Integer> turkeyRecursos = new HashMap<String, Integer>();
		iranRecursos.put("Cereal", 3);

		ArrayList<String> frontTurkey = new ArrayList<>(
				Arrays.asList("Pakistan", "Black Sea", "Iran", "Iraq", "Pakistan", "Kazakh"));
		Territorio turkey = new Territorio(this.p, "Turkey", frontTurkey, new PVector(1000, 380), 60, 25, null,
				turkeyRecursos);
		territorios.add(turkey);

	}

	public void setupMarAzulClaro() {
		// South America
		ArrayList<String> frontBarkleySound = new ArrayList<>(
				Arrays.asList("Alaska", "Western Canada", "North Pacific", "Western USA", "Santa Barbara Passage"));
		MarAzulClaro barkleySound = new MarAzulClaro(this.p, "Barkley Sound", frontBarkleySound, new PVector(158, 245),
				100, 30, null);
		territorios.add(barkleySound);

		ArrayList<String> frontSantaBarbara = new ArrayList<>(Arrays.asList("Mid Pacific", "Gulf of Panama",
				"North Pacific", "Western USA", "Central America", "Barkley Sound"));
		MarAzulClaro santaBarbara = new MarAzulClaro(this.p, "Santa Barbara Passage", frontSantaBarbara,
				new PVector(100, 351), 130, 30, null);
		territorios.add(santaBarbara);

		ArrayList<String> frontGulfOfPanama = new ArrayList<>(
				Arrays.asList("Mid Pacific", "Santa Barbara Passage", "Venezuela", "Lima Bay", "Central America"));
		MarAzulClaro gulfOfPanama = new MarAzulClaro(this.p, "Gulf of Panama", frontGulfOfPanama, new PVector(190, 524),
				100, 30, null);
		territorios.add(gulfOfPanama);

		ArrayList<String> frontLimaBay = new ArrayList<>(
				Arrays.asList("Mid Pacific", "Argentina", "Venezuela", "Peru", "Gulf of Panama"));
		MarAzulClaro limaBay = new MarAzulClaro(this.p, "Lima Bay", frontLimaBay, new PVector(290, 717), 100, 30, null);
		territorios.add(limaBay);

		ArrayList<String> frontRioDeLaPlata = new ArrayList<>(
				Arrays.asList("South Atlantic", "Argentina", "Baia Santos"));
		MarAzulClaro RioDeLaPlata = new MarAzulClaro(this.p, "Rio de la Plata", frontRioDeLaPlata,
				new PVector(490, 832), 100, 30, null);
		territorios.add(RioDeLaPlata);

		ArrayList<String> frontBaiaSantos = new ArrayList<>(
				Arrays.asList("South Atlantic", "Argentina", "Rio de la Plata", "Brazil", "Mid Atlantic"));
		MarAzulClaro baiaSantos = new MarAzulClaro(this.p, "Baia Santos", frontBaiaSantos, new PVector(537, 697), 70,
				30, null);
		territorios.add(baiaSantos);

		ArrayList<String> frontCarribeanSea = new ArrayList<>(Arrays.asList("Central America", "Venezuela",
				"Mid-West USA", "Brazil", "Mid Atlantic", "Eastern USA", "Long Island Sound"));
		MarAzulClaro carribeanSea = new MarAzulClaro(this.p, "Carribean Sea", frontCarribeanSea, new PVector(380, 492),
				100, 30, null);
		territorios.add(carribeanSea);

		ArrayList<String> frontLongIslandSound = new ArrayList<>(Arrays.asList("Carribean Sea", "Gulf of St. Lawrence",
				"Mid Atlantic", "Eastern USA", "North Atlantic"));
		MarAzulClaro longIslandSound = new MarAzulClaro(this.p, "Long Island Sound", frontLongIslandSound,
				new PVector(400, 390), 100, 30, null);
		territorios.add(longIslandSound);

		ArrayList<String> frontGulfOfStLawrence = new ArrayList<>(
				Arrays.asList("Eastern Canada", "Long Island Sound", "Hudson Strait", "Eastern USA", "North Atlantic"));
		MarAzulClaro gulfOfStLawrence = new MarAzulClaro(this.p, "Gulf of St. Lawrence", frontGulfOfStLawrence,
				new PVector(488, 311), 110, 30, null);
		territorios.add(gulfOfStLawrence);

		ArrayList<String> frontHudsonStrait = new ArrayList<>(Arrays.asList("Eastern Canada", "Gulf of St. Lawrence",
				"Greenland", "Western Canada", "North Atlantic", "Northern Canada"));
		MarAzulClaro hudsonStrait = new MarAzulClaro(this.p, "Hudson Strait", frontHudsonStrait, new PVector(454, 188),
				90, 30, null);
		territorios.add(hudsonStrait);
		// Europe
		ArrayList<String> frontNorthSea = new ArrayList<>(Arrays.asList("Scandinavia", "British Isles", "Baltic Sea",
				"Barents Sea", "North Atlantic", "Bay of Biscay", "Western Europe"));
		MarAzulClaro northSea = new MarAzulClaro(this.p, "North Sea", frontNorthSea, new PVector(701, 149), 70, 30,
				null);
		territorios.add(northSea);

		ArrayList<String> frontBarentsSea = new ArrayList<>(
				Arrays.asList("Scandinavia", "Kola", "Siberia", "North Sea"));
		MarAzulClaro barentsSea = new MarAzulClaro(this.p, "Barents Sea", frontBarentsSea, new PVector(1011, 107), 90,
				30, null);
		territorios.add(barentsSea);

		ArrayList<String> frontBalticSea = new ArrayList<>(
				Arrays.asList("Scandinavia", "Russia", "Poland", "North Sea", "Eastern Europe", "Western Europe"));
		MarAzulClaro balticSea = new MarAzulClaro(this.p, "Baltic Sea", frontBalticSea, new PVector(861, 200), 60, 25,
				null);

		ArrayList<String> frontBayOfBiscay = new ArrayList<>(
				Arrays.asList("North Sea", "Mediterranean Sea", "North Atlantic", "Iberia", "Western Europe"));
		MarAzulClaro bayOfBiscay = new MarAzulClaro(this.p, "Bay of Biscay", frontBayOfBiscay, new PVector(641, 319),
				90, 30, null);

		// Oriente Medio
		ArrayList<String> frontMediterraneanSea = new ArrayList<>(Arrays.asList("Iberia", "Sea of Crete",
				"North Atlantic", "Red Sea", "Western Europe", "Mid Atlantic", "Eastern Europe", "Sahara", "Egypt"));
		MarAzulClaro mediterraneanSea = new MarAzulClaro(this.p, "Mediterranean Sea", frontMediterraneanSea,
				new PVector(702, 426), 130, 30, null);

		ArrayList<String> frontSeaOfCrete = new ArrayList<>(Arrays.asList("Black Sea", "Mediterranean Sea", "Red Sea",
				"Eastern Europe", "Greece", "Middle East", "Turkey"));
		MarAzulClaro seaOfCrete = new MarAzulClaro(this.p, "Sea of Crete", frontSeaOfCrete, new PVector(862, 418), 90,
				30, null);

		ArrayList<String> frontBlackSea = new ArrayList<>(
				Arrays.asList("Sea of Crete", "Romania", "Kazakh", "Greece", "Turkey"));
		MarAzulClaro blackSea = new MarAzulClaro(this.p, "Black Sea", frontBlackSea, new PVector(966, 338), 70, 30,
				null);

		// Africa
		ArrayList<String> frontGulfOfGuinea = new ArrayList<>(
				Arrays.asList("Mid Atlantic", "South Atlantic", "Nigeria", "Zaire", "Cape of Good Hope"));
		MarAzulClaro golfOfGuinea = new MarAzulClaro(this.p, "Gulf of Guinea", frontGulfOfGuinea, new PVector(713, 637),
				90, 30, null);

		ArrayList<String> frontCapeOfGoodHope = new ArrayList<>(Arrays.asList("Straits of Malacca", "South Atlantic",
				"Indian Ocean", "Zaire", "Gulf of Guinea", "South Africa", "Mozambique"));
		MarAzulClaro capeOfGoodHope = new MarAzulClaro(this.p, "Cape of Good Hope", frontCapeOfGoodHope,
				new PVector(846, 834), 110, 30, null);

		ArrayList<String> frontStraitsOfMalacca = new ArrayList<>(
				Arrays.asList("Cape of Good Hope", "Mozambique", "Indian Ocean", "Red Sea", "South Africa"));
		MarAzulClaro straitsOfMalacca = new MarAzulClaro(this.p, "Straits of Malacca", frontStraitsOfMalacca,
				new PVector(973, 713), 110, 30, null);

		ArrayList<String> frontRedSea = new ArrayList<>(Arrays.asList("Straits of Malacca", "Mozambique", "Egypt",
				"Arabia", "Arabian Sea", "Middle East", "Sea of Crete", "Mediterranean Sea"));
		MarAzulClaro redSea = new MarAzulClaro(this.p, "Red Sea", frontRedSea, new PVector(920, 540), 60, 25, null);

		// Russia & China
		ArrayList<String> frontSeaOfOkhotsk = new ArrayList<>(
				Arrays.asList("North Pacific", "Yakutsk", "Buryatsk", "Japan", "Sea of Japan", "Tokyo Bay"));
		MarAzulClaro seaOfOkhotsk = new MarAzulClaro(this.p, "Sea of Okhotsk", frontSeaOfOkhotsk,
				new PVector(1350, 251), 110, 25, null);

		ArrayList<String> frontTokyoBay = new ArrayList<>(
				Arrays.asList("North Pacific", "Sea of Okhotsk", "Japan", "Sea of Japan"));
		MarAzulClaro tokyoBay = new MarAzulClaro(this.p, "Tokyo Bay", frontTokyoBay, new PVector(1416, 330), 60, 30,
				null);

		ArrayList<String> frontSeaOfJapan = new ArrayList<>(Arrays.asList("North Pacific", "Sea of Okhotsk", "Japan",
				"Tokyo Bay", "Manchuria", "Mongolia", "Shantung", "Nanling", "South China Sea", "Mid Pacific"));
		MarAzulClaro seaOfJapan = new MarAzulClaro(this.p, "Sea of Japan", frontSeaOfJapan, new PVector(1370, 415), 80,
				25, null);

		ArrayList<String> frontSouthChinaSea = new ArrayList<>(
				Arrays.asList("Mid Pacific", "Sea of Japan", "Indo-China", "Java Sea", "Nanling"));
		MarAzulClaro southChinaSea = new MarAzulClaro(this.p, "South China Sea", frontSouthChinaSea,
				new PVector(1379, 476), 100, 25, null);

		// Oceania
		ArrayList<String> frontTasmanSea = new ArrayList<>(Arrays.asList("South Pacific", "Timor Sea", "New Zealand",
				"Eastern Australia", "Great Australian Blight"));
		MarAzulClaro tasmanSea = new MarAzulClaro(this.p, "Tasman Sea", frontTasmanSea, new PVector(1338, 941), 70, 25,
				null);

		ArrayList<String> frontTimorSea = new ArrayList<>(Arrays.asList("South Pacific", "Tasman Sea",
				"Western Australia", "Eastern Australia", "Mid Pacific", "Shark Bay", "Bay of Bengal", "Indonesia"));
		MarAzulClaro timorSea = new MarAzulClaro(this.p, "Timor Sea", frontTimorSea, new PVector(1262, 705), 70, 25,
				null);

		ArrayList<String> frontTSharkBay = new ArrayList<>(
				Arrays.asList("Indian Ocean", "Timor Sea", "Western Australia", "Great Australian Blight"));
		MarAzulClaro sharkBay = new MarAzulClaro(this.p, "Shark Bay", frontTSharkBay, new PVector(1123, 780), 70, 25,
				null);

		// India - Oriente
		ArrayList<String> frontArabianSea = new ArrayList<>(Arrays.asList("Indian Ocean", "Red Sea", "Arabia", "Iraq",
				"Iran", "Pakistan", "India", "Bay of Bengal"));
		MarAzulClaro arabianSea = new MarAzulClaro(this.p, "Arabian Sea", frontArabianSea, new PVector(1097, 572), 70,
				25, null);

		ArrayList<String> frontBayOfBengal = new ArrayList<>(
				Arrays.asList("Indian Ocean", "Arabian Sea", "Burma", "India", "Java Sea", "Timor Sea", "Indonesia"));
		MarAzulClaro bayOfBengal = new MarAzulClaro(this.p, "Bay of Bengal", frontBayOfBengal, new PVector(1142, 627),
				90, 25, null);

		ArrayList<String> frontJavaSea = new ArrayList<>(
				Arrays.asList("Mid Pacific", "Indo-China", "Burma", "South China Sea", "Bay of Bengal", "Indonesia"));
		MarAzulClaro javaSea = new MarAzulClaro(this.p, "Java Sea", frontJavaSea, new PVector(1328, 616), 60, 25, null);
		territorios.add(balticSea);
		territorios.add(bayOfBiscay);
		territorios.add(mediterraneanSea);
		territorios.add(seaOfCrete);
		territorios.add(blackSea);
		territorios.add(golfOfGuinea);
		territorios.add(capeOfGoodHope);
		territorios.add(straitsOfMalacca);
		territorios.add(redSea);
		territorios.add(seaOfOkhotsk);
		territorios.add(tokyoBay);
		territorios.add(seaOfJapan);
		territorios.add(southChinaSea);
		territorios.add(tasmanSea);
		territorios.add(timorSea);
		territorios.add(sharkBay);
		territorios.add(arabianSea);
		territorios.add(bayOfBengal);
		territorios.add(javaSea);

	}

	public void setupMarAzulEscuro() {
		ArrayList<String> frontNorthPacific = new ArrayList<>(Arrays.asList("Alaska", "Barkley Sound", "",
				"Mid Pacific", "Santa Barbara Passage", "Yakutsk", "Sea of Okhotsk", "Tokyo Bay", "Sea of Japan"));
		ArrayList<String> frontMidPacific = new ArrayList<>(
				Arrays.asList("Gulf of Panama", "Lima Bay", "North Pacific", "Santa Barbara Passage", "South Pacific",
						"Sea of Japan", "South China Sea", "Java Sea", "Indonesia", "Timor Sea"));
		ArrayList<String> frontSouthPacific = new ArrayList<>(Arrays.asList("Argentina", "Lima Bay", "Mid Pacific",
				"South Atlantic", "Tasman Sea", "Great Australian Blight"));
		ArrayList<String> frontSouthAtlantic = new ArrayList<>(Arrays.asList("Rio de la Plata", "Baia Santos",
				"Mid Atlantic", "South Pacific", "Gulf of Guinea", "Cape of Good Hope", "Indian Ocean"));
		ArrayList<String> frontMidAtlantic = new ArrayList<>(
				Arrays.asList("Brazil", "Baia Santos", "North Atlantic", "Sahara", "Gulf of Guinea", "Carribean Sea",
						"Long Island Sound", "Mediterranean Sea", "South Atlantic"));
		ArrayList<String> frontNorthAtlantic = new ArrayList<>(Arrays.asList("Gulf of St. Lawrence", "Mid Atlantic",
				"Hudson Strait", "North Sea", "Bay of Biscay", "Long Island Sound", "Mediterranean Sea"));
		ArrayList<String> frontIndianOcean = new ArrayList<>(
				Arrays.asList("Cape of Good Hope", "South Atlantic", "Straits of Malacca", "Red Sea", "Arabian Sea",
						"Bay of Bengal", "Timor Sea", "Shark Bay", "Great Australian Blight"));
		ArrayList<String> frontGreatAusBlight = new ArrayList<>(Arrays.asList("Indian Ocean", "Western Australia",
				"Shark Bay", "Eastern Australia", "Tasman Sea", "South Pacific"));

		MarAzulEscuro northPacific = new MarAzulEscuro(this.p, "North Pacific", frontNorthPacific, new PVector(63, 191),
				new PVector(1436, 208), 70, 30, null);

		MarAzulEscuro midPacific = new MarAzulEscuro(this.p, "Mid Pacific", frontMidPacific, new PVector(58, 561),
				new PVector(1423, 522), 70, 40, null);

		MarAzulEscuro southPacific = new MarAzulEscuro(this.p, "South Pacific", frontSouthPacific, new PVector(73, 751),
				new PVector(1429, 760), 70, 40, null);

		MarAzulEscuro southAtlantic = new MarAzulEscuro(this.p, "South Atlantic", frontSouthAtlantic,
				new PVector(630, 824), null, 80, 40, null);

		MarAzulEscuro midAtlantic = new MarAzulEscuro(this.p, "Mid Atlantic", frontMidAtlantic, new PVector(543, 543),
				null, 80, 40, null);

		MarAzulEscuro northAtlantic = new MarAzulEscuro(this.p, "North Atlantic", frontNorthAtlantic,
				new PVector(533, 370), null, 90, 40, null);

		MarAzulEscuro indianOcean = new MarAzulEscuro(this.p, "Indian Ocean", frontIndianOcean, new PVector(960, 878),
				null, 90, 40, null);

		MarAzulEscuro greatAusBlight = new MarAzulEscuro(this.p, "Great Australian Blight", frontGreatAusBlight,
				new PVector(1195, 956), null, 140, 40, null);

		territorios.add(northPacific);
		territorios.add(midPacific);
		territorios.add(southPacific);
		territorios.add(southAtlantic);
		territorios.add(midAtlantic);
		territorios.add(northAtlantic);
		territorios.add(indianOcean);
		territorios.add(greatAusBlight);

	}

	private void setupAsia() {
		ArrayList<String> frontPakistan = new ArrayList<>(
				Arrays.asList("Iran", "Turkey", "Kazakh", "Tibet", "India", "Arabian Sea"));
		ArrayList<String> frontIndia = new ArrayList<>(
				Arrays.asList("Pakistan", "Burma", "Nanling", "Tibet", "Bay of Bengal", "Arabian Sea"));
		ArrayList<String> frontBurma = new ArrayList<>(
				Arrays.asList("India", "Indo-China", "Nanling", "Java Sea", "Bay of Bengal"));
		ArrayList<String> frontIndoChina = new ArrayList<>(
				Arrays.asList("Burma", "South China Sea", "Nanling", "Java Sea"));
		ArrayList<String> frontTibet = new ArrayList<>(
				Arrays.asList("Pakistan", "India", "Nanling", "Mongolia", "Shantung", "Kazakh"));
		ArrayList<String> frontNanling = new ArrayList<>(
				Arrays.asList("Tibet", "India", "Burma", "Indo-China", "Shantung", "South China Sea"));
		ArrayList<String> frontShantung = new ArrayList<>(
				Arrays.asList("Nanling", "Tibet", "Mongolia", "Sea of Japan"));
		ArrayList<String> frontMongolia = new ArrayList<>(
				Arrays.asList("Shantung", "Tibet", "Manchuria", "Sea of Japan", "Kazakh", "Buryatsk"));
		ArrayList<String> frontManchuria = new ArrayList<>(Arrays.asList("Mongolia", "Sea of Japan", "Buryatsk"));
		ArrayList<String> frontJapan = new ArrayList<>(Arrays.asList("Tokyo Bay", "Sea of Japan"));
		Map<String, Integer> pakistanRecursos = new HashMap<String, Integer>();
		Map<String, Integer> indiaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> burmaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> indoChinaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> tibetRecursos = new HashMap<String, Integer>();
		Map<String, Integer> nanlingRecursos = new HashMap<String, Integer>();
		Map<String, Integer> shantungRecursos = new HashMap<String, Integer>();
		Map<String, Integer> mongoliaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> manchuriaRecursos = new HashMap<String, Integer>();
		Map<String, Integer> japanRecursos = new HashMap<String, Integer>();
		indiaRecursos.put("Minerio", 3);
		manchuriaRecursos.put("Minerio", 2);
		nanlingRecursos.put("Petroleo", 2);
		nanlingRecursos.put("Cereal", 3);
		shantungRecursos.put("Petroleo", 3);
		mongoliaRecursos.put("Minerio", 3);
		shantungRecursos.put("Cereal", 2);

		Territorio pakistan = new Territorio(this.p, "Pakistan", frontPakistan, new PVector(1105, 430), 60, 30, null,
				pakistanRecursos);
		Territorio india = new Territorio(this.p, "India", frontIndia, new PVector(1175, 505), 60, 30, null,
				indiaRecursos);
		Territorio burma = new Territorio(this.p, "Burma", frontBurma, new PVector(1250, 505), 60, 30, null,
				burmaRecursos);
		Territorio indoChina = new Territorio(this.p, "Indo-China", frontIndoChina, new PVector(1320, 510), 70, 30,
				null, indoChinaRecursos);
		Territorio tibet = new Territorio(this.p, "Tibet", frontTibet, new PVector(1170, 420), 60, 30, null,
				tibetRecursos);
		Territorio nanling = new Territorio(this.p, "Nanling", frontNanling, new PVector(1270, 445), 70, 30, null,
				nanlingRecursos);

		Territorio shantung = new Territorio(this.p, "Shantung", frontShantung, new PVector(1270, 410), 80, 30, null,
				shantungRecursos);
		Territorio mongolia = new Territorio(this.p, "Mongolia", frontMongolia, new PVector(1230, 380), 90, 30, null,
				mongoliaRecursos);
		Territorio manchuria = new Territorio(this.p, "Manchuria", frontManchuria, new PVector(1260, 333), 90, 30, null,
				manchuriaRecursos);
		Territorio japan = new Territorio(this.p, "Japan", frontJapan, new PVector(1380, 375), 90, 30, null,
				japanRecursos);

		territorios.add(pakistan);
		territorios.add(india);
		territorios.add(burma);
		territorios.add(indoChina);
		territorios.add(tibet);
		territorios.add(nanling);
		territorios.add(shantung);
		territorios.add(mongolia);
		territorios.add(manchuria);
		territorios.add(japan);
		RepPopChi.add(tibet);
		RepPopChi.add(shantung);
		RepPopChi.add(nanling);
		RepPopChi.add(mongolia);
		RepPopChi.add(manchuria);

	}

	public boolean encerrarPartida() {
		return true;

	}

	public void trocarTurno() {
		avaliarVencedor();
		numRodadas++;
		currentState = estados.esperandoLance;
	}

	public void avaliarVencedor() {
		boolean acabou = false;
		if (jogadorLocal.getOwnedTerrain().isEmpty() || jogadorLocal.central.getDinheiro() <= 0) {
			vencedor = jogadorRemoto.getNome();
			acabou = true;
		} else if (jogadorRemoto.getOwnedTerrain().isEmpty() || jogadorRemoto.central.getDinheiro() <= 0) {
			vencedor = jogadorLocal.getNome();
			acabou = true;
		}
		if (acabou) {
			JOptionPane.showMessageDialog(null, "Vencedor da partida é "+vencedor+"!!");
			Lance lance = new Lance();
			lance.setEstado(estados.fim);
			lance.setVencedor(vencedor);
			net.enviarJogada(lance);
			p.exit();
		} else {
			JOptionPane.showMessageDialog(null, "Trocou turno");
			Lance novoLance = new Lance();
			novoLance.setEstado(estados.fim);
			novoLance.setVencedor("");
			net.enviarJogada(novoLance);
		}
	}
	public void finalizarJogo() {
		this.p.exit();
	}
}
