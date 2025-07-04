package it.unibo.monopoly.model.transactions.impl.titledeed;

import java.util.List;
import java.util.Set;

import it.unibo.monopoly.model.gameboard.impl.Group;
import it.unibo.monopoly.model.transactions.api.RentOption;
import it.unibo.monopoly.model.transactions.api.RentOptionFactory;
import it.unibo.monopoly.model.transactions.api.SpecialPropertyFactory;
import it.unibo.monopoly.model.transactions.api.TitleDeed;
import it.unibo.monopoly.model.transactions.impl.rentoption.RentOptionFactoryImpl;

/**
 * implementation of the special property factory interface.
 */
public final class SpecialPropertyFactoryImpl implements SpecialPropertyFactory {

    private final RentOptionFactory f = new RentOptionFactoryImpl();

    @Override
    public TitleDeed station(final String name) {

        final int startRent = 50;
        final int salePrice = 200;
        final List<RentOption> rentO = f.progressivelyIncreasingPrice(startRent, 2, 4);
        final List<RentOption> rent = rentO.subList(1, rentO.size());
        return new BaseTitleDeed(Group.STATION, name, salePrice, p -> p / 4 * 3, startRent, rent);
    }

    @Override
    public TitleDeed society(final String name) {
        final int salePrice = 120;
        final int startFactor = 5;
        final List<RentOption> rentO = f.progressivelyIncreasingPrice(startFactor, 2, 2);
        final List<RentOption> rent = rentO.subList(1, rentO.size());
        return new TitleDeed() {

            private final TitleDeed titleDeed = new BaseTitleDeed(Group.SOCIETY, name, salePrice,
                                                             p -> p / 4 * 3, startFactor, rent);

            @Override
            public int getOwnerId() {
                return titleDeed.getOwnerId();
            }

            @Override
            public void setOwner(final int ownerId) {
                titleDeed.setOwner(ownerId);
            }

            @Override
            public void removeOwner() {
                titleDeed.removeOwner();
            }

            @Override
            public Group getGroup() {
                return titleDeed.getGroup();
            }

            @Override
            public String getName() {
                return titleDeed.getName();
            }

            @Override
            public Integer getSalePrice() {
                return titleDeed.getSalePrice();
            }

            @Override
            public Integer getMortgagePrice() {
                return titleDeed.getMortgagePrice();
            }

            @Override
            public Integer getRent(final Set<TitleDeed> groupTitleDeeds, final int diceThrow) {
                return titleDeed.getRent(groupTitleDeeds, diceThrow) * diceThrow;
            }

            @Override
            public List<RentOption> getRentOptions() {
                return titleDeed.getRentOptions();
            }

            @Override
            public boolean isOwned() {
                return titleDeed.isOwned();
            }

            @Override
            public int getHousePrice() {
                return titleDeed.getHousePrice();
            }

            @Override
            public int getHotelPrice() {
                return titleDeed.getHotelPrice();
            }

        };
    }

}
