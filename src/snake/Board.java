package snake;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {

    private static final int BOARD_WIDTH = 600,     	 //no modifier
                            BOARD_HEIGHT = 400,
                            DOT_SIZE = 20,				 //snake-block size
                            NUMBER_OF_DOTS = (BOARD_WIDTH*BOARD_HEIGHT)/(DOT_SIZE*DOT_SIZE), 		 //number of possible board dots
                            RANDOM_WIDTH = BOARD_WIDTH/DOT_SIZE - 1,     		 //to be used when placing apple
                            RANDOM_HEIGHT = BOARD_HEIGHT/DOT_SIZE - 1,
                            INITIAL_SNAKE_LENGTH = 4,	//must not exceed BOARD_WIDTH/20
                            GAME_OVER_COUNTDOWN = 7,
                            INITIAL_DELAY = 150,		//game speed
                            SPEED_CHANGE = 2,			//increase in speed for every apple eaten
                            MAX_SPEED = 50;				

    private Coordinate[] snake;
    private Coordinate apple;
    private int snakeLength;
    private int delay;           	 //controls speed of game
    private int gameOverCountDown;

    static boolean left;
    static boolean right;
    static boolean up;
    static boolean down;
    static boolean keyPressed;
    private boolean inGame;

    private Timer timer;
    private Image snakeBlockImage;
    private Image appleImage;
    private Image snakeHeadImage;
    private Image headCollisionImage;
    
    private JButton restartButton;

    Board() {
    	initializeVariables();
    	
        addKeyListener(new EventHandler());	
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        loadImages();
        addRestartButton();
        
        timer = new Timer(delay, this);
        timer.start();
    }
    private void initializeVariables() {
    	createSnake();
    	createApple();
    	left = false;
    	right = true;
    	up = false;
    	down = false;
    	keyPressed = false;
    	inGame = true;
    	delay = INITIAL_DELAY;
    	gameOverCountDown = GAME_OVER_COUNTDOWN;
    }
    private void loadImages() {	
    	//ImageIcon snakeBlockIcon = new ImageIcon("/images/snakeBody20px.png");
    	ImageIcon snakeBlockIcon = new ImageIcon(getClass().getResource("/images/snakeBody20px.png"));
        snakeBlockImage = snakeBlockIcon.getImage();
        snakeBlockImage = snakeBlockImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);
        
        //ImageIcon appleIcon = new ImageIcon("apple20px.png");
        ImageIcon appleIcon = new ImageIcon(getClass().getResource("/images/apple20px.png"));
        appleImage = appleIcon.getImage();
        appleImage = appleImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);
        
        //ImageIcon snakeHeadIcon = new ImageIcon("snakeHead20px.png");
        ImageIcon snakeHeadIcon = new ImageIcon(getClass().getResource("/images/snakeHead20px.png"));
        snakeHeadImage = snakeHeadIcon.getImage();
        snakeHeadImage = snakeHeadImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);
        
        //ImageIcon headCollisionIcon = new ImageIcon("snakeHeadCollision20px.png");
        ImageIcon headCollisionIcon = new ImageIcon(getClass().getResource("/images/snakeHeadCollision20px.png"));
        headCollisionImage = headCollisionIcon.getImage();
        headCollisionImage = headCollisionImage.getScaledInstance(DOT_SIZE, DOT_SIZE, Image.SCALE_DEFAULT);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (inGame) {
    		keyPressed = false;				//only allows one direction change per tick
    		Coordinate head = generateNewHead();
    		checkAppleAndReactAccordingly(head);
    		if(checkCollisionWithSnake(head)) inGame = false;
    		else move(head);
    	}
    	else {
    		if (gameOverCountDown == 0) {
    			setButtonVisibility(true);
    			timer.stop();
    		}
    	}
    	repaint();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }
    private void doDrawing(Graphics g) {
        if (inGame) {
            drawApple(g);
            drawSnakeHead(g, snakeHeadImage);
            drawSnakeBody(g);        
            Toolkit.getDefaultToolkit().sync();
        } 
        else {
        	endGame(g);
        }
    }
    private void endGame(Graphics g) {	
    	if (gameOverCountDown == 0) gameOver(g);
    	else {
    		if (gameOverCountDown%2 != 0) {
    			drawApple(g);
    			drawSnakeBody(g);
    			drawSnakeHead(g, headCollisionImage);
    		}
    		else {
    			drawApple(g);
    			drawSnakeBody(g);
    			drawSnakeHead(g, snakeHeadImage);
    		}
    		gameOverCountDown--;
    	}
    }
    private void gameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, DOT_SIZE);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH - getFontMetrics(font).stringWidth(message)) / 2, BOARD_HEIGHT / 2);       
    }

    private void checkAppleAndReactAccordingly(Coordinate head) {
        if (apple.equals(head)) {
        	if (!(snakeLength == NUMBER_OF_DOTS)) {		//limit snakeLength to board size
        		snakeLength++;
        		increaseSpeed();
        	}
            createApple();
        }
    }
    private void increaseSpeed() {
    	if (delay == MAX_SPEED) return;	//do not increase speed above MAX_SPEED
        delay -= SPEED_CHANGE;			//increase speed of snake
        timer.setDelay(delay);
    }
    private void move(Coordinate c){	//shift values backwards and add new head at index 0
		for (int i=snakeLength;i>0;i--){
			snake[i] = snake[i-1];
		}
		snake[0] = c;
	}
    private void drawSnakeBody(Graphics g) {
    	for (int i=1;i<snakeLength;i++) {
    		g.drawImage(snakeBlockImage, snake[i].getX(), snake[i].getY(), this);
    	}
    }
    private void drawApple(Graphics g) {
        g.drawImage(appleImage, apple.getX(), apple.getY(), this);
    }
    private void drawSnakeHead(Graphics g, Image img) {
    	g.drawImage(img, snake[0].getX(), snake[0].getY(), this);
    }
    private void createSnake() {
    	snake = new Coordinate[NUMBER_OF_DOTS];
    	snakeLength = INITIAL_SNAKE_LENGTH;
    	for (int i=0;i<snakeLength;i++) {
    		snake[i] = new Coordinate((snakeLength-i)*DOT_SIZE,DOT_SIZE);
    	}
    }
    private void createApple() {
    	do {
    		int appleX = ((int) (Math.random() * RANDOM_WIDTH)) * DOT_SIZE;
    		int appleY = ((int) (Math.random() * RANDOM_HEIGHT)) * DOT_SIZE;
    		apple = new Coordinate(appleX,appleY);
    	} 
    	while(checkCollisionWithSnake(apple));
    }
    private boolean checkCollisionWithSnake(Coordinate c) {
    	for (int i=0;i<snakeLength;i++) {
    		if (snake[i].equals(c)) {
    			return true;
    		}
    	}
    	return false;
    }
    private Coordinate generateNewHead() {	//Snake is supposed to go through walls
    	Coordinate c;	
    	if (left) {
    		c = snake[0].copy().decreaseX(DOT_SIZE);
    		if (c.getX() < 0) {
    			c.resetX(BOARD_WIDTH-DOT_SIZE);
    		}
    		return c;
    	}
    	else if (right) {
    		c = snake[0].copy().increaseX(DOT_SIZE);
    		if (c.getX() > BOARD_WIDTH-DOT_SIZE) {
    			c.resetX(0);
    		}
    		return c;
    	}
    	else if (up) {
    		c = snake[0].copy().decreaseY(DOT_SIZE);
    		if (c.getY() < 0) {
    			c.resetY(BOARD_HEIGHT-DOT_SIZE);
    		}
    		return c;
    	}
    	else if (down) {
    		c = snake[0].copy().increaseY(DOT_SIZE);
    		if (c.getY() > BOARD_HEIGHT-DOT_SIZE) {
    			c.resetY(0);
    		}
    		return c;
    	}
    	else {
    		System.exit(1);
    	}
		return null;
    }
    private void addRestartButton() {
    	String buttonText = "New Game?";
		restartButton = new JButton(buttonText);
		Font font = new Font("Helvetica", Font.BOLD, (DOT_SIZE*2)/3);
		restartButton.setFont(font);
		
        setLayout(null);
		restartButton.setBounds((BOARD_WIDTH - 6*DOT_SIZE) / 2, BOARD_HEIGHT/2 + 3*DOT_SIZE, DOT_SIZE*6, (DOT_SIZE*3)/2);
		add(restartButton);
		
		restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restart();
            }
        });  
		
		setButtonVisibility(false);
    }
    private void setButtonVisibility(Boolean value) {
		restartButton.setVisible(value);
		restartButton.setEnabled(value);
    }
    private void restart() {
    	initializeVariables();
    	addKeyListener(new EventHandler());	
    	
    	timer = new Timer(delay, this);
        timer.start();
        
        setButtonVisibility(false);
    }
}
