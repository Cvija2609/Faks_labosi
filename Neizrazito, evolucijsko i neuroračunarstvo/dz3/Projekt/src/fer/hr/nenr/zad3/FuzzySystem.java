package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.NoElementException;
import fer.hr.nenr.zad1.ProductMustHaveAtLeastTwoVariablesException;
import fer.hr.nenr.zad2.NonMatchingDomainsException;

public interface FuzzySystem {
    int zakljuci(int L, int D, int LK, int DK, int V, int S) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException;
}
