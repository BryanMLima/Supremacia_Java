package Combat;

import Game.Jogador;

public class Tropa {
	private Jogador owner;
	private int salario = 100;
	
	public int getSalario() {
		return salario;
	}

	public Jogador getOwner() {
		return owner;
	}
	public void setOwner(Jogador owner) {
		this.owner = owner;
	}
}
