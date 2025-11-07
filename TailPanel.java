
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TailPanel extends JPanel{


private int width;
    private int height;
    private int direction;
    private int delay;
    private Color bgColor;
    private Color sColor;
    private boolean up = false;
    private boolean right = false;
    private boolean down = false;
    private boolean left = false;
    private Timer t;
    private int animationStep = 1;
    private boolean tail = false;
    private ImageIcon image;

    //tail
    public TailPanel(int w, int h, boolean u, boolean r, boolean d, boolean l, int e, Color c1, Color c2, boolean ta, int p){
        width = w;
        height = h;
        bgColor = c1;
        sColor = c2;
         
        // tracking direction
        up = u;
        right = r;
        down = d;
        left = l; 
         
        // animation stuff       
        delay = e/5;
        tail = ta;
        animationStep = p;
        
        t = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {

                repaint(); // update the drawing
                //System.out.println("animationStep within SP class: " + animationStep);
                animationStep++;
                
 
                if (animationStep > 5) { // reset the animation once done
                       animationStep=1;
                       t.stop();
                }     
            }   
        });
        
        t.start();
    }
        

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
       
        // filling square with color
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);
        
        // drawing an image into the middle of the panel
        if (image != null){
            g.drawImage(image.getImage(), (width-image.getImage().getWidth(this))/2, (height-image.getImage().getHeight(this))/2, this);
        
        }
        

        // drawing a border
       // g.setColor(Color.BLACK);
       // g.drawRect(0, 0, width - 1, height - 1);
        
        int x1 = (int)(0.1 * width);
        int x2 = (int)(0.9 * width);
        int y1 = (int)(0.1 * height);
        int y2 = (int)(0.9 * height);
        int rectWidth = (int)(0.8*width);
        int rectHeight = (int)(0.8*height);
        g.setColor(sColor);

        int x = (int)width/5;
        int y = (int)height/5;
        // rendering tail
        
        if (up){
        
            g.setColor(sColor);
            g.fillRect(x1,0,rectWidth,height-y*animationStep);
            
        } else if (right){
        
            g.setColor(sColor);
            g.fillRect(x*animationStep,y1,width-x*animationStep,rectHeight);
        
        } else if (down){
        
            g.setColor(sColor);
            g.fillRect(x1, y*animationStep, rectWidth, height-y*animationStep);

        } else if (left){
        
            g.setColor(sColor);
            g.fillRect(0,y1,width-x*animationStep,rectHeight);
        
        }

    
    }                   
            

    

    
}


