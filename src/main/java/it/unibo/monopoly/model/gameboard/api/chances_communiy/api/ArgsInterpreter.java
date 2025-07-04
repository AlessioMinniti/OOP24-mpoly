package it.unibo.monopoly.model.gameboard.api.chances_communiy.api;

import it.unibo.monopoly.model.gameboard.api.Board;
import it.unibo.monopoly.model.turnation.api.TurnationManager;

/**
 * an interpreter for arguments of base commands.
 */
public interface ArgsInterpreter {

    /**
     * sets all the argument needed for the Basecommand.
     * @param toInterpretString a string containing 
     * all the arguments in a pseudo natural language
     * @param command the command where the arguments will be added
     * @param board to interpret some arguments
     * @param turnM to interpret some arguments
     */
    void interpret(String toInterpretString, BaseCommand command, Board board, TurnationManager turnM);
}
