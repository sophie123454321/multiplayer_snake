

public class GridSquare{

    

    // attributes
    private final int xCoord;
    private final int yCoord;
    private Snake occupyingSnake;
    private int segment;
    private boolean hadSnake;
    private boolean hasFruit;
    private Boost currBoost;

    // constructor
    public GridSquare(int x, int y){

        xCoord = x;
        yCoord = y;
        occupyingSnake = null;
        segment = 0;
        hadSnake = false;
        hasFruit = false;
        currBoost = null;

    }

    // methods
    public void setFruit(boolean b){
        hasFruit = b;
    }
    
    public boolean getHasFruit(){
        return hasFruit;
    }

    public void setCurrBoost(Boost b){
        currBoost = b;
    }

    public Boost getCurrBoost(){
        return currBoost;
    }
    
    public int getX(){
        return xCoord;
    }

    public int getY(){
        return yCoord;
    }

    public boolean getHadSnake(){
        return hadSnake;
    }

    public void setHadSnake(boolean b){
        hadSnake = b;
    }
    

    public int getSegment(){
        return segment;
    }

    

    public void setSegment(int s){
        segment=s;
    }

    // if the current segment number exceeds snake's length, remove snake from the square
    public void updateLength(int length){
        hadSnake = false;
        
        if(occupyingSnake!=null && segment==occupyingSnake.getLength()){
            occupyingSnake = null;
            segment=0;
            hadSnake = true;
        }

    }



    public Snake getOccupyingSnake() {
        return occupyingSnake;
    }

    public void setOccupyingSnake(Snake occupyingSnake) {
        this.occupyingSnake = occupyingSnake;
    }

  




}