package Checkers;

import Players.Player;

/***
 * Game class represents a Game of checkers where two Players play against each other
 * Member Variables:
 *              checkersBoard - The current board position in the game
 *              player1 - The first Player, who takes White alliance
 *              player2 - The second Player, who takes Black alliance
 *              playerTomove - The Player whose move it is in the current board position
 */
public class Game {
    private final CheckersBoard checkersBoard;
    private final Player player1;
    private final Player player2;
    private Player playerToMove;


    /***
     * Parameterized constructor creates a game with player1 as White and player2 as Black
     * and the board position is set to the position at start of a checkers game
     * @param player1 First Player who takes White
     * @param player2 Second Player who takes Black
     */
    public Game(Player player1, Player player2){
        //Set player1 and player2 to player1 and player2 respectively
        this.player1 = player1;
        this.player2 = player2;
        //Set the alliance of player1 to White and player2 to Black
        player1.setAlliance(Alliance.WHITE);
        player2.setAlliance(Alliance.BLACK);
        //Create a new CheckersBoard and set to initial board position
        checkersBoard = new CheckersBoard();
        checkersBoard.setInitialBoardPosition();
        //Set player to move to the player1
        playerToMove = player1;
    }

    /***
     * Parameterized constructor creates a game with player1 as White and player2 as Black
     * and the board position is set to the given board postion
     * @param player1 First player who takes White
     * @param player2 Second player who takes Black
     * @param checkersBoard The given CheckersBoard position
     */
    public Game(Player player1, Player player2, CheckersBoard checkersBoard){
        //Set player1 and player2 to player1 and player2 respectively
        this.player1 = player1;
        this.player2 = player2;
        //Set the alliance of player1 to White and player2 to Black
        player1.setAlliance(Alliance.WHITE);
        player2.setAlliance(Alliance.BLACK);
        //Set the checkersboard position to the given checkersboard position
        this.checkersBoard = checkersBoard;
        //Set the playerToMove to the player whose alliance is to make the next move according to the given board position
        this.playerToMove = (checkersBoard.getAllianceToMove()== Alliance.WHITE)? player1 : player2;
    }

    /***
     * Starts a game of checkers between the two players from the current board position
     * @return  GameResult object which represents the result of the match from the perspective of the White Alliance
     */
    public GameResult start(){
        //Keeping making alternate moves from each player till the game ends
        while (checkersBoard.getPlyCount() < 200){      //Game ends in draw if 200 plyCount is reached, hence the condition
            //Get the best move from the player whose turn it is
            Move nextMove = playerToMove.bestMove(checkersBoard.clone());
            //Game ends in loss for the player to move if he has no possible moves to make, So check fro this condition
            if (nextMove==null){    //Condition to check if the player to move had no possible move
                if(playerToMove.getAlliance()== Alliance.WHITE) //Condition to check if the alliance of the payer to move was White
                    return GameResult.LOST;     //As White has lost, return LOST as GameResult
                else                                            //If the alliance of the player to move was Black
                    return GameResult.WON;      //As Black has lost, return WON as GameResult as White has won
            }
            //Make the found move on the board position
            checkersBoard.makeMove(nextMove);
            //Alternate the playerToMove to the other player
            playerToMove = playerToMove == player1 ? player2: player1;
        }
        //If the Game reaches a plyCount of 200, game has drawn so return DRAWN as GameResult
        return GameResult.DRAWN;
    }

    /***
     * Starts a game of checkers between the two players from the current board position, and prints the board position before each move is made
     * @return  GameResult object which represents the result of the match from the perspective of the White Alliance
     */
    public GameResult startAndShow(){
        //Keeping making alternate moves from each player till the game ends
        while (checkersBoard.getPlyCount() < 200){      //Game ends in draw if 200 plyCount is reached, hence the condition
            //Show the board position
            checkersBoard.showBoard();

            //Get the best move from the player whose turn it is
            Move nextMove = playerToMove.bestMove(checkersBoard.clone());
            if(nextMove instanceof JumpMove)
                System.out.println("********JumpMove has been made*********");
            //Game ends in loss for the player to move if he has no possible moves to make, So check fro this condition
            if (nextMove==null){    //Condition to check if the player to move had no possible move
                if(playerToMove.getAlliance()== Alliance.WHITE) //Condition to check if the alliance of the payer to move was White
                    return GameResult.LOST;     //As White has lost, return LOST as GameResult
                else                                            //If the alliance of the player to move was Black
                    return GameResult.WON;      //As Black has lost, return WON as GameResult as White has won
            }
            //Make the found move on the board position
            checkersBoard.makeMove(nextMove);
            //Alternate the playerToMove to the other player
            playerToMove = playerToMove == player1 ? player2: player1;
        }
        //If the Game reaches a plyCount of 200, game has drawn so return DRAWN as GameResult
        return GameResult.DRAWN;
    }
}
