import java.awt.Color;
import java.io.Serializable;


public class Snake implements Serializable{

    private static final long serialVersionUID = 1L;
    private Player player;
    private int startX;
    private int startY;
    private Point2D head;
    private int scoreBoost;
    private boolean snakeMoved;
    private int direction;
    private int pastDirection;
    private int length;
    private int score;
    //private int wins;
    private Color defaultColor;
    private Color actualColor; // if invis
    private int moveStep;


    public Snake(int x, int y, Color c, Player p){
        startX = x;
        startY = y;
        defaultColor = c;
        player = p;
        snakeMoved = true;
        head = new Point2D(startX,startY);
        direction = -1;
        pastDirection = -1;
        actualColor = defaultColor;
        scoreBoost = 1;
        moveStep = 0;
        //wins = 0;
        
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

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


}
