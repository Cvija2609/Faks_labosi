package fer.hr.nenr.zad1;

public interface IDomain extends Iterable<DomainElement>{
    int getCardinality();
    IDomain getComponent(int indexAt);
    int getNumberOfComponents();
    int indexOfElement(DomainElement element) throws NoElementException;
    DomainElement elementForIndex(int index);
}
