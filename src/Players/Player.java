package Players;

import Checkers.Alliance;
import Checkers.CheckersBoard;
import Checkers.Move;

public abstract class Player {
    private Alliance alliance;

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance){
        this.alliance = alliance;
    }

    abstract public Move bestMove(CheckersBoard checkersBoard);

}
