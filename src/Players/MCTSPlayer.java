package Players;

import Checkers.*;

import java.util.ArrayList;
import java.util.List;

public class MCTSPlayer extends Player{
    private static RandomPlayer randomPlayer1 = new RandomPlayer();
    private static RandomPlayer randomPlayer2 = new RandomPlayer();
    private static double epsilon = 1e-6;
    MCTSNode root;
    int maximumThinkingTime;

    public MCTSPlayer(int maximumThinkingTime) {
        this.maximumThinkingTime = maximumThinkingTime;
    }

    @Override
    public Move bestMove(CheckersBoard checkersBoard) {
        root = new MCTSNode(checkersBoard);
        int count = 0;
        while(count<maximumThinkingTime){
            selectAction();
            count++;
        }
        if(root.isLeaf())
            return null;
        Move bestMove = root.select().lastMove;
        return bestMove;
    }

    private void selectAction(){
        List<MCTSNode> visited = new ArrayList<>();
        MCTSNode current = root;
        visited.add(current);
        while(!current.isLeaf()){
            current = current.select();
        }

        int value;
        if(current.expand()){
            MCTSNode newNode = current.select();
            visited.add(newNode);
            value = newNode.rollOut();
        }else{
            value = (current.checkersBoard.getAllianceToMove() == Alliance.WHITE)? -1: 1;
        }
        for(MCTSNode node: visited){
            node.updateStatus(value);
        }
    }


    class MCTSNode{
        CheckersBoard checkersBoard;
        Move lastMove;
        int numberOfVisits, totalValue;
        List<MCTSNode> children = new ArrayList<>();

        public MCTSNode(CheckersBoard checkersBoard) {
            this.checkersBoard = checkersBoard;
        }

        public MCTSNode(CheckersBoard checkersBoard, Move lastMove) {
            this.checkersBoard = checkersBoard;
            this.lastMove = lastMove;
        }

        public void updateStatus(int value) {
            this.numberOfVisits++;
            this.totalValue += value;
        }

        public int rollOut() {
            Game game = new Game(randomPlayer1, randomPlayer2, this.checkersBoard.clone());
            GameResult matchResult = game.start();
            if(matchResult == GameResult.WON)
                return 1;
            else if(matchResult == GameResult.DRAWN)
                return 0;
            else
                return -1;
        }

        public boolean expand() {
            List<Move> legalMoves = this.checkersBoard.getAllMoves();
            if(legalMoves.size() == 0)
                return false;
            for(Move move: legalMoves){
                CheckersBoard nextBoard = this.checkersBoard.clone();
                nextBoard.makeMove(move);
                children.add(new MCTSNode(nextBoard,  move));
            }
            return true;
        }

        public MCTSNode select() {
            MCTSNode selected = null;
            if(root.checkersBoard.getAllianceToMove() == Alliance.WHITE){
                double bestValue = Integer.MIN_VALUE;
                for (MCTSNode child: this.children){
                    double uctValue =
                            (double) child.totalValue/(child.numberOfVisits + epsilon) +
                                    Math.sqrt(2)* Math.sqrt(Math.log(this.numberOfVisits+1)/(child.numberOfVisits + epsilon)) +
                                    Math.random()*epsilon;
                    if(uctValue>bestValue){
                        bestValue = uctValue;
                        selected = child;
                    }
                }
            }else{
                double bestValue = Integer.MAX_VALUE;
                for (MCTSNode child: this.children){
                    double uctValue =
                            (double) child.totalValue/(child.numberOfVisits + epsilon) +
                                    Math.sqrt(2)* Math.sqrt(Math.log(this.numberOfVisits+1)/(child.numberOfVisits + epsilon)) +
                                    Math.random()*epsilon;
                    if(uctValue<bestValue){
                        bestValue = uctValue;
                        selected = child;
                    }
                }
            }
            return selected;
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }
    }
}


