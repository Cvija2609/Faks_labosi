package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;
import fer.hr.nenr.zad2.NonMatchingDomainsException;
import fer.hr.nenr.zad2.Relations;

public class Rule {
    private IFuzzySet antecedent;
    private IFuzzySet konzekvens;

    public Rule(IFuzzySet antecedent, IFuzzySet konzekvens){
        this.antecedent = antecedent;
        this.konzekvens = konzekvens;
    }

    public IFuzzySet getAntecedent() {
        return antecedent;
    }

    public IFuzzySet getKonzekvens() {
        return konzekvens;
    }

    public IFuzzySet make_R() throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        return combineTwoRelations(getAntecedent(), getKonzekvens());
    }

    public IFuzzySet combineTwoRelations(IFuzzySet antecedent, IFuzzySet konzekvens) throws ProductMustHaveAtLeastTwoVariablesException, NoElementException {
        IDomain d1 = antecedent.getDomain();
        IDomain d2 = konzekvens.getDomain();
        IDomain combined = Domain.combine(d1, d2);

        int prva = d1.getCardinality();
        int druga = d2.getCardinality();

        MutableFuzzySet retSet = new MutableFuzzySet(combined);
        for(int i=0;i<prva;i++){
            for(int j=0;j<druga;j++){
                retSet.set(combined.elementForIndex(i*druga+j), Math.min(antecedent.getMemberships()[i], konzekvens.getMemberships()[j]));
            }
        }
        return retSet;
    }
}
