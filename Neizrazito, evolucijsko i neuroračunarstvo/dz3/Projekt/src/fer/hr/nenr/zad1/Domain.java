package fer.hr.nenr.zad1;

public abstract class Domain implements IDomain{
    public Domain(){
    }

    public int indexOfElement(DomainElement element) throws NoElementException{
        return 0;
    }

    public DomainElement elementForIndex(int index) throws IndexOutOfBoundsException{
        return null;
    }

    public static IDomain intRange(int first, int last){
        return new SimpleDomain(first, last);
    }

    public static Domain combine(IDomain ... elements) throws ProductMustHaveAtLeastTwoVariablesException {
        return new CompositeDomain(elements);
    }
}
