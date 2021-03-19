package fer.hr.nenr.zad1;

public interface IFuzzySet{
    IDomain getDomain();
    double getValueAt(DomainElement element) throws NoElementException;
    double[] getMemberships();
    int getIndexOf(DomainElement element) throws NoElementException;
}
