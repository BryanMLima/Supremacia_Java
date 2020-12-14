package Land;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Combat.Tropa;
import Game.Jogador;

public class Territorio {

	private String name;
	private final ArrayList<String> fronteiras;
	private PVector pos;
	private final PApplet p;
	private int width, height;
	private ArrayList<Tropa> ocupantes;
	private boolean isKabooned = false;

	private int custo = 500;
	private boolean clickable = false;
	private String currentColor = "cinza";
	private Map<String,Integer> recursos = new HashMap<String,Integer>();

	public Map<String, Integer> getRecursos() {
		return recursos;
	}
	
	//	Apenas um ocupante
	public Territorio(PApplet p, String name, ArrayList<String> fronteiras, PVector pos, int width, int height, ArrayList<Tropa> ocupantes, Map<String,Integer> recursos) {
		this.name = name;
		this.fronteiras = fronteiras;
		this.pos = pos;
		this.width = width;
		this.height = height;
		this.p = p;
		this.recursos = recursos;
		if (ocupantes != null) {
			this.ocupantes = ocupantes;
		} else {
			this.ocupantes = new ArrayList<>();
		}
	}

	public void setOcupantes(ArrayList<Tropa> ocupantes) {
		this.ocupantes = ocupantes;
	}

	public String getName() {
		return name;
	}
	public ArrayList<String> getFronteiras() {
		return this.fronteiras;
	}
	public void Kaboom() {
		this.isKabooned = true;
		if (estaOcupado()) {
			this.ocupantes.clear();
		}
	}
	

	public void show() {
		int[] rgb;
		if (estaOcupado() && !isClickable()) {
			rgb = getColor(getOwner().getColor());
		} else {
			rgb = getColor(getCurrentColor());
		}
		p.fill(rgb[0], rgb[1], rgb[2]);
		p.rectMode(PConstants.CENTER);
		p.rect(this.pos.x, this.pos.y, this.width, this.height);
		p.fill(0, 0, 0);
		p.textAlign(PConstants.CENTER, PConstants.BOTTOM);
		String text = this.name;
		p.textSize(12);
		if(!this.getOcupantes().isEmpty()) {
			p.text(this.ocupantes.size(), this.pos.x, this.pos.y+10);
		}
		p.text(text , this.pos.x, this.pos.y);
		
	}
	

	public boolean isPointInside(int x, int y) {
		return x > this.pos.x - this.width/2 && x < this.pos.x + this.width/2 &&
				y > this.pos.y - this.height/2 && y < this.pos.y + this.height/2;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PVector getPos() {
		return pos;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public PApplet getP() {
		return p;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ArrayList<Tropa> getOcupantes() {
		return this.ocupantes;
	}

	public void addOcupantes(ArrayList<Tropa> ocupantes) {
		System.out.println(ocupantes);
		System.out.println("setado");
		this.ocupantes.addAll(ocupantes);
	}
	public void addOcupante(Tropa tropa) {
		this.ocupantes.add(tropa);
	}
	public Tropa removeOcupante() {
		if (ocupantes.size() >= 1) {
			Tropa t = this.ocupantes.get(ocupantes.size() - 1);
			this.ocupantes.remove(ocupantes.size() - 1);
			return t;
		} 
		return null;
	}
	public void removeOcupantes(int mortos) {
		if(this.ocupantes.size() - mortos <= 0) {
			this.ocupantes.clear();
		} else {
			for (int i = mortos; i > 0; i--) {
				
				this.ocupantes.remove(i);
			}
		}
	}

	public int getCusto() {
		return custo;
	}

	public void setCusto(int custo) {
		this.custo = custo;
	}
	public boolean estaOcupado() {
		return !ocupantes.isEmpty();
	}
	public Jogador getOwner() {
		if(estaOcupado()) {
			return ocupantes.get(0).getOwner();
		}
		return null;
	}

	
	public int[] getColor(String color) {
		int[] cor = {150, 150,150};
		if (color.equals("verdeClaro")) {
			cor[0] = 0;
			cor[1] = 200;
			cor[2] = 0;
		} else if (color.equals("vermelho")) {
			cor[0] = 255;
			cor[1] = 0;
			cor[2] = 0;
		} else if (color.equals("laranja")) {
			cor[0] = 255;
			cor[1] = 125;
			cor[2] = 0;
		} else if (color.equals("rosa")) {
			cor[0] = 255;
			cor[1] = 0;
			cor[2] = 125;
		} else if (color.equals("roxo")) {
			cor[0] = 190;
			cor[1] = 0;
			cor[2] = 255;
		} else if (color.equals("amarelo")) {
			cor[0] = 255;
			cor[1] = 190;
			cor[2] = 0;
		} else if (color.equals("verdeEscuro")) {
			cor[0] = 0;
			cor[1] = 100;
			cor[2] = 0;
		} else if (color.equals("cinza")) {
			cor[0] = 150;
			cor[1] = 150;
			cor[2] = 150;
		}  else if (color.equals("branco")) {
			cor[0] = 255;
			cor[1] = 255;
			cor[2] = 255;
		}
		return cor;
	}

	public boolean isClickable() {
		return clickable && !isKabooned;
	}

	public void setClickable(boolean clickable) {
		if (clickable) {
			setCurrentColor("branco");
		} else {
			setCurrentColor("cinza");
		}
		this.clickable = clickable;
	}

	public String getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(String currentColor) {
		this.currentColor = currentColor;
	}
}
