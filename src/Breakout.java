import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;
import org.w3c.dom.css.RGBColor;

import java.awt.event.MouseEvent;
import java.awt.Color;
public class Breakout extends GraphicsProgram {

    private Ball ball;
    private Paddle paddle;
    private int numBricksInRow;
    private int lives = 5;
    private GLabel livesLabel = new GLabel("LIVES: "+lives);
    private int points = 0;
    private GLabel pointsLabel = new GLabel("POINTS: "+points);
    private int pointsIncrement = 10;
    private int tick = 0;
    private int ticksSincePowerUp = 0;
    @Override
    public void init(){
        numBricksInRow = (int) (getWidth()/(Brick.WIDTH+5.0));
        add(livesLabel,10,15);
        add(pointsLabel,10,30);
        for(int row = 0; row < 10; row++){
            for(int col = 0; col < numBricksInRow; col++){
                int r = (int) (255-row*25.5);
                int g = (int) (row*56.6666666);
                if(g>255){
                    g=g-255;
                }
                int b = (int) ((row-4.5)*51);
                if(b<0){
                    b=0;
                }
                add(new Brick(10+col*(Brick.WIDTH+5),4*Brick.HEIGHT+row*(Brick.HEIGHT+5), new Color(r,g,b),row,RandomGenerator.getInstance().nextInt(1,100-row)));
            }
        }
        ball = new Ball(getWidth()/2, 350, 10, this.getGCanvas());
        add(ball);

        paddle = new Paddle(230, 430, 50 ,10);
        add(paddle);
    }

    @Override
    public void run(){
        addMouseListeners();
        waitForClick();
        gameLoop();
    }

    @Override
    public void mouseMoved(MouseEvent me){
        // make sure that the paddle doesn't go offscreen
        if((me.getX() < getWidth() - paddle.getWidth()/2)&&(me.getX() > paddle.getWidth() / 2)){
            paddle.setLocation(me.getX() - paddle.getWidth()/2, paddle.getY());
        }
    }

    private void gameLoop(){
        while(true){
            // move the ball
            ball.handleMove();

            // handle collisions
            handleCollisions();

            // handle losing the ball
            if(ball.lost){
                handleLoss();
            }
            tick +=1;
            ticksSincePowerUp+=1;
            if(ticksSincePowerUp > 500){
                ball.setSize(10,10);
                paddle.setSize(50,10);
                ball.floor=false;
                ticksSincePowerUp=0;
                pointsIncrement=10;
            }
            pause(5);
        }
    }

    private void handleCollisions(){
        // obj can store what we hit
        GObject obj = null;

        // check to see if the ball is about to hit something

        if(obj == null){
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY());
        }
        if(obj == null){
            obj = this.getElementAt(ball.getX(), ball.getY());
        }
        if(obj == null){
            obj = this.getElementAt(ball.getX(), ball.getY()+ball.getHeight());
        }
        if(obj == null){
            obj = this.getElementAt(ball.getX()+ball.getWidth(), ball.getY()+ball.getHeight());
        }
        if(obj != null){
            if(obj instanceof Paddle) {
                if (ball.getY() + ball.getHeight() <= paddle.getY() + paddle.getHeight()) {
                    if (ball.getX() + ball.getWidth() < paddle.getX() + (paddle.getWidth() / 4)) {
                        ball.setDeltaY(1);
                        ball.setDeltaX(ball.getDeltaX() - 1);
                    } else if (ball.getX() > paddle.getX() + paddle.getWidth() * 3 / 4) {
                        ball.setDeltaY(1);
                        ball.setDeltaX(ball.getDeltaX() + 1);
                    } else {
                        ball.setDeltaY(1);
                    }
                }
            }
            if(obj instanceof Brick){
                points += pointsIncrement;
                pointsLabel.setLabel("POINTS: "+points);
                if((ball.getX()+ball.getWidth()>=obj.getX()&&ball.getX()<=obj.getX()+obj.getWidth()&&ball.getDeltaY()<=0&&ball.getY()+ball.getHeight()>=obj.getY()&&ball.getY()<=obj.getY()||(ball.getX()+ball.getWidth()>=obj.getX()&&ball.getX()<=obj.getX()+obj.getWidth()&&ball.getDeltaY()>=0&&ball.getY()<=obj.getHeight()+obj.getY()&&ball.getY()+ball.getHeight()>=obj.getY()+obj.getHeight()))){
                    ball.setDeltaY(ball.getDeltaY()*-1);
                } else {
                    ball.setDeltaX(ball.getDeltaX()*-1);
                }
                Brick brick = (Brick) obj;
                brick.hit();
                if(brick.lives<=0) {
                    this.remove(obj);
                }
                if(brick.powerUp==2){
                    ball.setSize(20,20);
                    ticksSincePowerUp=0;
                } else if (brick.powerUp==3){
                    paddle.setSize(paddle.getWidth()*2, paddle.getHeight());
                    ticksSincePowerUp=0;
                } else if (brick.powerUp==4){
                    for(int i=0;i<10;i++){
                        int x = (int) (brick.getX()+brick.getWidth()/2+RandomGenerator.getInstance().nextInt(0,200)-100);
                        int y = (int) (brick.getY()+brick.getHeight()/2+RandomGenerator.getInstance().nextInt(0,200)-100);
                        GObject b = this.getElementAt(x,y);
                        if(b instanceof Brick){
                            Brick c = (Brick) b;
                            for(int j = 0; j < RandomGenerator.getInstance().nextInt(1,10);j++) {
                                c.hit();
                            }
                        }
                    }
                } else if (brick.powerUp==5){
                    ball.floor = true;
                    ticksSincePowerUp=0;
                } else if (brick.powerUp==6){
                    ticksSincePowerUp=0;
                    pointsIncrement=25;
                }
            }
        }
    }

    private void handleLoss(){
        ball.lost = false;
        lives -= 1;
        livesLabel.setLabel("LIVES: "+lives);
        if(lives <= 0){
            System.exit(0);
        }
        reset();
    }

    private void reset(){
        ball.setLocation(getWidth()/2, 350);
        ball.setDeltaX(1);
        paddle.setLocation(230, 430);
        waitForClick();
    }

    public static void main(String[] args) {
        new Breakout().start();
    }

}