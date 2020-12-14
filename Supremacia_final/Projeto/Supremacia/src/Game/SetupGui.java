package Game;

import processing.core.PApplet;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import Rede.NetGames;
public class SetupGui extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton conectarButton;
    private JButton desconectarButton;
    private JButton iniciarPartidaButton;
    private JPanel mainPanel;
    private boolean jogadorConectado = false;
    private NetGames net;
	private Integer pos;
	Random rnd = new Random();
    
    public SetupGui(String title, NetGames net){
    	super(title);
    	this.net = net;
    	this.net.addObserver(this);
        mainPanel = new JPanel();
        new JPanel();
        conectarButton = new JButton("conectar");
        desconectarButton = new JButton("desconectar"); 
        iniciarPartidaButton = new JButton("iniciar"); 
        this.mainPanel.add(conectarButton);
        this.mainPanel.add(desconectarButton);
        this.mainPanel.add(iniciarPartidaButton);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setPreferredSize(new Dimension(1000,500));
        this.pack();
        
        conectarButton.addActionListener(e -> {
            this.conectar();
        });
        iniciarPartidaButton.addActionListener(e -> {
        	boolean iniciavel = iniciarPartida();
        	if (!iniciavel) {
        		JOptionPane.showMessageDialog(null,"Não foi possivel iniciar");
        	}
//        		String rodadas = JOptionPane.showInputDialog("Deseja configurar um numero de rodadas mÃ¡xima? Caso sim, digite o valor: ");
//                if (!rodadas.equals("")){
//                    tabuleiro.setMaxRodadas(Integer.parseInt(rodadas));
//                }
        	});
        desconectarButton.addActionListener(e ->{
        		this.desconectar();
        		});
    }
    public boolean iniciarPartida() {
    	boolean conectado = net.informarConectado();
		if(conectado) {
			net.iniciarPartida();
			return true;
		}
		JOptionPane.showMessageDialog(null,"Não conectado");
		return false;

    }

    public void desconectar() {
    	System.out.println("desconnect");
    	this.jogadorConectado = this.net.informarConectado();
    	if (!jogadorConectado){
    		JOptionPane.showMessageDialog(null,"Não conectado");
    	} else {
    		JOptionPane.showMessageDialog(null,this.net.desconectar());
    	}
    }
    public void conectar() {
    	this.jogadorConectado = this.net.informarConectado();
    	if (!jogadorConectado){
            String servidor = JOptionPane.showInputDialog("Informe o servidor: ");
            String name = JOptionPane.showInputDialog("Informe o seu nome: ");
    		
            String result = this.net.conectar(servidor, name);
            if (result.equals("Sucesso: conectado a Netgames Server")) {
            	JOptionPane.showMessageDialog(null,"Conectado");
            	this.jogadorConectado = true;
            	
            } else {
            	JOptionPane.showMessageDialog(null, result);
            }
            
        } else {
            JOptionPane.showMessageDialog(null,"Voce ja esta conectado");
        }
    }

    
	public void iniciarByNetGames(Integer posicao) {
		System.out.println("Tabuleiro init");
		 String[] appletArgs = new String[] { "Game.UsingProcessing" };
		 try {
			 PApplet.main(appletArgs);
		 } catch (Exception e) {
			 System.out.println(e);
		 }
		 this.pos = posicao;
		 
		
	}

	Integer getPos() {
		return this.pos;
	}
	 public String createRandomCode(){   
	     char[] chars = "awert".toCharArray();
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < 5; i++) {
	            char c = chars[rnd.nextInt(chars.length)];
	            sb.append(c);
	        }
	        String output = sb.toString();
	        System.out.println(output);
	        return output ;
	    } 
}
