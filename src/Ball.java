import acm.graphics.GCanvas;
import acm.graphics.GOval;

public class Ball extends GOval{
    private double deltaX = 1;
    private double deltaY = 1;
    private GCanvas screen;
    public boolean lost = false;
    public boolean floor = false;
    public Ball(double x, double y, double size, GCanvas screen){
        super(x, y, size, size);
        this.screen = screen;
        setFilled(true);
    }
    public void handleMove(){
        //move the ball
        pause(5);
        move(deltaX,-deltaY);
        //check if ball hits top
        if(getY()<=0){
            deltaY *= -1;
        }
        //check if ball hits bottom
        if(getY()>=screen.getHeight()-getHeight()){
            deltaY *= -1;
            if(!floor) {
                lost = true;
            }
        }
        //check if ball hits right
        if(getX()>=screen.getWidth()-getWidth()){
            deltaX*=-1;
            if(deltaX>=0){
                deltaX*=-1;
            }
        }
        // check if ball hits left
        if(getX()<=0){
            deltaX*=-1;
            if(deltaX<=0){
                deltaX*=-1;
            }
        }
    }
    public void setDeltaX(double x){
        deltaX = x;
        if(deltaX>5){
            deltaX=5;
        } else if(deltaX<-5){
            deltaX=-5;
        }
    }
    public void setDeltaY(double y){
        deltaY = y;
        if ((deltaY > 5)) {
            deltaY=5;
        } else if (deltaY<-5){
            deltaY=-5;
        }
    }
    public double getDeltaX(){
        return deltaX;
    }
    public double getDeltaY(){
        return deltaY;
    }
}
