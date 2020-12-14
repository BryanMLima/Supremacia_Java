package Game;

import java.util.ArrayList;

import Combat.Tropa;
import Land.Territorio;

public class Jogador {
	private boolean seuTurno = false;
	private String nome;
	private String nation;
	private String color;
	public CentralSuprimentos central;
	private boolean primeiraBomba = true;
	private boolean primeiroLaser = true;
	private Tabuleiro t;
	
	public boolean isPrimeiroLaser() {
		return primeiroLaser;
	}
	public void setPrimeiroLaser(boolean primeiroLaser) {
		this.primeiroLaser = primeiroLaser;
	}
	public boolean isPrimeiraBomba() {
		return primeiraBomba;
	}
	public void setPrimeiraBomba(boolean primeiraBomba) {
		this.primeiraBomba = primeiraBomba;
	}
	public ArrayList<Tropa> getOwnedTropa() {
		ArrayList<Tropa> tropas = new ArrayList<Tropa>();
		for (Territorio territorio : t.getTerritorios()) {
			if (territorio.estaOcupado()) {
				if (territorio.getOwner().equals(this)) {
					tropas.addAll(territorio.getOcupantes());
				}
			}
		}
		return tropas;
	}

	

	
	public Jogador(String nation, String nome, Tabuleiro t) {
		this.setNation(nation);
		this.nome = nome;
		this.central = new CentralSuprimentos();
		this.t = t;
	}

	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public boolean informarTurno() {
		return seuTurno;
	}
	public String getNome() {
		return nome;
	}
	public void inverterTurno() {
		seuTurno = !seuTurno;
	}
	public ArrayList<Territorio> getOwnedTerrain() {
		ArrayList<Territorio> terri = new ArrayList<Territorio>();
		for (Territorio territorio : this.t.getTerritorios()) {
			if (territorio.estaOcupado()) {
				if (territorio.getOwner().getNome().equals(getNome())) {
					terri.add(territorio);
				}
			}
		}
		return terri;
	}

	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	
}
