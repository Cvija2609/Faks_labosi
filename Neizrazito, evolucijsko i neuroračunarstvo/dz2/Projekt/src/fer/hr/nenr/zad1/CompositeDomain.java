package fer.hr.nenr.zad1;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CompositeDomain extends Domain {

    private List<IDomain> simples;
    private List<DomainElement> elements = new LinkedList<>();

    public CompositeDomain(IDomain ... domains) throws ProductMustHaveAtLeastTwoVariablesException {
        if(domains.length == 0 || domains.length == 1){
            throw new ProductMustHaveAtLeastTwoVariablesException("At least two domains are needed for product, not " + Integer.toString(domains.length));
        }
        this.simples = Arrays.asList(domains);
        int cardinality = getCardinality();     // Daje kardinalitet CompositeDomain elementa
        int components = 0;
        for(int i=0; i<getCardinality();i++){   // Inicijaliziraj polja DomainElement
            elements.add(new DomainElement(new int[getNumberOfComponents()]));
        }
        for(IDomain s: domains) {  // prodji kroz sve jednostavne domene
            int k = 0;
            cardinality /= s.getCardinality();  //prvog elementa ima onoliko koliko ukupni_kardinalitet/kardinalitet_tog_jednostavnog_skupa
            while(k<getCardinality()) {
                for (int j = 0; j < s.getCardinality(); j++) {  //kardinalitet == broju elemenata skupa
                    for (int i = 0; i < cardinality; i++) {
                        elements.get(k).getValues()[components] = s.elementForIndex(j).getValues()[0];
                        k++;
                    }
                }
            }
            components++;
        }
    }

    @Override
    public int getCardinality() {
        int card = 1;
        for(IDomain s: simples){
            card*=s.getCardinality();
        }
        return card;
    }

    @Override
    public IDomain getComponent(int indexAt) throws IndexOutOfBoundsException{
        if(indexAt >= simples.size()){
            throw new IndexOutOfBoundsException("Invalid component index " + Integer.toString(indexAt));
        }
        return simples.get(indexAt);
    }

    @Override
    public int getNumberOfComponents() {
        return simples.size();
    }

    @Override
    public Iterator<DomainElement> iterator() {
        return new CompositeDomainIterator();
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

    public class CompositeDomainIterator implements Iterator<DomainElement> {

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

    @Override
    public String toString() {
        return "CompositeDomain{" +
                "simples=" + simples +
                ", elements=" + elements +
                '}';
    }
}
