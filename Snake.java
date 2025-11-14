import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;


public class Snake implements Serializable{

    private static final long serialVersionUID = 1L;


    private Player player;
    private int startX;
    private int startY;
    private Point2D head;

    //private int activeBoost; // current active boost
    //private int boostSpawnChance; // keeps boost spawning chances consistent across different speeds
    private int scoreBoost;
    //private boolean switchDirections;
    //private boolean currentlyInvis;

    private boolean snakeMoved;

    private int direction;
    private int pastDirection;
    private int length;
    private int score;
    private int wins;


    
    private SquarePanel tailSP;
    private SquarePanel headSP = new SquarePanel(1,1,new Color(255,255,255));

    private Color defaultColor;
    private Color actualColor; // if invis
    private Color headColor;

    private int moveStep;





    public Snake(int x, int y, Color c, Player p){
        startX = x;
        startY = y;
        defaultColor = c;
        //headColor = d;
        player = p;
        snakeMoved = true;
        head = new Point2D(startX,startY);
        direction = -1;
        pastDirection = -1;
        //switchDirections = false;
        actualColor = defaultColor;
        //currentlyInvis = false;
        scoreBoost = 1;
        moveStep = 0;
        wins = 0;
        
        /*activeBoost = 0;
        currType = 0;
        prevType = 0;
        boost = new Boost(new Point (-1,-1), currType);
        boostSpawnChance = 30;*/

        score=0;
        length=4;
        
    }

    public String headToString(){
        return "(" + head.getX() + ", " + head.getY() + ")";
    }

    public int getPastDirection(){
        return pastDirection;
    }

    public void setPastDirection(int dir){
        pastDirection = dir;
    }


    public Point2D getHead() {
        return head;
    }

    public void setHead(Point2D p){
        head = p;
    }

    public double getHeadAnimStep(){
        double x_diff = head.getX() - (int)(head.getX());
        double y_diff = head.getY() - (int)(head.getY());
        if (x_diff > y_diff){
            return x_diff;
        } else {
            return y_diff;
        }

    }

    public int getMoveStep(){
        return moveStep;
    }
    public void setMoveStep(int i){
        moveStep = i;
    }
    public void tickMoveStep(){
        moveStep++;
    }


    public boolean getSnakeMoved() {
        return snakeMoved;
    }

    public void setSnakeMoved(boolean s) {
        snakeMoved = s;
    }

   
    
 
    


   /* public void resetBoostStats(){
        delay = 200;
        scoreBoost = 1;
        boostSpawnChance = 30;
        switchDirections = false;
        currentlyInvis = false;
    }*/

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }


    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SquarePanel getTailSP() {
        return tailSP;
    }

    public void setTailSP(SquarePanel tailSP) {
        this.tailSP = tailSP;
    }

    public Color getActualColor() {
        return actualColor;
    }

    public void setActualColor(Color actualColor) {
        this.actualColor = actualColor;
    }

    public int getScoreBoost() {
        return scoreBoost;
    }

    public void setScoreBoost(int scoreBoost) {
        this.scoreBoost = scoreBoost;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


    public SquarePanel getHeadSP() {
        return headSP;
    }

    public void setHeadSP(SquarePanel headSP) {
        this.headSP = headSP;
    }

    public Color getHeadColor() {
        return headColor;
    }

    public void setHeadColor(Color headColor) {
        this.headColor = headColor;
    }

  

}
