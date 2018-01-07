package GUI;

import Checkers.*;
import Players.HumanPlayer;
import Players.Player;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class GamePanel extends JPanel{
    private Player player1, player2, toMove;
    private Stack<CheckersBoard> gameStack = new Stack<>();
    private final BoardPanel boardPanel;
    private final MovePanel movePanel;
    private boolean isClickedBefore;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private static final Dimension BOARD_PANEL_DIMENSON = new Dimension(400,350);
    private static final Dimension TITLE_PANEL_DIMENSON = new Dimension(400,350);
    private static final Dimension MOVE_PANEL_DIMENSION = new Dimension(150,350);

    public GamePanel(CheckersBoard checkersBoard, Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        player1.setAlliance(Alliance.WHITE);
        player2.setAlliance(Alliance.BLACK);
        toMove = checkersBoard.getAllianceToMove()==Alliance.WHITE ? player1 : player2;
        this.gameStack.push(checkersBoard);
        this.setLayout(new BorderLayout());
        boardPanel = new BoardPanel();
        boardPanel.showBoard();
        movePanel = new MovePanel();
        this.add(movePanel, BorderLayout.EAST);
        this.add(boardPanel, BorderLayout.CENTER);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                GamePanel.this.start();
            }
        });
        thread.start();
    }

    private void start(){
        while(gameStack.peek().getPlyCount()<200){
            if(gameStack.peek().getAllMoves().size() == 0){
                JOptionPane.showMessageDialog(null,"Match Over:\n"+gameStack.peek().getAllianceToMove()+" LOST");
            }
            if(!(toMove instanceof HumanPlayer)){
                Move move = toMove.bestMove(gameStack.peek().clone());
                CheckersBoard newBoardPosition = gameStack.peek().clone();
                newBoardPosition.makeMove(move);
                gameStack.push(newBoardPosition);
                boardPanel.showBoard();
                toMove = toMove == player1 ? player2 : player1;
            }
        }
    }
    class MovePanel extends JPanel{
        JTextArea textArea;
        MovePanel(){
            JLabel label = new JLabel("Move List:");
            textArea = new JTextArea();
            textArea.setText("move list goes here");
            this.setLayout(new BorderLayout());
            this.add(label, BorderLayout.NORTH);
            this.add(textArea, BorderLayout.CENTER);
            this.setPreferredSize(MOVE_PANEL_DIMENSION);
        }
    }

    class BoardPanel extends JPanel {
        TilePanel[][] boardTiles;
        boolean[][] possibleTiles;
        Move[][] possibleMoves;

        BoardPanel(){
            super(new GridLayout(8,8));
            this.possibleTiles = new boolean[8][8];
            this.possibleMoves = new Move[8][8];
            this.boardTiles = new TilePanel[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardTiles[i][j] = new TilePanel(i,j);
                    add(boardTiles[i][j]);
                }
            }
            setPreferredSize(BOARD_PANEL_DIMENSON);
        }

        public void resetPossibleTiles(){
            possibleTiles = new boolean[8][8];
            possibleMoves = new Move[8][8];
        }

        public void updatePossibleTiles(List<Move> legalMoves){
            for(Move move: legalMoves){
                if(move instanceof SimpleMove)
                    updatePossibleTilesForMove((SimpleMove) move);
                else if(move instanceof JumpMove)
                    updatePossibleTiles((JumpMove) move);
            }
        }

        private void updatePossibleTilesForMove(SimpleMove move){
            int finalRow = move.finalRowPosition;
            int finalColumn = move.finalColumnPosition;
            possibleTiles[finalRow][finalColumn] = true;
            possibleMoves[finalRow][finalColumn] = move;
        }

        private void updatePossibleTiles(JumpMove move){
            int finalRow = move.jumps.get(move.jumps.size()-1).finalRowPosition;
            int finalColumn = move.jumps.get(move.jumps.size()-1).finalColumnPosition;
            possibleTiles[finalRow][finalColumn] = true;
            possibleMoves[finalRow][finalColumn] = move;
        }

        public void showBoard(){
            removeAll();
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardTiles[i][j].drawTile();
                    add(boardTiles[i][j]);
                }
            }
            repaint();
        }
    }

    class TilePanel extends JPanel{
        int row, column;
        JLabel label;
        public TilePanel(int i, int j) {
            super(new GridBagLayout());
            row = i;
            column = j;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(!isClickedBefore && (row+column)%2 != 0){
                        List<Move> legalMoves = gameStack.peek().getAllMovesOfPieceAt(row, column);
                        boardPanel.updatePossibleTiles(legalMoves);
                        isClickedBefore = true;
                    }else{
                        if(boardPanel.possibleTiles[row][column]){
                            Move moveToMake = boardPanel.possibleMoves[row][column];
                            CheckersBoard nextBoardPosition = gameStack.peek().clone();
                            nextBoardPosition.makeMove(moveToMake);
                            nextBoardPosition.showBoard();
                            gameStack.push(nextBoardPosition);
                            toMove = toMove == player1 ? player2: player1;
                        }
                        isClickedBefore = false;
                        boardPanel.resetPossibleTiles();
                    }
                    boardPanel.showBoard();
                    GamePanel.this.repaint();
                    boardPanel.repaint();
                }
            });
            setPreferredSize(TITLE_PANEL_DIMENSON);
        }

        void drawTile(){
            removeAll();
            assignTileColor();
            assignTilePiece();
            repaint();
        }

        void assignTileColor() {
            if(boardPanel.possibleTiles[row][column]){
                this.setBackground(Color.GREEN);
                return;
            }
            Color tileColor = (row+column)%2==0? Color.BLACK: Color.WHITE;
            this.setBackground(tileColor);
        }

        void assignTilePiece() {
            Piece piece = gameStack.peek().getBoard()[row][column];
            if(piece==null)
                return;
            BufferedImage image = null;
            try {
                switch (piece) {
                    case WHITE_PAWN:
                        image = ImageIO.read(new File("./src/GUI/PieceArt/wp.png"));
                        break;
                    case WHITE_KING:
                        image = ImageIO.read(new File("./src/GUI/PieceArt/wk.png"));
                        break;
                    case BLACK_PAWN:
                        image = ImageIO.read(new File("./src/GUI/PieceArt/bp.png"));
                        break;
                    case BLACK_KING:
                        image = ImageIO.read(new File("./src/GUI/PieceArt/bk.png"));
                        break;
                    default:
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            if(image!=null){
                label = new JLabel(new ImageIcon(image));
                add(label);
            }
        }
    }
}
