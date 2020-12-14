package Game;

public class CentralSuprimentos {
	private int qtdCereal = 0;
	private int qtdMinerio = 0;
	private int qtdPetroleo = 0;
	private int qtdBombaAtomica;
	private int qtdLaserStar;
	private int dinheiro = 70000;
	
	public int getDinheiro() {
		return this.dinheiro;
	}
	
	public boolean removerConjuntoSuprimentos(String objetivo) {
		switch (objetivo) {
		case "construir":
			if(this.dinheiro >= 1000 && this.qtdCereal >= 1 && this.qtdMinerio >= 1 && this.qtdPetroleo >= 1) {
				this.dinheiro -= 1000;
				this.qtdCereal -= 1;
				this.qtdPetroleo -= 1;
				this.qtdMinerio -= 1;
				return true;
			} else {
				return false;
			}
		case "atacar":
			if(this.qtdCereal >= 1 && this.qtdMinerio >= 1 && this.qtdPetroleo >= 1) {
				this.qtdCereal -= 1;
				this.qtdPetroleo -= 1;
				this.qtdMinerio -= 1;
				return true;
			} else {
				return false;
			}
		default:
			break;
		}
		return false;
		
	}
	
	public void addValor(String recurso, int valor) {
		switch(recurso) {
		case "Petroleo":
			int basePetroleo = getQtdPetroleo();
			basePetroleo += valor;
			setQtdPetroleo(basePetroleo);
			break;
			
		case "Minerio":
			int baseMinerio = getQtdMinerio();
			baseMinerio += valor;
			setQtdMinerio(baseMinerio);
			break;
			
		case "Cereal":
			int baseCereal = getQtdCereal();
			baseCereal += valor;
			setQtdCereal(baseCereal);	
			break;
		}
	}
	
	public void removerValor(String recurso, int valor) {
		switch(recurso) {
		case "Petroleo":
			int basePetroleo = getQtdPetroleo();
			basePetroleo -= valor;
			
			setQtdPetroleo(basePetroleo);
			break;
			
		case "Minerio":
			int baseMinerio = getQtdMinerio();
			baseMinerio -= valor;
			setQtdMinerio(baseMinerio);
			break;
			
		case "Cereal":
			int baseCereal = getQtdCereal();
			baseCereal -= valor;
			setQtdCereal(baseCereal);	
			break;
		}
	}
	
	public int getQtdRecurso(String produto) {
		switch(produto) {
		case "Petroleo":
			return getQtdPetroleo();
			
		case "Minerio":
			return getQtdMinerio();
			
		case "Cereal":
			return getQtdCereal();
		default:
			return 0;
		}
	}
	
	public int getQtdCereal() {
		return qtdCereal;
	}
	public void addDinheiro(int valor) {
		this.dinheiro += valor;
	}
	public void removerDinheiro(int valor) {
		this.dinheiro -= valor;
	}

	public void setQtdCereal(int qtdCereal) {
		this.qtdCereal = qtdCereal;
	}

	public void setDinheiro(int dinheiro) {
		this.dinheiro = dinheiro;
	}

	public int getQtdMinerio() {
		return qtdMinerio;
	}

	public void setQtdMinerio(int qtdMinerio) {
		this.qtdMinerio = qtdMinerio;
	}

	public int getQtdPetroleo() {
		return qtdPetroleo;
	}

	public void setQtdPetroleo(int qtdPetroleo) {
		this.qtdPetroleo = qtdPetroleo;
	}
	public void addBomba(int valor) {
		this.qtdBombaAtomica += valor;
	}
	public void removerBomba(int valor) {
		this.qtdBombaAtomica -= valor;
	}
	public void addLaser(int valor) {
		this.qtdLaserStar += valor;
	}
	public void removerLaser(int valor) {
		this.qtdLaserStar -= valor;
	}
	
	public int getQtdBombaAtomica() {
		return qtdBombaAtomica;
	}

	public void setQtdBombaAtomica(int qtdBombaAtomica) {
		this.qtdBombaAtomica = qtdBombaAtomica;
	}

	public int getQtdLaserStar() {
		return qtdLaserStar;
	}

	public void setQtdLaserStar(int qtdLaserStar) {
		this.qtdLaserStar = qtdLaserStar;
	}
	public int[] getRecursos() {
		int[] recursos = {this.qtdMinerio, this.qtdCereal, this.qtdPetroleo, this.qtdBombaAtomica, this.qtdLaserStar};
		return recursos;
		
	}
}
