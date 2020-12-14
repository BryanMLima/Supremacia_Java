package Economy;

public class Mercado {
	private int valorPetroleo = 5000;
	private int valorMinerio = 5000;
	private int valorCereal = 5000;
	
	public Mercado() {
		
	}
	
	public void addValor(String recurso, int unidades) {
		switch(recurso) {
		case "Petroleo":
			int basePetroleo = getValorPetroleo();
			int novoValorPetroleo = unidades * 1000;
			basePetroleo += novoValorPetroleo;
			if (basePetroleo > 12000) {
				setValorPetroleo(12000);
			} else {				
				setValorPetroleo(basePetroleo);
			}
			break;
		case "Minerio":
			int baseMinerio = getValorMinerio();
			int novoValorMinerio = unidades * 1000;
			baseMinerio += novoValorMinerio;
			if (baseMinerio > 12000) {
				setValorMinerio(12000);
			} else {				
				setValorMinerio(baseMinerio);			
			}
			break;
			
		case "Cereal":
			int baseCereal = getValorCereal();
			int novoValorCereal= unidades * 1000;
			baseCereal += novoValorCereal;
			if (baseCereal > 12000) {
				setValorCereal(12000);
			} else {				
				setValorCereal(baseCereal);			
			}
			break;
		}
		
	}
	
	public void removerValor(String recurso, int unidades) {
		switch(recurso) {
		case "Petroleo":
			int basePetroleo = getValorPetroleo();
			int novoValorPetroleo = unidades * 1000;
			basePetroleo -= novoValorPetroleo;
			if (basePetroleo < 1000) {
				setValorPetroleo(1000);
			} else {				
				setValorPetroleo(basePetroleo);			
			}
			break;
		case "Minerio":
			int baseMinerio = getValorMinerio();
			int novoValorMinerio = unidades * 1000;
			baseMinerio -= novoValorMinerio;
			if (baseMinerio < 1000) {
				setValorMinerio(1000);
			} else {				
				setValorMinerio(baseMinerio);			
			}
			break;
			
		case "Cereal":
			int baseCereal = getValorCereal();
			int novoValorCereal= unidades * 1000;
			baseCereal -= novoValorCereal;
			if (baseCereal < 1000) {
				setValorCereal(1000);
			} else {				
				setValorCereal(baseCereal);			
			}
			break;
		}
	}
	
	public int getValor(String produto) {
		switch(produto) {
		case "Petroleo":
			return getValorPetroleo();
			
		case "Minerio":
			return getValorMinerio();
			
		case "Cereal":
			return getValorCereal();
		default:
			return 0;
		}
	}
	
	public int getValorPetroleo() {
		return valorPetroleo;
	}
	public void setValorPetroleo(int valorPetroleo) {
		this.valorPetroleo = valorPetroleo;
	}
	public int getValorMinerio() {
		return valorMinerio;
	}
	public void setValorMinerio(int valorMinerio) {
		this.valorMinerio = valorMinerio;
	}
	public int getValorCereal() {
		return valorCereal;
	}
	public void setValorCereal(int valorCereal) {
		this.valorCereal = valorCereal;
	}
	
	

}
