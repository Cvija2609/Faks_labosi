package fer.hr.nenr.zad1;

import java.util.Arrays;

public class MutableFuzzySet implements IFuzzySet{
    private double[] memberships;
    private IDomain domena;

    public MutableFuzzySet(IDomain domena){
        this.domena=domena;
        memberships = new double[this.domena.getCardinality()];
    }

    @Override
    public IDomain getDomain() {
        return this.domena;
    }

    @Override
    public int getValueAt(DomainElement element) throws NoElementException {
        return domena.indexOfElement(element);
    }

    public MutableFuzzySet set(DomainElement e, double membership) throws NoElementException {
        memberships[getValueAt(e)]=membership;
        return this;
    }

    @Override
    public double[] getMemberships() {
        return memberships;
    }

    @Override
    public String toString() {
        return "MutableFuzzySet{" +
                "memberships=" + Arrays.toString(memberships) +
                ", domena=" + domena +
                '}';
    }
}
