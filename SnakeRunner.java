import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.sound.sampled.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;


public class SnakeRunner {


    // JFrame attributes
    private static JFrame frame;
    private static JPanel panelHome;
    private static JPanel panelPlay;
    private static JPanel panelLost;
    private static JPanel panelScore;
    private static JPanel panelEffects;
    private static JButton buttonPlay;
    private static JButton buttonReplay;
    private static JButton buttonMusic;
    private static JButton buttonKeySounds;
    private static JButton buttonClose;
    //private static JButton buttonSave;
    //private static JButton buttonLoad;
    private static JLabel player1ScoreLabel;
    private static JLabel player2ScoreLabel;
    private static JLabel effectsLabel;
    private static JLabel boostLabel;
    private static JLabel scoreMultiplierLabel;
    private static int cellWidth;
    private static int cellHeight;
    
    // images
    private static ImageIcon playGame; // start game
    private static ImageIcon resetGame; // resets the game
    private static ImageIcon loadSave; // loads the previous savefile
    private static ImageIcon closeWindow; // closes window
    private static ImageIcon boostExplanation; // explains boosts
    private static ImageIcon f; // fruit 
    private static ImageIcon next; // next boost 
    private static ImageIcon b0; // reset stats
    private static ImageIcon b1; // speed
    private static ImageIcon X2; // x2 fruit multiplier
    private static ImageIcon b2; // slowness
    private static ImageIcon b3; // invisibility
    private static ImageIcon b4; // direction switch
    private static ImageIcon X5; // x5 fruit multiplier
    private static ImageIcon yesMusic; // bg music is on
    private static ImageIcon noMusic; // bg music is off
    private static ImageIcon keySoundsOn; // keyboard sounds on
    private static ImageIcon keySoundsOff; // keyboard sounds off
    
    // sounds
    private static Clip buttonClick;
    private static Clip boostObtained;
    private static Clip boostSpawned;
    private static Clip snakeDeath;
    private static Clip fruitEaten;
    private static Clip bgMusic; 
    private static Clip keyPress;
    private static boolean playMusic;
    private static boolean playKeySounds;


    //private static final String SAVE_FILE = "snake_game_save.dat";

    private static boolean gameStarted;
    private static Timer gameTimer;
    private static double snakeSpeed; // ticks / second
    private static final int targetFPS = 60;
    private static final long frameTime = 1000 / targetFPS; // ~16 ms per frame

    // game logic
    private static final int gridX = 15;
    private static final int gridY = 15;


    private static Point fruit;

    private static int[] boostArr; // 3 x 1, index 0 = speed, index 1 = invis, index 2 = direction switch
    private static final int boostSpawnChance = 100; 
            

    private static volatile boolean cont;

    private static volatile GridSquare[][] grid;
    public static SquarePanel[][] gridPanels;

     
    private static final int winBonus = 20;
    
    // graphics logic
    private static final Dimension panelDimensions = new Dimension(600,50);
    private static final Dimension smallButtonDimensions = new Dimension(50,50);
    private static final Dimension frameDimensions = new Dimension(600,700);
    private static final Color defaultSnake1 = new Color(30,100,135);
    private static final Color defaultSnake2 = new Color(155,22,80);
    private static final Color lightSquare = new Color(218, 247, 197);
    private static final Color darkSquare = new Color(194, 230, 170);

    private static int steps; // animation steps


    private static Snake snake1;
    private static Snake snake2;


/*
    public static void saveGame(GridSquare[][] grid, Snake snake1, Snake snake2) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(grid);
            out.writeObject(snake1);
            out.writeObject(snake2);
            System.out.println("Game saved successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadGame() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists()){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
                
                grid = (GridSquare[][]) in.readObject();

                snake1 = (Snake) in.readObject();
                snake2 = (Snake) in.readObject();

                gameStarted = false;


                cont = true;
                System.out.println("gridX: " + grid.length);
                initFrame();
                
                System.out.println("Game loaded successfully.");

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } //finally{
               // System.out.println("Attempted to load a previous save.");
            //}
        } else {
            System.out.println("No save file found");
        }
    }
*/
    public static void resetVariables(){
        gameStarted = GameVar.gameStarted;
        boostArr = new int[3];
        snakeSpeed = GameVar.snakeSpeed;
        steps = (int)(GameVar.animStepSpeed*GameVar.timerDelay/snakeSpeed);
        //boostArr[1] = 1; //debug statements
        //boostArr[2] = 1;
        fruit=new Point((int)(gridX/2),(int)(gridY/2));

    }

