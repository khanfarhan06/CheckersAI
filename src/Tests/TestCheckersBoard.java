package Tests;

import Checkers.*;

import java.util.List;

public class TestCheckersBoard {

    public static void main(String[] args) {
        CheckersBoard checkersBoard = new CheckersBoard();
        setPiecesForWhiteKingCircularJumpTest(checkersBoard);
        System.out.println("Initial Board Position:");
        checkersBoard.showBoard();
        List<JumpMove> legalJumps =  checkersBoard.getAllJumpMoves();
        for(JumpMove move: legalJumps){
            CheckersBoard checkersBoardCopy = checkersBoard.clone();
            checkersBoardCopy.makeMove(move);
            System.out.println("Jumped Position");
            checkersBoardCopy.showBoard();
        }
    }

    private static void setPiecesForWhiteKingCircularJumpTest(CheckersBoard checkersBoard){
        checkersBoard.setAllianceToMove(Alliance.WHITE);
        checkersBoard.setAllPiecesEmpty();
        checkersBoard.setPieceAtPosition(4,3,Piece.WHITE_KING);
        checkersBoard.setPieceAtPosition(3,2,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(3,4,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(3,6,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(5,2,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(5,4,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(5,6,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,2,Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,4,Piece.BLACK_PAWN);
    }

    private static void setPiecesForWhiteJumpTest(CheckersBoard checkersBoard){
        checkersBoard.setAllianceToMove(Alliance.WHITE);
        checkersBoard.setAllPiecesEmpty();
        checkersBoard.setPieceAtPosition(4,3, Piece.WHITE_PAWN);
        checkersBoard.setPieceAtPosition(3,2, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(3,4, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,4, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,6, Piece.BLACK_PAWN);
    }

    private static void setPiecesForWhiteJumpTest2(CheckersBoard checkersBoard){
        checkersBoard.setAllianceToMove(Alliance.WHITE);
        checkersBoard.setAllPiecesEmpty();
        checkersBoard.setPieceAtPosition(4,3, Piece.WHITE_PAWN);
        checkersBoard.setPieceAtPosition(3,2, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(3,4, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,0, Piece.BLACK_PAWN);
        checkersBoard.setPieceAtPosition(1,2, Piece.BLACK_PAWN);
    }

    private static void setPiecesForBlackJumpTest(CheckersBoard checkersBoard){
        checkersBoard.setAllianceToMove(Alliance.BLACK);
        checkersBoard.setAllPiecesEmpty();
        checkersBoard.setPieceAtPosition(4,3, Piece.WHITE_PAWN);
        checkersBoard.setPieceAtPosition(3,4, Piece.BLACK_KING);
        checkersBoard.setPieceAtPosition(4,5, Piece.WHITE_PAWN);
        checkersBoard.setPieceAtPosition(6,5, Piece.WHITE_PAWN);
        checkersBoard.setPieceAtPosition(6,7, Piece.WHITE_PAWN);
    }
}
