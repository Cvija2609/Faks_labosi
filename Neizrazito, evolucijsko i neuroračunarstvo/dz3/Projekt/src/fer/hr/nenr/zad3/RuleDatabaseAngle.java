package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;

import java.util.LinkedList;
import java.util.List;

public class RuleDatabaseAngle extends RuleDatabase{
    // Univerzalni skup za vrijednost kut -> jedan broj predstavlja kut od 15 stupnjeva
    private static IDomain u4 = Domain.intRange(0, 36);

    private List<Rule> rules;

    public RuleDatabaseAngle() throws NoElementException{
        rules = new LinkedList<>();

        // Ako L je kritično blizu onda (kut) K je jednak -90
        rules.add(new Rule(kriticno_blizu(u1), exact_value(u4, -90)));
        // Ako D je kritično blizu onda K je jednak 90
        rules.add(new Rule(kriticno_blizu(u1), exact_value(u4, 90)));
        // Ako S je 0 onda K je 170
        rules.add(new Rule(exact_value_S(u2, 0), exact_value(u4, 170)));
        // Ako LK je kritično blizu onda K je jednak -60
        rules.add(new Rule(kriticno_blizu(u1), exact_value(u4, -60)));
        // Ako DK je kritično blizu onda K je jednak 60
        rules.add(new Rule(kriticno_blizu(u1), exact_value(u4, 60)));
    }

    private IFuzzySet exact_value_S(IDomain domena, int i) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        retSet.set(domena.elementForIndex(i), 1);

        return retSet;
    }

    public static IFuzzySet exact_value(IDomain domena, int value) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        int card = domena.getCardinality();
        int newVal = valueToIndex(value) + (domena.getCardinality() / 2);

        if(newVal >= 1) {
            retSet.set(domena.elementForIndex(newVal - 1), 0.8);
        }
        if(newVal >= 2) {
            retSet.set(domena.elementForIndex(newVal - 2), 0.5);
        }
        if(newVal >= 3) {
            retSet.set(domena.elementForIndex(newVal - 3), 0.2);
        }
        retSet.set(domena.elementForIndex(newVal), 1);
        if(newVal < card - 1) {
            retSet.set(domena.elementForIndex(newVal + 1), 0.8);
        }
        if(newVal < card - 2) {
            retSet.set(domena.elementForIndex(newVal + 2), 0.5);
        }
        if (newVal < card - 3) {
            retSet.set(domena.elementForIndex(newVal + 3), 0.2);
        }
        return retSet;
    }

    public static int valueToIndex(int value) {
        return (int) (value/10);
    }

    @Override
    public List<Rule> getRules() {
        return this.rules;
    }
}
