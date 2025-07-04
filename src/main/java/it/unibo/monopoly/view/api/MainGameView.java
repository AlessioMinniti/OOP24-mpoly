package it.unibo.monopoly.view.api;

import java.awt.Color;
import java.util.List;
import java.util.Set;

import it.unibo.monopoly.controller.api.GameController;
import it.unibo.monopoly.model.gameboard.api.Board;
import it.unibo.monopoly.model.gameboard.api.Pawn;
import it.unibo.monopoly.model.gameboard.api.Property;
import it.unibo.monopoly.model.gameboard.api.Special;
import it.unibo.monopoly.model.gameboard.api.Tile;
import it.unibo.monopoly.model.transactions.api.Bank;
import it.unibo.monopoly.model.transactions.api.BankAccount;
import it.unibo.monopoly.model.transactions.api.PropertyActionsEnum;
import it.unibo.monopoly.model.transactions.api.TitleDeed;
import it.unibo.monopoly.model.turnation.api.Player;

/**
 * Interface for the game view.
 * An object that displays to the user the ongoing game,
 * allowing it to play and iteract with it.
 */
public interface MainGameView {

    /**
     * Clears all panels displaying information about the 
     * current player and resets them, to be ready for the turn of a new player to begin.
     * @param canThrowDices tells whether to enable or not the button to throw the dices
     */
    void refreshUIForNewTurn(boolean canThrowDices);

    /**
     * Ask the {@code view} to refresh the information related
     * to the player that is currently playing. 
     * This method requires the {@link Player} instance and its 
     * associated {@link BankAccount} to be passed as input.
     * @param player an object containing generic information of a player
     * @param account an object containing all information related to the {@code balance} of a specific player
     */
    void displayPlayerInfo(Player player, BankAccount account);

    /**
     * Display information of the {@link TitleDeed}
     * corresponding to the {@link Property} the player's {@link Pawn}
     * is currently on.
     * @param propertyContract the title deed to display
     */
    void displayPropertyContractInfo(TitleDeed propertyContract);

    /**
     * Display information of the {@link Special}
     * corresponding to the {@link Tile} the player's {@link Pawn}
     * is currently on.
     * @param tile the tile to display
     */
    void displaySpecialInfo(Special tile);

    /**
     * Display interactable UI elements that show the possible actions for a player.
     * @param actions the names of the actions that the player can do. When a player selects an action
     * this will be executed by asking the {@link GameController} instance
     * that was previously attached to this view to execute the action.
     * This is done by calling the method {@link GameController#executeAction(String)}
     */
    void displayPlayerActions(Set<PropertyActionsEnum> actions);

    /**
     * Display the result of the call on {@link GameController#throwDices()}.
     * @param results a {@link List} of {@code int} containing the results of each
     * dice thrown
     */
    void displayDiceResult(List<Integer> results);

    /**
     * Displays the game rules and general information
     * to play the game.
     * @param rules the text of the game rules
     */
    void displayRules(String rules);


    /**
     * Displays all statistics related to the {@link Player}, its {@link BankAccount}
     * and the {@code titledeeds} owned by that player.
     * @param player The player whose statistics have to be displayed
     * The view will then make subsequent calls to controller methods to retrieve
     * all information related to that player.
     * @param bank
     * @param board 
     */
    void displayPlayerStats(Player player, Bank bank, Board board);

    /**
     * Displays an error on the UI.
     * @param e the {@code exception} thrown and whose information has to be displayed.
     */
    void displayError(Exception e);

    /**
     * Displays a generic message to the user.
     * @param message The message to display
     */
    void displayMessage(String message);

    /**
     * call the change position method in the gameboard view.
     */
    void callChangePositions();
    /**
     * call the clear panel method in the gameboard view.
     * @param name the property to clear
     */
    void callClearPanel(String name);
    /**
     * call the buy property method in the gameboard view.
     * @param name name of the property to buy
     * @param color color of the player who buy the property
     */
    void callBuyProperty(String name, Color color);
    /**
     * Displays a generic yes/no option message sent to the user.
     * @param message The message to display
     */
    void displayOptionMessage(String message);
    /**
     * call the gameboard view method to buy an house for the property.
     * @param propName property name
     * @param color color
     * @param nHouses current number of houses
     */
    void callBuyHouse(String propName, Color color, int nHouses);
    /**
     * call the gameboard view method to buy the hotel for the property.
     * @param propName property name
     * @param color color
     */
    void callBuyHotel(String propName, Color color);
    /**
     * call the gameboard view method to sell an house for the property.
     * @param propName property name
     * @param nHouses current number of houses 
     * @param color player color
     */
    void callSellHouse(String propName, int nHouses, Color color);
    /**
     * call the gameboard view method to sell the hotel for the property.
     * @param propName property name
     * @param color color
     */
    void callSellHotel(String propName, Color color);

    /**
     * call the delete player method in gameboard view.
     * @param color color of the player
     * @param id of the player
    */
    void callDeletePlayer(Color color, int id);
    /**
     * call the frame that shows the final ranking.
     */
    void showRanking();
    /**
     * calls the clear all method in the gameboard view.
     */
    void callClearAll();
}
