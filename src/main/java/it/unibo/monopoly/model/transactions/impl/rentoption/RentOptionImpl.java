package it.unibo.monopoly.model.transactions.impl.rentoption;

import java.util.Set;
import java.util.function.BiPredicate;

import it.unibo.monopoly.model.gameboard.api.Property;
import it.unibo.monopoly.model.transactions.api.RentOption;
import it.unibo.monopoly.model.transactions.api.TitleDeed;

/**
 * Unmodifiable implementation of a {@link RentOption}.
 * @param title The title of this rent option
 * @param description Additional information about how cost is calculated 
 * and applicability conditions
 * @param price the amount of money to pay if this option is selected
 * @param applicabilityCondition used to verify whether this rent option 
 * can be chosen or not
 */
public record RentOptionImpl(String title,
                            String description,
                            int price,
                            BiPredicate<Set<TitleDeed>, Integer> applicabilityCondition) implements RentOption {

    @Override
    public String getTitle() {
       return this.title();
    }

    @Override
    public String getDescription() {
        return this.description();
    }

    @Override
    public int getPrice() {
        return this.price();
    }

    @Override
    public boolean canBeApplied(final Set<TitleDeed> groupDeeds, final int ownerId, final Property property) {
        return applicabilityCondition.test(groupDeeds, ownerId);
    }
}
