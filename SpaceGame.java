package starship_odyssey;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class SpaceGame extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private ArrayList<Rectangle> asteroids;
    private int spaceshipX = 100;
    private int spaceshipY = 100;
    private int spaceshipVel = 0;
    private final int WIDTH = 800, HEIGHT = 600;
    private final int ASTEROID_WIDTH = 40, ASTEROID_HEIGHT = 40;
    private final int SPACESHIP_WIDTH = 40, SPACESHIP_HEIGHT = 40;
    private int score = 0;
    private Image spaceshipImage;
    private Image asteroidImage;
    private int spaceshipVelX = 0;

    public SpaceGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        timer = new Timer(20, this);
        asteroids = new ArrayList<>();
        addKeyListener(this);
        setFocusable(true);
        addAsteroid(true);
        addAsteroid(true);
        timer.start();
        spaceshipImage = new ImageIcon("spaceship.png").getImage();
        asteroidImage = new ImageIcon("asteroid.png").getImage();
    }

    private void addAsteroid(boolean start) {
        int positionY = (int) (Math.random() * HEIGHT - ASTEROID_HEIGHT);
        int width = ASTEROID_WIDTH;
        int height = ASTEROID_HEIGHT;
        if (start) {
            asteroids.add(new Rectangle(WIDTH + width + asteroids.size() * 300, positionY, width, height));

        } else {
            asteroids.add(new Rectangle(asteroids.get(asteroids.size() - 1).x + 600, positionY, width, height));
            score++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(spaceshipImage, spaceshipX, spaceshipY, this);

        g.setColor(Color.GRAY);
        for (Rectangle asteroid : asteroids) {
            g.drawImage(asteroidImage, asteroid.x, asteroid.y, this);
            g.setColor(Color.WHITE); // Set the color to white for the score text
            g.setFont(new Font("Arial", Font.BOLD, 18)); // Set the font for the score text
            g.drawString("Score: " + score, 10, 20); // Draw the score at the top-left
        }
    }

    public void actionPerformed(ActionEvent e) {
        Rectangle rect;
        for (int i = 0; i < asteroids.size(); i++) {
            rect = asteroids.get(i);
            rect.x -= 10;
            if (rect.x + rect.width < 0) {
                asteroids.remove(rect);
                addAsteroid(false);
            }
        }

        spaceshipY += spaceshipVel;
        spaceshipX += spaceshipVelX;

        if (spaceshipY < 0 || spaceshipY > HEIGHT - SPACESHIP_HEIGHT) {
            gameOver();
        }

        for (Rectangle asteroid : asteroids) {
            if (asteroid.intersects(new Rectangle(spaceshipX, spaceshipY, SPACESHIP_WIDTH, SPACESHIP_HEIGHT))) {
                gameOver();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            spaceshipVel = -20; // move up
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            spaceshipVel = 30; // move down
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            spaceshipVelX = -30; // move left
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            spaceshipVelX = 20; // move right
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            spaceshipVel = 0; // stop vertical movement
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            spaceshipVelX = 0; // stop horizontal movement
        }
    }

    private void gameOver() {
        timer.stop();
        JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.YES_NO_OPTION);
        System.exit(0);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Starship_Odyssey");
        SpaceGame game = new SpaceGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
