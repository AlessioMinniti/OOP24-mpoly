package it.unibo.monopoly.model.gameboard.api.chances_communiy.api;

/**
 * interface for a generic parser.
 */
public interface Parser {

    /**
     * this method tells if the string has come to its end.
     * @return wether or not the string has reached the end
     */
    boolean hasNesxt();

    /**
     * this method get the next substring based on the type of parser.
     * @return the substring
     */
    String next();

}
