import java.awt.Graphics;
import javax.swing.JPanel;


// takes in a SquarePanel[][] to draw the JPanel
public class GamePanel extends JPanel{

    private SquarePanel[][] gameGridPanels;

    // constructor
    public GamePanel(){
        gameGridPanels = new SquarePanel[0][0];
    }

    // methods

    public void updateGrid(SquarePanel[][] g){
        gameGridPanels = g;
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);

        if (gameGridPanels == null) return;

        // calculate cell dimensions
        int rows = gameGridPanels.length;
        int cols = gameGridPanels[0].length;
        int cellWidth  = getWidth()  / cols;
        int cellHeight = getHeight() / rows;
    
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                SquarePanel cell = gameGridPanels[y][x];
                int px = x * cellWidth;
                int py = y * cellHeight;
                cell.draw(g, px, py, cellWidth, cellHeight);
            }
        }
    }


}
