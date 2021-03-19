package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;

import java.util.LinkedList;
import java.util.List;

public class RuleDatabaseAcc extends RuleDatabase{
    // Domena akceleracije
    private static IDomain u4 = new SimpleDomain(0, 8);

    private List<Rule> rules;

    public RuleDatabaseAcc() throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        rules = new LinkedList<>();

        // Ako voziš prebrzo uspori
        rules.add(new Rule(prebrzo(u3), uspori(u4)));
        // Ako voziš presporo ubrzaj
        rules.add(new Rule(presporo(u3), ubrzaj(u4)));

    }

    private IFuzzySet ubrzaj(IDomain domena) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        int card = domena.getCardinality();

        retSet.set(domena.elementForIndex(card - 1), 0.8);
        retSet.set(domena.elementForIndex(card - 2), 0.9);
        retSet.set(domena.elementForIndex(card - 3), 0.2);

        return retSet;
    }

    private IFuzzySet presporo(IDomain domena) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);

        retSet.set(domena.elementForIndex(0), 1);
        retSet.set(domena.elementForIndex(1), 0.6);
        retSet.set(domena.elementForIndex(2), 0.2);

        return retSet;
    }

    private IFuzzySet uspori(IDomain domena) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);

        retSet.set(domena.elementForIndex(0), 0.8);
        retSet.set(domena.elementForIndex(1), 0.9);
        retSet.set(domena.elementForIndex(2), 0.2);

        return retSet;
    }

    private IFuzzySet prebrzo(IDomain domena) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        int card = domena.getCardinality();

        retSet.set(domena.elementForIndex(card - 1), 1);
        retSet.set(domena.elementForIndex(card - 2), 0.6);
        retSet.set(domena.elementForIndex(card - 3), 0.2);

        return retSet;
    }

    private static IFuzzySet greater_than(IDomain domena, int number) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        int num = number / 10;
        int zadnjiElement = domena.getNumberOfComponents();

        for(int i=0;i<zadnjiElement;i++){
            if(i>num) {
                retSet.set(domena.elementForIndex(i), 1);
            }
        }

        return retSet;
    }

    private static IFuzzySet less_than(IDomain domena, int number) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        int num = number / 10;
        int zadnjiElement = domena.getNumberOfComponents();

        for(int i=0;i<zadnjiElement;i++){
            if(i<num) {
                retSet.set(domena.elementForIndex(i), 1);
            }
        }

        return retSet;
    }

    @Override
    public List<Rule> getRules() {
        return this.rules;
    }
}
