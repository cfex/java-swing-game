package com.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import static java.awt.Color.*;

public class GamePanel extends JPanel implements ActionListener {
    //TODO implement restart function
    //TODO store max score as CSV file

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNIT = (SCREEN_HEIGHT * SCREEN_WIDTH) / UNIT_SIZE;
    private final int[] xCoordinate = new int[GAME_UNIT];
    private final int[] yCoordinate = new int[GAME_UNIT];

    private int snakeSize = 3;
    private int delayTime = 80;
    private int pointXCoordinate;
    private int pointYCoordinate;
    private int pointsCounter;
    private boolean gameRunning = false;
    private char movingDirection = 'R';
    private Random random;
    private Timer timer;

    public GamePanel() {
        this.addKeyListener(new OverrideKeyAdapter());
        this.random = new Random();
        this.setBackground(new Color(191, 153, 153));
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        initGame();
    }

    private void initGame() {
        generateRandomPointsCoordinates();
        timer = new Timer(this.delayTime, this);
        timer.restart();
        gameRunning = true;
    }

    private void checkIfPointPicked() {
        if ((xCoordinate[0] == pointXCoordinate) && (yCoordinate[0] == pointYCoordinate)) {
            snakeSize++;
            pointsCounter++;
            generateRandomPointsCoordinates();
        }
    }

    private void moveSnakeParts() {
        for (int i = snakeSize; i > 0; i--) {
            xCoordinate[i] = xCoordinate[(i - 1)];
            yCoordinate[i] = yCoordinate[(i - 1)];
        }

        switch (movingDirection) {
            case 'U':
                yCoordinate[0] = (yCoordinate[0] - UNIT_SIZE);
                break;
            case 'D':
                yCoordinate[0] = (yCoordinate[0] + UNIT_SIZE);
                break;
            case 'L':
                xCoordinate[0] = (xCoordinate[0] - UNIT_SIZE);
                break;
            case 'R':
                xCoordinate[0] = (xCoordinate[0] + UNIT_SIZE);
                break;
        }
    }

    private void generateRandomPointsCoordinates() {
        pointXCoordinate = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        pointYCoordinate = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    private void checkForCollisions() {
        for (int i = snakeSize; i > 0; i--) {
            // Check if snake hits itself
            if ((xCoordinate[0] == xCoordinate[i]) && (yCoordinate[0] == yCoordinate[i])) {
                gameRunning = false;
                break;
            }
            // Check if snake hits borders
            if (xCoordinate[0] < 0 || xCoordinate[0] > SCREEN_WIDTH) {
                gameRunning = false;
            } else if (yCoordinate[0] < 0 || yCoordinate[0] > SCREEN_HEIGHT) {
                gameRunning = false;
            }

            if (!gameRunning) {
                timer.stop();
            }
        }
    }

    private void gameOver(Graphics graphics) {
        graphics.setColor(BLACK);
        graphics.setFont(new Font("Helvetica", Font.BOLD, 40));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER",
                (SCREEN_WIDTH - fontMetrics.stringWidth("GAME OVER")) / 2,
                SCREEN_HEIGHT / 2);
    }

    public void drawComponent(Graphics graphics) {
        ImageIcon starIcon = new ImageIcon("src/resources/star.png");

        if (gameRunning) {
            graphics.drawImage(starIcon.getImage(), pointXCoordinate, pointYCoordinate, this);

            //Fill snake parts
            for (int i = 0; i < snakeSize; i++) {
                graphics.setColor(BLACK);
                graphics.fillRect(xCoordinate[i], yCoordinate[i], UNIT_SIZE, UNIT_SIZE);
            }

            //Display points counter on display
            graphics.setColor(BLACK);
            graphics.setFont(new Font("Helvetica", Font.BOLD, 20));
            FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("SCORE: " + pointsCounter,
                    (SCREEN_WIDTH - fontMetrics.stringWidth("SCORE: " + pointsCounter)) / 2,
                    graphics.getFont().getSize() + 10);
        } else {
            gameOver(graphics);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        drawComponent(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (gameRunning) {
            moveSnakeParts();
            checkForCollisions();
            checkIfPointPicked();
        }

        repaint();
    }

    public class OverrideKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (movingDirection != 'D') movingDirection = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if (movingDirection != 'U') movingDirection = 'D';
                    break;
                case KeyEvent.VK_LEFT:
                    if (movingDirection != 'R') movingDirection = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if (movingDirection != 'L') movingDirection = 'R';
                    break;
            }
        }
    }
}
