package Game;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class Botao {
    private final PApplet p;
    private PVector pos;
    private int width, height;
    private String name;
    private int color = 255;

    public Botao(PApplet p, String name, PVector pos, int width, int height){
        this.p = p;
        this.name = name;
        this.pos = pos;
        this.width = width;
        this.height = height;
    }
    public boolean isPointInside(int x, int y) {
        return x > this.pos.x - this.width/2 && x < this.pos.x + this.width/2 &&
                y > this.pos.y - this.height/2 && y < this.pos.y + this.height/2;
    }

    public void show() {
        p.fill(color);
        p.rectMode(PConstants.CENTER);
        p.rect(this.pos.x, this.pos.y, this.width, this.height);
        p.fill(0, 0, 0);
        p.textAlign(PConstants.CENTER, PConstants.BOTTOM);
        p.text(this.name, this.pos.x, this.pos.y);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
