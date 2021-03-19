package fer.hr.nenr.zad1;

import java.util.Arrays;
import java.util.Objects;

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
    public double getValueAt(DomainElement element) throws NoElementException {
        return memberships[getIndexOf(element)];
    }

    public int getIndexOf(DomainElement element) throws NoElementException {
        return domena.indexOfElement(element);
    }

    public MutableFuzzySet set(DomainElement e, double membership) throws NoElementException {
        memberships[getIndexOf(e)]=membership;
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


    //Dodano zbog drugog zadatka equals i hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MutableFuzzySet that = (MutableFuzzySet) o;
        return Arrays.equals(memberships, that.memberships) &&
                domena.equals(that.domena);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(domena);
        result = 31 * result + Arrays.hashCode(memberships);
        return result;
    }
}
