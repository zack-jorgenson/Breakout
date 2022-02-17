import acm.graphics.GRect;
import java.awt.Color;
public class Brick extends GRect {
    public static final int WIDTH = 44;
    public static final int HEIGHT = 20;
    public int lives;
    public int powerUp;
    public Brick(double x, double y, Color color,int row, int powerUp){
        super(x,y,WIDTH,HEIGHT);
        this.powerUp = powerUp;
        lives = (12-row)/2;
        this.setFillColor(color);
        if(powerUp>=2&&powerUp<=13){
            lives = 1;
            this.setFillColor(Color.white);
        }
        this.setFilled(true);
    }
    public void hit(){
        lives -= 1;
        int r = this.getFillColor().getRed()/2;
        int g = this.getFillColor().getGreen()/2;
        int b = this.getFillColor().getBlue()/2;
        this.setFillColor(new Color(r,g,b));
    }
}
