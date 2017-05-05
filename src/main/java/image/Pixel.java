package image;

import java.awt.*;

/**
 * Created by Egor Nemchinov on 03.05.17.
 *
 * @Link github.com/ImmortalTurtle
 * SPbU, 2017
 */
public class Pixel {
    private int r,g,b;
    private float a;

    public Pixel(int r, int g, int b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color getColor() {
        return new Color(r, g, b);
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }
}
