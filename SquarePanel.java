import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;



class SquarePanel extends JPanel {

    private int steps;


    private int width;
    private int height;
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
    public SquarePanel(int w, int h, Color c){
        width = w;
        height = h;
        bgColor = c;
        setPreferredSize(new Dimension(width, height));
        direction=-2;
    }
    
    // grid squares with an image
    public SquarePanel(int w, int h, Color c, ImageIcon i){
    
        width = w;
        height = h;
        bgColor = c;
        setPreferredSize(new Dimension(width, height));
        direction=-2;
        image = i;
        
    
    }


    public void resetVals(){
        width = 0;
        height = 0;
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
    /*public SquarePanel(int w, int h, int d, int animStep, Color c1, Color c2){
        resetVals();
        width = w;
        height = h;
        direction = d;
        bgColor = c1;
        sColor = c2;
        animationStep = animStep;        
    
    }
    
    
  
    //tail
    public SquarePanel(int w, int h, boolean u, boolean r, boolean d, boolean l, int animStep, Color c1, Color c2, boolean isTail){
        resetVals();
        width = w;
        height = h;
        bgColor = c1;
        sColor = c2;
         
        // tracking adjacent squares
        up = u;
        right = r;
        down = d;
        left = l; 
         
        animationStep = animStep;

        tail = isTail;        
    
    }
    

    // body segments
    public SquarePanel(int w, int h, boolean u, boolean r, boolean d, boolean l, Color c1, Color c2){
        resetVals();
        width = w;
        height = h;
        bgColor = c1;
        sColor = c2;
        up = u;
        right = r;
        down = d;
        left = l;
        direction=-2;
        setPreferredSize(new Dimension(width, height));
    
    }*/


    // methods
    
    //head
    public void setHead(int w, int h, int d, int animStep, double snakeSpeed, Color c1, Color c2){
        resetVals();

        width = w;
        height = h;
        direction = d;
        bgColor = c1;
        sColor = c2;
        animationStep = animStep; 
        setSteps(snakeSpeed);
    
    }

    // tail
    public void setTail(int w, int h, boolean u, boolean r, boolean d, boolean l, int animStep, double snakeSpeed, Color c1, Color c2, boolean isTail){
        resetVals();
        width = w;
        height = h;
        bgColor = c1;
        sColor = c2;
         
        // tracking adjacent squares
        up = u;
        right = r;
        down = d;
        left = l; 
         
        animationStep = animStep;
        setSteps(snakeSpeed);


        tail = isTail;        
    
    }
    //body
    public void setBody(int w, int h, boolean u, boolean r, boolean d, boolean l, Color c1, Color c2){
        resetVals();
        width = w;
        height = h;
        bgColor = c1;
        sColor = c2;
        up = u;
        right = r;
        down = d;
        left = l;
        direction=-2;
        setPreferredSize(new Dimension(width, height));
    }



    // normal grid squares
    public void setEmpty(int w, int h, Color c){
        resetVals();
        width = w;
        height = h;
        bgColor = c;
        setPreferredSize(new Dimension(width, height));
        direction=-2;
    }
    
    // grid squares with an image
    public void setImage(int w, int h, Color c, ImageIcon i){
        resetVals();
   
        width = w;
        height = h;
        bgColor = c;
        setPreferredSize(new Dimension(width, height));
        direction=-2;
        image = i;

        
    
    }
    
    public void setSteps(int s){
        steps = s;
    }

    public void setSteps(double snakeSpeed){
        steps = (int)(GameVar.animStepSpeed * GameVar.timerDelay / snakeSpeed);

    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        //long start = System.nanoTime();

        // filling square with color
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);
        
        // drawing an image into the middle of the panel
        if (image != null){
            g.drawImage(image.getImage(), (width-image.getImage().getWidth(this))/2, (height-image.getImage().getHeight(this))/2, this);
        
        }

        // drawing a border
       // g.setColor(new Color(75, 150, 100));
        //g.drawRect(0, 0, width - 1, height - 1);
        
        
        
        int x1 = (int)(0.1 * width);
        int x2 = (int)(0.9 * width);
        int y1 = (int)(0.1 * height);
        int y2 = (int)(0.9 * height);
        int rectWidth = (int)(0.8*width);
        int rectHeight = (int)(0.8*height);
        g.setColor(sColor);

        
        if (direction ==-2){ //square has body (doesn't contain snake head or tail)
                                  
            if (up){
                g.fillRect(x1, 0, rectWidth, y2);        
            }
            if (right){
                g.fillRect(x1,y1,x2,rectHeight);
            }
            if (down){
                g.fillRect(x1,y1,rectWidth,y2);
            }
            if (left){
                g.fillRect(0,y1,x2,rectHeight);
            }
                       
           
        } else {
        
            int x = (int)width/steps;
            int y = (int)height/steps;
              
            if (!tail){ // rendering head
            
                if (direction==-1){ // movement hasn't started yet
                        
                    // just head with eyes - no animations
                    g.setColor(sColor);
                    g.fillRect(0,y1,x*5,rectHeight);
                    g.setColor(new Color(255,255,255));
                    /*
                    g.fillRect(x*5-rectWidth/4-x/3,2*y1, x, rectHeight/4);
                    g.fillRect(x*5-rectWidth/4-x/3, 6*y1, x, rectHeight/4);
                    g.setColor(new Color (0,0,0));
                    g.fillOval(x*5-rectHeight/4, 3*y1, rectHeight/7, rectHeight/7);
                    g.fillOval(x*5-rectHeight/4+x/3, 7*y1, rectHeight/7, rectHeight/7);*/
                   
    
                } else if (direction == 0){ // up
       
                    g.setColor(sColor);
                    g.fillRect(x1, height-(animationStep+1)*y, rectWidth, (animationStep + 1)*y);
                    
                    /*// eyes
                    g.setColor(new Color(255,255,255));
                    g.fillRect(2*x1, height-y*animationStep+y/3, rectWidth/4, y);
                    g.fillRect(6*x1, height-y*animationStep+y/3, rectWidth/4, y);
                    g.setColor(new Color (0,0,0));
                    g.fillOval(3*x1, height-y*animationStep+y/3, rectWidth/7, rectWidth/7);
                    g.fillOval(7*x1, height-y*animationStep, rectWidth/7, rectWidth/7);
                */
            
                    
                     
                } else if (direction ==1){ // right
             
                     g.setColor(sColor);
                     g.fillRect(0,y1,(animationStep + 1)*x,rectHeight);
                     /*
                     g.setColor(new Color(255,255,255));
                     g.fillRect(x*animationStep-rectWidth/4-x/3,2*y1, x, rectHeight/4);
                     g.fillRect(x*animationStep-rectWidth/4-x/3, 6*y1, x, rectHeight/4);
                     g.setColor(new Color (0,0,0));
                     g.fillOval(x*animationStep-rectHeight/4, 3*y1, rectHeight/7, rectHeight/7);
                     g.fillOval(x*animationStep-rectHeight/4+x/3, 7*y1, rectHeight/7, rectHeight/7);*/
           
                 
                } else if (direction ==2){ // down
                     g.setColor(sColor);
                     g.fillRect(x1,0,rectWidth,(animationStep + 1)*y);
                     /*
                     g.setColor(new Color (255,255,255));
                     g.fillRect(2*x1, y*animationStep-rectHeight/4-y/3, rectWidth/4, y);
                     g.fillRect(6*x1, y*animationStep-rectHeight/4-y/3, rectWidth/4, y);
                     g.setColor(new Color (0,0,0));
                     g.fillOval(3*x1, y*animationStep-y+y/3, rectWidth/7, rectWidth/7);
                     g.fillOval(7*x1, y*animationStep-y, rectWidth/7, rectWidth/7);*/
                 
                 
                } else if (direction ==3){ // left
                     g.setColor(sColor);
                     g.fillRect(width-(animationStep + 1)*x,y1,(animationStep + 1)*x,rectHeight);
                     /*                     
                     g.setColor(new Color(255,255,255));
                     g.fillRect(width-x*animationStep+x/3,2*y1, x, rectHeight/4);
                     g.fillRect(width-x*animationStep+x/3, 6*y1, x, rectHeight/4);
                     g.setColor(new Color (0,0,0));
                     g.fillOval(width-x*animationStep, 3*y1, rectHeight/7, rectHeight/7);
                     g.fillOval(width-x*animationStep+x/3, 7*y1, rectHeight/7, rectHeight/7);*/
                     
                }
                    
                    
            } else { // rendering tail
            
                if (up){
                
                    g.setColor(sColor);
                    g.fillRect(x1,0,rectWidth,height-(animationStep + 1)*y);
                    
                } else if (right){
                
                    g.setColor(sColor);
                    g.fillRect((animationStep + 1)*x,y1,width-(animationStep + 1)*x,rectHeight);
                
                } else if (down){
                
                    g.setColor(sColor);
                    g.fillRect(x1, (animationStep + 1)*y, rectWidth, height-(animationStep + 1)*y);

                } else if (left){
                
                    g.setColor(sColor);
                    g.fillRect(0,y1,width-(animationStep + 1)*x,rectHeight);
                
                }

            
            }                   
                

        }
        //long end = System.nanoTime();
        //System.out.println("paintComponent took " + ((end - start)/1_000_000.0) + " ms");
   
        
    }
    
    

}