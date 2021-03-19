package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;

import java.util.List;

public abstract class RuleDatabase {
    // Univerzalni skup elemenata [0, 1300] -> ako je vrijednost 1246, zaokruzuje se na 12
    // za vrijednosti L, D, LK i DK
    public static IDomain u1 = Domain.intRange(0, 13);
    // Univerzalni skup za vrijednost S
    public static IDomain u2 = Domain.intRange(0, 2);
    // Univerzalni skup za vrijednost Vm
    public static IDomain u3 = Domain.intRange(0, 6);

    public static IFuzzySet kriticno_blizu(IDomain domena) throws NoElementException{
        MutableFuzzySet retSet = new MutableFuzzySet(domena);
        //int zadnjiElement = domena.getCardinality();
        retSet.set(domena.elementForIndex(0), 1);
        retSet.set(domena.elementForIndex(1), 1);
        retSet.set(domena.elementForIndex(2), 1);
        retSet.set(domena.elementForIndex(3), 0.8);
        retSet.set(domena.elementForIndex(4), 0.5);
        //for(int i = 0;i < zadnjiElement ; i++){
        //    if(i == zadnjiElement - 1){
        //        retSet.set(domena.elementForIndex(i), 1);
        //    }
        //    else if(i == zadnjiElement - 2){
        //        retSet.set(domena.elementForIndex(i), 0.8);
        //    }
        //    else if(i == zadnjiElement - 3){
        //        retSet.set(domena.elementForIndex(i), 0.5);
        //    }
        //}
        return retSet;
    }

    public abstract List<Rule> getRules();
}
