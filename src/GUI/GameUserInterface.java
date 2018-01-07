package GUI;

import Checkers.CheckersBoard;
import Checkers.Piece;
import Players.HumanPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameUserInterface {

    private final JFrame gameFrame;
    private final GamePanel gamePanel;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(700,600);

    public GameUserInterface(CheckersBoard checkersBoard) {
        gameFrame = new JFrame("CheckersAI");
        gameFrame.setLayout(new BorderLayout());
        gamePanel = new GamePanel(checkersBoard, new HumanPlayer(), new HumanPlayer());
        gameFrame.add(gamePanel);
        gameFrame.setJMenuBar(createMenuBar());
        gameFrame.setSize(OUTER_FRAME_DIMENSION);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    private JMenuBar createMenuBar(){
        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(createFileMenu());
        return jMenuBar;
    }

    private JMenu createFileMenu() {
        JMenu jMenu = new JMenu("File");
        JMenuItem savePGNMenuItem = new JMenuItem("Save As PGN");
        savePGNMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Saved");
            }
        });
        jMenu.add(savePGNMenuItem);
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        jMenu.add(exitMenuItem);
        return jMenu;
    }


}
