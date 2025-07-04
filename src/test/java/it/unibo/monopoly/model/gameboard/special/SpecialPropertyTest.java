package it.unibo.monopoly.model.gameboard.special;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Set;

import it.unibo.monopoly.model.transactions.api.SpecialPropertyFactory;
import it.unibo.monopoly.model.transactions.api.TitleDeed;
import it.unibo.monopoly.model.transactions.impl.titledeed.SpecialPropertyFactoryImpl;



class SpecialPropertyTest {

    private static final int PLAYER1_ID = 1;

    private static final String STATION_NAME1 = "Nord";
    private static final String STATION_NAME2 = "Sud";
    private static final String STATION_NAME3 = "Ovest";
    private static final String STATION_NAME4 = "Est";
    private static final String SOCIETY_NAME1 = "Acqua";
    private static final String SOCIETY_NAME2 = "Elettricità";


    private final SpecialPropertyFactory factory = new SpecialPropertyFactoryImpl();


    @Test
    void station() {
        final TitleDeed s1 = factory.station(STATION_NAME1);
        final TitleDeed s2 = factory.station(STATION_NAME2);
        final TitleDeed s3 = factory.station(STATION_NAME3);
        final TitleDeed s4 = factory.station(STATION_NAME4);
        final int dice1 = 3;
        final int expected1 = 50;
        final int expected2 = 100;
        final int expected3 = 200;
        final int expected4 = 400;

        s1.setOwner(PLAYER1_ID);
        assertEquals(expected1, s1.getRent(Set.of(s1), dice1));
        s2.setOwner(PLAYER1_ID);
        assertEquals(expected2, s1.getRent(Set.of(s2, s1), dice1));
        s3.setOwner(PLAYER1_ID);
        assertEquals(expected3, s1.getRent(Set.of(s3, s2, s1), dice1));
        s4.setOwner(PLAYER1_ID);
        assertEquals(expected4, s1.getRent(Set.of(s4, s3, s2, s1), dice1));
    }

    @Test
    void society() {
        final TitleDeed s1 = factory.society(SOCIETY_NAME1);
        final TitleDeed s2 = factory.society(SOCIETY_NAME2); 
        final int expected1 = 50;
        final int expected2 = 70;
        final int dice1 = 10;
        final int dice2 = 7;

        s1.setOwner(PLAYER1_ID);
        assertEquals(expected1, s1.getRent(Set.of(s1), dice1));
        s2.setOwner(PLAYER1_ID);
        assertEquals(expected2, s1.getRent(Set.of(s2, s1), dice2));
    }
}
