class Point2D{
    private double x;
    private double y;

    public Point2D(double x1, double y1){
        x = x1;
        y = y1;
    }

    public Point2D(Point2D newPt){
        x = newPt.getX();
        y = newPt.getY();
    }

    public void setX(double x1){
        x = x1;
    }

    public void setY(double y1){
        y = y1;
    }

    public void setPoint(double x1, double y1){
        x = x1;
        y = y1;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }


}