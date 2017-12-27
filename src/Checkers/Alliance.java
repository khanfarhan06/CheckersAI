package Checkers;

/***
 * The Alliance Enumeration defines the two alliances in the checkers game - WHITE and BLACK
 */
public enum Alliance {
    WHITE{
        @Override
        /***
         * @return The Alliance of opposite player,which for WHITE is BLACK
         */
        public Alliance getOppositeAlliance(){
            return BLACK;
        }
    },
    BLACK{
        @Override
        /***
         * @return The Alliance of the opposite player,which for BLACK is WHITE
         */
        public Alliance getOppositeAlliance() {
            return WHITE;
        }
    };

    /***
     * Each Alliance must define a getOppositeAlliance() method which returns the Alliance of the opposite player
     * @return The Alliance of the opposite player
     */
    public abstract Alliance getOppositeAlliance();
}
