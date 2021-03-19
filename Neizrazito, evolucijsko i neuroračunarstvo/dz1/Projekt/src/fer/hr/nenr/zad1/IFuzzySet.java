package fer.hr.nenr.zad1;

public interface IFuzzySet{
    IDomain getDomain();
    int getValueAt(DomainElement element) throws NoElementException;
    double[] getMemberships();
}
