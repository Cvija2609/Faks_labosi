package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;
import fer.hr.nenr.zad2.NonMatchingDomainsException;
import fer.hr.nenr.zad2.Relations;

public class KormiloFuzzySystemMin implements FuzzySystem{
    private Defuzzifier def;
    private RuleDatabase db;

    // Tri su pravila:
    // 0 ako ti je zdesna zid, skreni lijevo oštro
    // 1 ako ti je slijeva zid, skreni desno oštro
    // 2 ako si u krivom smjeru, okreni se
    public KormiloFuzzySystemMin(Defuzzifier def) throws NoElementException {
        this.def = def;
        this.db = new RuleDatabaseAngle();
    }
    @Override
    public int zakljuci(int L, int D, int LK, int DK, int V, int S) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException {
        IFuzzySet lijevo = singletonL(L);
        IFuzzySet desno = singletonL(D);
        IFuzzySet smjer = singletonS(S);
        IFuzzySet lijevoobala = singletonL(LK);
        IFuzzySet desnoobala = singletonL(DK);

        Rule pravilo0 = this.db.getRules().get(0);
        Rule pravilo1 = this.db.getRules().get(1);
        Rule pravilo2 = this.db.getRules().get(2);
        Rule pravilo3 = this.db.getRules().get(3);
        Rule pravilo4 = this.db.getRules().get(4);


        IFuzzySet z0 = compositionSingleton(lijevo, pravilo0.make_R());
        IFuzzySet z1 = compositionSingleton(desno, pravilo1.make_R());
        IFuzzySet z2 = compositionSingleton(smjer, pravilo2.make_R());
        IFuzzySet z3 = compositionSingleton(lijevoobala, pravilo3.make_R());
        IFuzzySet z4 = compositionSingleton(desnoobala, pravilo4.make_R());

        IFuzzySet union1 = Operations.binaryOperation(z0, z1, Operations.zadehOr());
        IFuzzySet union2 = Operations.binaryOperation(union1, z2, Operations.zadehOr());
        IFuzzySet union3 = Operations.binaryOperation(union2, z3, Operations.zadehOr());
        IFuzzySet union4 = Operations.binaryOperation(union3, z4, Operations.zadehOr());

        int ret = this.def.decode(union4);

        return vr_kuta(ret, union4.getDomain().getCardinality());
    }

    public static IFuzzySet singletonL(int number) throws NoElementException {
        if(number > 120){
            number = 120;
        }
        int num = valueToIndex(number);
        MutableFuzzySet retSet = new MutableFuzzySet(RuleDatabase.u1);
        retSet.set(RuleDatabase.u1.elementForIndex(num), 1);

        return retSet;
    }

    private static int valueToIndex(int number) {
        return (int) (number / 10);
    }

    public static IFuzzySet singletonS(int number) throws NoElementException {
        MutableFuzzySet retSet = new MutableFuzzySet(RuleDatabase.u2);
        retSet.set(RuleDatabase.u2.elementForIndex(number), 1);

        return retSet;
    }

    public static IFuzzySet composition(IFuzzySet set1, IFuzzySet set2) throws NonMatchingDomainsException, NoElementException {
        IDomain d1 = set1.getDomain();
        IDomain d2 = set2.getDomain();
        int brkomp = d2.getNumberOfComponents();
        // Broj elemenata matrice bez zadnje dimenzije
        int ostali = d2.getCardinality() / d1.getCardinality();
        IDomain newDomain = d2.getComponent(brkomp-1);
        MutableFuzzySet retSet = new MutableFuzzySet(newDomain);

        // Ako se ne poklapaju domene
        if(d1.getCardinality() != d2.getComponent(0).getCardinality()){
            throw new NonMatchingDomainsException(d1.toString() + "\n\nand\n\n" + d2.toString() + "\n\n are not matching");
        }

        int card2 = d1.getCardinality();
        int card = newDomain.getCardinality();
        double max = set1.getMemberships()[0];

        for(int i=0; i<card;i++){
            for(int j=0;j<card2;j++) {
                double p = Math.max(set1.getMemberships()[j], set2.getMemberships()[j*ostali+i]);
                if(p>max){
                    max = p;
                }
            }
            retSet.set(newDomain.elementForIndex(i), max);
        }
        return retSet;
    }

    public static IFuzzySet compositionSingleton(IFuzzySet set1, IFuzzySet set2) throws NoElementException {
        IDomain d1 = set1.getDomain();
        IDomain d2 = set2.getDomain();
        int brkomp = d2.getNumberOfComponents();
        IDomain newDomain = d2.getComponent(brkomp-1);
        MutableFuzzySet retSet = new MutableFuzzySet(newDomain);
        int index = 0;
        int card = newDomain.getCardinality();

        for(DomainElement el: d1){
            if(set1.getValueAt(el) == 1.0){
                index = set1.getIndexOf(el);
            }
        }

        for(int i=0;i<card;i++){
            retSet.set(newDomain.elementForIndex(i), set2.getMemberships()[index*card+i]);
        }

        return retSet;
    }

    private static int vr_kuta(int ret, int cardinality) {
        if(ret == 0)
            return ret;
        else
            return (ret - (cardinality / 2)) * 10;
    }
}
