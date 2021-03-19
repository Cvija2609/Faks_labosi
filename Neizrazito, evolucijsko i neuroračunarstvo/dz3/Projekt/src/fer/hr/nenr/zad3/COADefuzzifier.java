package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.DomainElement;
import fer.hr.nenr.zad1.IFuzzySet;
import fer.hr.nenr.zad1.NoElementException;

public class COADefuzzifier implements Defuzzifier{

    @Override
    public int decode(IFuzzySet set) throws NoElementException {
        int i;
        double brojnik = 0.0;
        double nazivnik = 0.0;
        int ukupno = set.getDomain().getCardinality();

        for(i=0;i<ukupno;i++){
            DomainElement de = set.getDomain().elementForIndex(i);
            nazivnik += set.getValueAt(de);
            brojnik += set.getValueAt(de) * de.getComponentValue(0);
        }

        return (int) ((brojnik / nazivnik) + 0.5);
    }
}
