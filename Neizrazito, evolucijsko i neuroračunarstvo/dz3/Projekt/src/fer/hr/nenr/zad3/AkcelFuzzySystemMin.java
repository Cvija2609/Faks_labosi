package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;

public class AkcelFuzzySystemMin implements FuzzySystem{
    private Defuzzifier def;
    private RuleDatabase db;

    public AkcelFuzzySystemMin(Defuzzifier def) throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        this.def = def;
        this.db = new RuleDatabaseAcc();
    }

    @Override
    public int zakljuci(int L, int D, int LK, int DK, int V, int S) throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        IFuzzySet brzina = singletonV(V);

        Rule pravilo0 = this.db.getRules().get(0);
        Rule pravilo1 = this.db.getRules().get(1);

        IFuzzySet z0 = KormiloFuzzySystemMin.compositionSingleton(brzina, pravilo0.make_R());
        IFuzzySet z1 = KormiloFuzzySystemMin.compositionSingleton(brzina, pravilo1.make_R());

        IFuzzySet union = Operations.binaryOperation(z0, z1, Operations.zadehOr());

        int ret = this.def.decode(union);

        return vr_akcel(ret, union.getDomain().getCardinality());
    }

    public static int vr_akcel(int ret, int cardinality) {
        return (ret - cardinality / 2) * 10;
    }

    public static IFuzzySet singletonV(int v) throws NoElementException {
        if(v>50){
            v = 50;
        }
        int num = valueToIndex(v);
        MutableFuzzySet retSet = new MutableFuzzySet(RuleDatabase.u3);

        retSet.set(RuleDatabase.u3.elementForIndex(num), 1);

        return retSet;
    }

    public static int valueToIndex(int v) {
        return (int) (v / 10);
    }

}
