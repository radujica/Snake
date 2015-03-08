package snake;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EventHandler extends KeyAdapter{

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (!Board.keyPressed) {
	        if ((key == KeyEvent.VK_LEFT) && (!Board.right)) {
	            Board.left = true;
	            Board.up = false;
	            Board.down = false;
	            Board.keyPressed = true;
	        }
	        else if ((key == KeyEvent.VK_RIGHT) && (!Board.left)) {
	            Board.right = true;
	            Board.up = false;
	            Board.down = false;
	            Board.keyPressed = true;
	        }
	        else if ((key == KeyEvent.VK_UP) && (!Board.down)) {
	            Board.up = true;
	            Board.right = false;
	            Board.left = false;
	            Board.keyPressed = true;
	        }
	        else if ((key == KeyEvent.VK_DOWN) && (!Board.up)) {
	            Board.down = true;
	            Board.right = false;
	            Board.left = false;
	            Board.keyPressed = true;
	        }
        }
    }
}
