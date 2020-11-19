package com.game;

import javax.swing.*;
import java.awt.*;

public class SnakeGameFrame extends JFrame {

    private final int FRAME_WIDTH = 800;
    private final int FRAME_HEIGHT = 800;

    public SnakeGameFrame() throws HeadlessException {
        this.setTitle("Sneaky");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.add(new GamePanel());
        this.setLocationRelativeTo(null);
        this.pack();
        this.setVisible(true);
    }

}
