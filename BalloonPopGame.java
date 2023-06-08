import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BalloonPopGame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int BALLOON_RADIUS = 50;
    private static final int MAX_BALLOONS = 10;
    private static final int GAME_DURATION = 30; // in seconds

    private List<Balloon> balloons;
    private int score;
    private Timer gameTimer;
    private int timeRemaining;

    public BalloonPopGame() {
        setTitle("Balloon Pop Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        balloons = new ArrayList<>();
        score = 0;

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                checkBalloonClicked(x, y);
            }
        });

        setFocusable(true);
        requestFocusInWindow();

        gameTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timeRemaining--;
                if (timeRemaining <= 0) {
                    endGame();
                }
                repaint();
            }
        });
    }

    public void startGame() {
        score = 0;
        balloons.clear();
        timeRemaining = GAME_DURATION;
        gameTimer.start();
        generateBalloons();
        repaint();
    }

    public void endGame() {
        gameTimer.stop();
        String message;
        if (score == MAX_BALLOONS) {
            message = "Congratulations! You've popped all the balloons!";
        } else {
            message = "Game Over! Your score: " + score;
        }
        JOptionPane.showMessageDialog(this, message);
    }

    private void generateBalloons() {
        Random random = new Random();
        for (int i = 0; i < MAX_BALLOONS; i++) {
            int x = random.nextInt(WIDTH - 2 * BALLOON_RADIUS) + BALLOON_RADIUS;
            int y = random.nextInt(HEIGHT - 2 * BALLOON_RADIUS) + BALLOON_RADIUS;
            balloons.add(new Balloon(x, y));
        }
    }

    private void checkBalloonClicked(int x, int y) {
        for (int i = 0; i < balloons.size(); i++) {
            Balloon balloon = balloons.get(i);
            if (balloon.isAlive() && balloon.contains(x, y)) {
                balloon.pop();
                score++;
                break;
            }
        }
        repaint();
        if (score == MAX_BALLOONS) {
            endGame();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        for (Balloon balloon : balloons) {
            if (balloon.isAlive()) {
                g.fillOval(balloon.getX() - BALLOON_RADIUS, balloon.getY() - BALLOON_RADIUS, 2 * BALLOON_RADIUS, 2 * BALLOON_RADIUS);
            }
        }

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 30);
        g.drawString("Time: " + timeRemaining, 20, 60);
    }

    private class Balloon {
        private int x;
        private int y;
        private boolean alive;

        public Balloon(int x, int y) {
            this.x = x;
            this.y = y;
            this.alive = true;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isAlive() {
            return alive;
        }

        public void pop() {
            alive = false;
        }

        public boolean contains(int x, int y) {
            double distance = Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(y - this.y, 2));
            return distance <= BALLOON_RADIUS;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BalloonPopGame game = new BalloonPopGame();
                game.setVisible(true);
                game.startGame();
            }
        });
    }
}
