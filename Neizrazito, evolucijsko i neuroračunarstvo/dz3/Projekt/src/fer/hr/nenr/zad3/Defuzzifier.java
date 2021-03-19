package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.IFuzzySet;
import fer.hr.nenr.zad1.NoElementException;

public interface Defuzzifier {
    int decode(IFuzzySet set) throws NoElementException;
}
