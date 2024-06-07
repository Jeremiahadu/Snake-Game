import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {



    static final int SCREEN_WIDTH = 600; // Width of the game screen
    static final int SCREEN_HEIGHT = 600; // Height of the game screen
    static final int UNIT_SIZE = 25; // Size of each unit in the game
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE; // Total number of units in the game
    static final int DELAY = 75; // Delay for the game timer in milliseconds
    final int x[] = new int[GAME_UNITS]; // Array to hold the x coordinates of the snake's body parts
    final int y[] = new int[GAME_UNITS]; // Array to hold the y coordinates of the snake's body parts
    int bodyParts = 6; // Initial number of body parts for the snake
    int applesEaten; // Counter for the number of apples eaten
    int appleX; // x coordinate of the current apple
    int appleY; // y coordinate of the current apple
    char direction = 'R'; // Initial direction of the snake ('R' for right)
    boolean running = false; // Flag to check if the game is running
    Timer timer; // Timer to control the game speed
    Random random; // Random number generator for placing apples
    JButton restartButton; // Button to restart the game

    GamePanel() {
        random = new Random(); // Initialize the random number generator
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT)); // Set the size of the game panel
        this.setBackground(Color.black); // Set the background color of the game panel
        this.setFocusable(true); // Make the panel focusable to receive key events
        this.addKeyListener(new MyKeyAdapter()); // Add a key listener for user input
        startGame(); // Start the game
        restartButton = new JButton("Try Again"); // Create a restart button
        int buttonWidth = 200;
        int buttonHeight = 80;
        int buttonX = (SCREEN_WIDTH - buttonWidth) /2;
        int buttonY = (SCREEN_HEIGHT - buttonHeight) /2;
        restartButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight); // Set the position and size of the restart button
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame(); // Restart the game when the button is clicked
            }
        });
        restartButton.setVisible(false); // Initially make the restart button invisible
        add(restartButton); // Add the restart button to the panel
    }

    public void startGame() {
        newApple(); // Generate a new apple
        running = true; // Set the game running flag to true
        timer = new Timer(DELAY, this); // Initialize the timer with the delay and action listener
        timer.start(); // Start the timer
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call the superclass's paintComponent method
        draw(g); // Draw the game elements
    }

    public void draw(Graphics g) {
        if (running) {
            // Code to draw the apple and the snake
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    // Snake changes color randomly
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g); // Display game over screen
        }
    }

    public void newApple() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // Generate a random x coordinate for the apple
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // Generate a random y coordinate for the apple
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1]; // Shift the positions of the snake's body parts
            y[i] = y[i - 1]; // Shift the positions of the snake's body parts
        }
        switch (direction) {
            case 'U': // Move up
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D': // Move down
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L': // Move left
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R': // Move right
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++; // Increase the body parts of the snake
            applesEaten++; // Increment the apple counter
            newApple(); // Generate a new apple
        }
    }

    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && y[0] == y[i]) {
                running = false; // Stop the game if collision detected
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop(); // Stop the timer if the game is not running
        }
    }

    public void gameOver(Graphics g) {
        // Display the score and game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("SCORE: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("SCORE: " + applesEaten)) / 2, g.getFont().getSize());

        // Display different game over messages based on the score
        if (applesEaten <= 10) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("U SUCK", (SCREEN_WIDTH - metrics2.stringWidth("U SUCK")) / 2, SCREEN_HEIGHT / 2);
        }
        if ((applesEaten >= 11) && (applesEaten <= 20)) {
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("DECENT", (SCREEN_WIDTH - metrics2.stringWidth("DECENT")) / 2, SCREEN_HEIGHT / 2);
        }
        if ((applesEaten >= 21) && (applesEaten <= 40)) {
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("GREAT", (SCREEN_WIDTH - metrics2.stringWidth("GREAT")) / 2, SCREEN_HEIGHT / 2);
        }
        if ((applesEaten >= 41) && (applesEaten <= 60)) {
            g.setColor(Color.magenta);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("AMAZING", (SCREEN_WIDTH - metrics2.stringWidth("AMAZING")) / 2, SCREEN_HEIGHT / 2);
        }
        if ((applesEaten >= 61) && (applesEaten <= 110)) {
            g.setColor(Color.cyan);
            g.setFont(new Font("Ink Free", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("LEGENDARY", (SCREEN_WIDTH - metrics2.stringWidth("LEGENDARY")) / 2, SCREEN_HEIGHT / 2);
        }
        restartButton.setVisible(true); // Make the restart button visible
    }

    public void restartGame() {
        // Reset game state variables
        restartButton.setVisible(false); // Hide the restart button
        running = true; // Set the game running flag to true
        bodyParts = 6; // Reset the body parts of the snake
        applesEaten = 0; // Reset the apples eaten counter
        direction = 'R'; // Reset the direction to right

        // Reset snake's position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0; // Reset x coordinates
            y[i] = 0; // Reset y coordinates
        }
        x[0] = SCREEN_WIDTH / 2; // Start at the center of the screen
        y[0] = SCREEN_HEIGHT / 2; // Start at the center of the screen

        newApple(); // Generate a new apple
        timer.start(); // Start the timer
        repaint(); // Repaint the panel
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move(); // Move the snake
            checkApple(); // Check if the snake has eaten an apple
            checkCollisions(); // Check for collisions
        }
        repaint(); // Repaint the panel
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_SPACE:
                    if (!running) {
                        restartGame(); // Restart the game if space is pressed
                    }
                    break;
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L'; // Move left
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R'; // Move right
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U'; // Move up
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D'; // Move down
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L'; // Move left
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R'; // Move right
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U'; // Move up
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D'; // Move down
                    }
                    break;
            }
        }
    }}
