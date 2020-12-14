package Game;
import Rede.NetGames;
import processing.core.*;

public class UsingProcessing extends PApplet {
	

	
	private  Tabuleiro tabuleiro;
	static SetupGui frame;
	private static  NetGames net = new NetGames();
    private java.util.ArrayList<Botao> botoes = new java.util.ArrayList<Botao>();
    
    
    public static void main(String[] args) {
        //PApplet.main("Game.UsingProcessing");
        frame = new SetupGui("Supremacia",net);
        frame.setVisible(true);
    }
    void initialize() {
    	
    }

    public void setup(){
    	this.tabuleiro = new Tabuleiro(this, net);
    	//this.tabuleiro.setP(this);
    	net.setTabuleiro(tabuleiro);
    	this.tabuleiro.start(frame.getPos());
        
    }
    public void draw(){
    	background(255, 255, 255);
        this.tabuleiro.show();
        for (int i = 0; i < botoes.size(); i++) {
            botoes.get(i).show();
        }
    }
    public void mouseClicked() {
    	int x = mouseX;
    	int y = mouseY;
    	this.tabuleiro.click(x, y);
        
    }

    // method used only for setting the size of the window
    public void settings(){
    	size(displayWidth, displayHeight);
    }

}
