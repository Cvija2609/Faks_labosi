package fer.hr.nenr.zad1;

import java.util.Arrays;

public class DomainElement {
    private int[] values;

    public DomainElement(int[] values) {
        this.values = values;
    }

    public int getNumberOfComponents() {
        return values.length;
    }

    public int getComponentValue(int index) {
        return values[index];
    }

    public int[] getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainElement that = (DomainElement) o;
        return Arrays.equals(this.values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        if(getValues().length == 1){
            return Integer.toString(getValues()[0]);
        }
        else{
            return Arrays.toString(getValues());
        }
    }

    public static DomainElement of(int ... more) throws NoElementException{
        if(more.length == 0){
            throw new NoElementException("YOu have to provide at least one element");
        }
        else {
            return new DomainElement(more);
        }
    }
}
