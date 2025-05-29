package it.unibo.monopoly.controller.api;

import java.util.List;

import it.unibo.monopoly.model.gameboard.api.Pawn;
import it.unibo.monopoly.model.gameboard.api.Property;
import it.unibo.monopoly.model.gameboard.api.Tile;
import it.unibo.monopoly.model.turnation.api.Player;
import it.unibo.monopoly.utils.impl.Configuration;
import it.unibo.monopoly.view.api.GameboardView;
import it.unibo.monopoly.view.api.MainGameView;

/**
 * interface for game controller of the game.
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
     * Loads the game rules from the file
     * and asks the {@link MainGameView} to display them.
     */
    void loadRules();

    /**
     * Get the {@link Configuration} for game settings.
     * @return the {@link Configuration} associated to this controller
     */
    Configuration getConfiguration();

    /**
     * Retrieves the player that is 
     * currently playing its turn and
     * asks the {@link MainGameView} to display
     * its information.
     */
    void loadCurrentPlayerInformation();

    /**
     * Allow to attach {@link MainGameView}s to the controller.
     * @param view the view we want to attach to this controller
     */
    void attachView(MainGameView view);

    /**
     * set the game over.
    */
    void gameOver();
    /**
     * remove the player who lose.
    */
    void playerGameOver();
    /**
     * change the pawns position.
    */
    void changePositions();
    /**
     * called if a player buy an house.
     * @param prop
    */
    void addHouse(Property prop);
    /**
     * called if a player buy an hotel.
     * @param prop
    */
    void addHotel(Property prop);
    /**
     * get the size of the board.
     * @param numTiles
     * @return int
    */
    int getSize(int numTiles);
    /**
     * get the tiles.
     * @return List Tile
    */
    List<Tile> getTiles();
    /**
     * get the list of the pawns.
     * @return List Pawn
    */
    List<Pawn> getPawns();

    /**
     * set the gameboard view.
     * @param view
    */
    void setBoardView(GameboardView view);

    /**
     * get the current player.
     * @return Player
    */
    Player getCurrPlayer();

}
