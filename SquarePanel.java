import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;


//public class SquarePanel extends JPanel {
public class SquarePanel {

    private int steps;
    private int direction;
    private Color bgColor;
    private Color sColor;
    private boolean up = false;
    private boolean right = false;
    private boolean down = false;
    private boolean left = false;
    private int animationStep = 1;
    private boolean tail = false;
    private ImageIcon image;
  
   
    // normal grid squares
    public SquarePanel(Color c){
        bgColor = c;
        direction=-2;
    }
    
    // grid squares with an image
    public SquarePanel(Color c, ImageIcon i){
        bgColor = c;
        direction=-2;
        image = i; 
    }


    // methods
   
    public void resetVals(){
        direction = 0;
        bgColor = null;
        sColor = null;
        up = false;
        right = false;
        down = false;
        left = false;
        animationStep = 1;
        tail = false;
        image = null;
        steps = 10;
    }
        
    //head
    public void setHead(int d, int animStep, double snakeSpeed, Color c1, Color c2){
        resetVals();       
        direction = d;
        bgColor = c1;
        sColor = c2;
        animationStep = animStep; 
        setSteps(snakeSpeed);
    }

    // tail
    public void setTail(boolean u, boolean r, boolean d, boolean l, int animStep, double snakeSpeed, Color c1, Color c2, boolean isTail){
        resetVals();
        bgColor = c1;
        sColor = c2;
        animationStep = animStep;
        setSteps(snakeSpeed);
        tail = isTail;
        // tracking adjacent squares
        up = u;
        right = r;
        down = d;
        left = l;     
    }

    //body
    public void setBody(boolean u, boolean r, boolean d, boolean l, Color c1, Color c2){
        resetVals();
        bgColor = c1;
        sColor = c2;
        up = u;
        right = r;
        down = d;
        left = l;
        direction=-2;
    }

    // normal grid squares
    public void setEmpty(Color c){
        resetVals();
        bgColor = c;
        direction=-2;
    }
    
    // grid squares with an image
    public void setImage(Color c, ImageIcon i){
        resetVals();
        bgColor = c;
        direction=-2;
        image = i;
    }
    
    public void setSteps(int s){
        steps = s;
    }

    public void setSteps(double snakeSpeed){
        steps = (int)(GameVar.animStepSpeed * GameVar.timerDelay / snakeSpeed);
    }

    // (px,py) = pixel coords of upper left corner
    public void draw(Graphics g, int px, int py, int w, int h) {

        // background
        g.setColor(bgColor);
        g.fillRect(px, py, w, h);

        // image 
        if (image != null)
            g.drawImage(image.getImage(), px, py, null);

        // compute rectangle coords for snake segments
        int x1 = px + (int)(0.1 * w);
        int x2 = (int)(0.9 * w);
        int y1 = py + (int)(0.1 * h);
        int y2 = (int)(0.9 * h);
        int rectWidth  = (int)(0.8 * w);
        int rectHeight = (int)(0.8*h);

        g.setColor(sColor);

        // body segments (direction == -2)
        if (direction == -2) {
            if (down) g.fillRect(x1, y1, rectWidth, y2);
            if (left) g.fillRect(px, y1, x2, rectHeight);
            if (up) g.fillRect(x1, py, rectWidth, y2);
            if (right) g.fillRect(x1, y1, x2, rectHeight);
            return;
        }

        // animation step sizes
        int xStep = w / Math.max(steps,1);
        int yStep = h / Math.max(steps,1);

        // head
        if (!tail) {
            if (direction == -1) { // movement hasn't started yet
                g.fillRect(px, y1, xStep * 5, rectHeight);
            } else if (direction == 0) { // up
                g.fillRect(x1, py + h - (animationStep + 1) * yStep,
                        rectWidth, (animationStep + 1) * yStep);
            } else if (direction == 1) { // right
                g.fillRect(px, y1, (animationStep + 1) * xStep, rectHeight);
            } else if (direction == 2) { // down
                g.fillRect(x1, py, rectWidth, (animationStep + 1) * yStep);
            } else if (direction == 3) { // left
                g.fillRect(px + w - (animationStep + 1) * xStep, y1,
                        (animationStep + 1) * xStep, rectHeight);
            }
            return;
        }

        // tail
        if (tail) {
            if (up) {
                g.fillRect(x1, py, rectWidth, h - (animationStep + 1) * yStep);
            } else if (right) {
                g.fillRect(px + (animationStep + 1) * xStep, y1,
                        w - (animationStep + 1) * xStep, rectHeight);
            } else if (down) {
                g.fillRect(x1, py + (animationStep + 1) * yStep,
                        rectWidth, h - (animationStep + 1) * yStep);
            } else if (left) {
                g.fillRect(px, y1, w - (animationStep + 1) * xStep, rectHeight);
            }
        }
    }

}