package snake;
/* Copyright (c) 2014, Radu Jica. 
 * Snake v1.0
 */


import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * @author Radu Jica
 * @version 1.0
 * @since 14.12.2014
 */

@SuppressWarnings("serial")
public class Snake extends JFrame {
	
	/* TODO:
	 *  - add startGame Panel
	 *  - handle game restart in a more clever way
	 *  - better graphics; consider using the .jpg in images folder
	 *  	- add SnakeBlob class containing Coordinate and direction int (0,1,2,3 for each direction, respectively)
	 *  	- use SwingWorker for loading images, if images with direction are used
	 *  - make applet ~ use tumbleItem applet example ~ implement init, start, stop, destroy
	 */

    public Snake() {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
    	EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame ex = new Snake();
                ex.setVisible(true);
            }
        });

    }
}