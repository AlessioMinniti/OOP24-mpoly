package it.unibo.monopoly.controller.api;
/**
 * Main controller of the game.
 */
public interface GameController {

    /**
     * End the turn of the user that's currently playing and 
     * start next player's turn.
     * If the player cannot end its turn execution will result
     * in an exception
     */
    void endTurn();

    /**
     * Throw the dices and update the position of the pawn on the gameBoard.
     */
    void throwDices();


    /**
     * Buy the property occupied by the player’s pawn
     * whose turn it is, if the property is not owned 
     * by any other player.
     */
    void buyProperty();

    /**
     * Pay the rent amount to the owner of the property
     * occupied by the player's pawn whose turn it is.
     */
    void payPropertyOwner();


    /**
     * Sell a {@link TitleDeed} back to the bank and refund the owner.
     * @param titledeedName the name of the {@link TitleDeed} to sell.
     */
    void sellProperty(String titledeedName);
}
