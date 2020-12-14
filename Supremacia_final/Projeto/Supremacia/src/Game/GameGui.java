package Game;

import Economy.Mercado;
import processing.core.PApplet;
import processing.core.PVector;

public class GameGui {
	PApplet p;
	Tabuleiro t;
	Mercado mercado;
	int minX = 1500;
	int maxY = 1060;
	private Jogador player;
	private Botao botaoTurno;
	
	public Botao getBotaoTurno() {
		return botaoTurno;
	}

	public GameGui(PApplet p, Tabuleiro tabuleiro, Mercado mercado) {
		this.p = p;
		this.t = tabuleiro;
		this.mercado = mercado;
		botaoTurno = new Botao(p, "Encerrar Turno",new PVector(1720, 650) , 90, 30);
	}
	
	public void show() {
		if (player != null) {
			botaoTurno.show();
			p.textSize(30);
			p.text(player.getNome(), 1720, 50);
			p.textSize(26);
			p.text("Dinheiro: "+player.central.getDinheiro(), 1720, 100);
			p.text("Recursos", 1720, 150);
			p.text("Petroleo: "+player.central.getQtdPetroleo(), 1720, 200);
			p.text("Minerio: "+player.central.getQtdMinerio(), 1720, 250);
			p.text("Cereal: "+player.central.getQtdCereal(), 1720, 300);
			p.text("Bombas: "+player.central.getQtdBombaAtomica(), 1720, 350);
			p.text("Laser-stars: "+player.central.getQtdLaserStar(), 1720, 400);
			
			
	//		Mercado
			p.text("MERCADO - Preço dos recursos", 1720, 450);
			p.text("Preço Petroleo:"+mercado.getValorPetroleo(), 1720, 500);
			p.text("Preço Minerio:"+mercado.getValorMinerio(), 1720, 550);
			p.text("Preço Cereal:"+mercado.getValorCereal(), 1720, 600);
			
			
			p.text("Nação: "+player.getNation(), 1720, 700);
			p.text("Rodada: "+t.getNumRodadas(), 1720, 750);
			p.text(t.getCurrentState(), 1720, 800);
		}
	}
	
	public void setPlayer(Jogador player) {
		this.player = player;
	}
}
