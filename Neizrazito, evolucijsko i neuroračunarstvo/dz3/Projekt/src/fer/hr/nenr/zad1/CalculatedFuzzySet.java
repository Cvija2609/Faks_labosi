package fer.hr.nenr.zad1;

import java.util.Arrays;

public class CalculatedFuzzySet implements IFuzzySet{
    private IDomain domain;
    private double[] memberships;

    public CalculatedFuzzySet(IDomain domain, IIntUnaryFunction function){
        this.domain=domain;
        int kardinalitet = domain.getCardinality();
        memberships = new double[kardinalitet];
        for(int i=0;i<kardinalitet;i++){
            memberships[i]=function.valueAt(i);
        }
    }

    @Override
    public IDomain getDomain() {
        return domain;
    }

    @Override
    public double getValueAt(DomainElement element) throws NoElementException {
        return memberships[getIndexOf(element)];
    }

    @Override
    public double[] getMemberships() {
        return memberships;
    }

    @Override
    public int getIndexOf(DomainElement element) throws NoElementException {
        return domain.indexOfElement(element);
    }

    public void setMemberships(double[] memberships) {
        this.memberships = memberships;
    }

    @Override
    public String toString() {
        return "CalculatedFuzzySet{" +
                "domain=" + domain +
                ", memberships=" + Arrays.toString(memberships) +
                '}';
    }
}