    // the game can only be initialized by classes in the same package or subclass
    protected static void initialize(){

        snakeSpeed = GameVar.snakeSpeed;

        resetVariables();
        
        playMusic = true;
        playKeySounds = true;
        loadSounds();

        // the first sound in the game never plays correctly, so the code plays a buffer sound beforehand
        buttonClick.setFramePosition(0);
        buttonClick.start();

        
        // creating frame
        frame = new JFrame();
        frame.setTitle("Not a snake ripoff");
        frame.setSize(frameDimensions);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // custom close action
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                
                buttonClick.setFramePosition(0);
                buttonClick.start();
                
                // exit
                System.exit(0);
            }
        });


        // background music
        bgMusic.setFramePosition(0);
        bgMusic.start();
        bgMusic.loop(Clip.LOOP_CONTINUOUSLY);


        // listens for directional inputs
        frame.addKeyListener(new KeyListener() {
           
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) { // key inputs : w/a/s/d for player 1, arrow keys for player 2
                
                if (!cont && e.getKeyCode() == KeyEvent.VK_ESCAPE){ // either on the menu screen or the lose screen
                    new Thread(() -> {
                        buttonClick.setFramePosition(0);
                        buttonClick.start();
                    }).start();
                    System.exit(0);
                }

                if(!cont && !gameStarted && e.getKeyCode() == KeyEvent.VK_SPACE){ // on the home screen: starts the game

                    new Thread(() -> {
                        buttonClick.setFramePosition(0);
                        buttonClick.start();
                    }).start();

                    snake1 = new Snake(1,5,defaultSnake1,Player.PLAYER_1);
                    snake2 = new Snake(1, 10, defaultSnake2,Player.PLAYER_2);       
                    

                    // creating the grid
                    grid = new GridSquare[gridY][gridX];
                    for (int y = 0; y < gridY; y++){
                        for (int x=0; x<gridX;x++){
                            grid[y][x]=new GridSquare(x,y);
                        }
                    }

                    grid[snake1.getStartY()][snake1.getStartX()].setOccupyingSnake(snake1);
                    grid[snake2.getStartY()][snake2.getStartX()].setOccupyingSnake(snake2);

                    cont=true;

                    initGridPanels();
                    renderGrid();
                    initFrame();
                }
                
                if (!gameStarted && cont){ // start the timer
                    System.out.println("timer is starting. run game.");
                    gameStarted = true;

                    /*Thread gameThread = new Thread(() -> {
                        final int frameTime = 16; // target ~60 FPS (adjust as needed)

                        while (cont) {
                            long start = System.nanoTime();

                            runGame(); // handles both logic + grid update

                            long elapsed = (System.nanoTime() - start) / 1000000;
                            long sleep = Math.max(0, frameTime - elapsed);
                            try {
                                Thread.sleep(sleep);
                            } catch (InterruptedException ex) {
                                break;
                            }
                        }

                        
                    });

                    gameThread.setDaemon(true); // optional, but good practice
                    gameThread.start();*/




                    
                    // adjust timer delay for speed/slowness boosts
                    gameTimer = new Timer(GameVar.timerDelay, new ActionListener(){

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            runGame();

                        }
                    });
                    gameTimer.setInitialDelay(0);
                    gameTimer.start();
                }
                if (gameStarted){
                    int direction1 = -1;
                    int direction2 = -1;
                    switch(e.getKeyCode()){
                        case KeyEvent.VK_SPACE:
                            if (!cont){ // on the loseScreen
                                System.out.println("Spacebar pressed. Reset game.");
                                new Thread(() -> {
                                    buttonClick.setFramePosition(0);
                                    buttonClick.start();
                                }).start();
                                resetGame();
                            }
                            break;
                        
                        case KeyEvent.VK_W: if (cont && snake1.getSnakeMoved() && snake1.getDirection()!=0 && snake1.getDirection()!=2){
                            if (boostArr[2] == 1){
                                direction1 = 2;    
                            } else {direction1=0;}
                            snake1.setDirection(direction1);
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            
                            snake1.setSnakeMoved(false);
                            //System.out.println("up");                
                            break;
                        }
                        break;
                        case KeyEvent.VK_D: if (cont && snake1.getSnakeMoved() && snake1.getDirection()!=1 && snake1.getDirection()!=3){
                            if (boostArr[2] == 1){
                                direction1 = 3;    
                            } else {direction1=1;}
                            
                            snake1.setDirection(direction1);
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake1.setSnakeMoved(false);

                        

                            break;
                        }
                        break;
                        case KeyEvent.VK_S: if (cont && snake1.getSnakeMoved() && snake1.getDirection()!=0 && snake1.getDirection()!=2){
                            if (boostArr[2] == 1){
                                direction1 = 0;    
                            } else {direction1=2;}
                            
                            snake1.setDirection(direction1);
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake1.setSnakeMoved(false);

                        
                            
                            break;
                        }
                        break;
                        case KeyEvent.VK_A: if (cont && snake1.getSnakeMoved() && snake1.getDirection()!=1 && snake1.getDirection()!=3){
                            if (boostArr[2] == 1){
                                direction1 = 1;    
                            } else {direction1=3;}
                            
                            snake1.setDirection(direction1);
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake1.setSnakeMoved(false);
                        
                            break;
                        }
                        break;

                        case KeyEvent.VK_UP: if (cont && snake2.getSnakeMoved() && snake2.getDirection()!=0 && snake2.getDirection()!=2){
                            if (boostArr[2] == 1){
                                direction2 = 2;    
                            } else {direction2=0;}
                            snake2.setDirection(direction2);

                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake2.setSnakeMoved(false);
                        
                            break;
                            
                        }
                        break;
                        case KeyEvent.VK_RIGHT: if (cont && snake2.getSnakeMoved() && snake2.getDirection()!=1 && snake2.getDirection()!=3){
                            if (boostArr[2] == 1){
                                direction2 = 3;    
                            } else {direction2=1;}

                            snake2.setDirection(direction2);
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake2.setSnakeMoved(false);

                            break;
                        }
                        break;
                        case KeyEvent.VK_DOWN: if (cont && snake2.getSnakeMoved() && snake2.getDirection()!=0 && snake2.getDirection()!=2){
                            if (boostArr[2] == 1){
                                direction2 = 0;    
                            } else {direction2=2;}
                            
                            snake2.setDirection(direction2);
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake2.setSnakeMoved(false);
                        
                            
                            break;
                        }
                        break;
                        case KeyEvent.VK_LEFT: if (cont && snake2.getSnakeMoved() && snake2.getDirection()!=1 && snake2.getDirection()!=3){
                            if (boostArr[2] == 1){
                                direction2 = 1;    
                            } else {direction2=3;}

                            snake2.setDirection(direction2);                      
                            
                            
                            if (playKeySounds){
                                new Thread(() -> {
                                    keyPress.setFramePosition(0);
                                    keyPress.start();
                                }).start();
                            }
                            snake2.setSnakeMoved(false);   
                            break;
                        }
                        break;
                    }
                }
 
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });


        // creating the images
        loadImages();

        // creating panelHome
        panelHome = new JPanel();
        buttonPlay = new JButton(playGame);
        buttonPlay.setPreferredSize(new Dimension(100,100));
        buttonPlay.setFocusable(false);
        
        // when buttonPlay is pressed, set cont to true and call render()
        buttonPlay.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
               
                buttonClick.setFramePosition(0);
                buttonClick.start();

                snake1 = new Snake(1,5,defaultSnake1,Player.PLAYER_1);
                snake2 = new Snake(1, 10, defaultSnake2,Player.PLAYER_2);       
                

                // creating the grid
                grid = new GridSquare[gridY][gridX];
                for (int y = 0; y < gridY; y++){
                    for (int x=0; x<gridX;x++){
                        grid[y][x]=new GridSquare(x,y);
                    }
                }

                grid[snake1.getStartY()][snake1.getStartX()].setOccupyingSnake(snake1);
                grid[snake2.getStartY()][snake2.getStartX()].setOccupyingSnake(snake2);

                cont=true;

                initGridPanels();
                renderGrid();
                initFrame();
               

            }
        });

         // background music toggle
         buttonMusic = new JButton(yesMusic);
         buttonMusic.setPreferredSize(new Dimension(100,100));
         buttonMusic.setFocusable(false);
         
         buttonMusic.addActionListener(new ActionListener(){
             @Override
             public void actionPerformed(ActionEvent e){
             
                 buttonClick.setFramePosition(0);
                 buttonClick.start();
                
                 if (playMusic){
                     playMusic = false;
                     buttonMusic.setIcon(noMusic);
                     bgMusic.stop();
                 
                 } else {
                     
                     playMusic = true;
                     buttonMusic.setIcon(yesMusic);
                     bgMusic.setFramePosition(0);
                     bgMusic.start();
                     bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
                 
                 }
             }
         });
         
         // keyboard sounds toggle
         buttonKeySounds = new JButton(keySoundsOn);
         buttonKeySounds.setPreferredSize(new Dimension(100,100));
         buttonKeySounds.setFocusable(false);
         
         buttonKeySounds.addActionListener(new ActionListener(){
             @Override
             public void actionPerformed(ActionEvent e){
             
                 buttonClick.setFramePosition(0);
                 buttonClick.start();
                
                 if (playKeySounds){
                     playKeySounds = false;
                     buttonKeySounds.setIcon(keySoundsOff);
                 
                 } else {
                     
                     playKeySounds = true;
                     buttonKeySounds.setIcon(keySoundsOn);
                                     
                 }
             }
         });

         // close button
        buttonClose = new JButton(closeWindow);
        buttonClose.setPreferredSize(new Dimension(100,100));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                buttonClick.setFramePosition(0);
                buttonClick.start();
                System.exit(0);
            }
        });

        /*// load previous game button
        buttonLoad = new JButton(loadSave);
        buttonLoad.setPreferredSize(new Dimension(100,100));
        buttonLoad.setFocusable(false);
        buttonLoad.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                buttonClick.setFramePosition(0);
                buttonClick.start();
                loadGame();

                initGridPanels();
                renderGrid();
                initFrame();

            }
        });

        // button to save the game state
        buttonSave = new JButton("Save and exit");
        buttonSave.setPreferredSize(new Dimension(100,100));
        buttonSave.setFocusable(false);
        buttonSave.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                buttonClick.setFramePosition(0);
                buttonClick.start();
                saveGame(grid, snake1, snake2);
                System.exit(0);

            }
        });*/

        // replay button
        buttonReplay = new JButton(resetGame);
        buttonReplay.setPreferredSize(smallButtonDimensions);
        buttonReplay.setFocusable(false);
        buttonReplay.addActionListener(e -> {
            
            buttonClick.setFramePosition(0);
            buttonClick.start();
            resetGame();
            
        });

        frame.add(panelHome,BorderLayout.NORTH);
        frame.add(new JLabel(boostExplanation)); // adds boost explanation image

        panelHome.add(buttonPlay);
        panelHome.add(buttonMusic);
        panelHome.add(buttonKeySounds);
        panelHome.add(buttonClose);
        //panelHome.add(buttonLoad);

        frame.setVisible(true);

        createPanels();

    }

    // initializes all the JPanels
    public static void createPanels(){

        // creating panelLost
        panelLost = new JPanel();
        panelLost.setLayout(new GridLayout(1, 4)); // 4 columns: Replay, toggle music, toggle key sounds, exit buttons
        panelLost.setPreferredSize(panelDimensions);

        // creating panelScore
        panelScore = new JPanel();
        panelScore.setLayout(new GridLayout(1,2));
        panelScore.setPreferredSize(panelDimensions);
        player1ScoreLabel = new JLabel();
        player2ScoreLabel = new JLabel();
        panelScore.add(player1ScoreLabel);
        panelScore.add(player2ScoreLabel);

        
        // creating panelEffects
        panelEffects = new JPanel();
        panelEffects.setLayout(new GridLayout(1,3));
        panelEffects.setPreferredSize(panelDimensions);
        //panelEffects.add(buttonSave);
        effectsLabel = new JLabel("    EFFECTS: ");
        panelEffects.add(effectsLabel);


        // creating panelPlay
        panelPlay = new JPanel();
        

    }

    // initializes GridPanels to keep track of SquarePanel graphics
    public static void initGridPanels() {
        gridPanels = new SquarePanel[gridY][gridX];
        panelPlay.removeAll();
        panelPlay.setLayout(new GridLayout(gridY, gridX));

        for (int y = 0; y < gridY; y++) {
            for (int x = 0; x < gridX; x++) {
                Color bgColor = (x % 2 == y % 2) ? darkSquare : lightSquare;
                SquarePanel square = new SquarePanel(cellWidth, cellHeight, bgColor);
                gridPanels[y][x] = square;
                panelPlay.add(square);
            }
        }
    }

    // initializes the frame and puts panels in place
    public static void initFrame(){
        // removes everthing and adds updated panels in
        frame.getContentPane().removeAll();

        updatePanelScore();
        updatePanelEffects();

        frame.getContentPane().add(panelScore, BorderLayout.NORTH);
        frame.getContentPane().add(panelPlay, BorderLayout.CENTER); 
        frame.getContentPane().add(panelEffects, BorderLayout.SOUTH);   
                
        frame.revalidate();
        frame.repaint();

        // requests focus again
        SwingUtilities.invokeLater(() -> {
            frame.setFocusable(true);
            frame.requestFocusInWindow();
        });


    }

    // updates individual SquarePanels in the grid
    public static void updateCell(int x, int y) {
        SquarePanel panel = gridPanels[y][x];
        GridSquare cell = grid[y][x]; 
        
        Color bgColor = (x % 2 == y % 2) ? darkSquare : lightSquare;
        
        if (cell.getOccupyingSnake() != null) {
            Snake s = cell.getOccupyingSnake();
            Color snakeColor;
            if (boostArr[1]==1){
                int colorAdjust = 5;
                snakeColor = new Color(Math.min(255, bgColor.getRed()+colorAdjust), Math.min(255, bgColor.getGreen()+colorAdjust), Math.min(255, bgColor.getBlue()+colorAdjust));
            } else {
                snakeColor = s.getActualColor();
            }
            if (grid[y][x].getSegment() == 0){ // has a head
                panel.setHead(cellWidth, cellHeight, s.getDirection(), (int)(s.getMoveStep()*steps*snakeSpeed), snakeSpeed, bgColor, snakeColor);
            } else {                   
               
                //checking surrounding tiles for snake segments
                int currSegment = grid[y][x].getSegment();
                int upSegment = -1;
                int downSegment = -1;
                int rightSegment = -1;
                int leftSegment = -1;
                boolean up = false;
                boolean down = false;
                boolean right = false;
                boolean left = false;
            
                // if there's a segment to the top/bottom/left/right
                if (y>0 && grid[y-1][x].getOccupyingSnake()==s){
                    upSegment = grid[y-1][x].getSegment();
                }
            
                if (y<gridY-1 && grid[y+1][x].getOccupyingSnake()==s){
                    downSegment = grid[y+1][x].getSegment();
                }
            
                if (x>0 && grid[y][x-1].getOccupyingSnake()==s){
                    leftSegment = grid[y][x-1].getSegment();                    
                }
            
                if (x<gridX-1 && grid[y][x+1].getOccupyingSnake()==s){
                    rightSegment = grid[y][x+1].getSegment();
                }
          
                // if the segments to the top/bottom/left/right are sequential with the current segment
                if (Math.abs(upSegment - currSegment)==1) up = true;
                if (Math.abs(downSegment - currSegment)==1) down = true;    
                if (Math.abs(rightSegment - currSegment)==1) right = true;                 
                if (Math.abs(leftSegment - currSegment)==1) left = true;
                
                // rendering the tail
                if (currSegment == s.getLength()-1){

                    panel.setTail(cellWidth, cellHeight, up, right, down, left, (int)(s.getMoveStep()*steps*snakeSpeed), snakeSpeed, bgColor, snakeColor, true);


                // rendering the body segments   
                } else {  
                    panel.setBody(cellWidth, cellHeight, up, right, down, left, bgColor, snakeColor);
                }  
               
            } 
        } else if (x == (int)fruit.getX() && y == (int)fruit.getY()) { // has fruit
            panel.setImage(cellWidth, cellHeight, bgColor, f);
            
        } else if (cell.getCurrBoost()!=null){ // has a boost

            int boostType = cell.getCurrBoost().getType();
            ImageIcon boostImage;
            if (boostType == 0){
                boostImage = b0;
            } else if (boostType == 1){
                boostImage = b1;
            } else if (boostType == 2){
                boostImage = b2;
            } else if (boostType == 3){
                boostImage = b3;
            } else if (boostType == 4){
                boostImage = b4;
            } else if (boostType == 5){
                boostImage = next;
            } else {
                boostImage = null;
            }

            panel.setImage(cellWidth, cellHeight, bgColor, boostImage);
        } else {
            panel.setEmpty(cellWidth, cellHeight, bgColor);
        }
        
        panel.repaint();
    }

    // updates only the grid containing the head, segment after the head, tail, and square after the tail
    public static void renderMovingPanels(){
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                
                // if the grid has a snake
                if (grid[y][x].getOccupyingSnake()!= null){
                    Snake occupant = grid[y][x].getOccupyingSnake();
                    
                    // square has head
                    if (grid[y][x].getSegment() == 0 || grid[y][x].getSegment() == 1 || grid[y][x].getSegment() == occupant.getLength()-1){
                        updateCell(x,y);
                    } 
                }

                if (grid[y][x].getHadSnake()){
                    updateCell(x,y);
                }
            }
        }      

    }

    // updates the entire grid
    public static void renderGrid(){
        
        // putting stuff onto the GridLayout
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                updateCell(x,y);
            }
        }
        
        panelPlay.setFocusable(true);

    }

    // updates the main grid throughout the game
    public static void updateGrid() {

        if (snake1.getMoveStep() == 0 || snake2.getMoveStep() == 0) {
            renderGrid();
        } else {
            renderMovingPanels();
        }

        panelPlay.repaint(); 
        
    }

    

    // every timer tick, move both snakes + update panels accordingly
    public static void runGame(){
        //long t0 = System.nanoTime();

        if (cont){

            // move snake 1
            int afterMoving = move(snake1);

            if (afterMoving == 0){ // snake is dead, end the game
                cont = false;
                gameTimer.stop();
                snakeDeath.setFramePosition(0);
                snakeDeath.start();
                loseScreen(snake1); // send to lose screen
                System.out.println("snake 1 loses");

            } else if (afterMoving == -1){ // head-on collision
                cont = false;
                gameTimer.stop();
                snakeDeath.setFramePosition(0);
                snakeDeath.start();
                loseScreen(new Snake(-1,-1,new Color(255,255,255), Player.TIE));
                System.out.println("Head-on collision");

            } else { // continue moving
                cont = true;
                checkBoost(snake1);

            }
            snake1.setSnakeMoved(true);

            // check fruit + update score
            int add = checkFruit(snake1);
            if (add>0){
                snake1.setLength(snake1.getLength()+add);
                snake1.setScore(snake1.getScore()+add);
                updatePanelScore();
            }
        }

        if (cont){

            // move snake 2 
            int afterMoving = move(snake2);

            if (afterMoving == 0){ // snake is dead, end the game
                cont = false;
                gameTimer.stop();
                snakeDeath.setFramePosition(0);
                snakeDeath.start();
                loseScreen(snake2); // send to lose screen
                System.out.println("snake 2 loses");
            } else if (afterMoving == -1){ // head-on collision
                cont = false;
                gameTimer.stop();
                snakeDeath.setFramePosition(0);
                snakeDeath.start();
                loseScreen(new Snake(-1,-1,new Color(255,255,255), Player.TIE));
                System.out.println("Head-on collision");
            } else { // continue moving
                cont = true;
                checkBoost(snake2);

            }
            snake2.setSnakeMoved(true);

            // check fruit + update score
            int add = checkFruit(snake2);
            if (add>0){
                snake2.setLength(snake2.getLength()+add);
                snake2.setScore(snake2.getScore()+add);
                updatePanelScore();
            }
            




            
        }
        spawnBoost();
        // update grid
        updateGrid();
        

        //long t1 = System.nanoTime();
        //System.out.println("runGame tick took " + ((t1 - t0) / 1_000_000.0) + " ms");
            

    }


    public static int move(Snake snake){
        if (snake.getDirection() == -1) return 1; // not moving yet
        //System.out.println("Coords: " + snake.headToString());

        // if the snake changed direction
        if (snake.getDirection() != snake.getPastDirection()){

            //System.out.println("changing direction");
            snake.setMoveStep(0);

            switch(snake.getDirection()){ 
          
                case 0: // going up
                    System.out.println("moving up");
                    snake.setHead(new Point2D((int)snake.getHead().getX(),(int)snake.getHead().getY()-snakeSpeed));
                    snake.setMoveStep(1);
                    break;
                case 1: // going right
                    System.out.println("moving right");

                    snake.setHead(new Point2D((int)snake.getHead().getX()+1,(int)snake.getHead().getY()));                
                    break;
                case 2: // going down
                    System.out.println("moving down");

                    snake.setHead(new Point2D((int)snake.getHead().getX(),(int)snake.getHead().getY()+1));
                    break;
                case 3: // going left
                    System.out.println("moving left");

                    snake.setHead(new Point2D((int)snake.getHead().getX()-snakeSpeed,(int)snake.getHead().getY()));
                    snake.setMoveStep(1);
                    break;
            }
            snake.setPastDirection(snake.getDirection());


            // move body segments up
            for (int y=0; y<grid.length; y++){
                for (int x=0; x<grid[0].length; x++){
                    if (grid[y][x].getOccupyingSnake() == snake){
                        grid[y][x].setSegment(grid[y][x].getSegment()+1);
                        grid[y][x].updateLength(snake.getLength());
                    }
                }
            }

            // collision check
            int result = checkSnake(snake);
            if (result != 1) return result;

          
            // fill the current square
            grid[(int)snake.getHead().getY()][(int)snake.getHead().getX()].setOccupyingSnake(snake);
            //System.out.println("moveStep after turning: " + snake.getMoveStep());



        } else{
            //System.out.println("not changing direction");
            //System.out.println("moveStep before moving: " + snake.getMoveStep());

            switch(snake.getDirection()){
                case 0: snake.setHead(new Point2D(snake.getHead().getX(), snake.getHead().getY()-snakeSpeed)); break;
                case 1: snake.setHead(new Point2D(snake.getHead().getX()+snakeSpeed, snake.getHead().getY())); break;
                case 2: snake.setHead(new Point2D(snake.getHead().getX(), snake.getHead().getY()+snakeSpeed)); break;
                case 3: snake.setHead(new Point2D(snake.getHead().getX()-snakeSpeed, snake.getHead().getY())); break;
            }

            snake.tickMoveStep();


            // check if head entered a new square
            if (snake.getMoveStep() >= Math.round(1/snakeSpeed)){
                //System.out.println("moveStep: " + snake.getMoveStep());
                //System.out.println("Coords rn: " + snake.headToString());

                snake.setMoveStep(0);

                // round off the float values                
                int newX = (int)Math.round(snake.getHead().getX());
                int newY = (int)Math.round(snake.getHead().getY());
                
                if (snake.getDirection() == 1 || snake.getDirection() == 2) {
                    snake.setHead(new Point2D(newX, newY));
                } else if (snake.getDirection()==3){
                    snake.setHead(new Point2D(newX-snakeSpeed, newY));
                    snake.tickMoveStep();
                } else {
                    snake.setHead(new Point2D(newX, newY-snakeSpeed));
                    snake.tickMoveStep();
                }

                // move body segments up
                for (int y=0; y<grid.length; y++){
                    for (int x=0; x<grid[0].length; x++){
                        if (grid[y][x].getOccupyingSnake() == snake){
                            grid[y][x].setSegment(grid[y][x].getSegment()+1);
                            grid[y][x].updateLength(snake.getLength());
                        }
                    }
                }

                // collision check
                int result = checkSnake(snake);
                if (result != 1) return result;

                // fill the current square
                grid[(int)snake.getHead().getY()][(int)snake.getHead().getX()].setOccupyingSnake(snake);

            }
        }
        return 1;
    }


    // updates panelScore
    public static void updatePanelScore(){

        player1ScoreLabel.setText("PLAYER 1: " + snake1.getScore());
        player1ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
        player2ScoreLabel.setText("PLAYER 2: " + snake2.getScore());
        player2ScoreLabel.setHorizontalAlignment(JLabel.CENTER);
               
    }

    // updates panelEffects
    public static void updatePanelEffects(){
        //long t0 = System.nanoTime();

        // add appropriate icons to keep track of active boosts
        panelEffects.removeAll();
        panelEffects.add(effectsLabel);
        if (boostArr[0] > 0){
            for (int i = 0; i < boostArr[0]; i++){
                panelEffects.add(new JLabel(b1));
            }
        } else if (boostArr[0] < 0){
            
            for (int i = 0; i < -boostArr[0]; i++){
                panelEffects.add(new JLabel(b2));
            }
        }
        if (boostArr[1] == 1){
            panelEffects.add(new JLabel(b3));
        }
        if (boostArr[2]==1){
            panelEffects.add(new JLabel(b4));
        }
        panelEffects.revalidate();
        panelEffects.repaint();
        //long t1 = System.nanoTime();
       // System.out.println("updatePanelEffects tick took " + ((t1 - t0) / 1_000_000.0) + " ms");
    }




    // checks if the snake goes out of bounds / crashes into itself / the other snake's head
    // -1 = head-on collision
    // 0 = other crash
    // 1 = nothing
    public static int checkSnake(Snake snake){
    
        int x = (int)snake.getHead().getX();
        int y = (int)snake.getHead().getY();   
        
        // out of bounds
        if (x < 0 || x >= gridX || y < 0 || y >= gridY) {
            snake.setDirection(-1);
            System.out.println("Game over. " + snake.getPlayer() + " is out of bounds.");
            return 0;
        }
        
        // crashes into a snake segment
        if (grid[y][x].getOccupyingSnake()!=null){

            //head-on collision
            if (grid[y][x].getSegment() == 0){
                System.out.println(snake.getPlayer() + " had a head-on collision with the other player.");
                return -1;
            }
           
            System.out.println("Crashed into snake at head coords " + x + ", " + y);
            return 0;
        }

       
        return 1;
        
    }


    // if snake eats a fruit, return scoreBoost
    // else, return 0
    public static int checkFruit(Snake snake){

        if((int)snake.getHead().getY()==(int)fruit.getY() && (int)snake.getHead().getX()==(int)fruit.getX()){ // fruit has been eaten
           
            new Thread(() -> {
                fruitEaten.setFramePosition(0);
                fruitEaten.start();
            }).start();
           
            // spawn a new fruit
            fruit = spawnPoint();
          
            return snake.getScoreBoost();  
            
        } else { // no fruit eaten
            return 0;    
        }
    }


    // spawns a boost randomly on the board
    public static void spawnBoost(){
    
       
        int generate = (int)(Math.random()*boostSpawnChance);
        
        // 1/100 chance of spawning a boost
        if (generate==0){
        
            int currType = (int)(Math.random()*6);
            
            
            // effects:
            // 0 = reset effects
            // 1 = speed boost
            // 2 = slowness
            // 3 = invisibility
            // 4 = key input switch
            // 5 = wipe boosts from board
            
            new Thread(() -> {
                boostSpawned.setFramePosition(0);
                boostSpawned.start();
            }).start();
            
            
                            
            // put the boost on the grid
            Point boostPt = spawnPoint();
            grid[(int)boostPt.getY()][(int)boostPt.getX()].setCurrBoost(new Boost(currType));

        
        }  
            
        
   
    }
    
    
    // if snake encounters a boost, change necessary game variables and wipe boost from GridSquare 
    public static void checkBoost(Snake snake){

        Boost currBoost = grid[(int)snake.getHead().getY()][(int)snake.getHead().getX()].getCurrBoost();
        grid[(int)snake.getHead().getY()][(int)snake.getHead().getX()].setCurrBoost(null);
    
        if (currBoost != null){
            new Thread(() -> {
                boostObtained.setFramePosition(0);
                boostObtained.start();
            }).start();
            

            if (currBoost.getType() == 0){ // reset effects
                
                boostArr = new int[boostArr.length];
                snakeSpeed = GameVar.snakeSpeed;
                steps = (int)(GameVar.animStepSpeed*GameVar.timerDelay/snakeSpeed);
                System.out.println("resetting boost effects");

            } else if (currBoost.getType() == 1){ // speed
              
                boostArr[0]++;
                // increase snakeSpeed + update animationSteps
                snakeSpeed = GameVar.snakeSpeed * Math.pow(2, boostArr[0]);
                steps = (int)(GameVar.animStepSpeed*GameVar.timerDelay/snakeSpeed);

                System.out.println("speeding up. snakeSpeed: " + snakeSpeed);
            
            } else if (currBoost.getType() == 2){ // slowness
 
                // decrease snakeSpeed + update animationSteps
                if (boostArr[0] >= -1){
                    boostArr[0]--;
                    snakeSpeed = GameVar.snakeSpeed * Math.pow(2, boostArr[0]);
                    steps = (int)(GameVar.animStepSpeed*GameVar.timerDelay/snakeSpeed);
                }

                System.out.println("slowing down. snakeSpeed: " + snakeSpeed);

            } else if (currBoost.getType() == 3){ // invisibility
     
                boostArr[1] = 1 - boostArr[1];
                System.out.println("Toggling invis");
   
            } else if (currBoost.getType() == 4){ // direction switch
           
                boostArr[2] = 1 - boostArr[2];
                System.out.println("Toggling input flip");
            
            } else if (currBoost.getType() == 5){  // wipe all boosts from board
                System.out.println("wiping boosts from board");
                for (int y = 0; y < grid.length; y++) {
                    for (int x = 0; x < grid[0].length; x++) {
                        grid[y][x].setCurrBoost(null);
                    }
                }

            }
            updatePanelEffects();

        }     
    
    }
    
    


    // spawns a new fruit / boost on the board
    public static Point spawnPoint(){
      
        int x = (int)(gridX*Math.random());
        int y = (int)(gridY*Math.random());         
        
        // if the new point doesn't overlap with a boost/fruit/snake, return the point
        // otherwise, try again
        if(grid[y][x].getOccupyingSnake()==null && !(x==(int)fruit.getX() && y==(int)fruit.getY()) && grid[y][x].getCurrBoost()==null){

            return new Point(x,y);
            
        } else {
            return spawnPoint();
        }
    }


    // used in the case of game over
    public static void loseScreen(Snake snake){

        if (playMusic){
            bgMusic.stop();
        }

        if (snake.getPlayer().equals(Player.PLAYER_1)){
            renderLose(1);
            
        } else if (snake.getPlayer().equals(Player.PLAYER_2)){
            renderLose(2);
        } else {
            renderLose(0);
        }
        
        frame.getContentPane().remove(panelEffects);
        frame.getContentPane().add(panelLost, BorderLayout.SOUTH);
        //frame.add(new JLabel(boostExplanation), BorderLayout.SOUTH); // adds boost explanation image

        frame.revalidate();
        frame.repaint();

        SwingUtilities.invokeLater(() -> {
            frame.setFocusable(true);
            frame.requestFocusInWindow();
        });

        System.out.println("gameStarted: " + gameStarted);


    }

    // creates game over screen
    public static void renderLose(int loser){

        boostArr = new int[boostArr.length];

        panelScore.removeAll();
        panelLost.removeAll();

         
        panelLost.add(buttonReplay);

        // load game button
        //panelLost.add(buttonLoad);
        
        // toggle music button
        JButton button3 = new JButton();
        
        if (playMusic){
            button3.setIcon(yesMusic);
        } else {
            button3.setIcon(noMusic);
        }
        
        button3.setPreferredSize(smallButtonDimensions);
        button3.setFocusable(false);
       
        button3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
            
                // play sound when clicked
                buttonClick.setFramePosition(0);
                buttonClick.start();
               
                if (playMusic){
                    playMusic = false;
                    button3.setIcon(noMusic);
                
                } else {
                    
                    playMusic = true;
                    button3.setIcon(yesMusic);
                                  
                }
            }
        });

        panelLost.add(button3);
        panelLost.add(buttonKeySounds); // toggle key sounds button

        // close button
        buttonClose.setPreferredSize(smallButtonDimensions);
        panelLost.add(buttonClose);

        // turn panelScore into the top ribbon that displays game results
        panelScore.setLayout(new GridLayout(1,5));

        JLabel gameOver;
        if (loser == 0){
            gameOver = new JLabel("<html>Head-on collision</html>");
        } else {
            gameOver = new JLabel("<html>PLAYER " + loser + " crashed</html>");
        }
        gameOver.setHorizontalAlignment(JLabel.CENTER);
        gameOver.setBorder(new EmptyBorder(10, 20, 10,0)); // top, left, bottom, right
        panelScore.add(gameOver);

        JLabel winBonusLabel;
        if (loser==2){
            snake1.setScore(snake1.getScore() + winBonus);
            winBonusLabel = new JLabel("<html>Survival Bonus (+20): PLAYER 1</html>");
        } else if (loser ==1){
            snake2.setScore(snake2.getScore() + winBonus);
            winBonusLabel = new JLabel("<html>Survival Bonus (+20): PLAYER 2</html>");
        } else {
            winBonusLabel = new JLabel("<html>Survival Bonus (+20): N/A</html>");
        }
        panelScore.add(winBonusLabel);

        // winner
        JLabel winnerLabel = new JLabel();
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);

        if (snake1.getScore() == snake2.getScore()){
            winnerLabel.setText("TIE");
        } else if (snake1.getScore() > snake2.getScore()){
            winnerLabel.setText("PLAYER 1 WINS");
        } else {
            winnerLabel.setText("PLAYER 2 WINS");
        }
        panelScore.add(winnerLabel);
        
        // final scores
        player1ScoreLabel.setText("PLAYER 1: " + snake1.getScore());
        player2ScoreLabel.setText("PLAYER 2: " + snake2.getScore());
      
        panelScore.add(player1ScoreLabel);
        panelScore.add(player2ScoreLabel);

    }

    // resets the game when button2 is pressed
    public static void resetGame() {
        
        if (playMusic){
            bgMusic.setFramePosition(0);
            bgMusic.start();
            bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }

        snake1.startGameStats();
        snake2.startGameStats();
    
    
        // resetting variables
        resetVariables();
        
        cont = true;
      

        // resetting grid
        for (int y = 0; y < gridY; y++) {
            for (int x = 0; x < gridX; x++) {
                grid[y][x].setOccupyingSnake(null);
                grid[y][x].setSegment(0);
                grid[y][x].setCurrBoost(null);
            }
        }
        grid[snake1.getStartY()][snake1.getStartX()].setOccupyingSnake(snake1);
        grid[snake2.getStartY()][snake2.getStartX()].setOccupyingSnake(snake2);

        // reset the frame graphics
        panelScore.removeAll();
        panelScore.add(player1ScoreLabel);
        panelScore.add(player2ScoreLabel);
        panelEffects.removeAll();
        panelEffects.add(effectsLabel);
        initFrame();
        renderGrid();
      

        
    }

    // buffers sounds that are fed into the method
    public static Clip bufferSound(String soundFile) {

        Clip clip = null;

        try {
            File file = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        } finally {
            System.out.println("Attempted to load " + soundFile);
        }

        return clip;
    }
    
    // initializes sounds
    public static void loadSounds(){
        buttonClick = bufferSound("buttonClick.wav");
        boostObtained = bufferSound("boostObtained.wav");
        snakeDeath = bufferSound("snakeDeath.wav");
        fruitEaten = bufferSound("fruitEaten.wav");
        boostSpawned = bufferSound("boostSpawned.wav");
        bgMusic = bufferSound("backgroundMusic.wav");
        keyPress = bufferSound("keyPress.wav");
   
    
    }

    // initializes images
    public static void loadImages(){

        cellWidth = frame.getWidth() / (gridX);
        cellHeight = (frame.getHeight()-100) / (gridY);
        
        playGame = new ImageIcon(new ImageIcon("playGame.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));
        resetGame = new ImageIcon(new ImageIcon("resetGame.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));

        loadSave = new ImageIcon(new ImageIcon("loadSave.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));

        closeWindow = new ImageIcon(new ImageIcon("closeWindow.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));

        boostExplanation = new ImageIcon(new ImageIcon("boostExplanation.png").getImage().getScaledInstance(400, 260,Image.SCALE_SMOOTH));

        f = new ImageIcon(new ImageIcon("fruit.png").getImage().getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH));
        next = new ImageIcon(new ImageIcon("next.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        b0 = new ImageIcon(new ImageIcon("boost0.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        b1 = new ImageIcon(new ImageIcon("boost1.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        X2= new ImageIcon(new ImageIcon("2multiplier.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));

        b2 = new ImageIcon(new ImageIcon("boost2.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        b3 = new ImageIcon(new ImageIcon("transparency.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        b4 = new ImageIcon(new ImageIcon("directionSwitch.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));
        X5 = new ImageIcon(new ImageIcon("5multiplier.png").getImage().getScaledInstance(cellWidth, cellHeight,Image.SCALE_SMOOTH));

        yesMusic = new ImageIcon(new ImageIcon("yesMusic.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));
        noMusic = new ImageIcon(new ImageIcon("noMusic.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));
        keySoundsOn = new ImageIcon(new ImageIcon("keySoundOn.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));
        keySoundsOff = new ImageIcon(new ImageIcon("keySoundOff.png").getImage().getScaledInstance(50, 50,Image.SCALE_SMOOTH));

    }
    
    
  
}

