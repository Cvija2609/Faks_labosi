package fer.hr.nenr.zad1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SimpleDomain extends Domain{
    private int first;
    private int last;
    private List<DomainElement> elements = new LinkedList<>();

    public SimpleDomain(int first, int last) {
        this.first = first;
        this.last = last;
        for(int i=first;i<last;i++){
            this.elements.add(new DomainElement(new int[]{i}));
        }
    }

    public SimpleDomain(DomainElement elem){
        this.elements.add(elem);
    }

    public int getFirst() {
        return first;
    }

    public int getLast() {
        return last;
    }

    public int getCardinality(){
        return this.last - this.first;
    }

    @Override
    public IDomain getComponent(int indexAt) {
        return this;
    }

    @Override
    public int getNumberOfComponents() {
        return 1;
    }

    @Override
    public int indexOfElement(DomainElement element) throws NoElementException{
        int cnt = 0;
        for(DomainElement e: elements){
            if(e.equals(element)){
                return cnt;
            }
            cnt++;
        }
        throw new NoElementException("Non existing element " + element.toString());
    }

    @Override
    public DomainElement elementForIndex(int index) throws IndexOutOfBoundsException{
        if(elements.size() < index){
            throw new IndexOutOfBoundsException("Max index is " + Integer.toString(elements.size() - 1));
        }
        return elements.get(index);
    }

    @Override
    public Iterator<DomainElement> iterator() {
        return new DomainElementIterator();
    }

    @Override
    public String toString() {
        String s = "SimpleDomain: ";
        for(DomainElement e: elements){
            s+=e.toString()+" ";
        }
        return s;
    }

    public class DomainElementIterator implements Iterator<DomainElement> {
        private int index=0;

        @Override
        public boolean hasNext() {
            return index<elements.size();
        }

        @Override
        public DomainElement next() {
            return elements.get(index++);
        }
    }


    //Equals i hashCode su dodani zbog zadatka 2
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleDomain that = (SimpleDomain) o;
        return elements.equals(that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}
