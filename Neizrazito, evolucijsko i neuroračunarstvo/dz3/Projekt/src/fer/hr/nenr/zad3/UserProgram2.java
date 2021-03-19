package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.IFuzzySet;
import fer.hr.nenr.zad1.NoElementException;
import fer.hr.nenr.zad1.Operations;
import fer.hr.nenr.zad1.ProductMustHaveAtLeastTwoVariablesException;
import fer.hr.nenr.zad2.NonMatchingDomainsException;

import java.util.Scanner;

public class UserProgram2 {
    public static void zakljuci(int L, int D, int LK, int DK, int V, int S, RuleDatabase db, Defuzzifier def) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException {
        IFuzzySet lijevo = KormiloFuzzySystemMin.singletonL(L);
        IFuzzySet desno = KormiloFuzzySystemMin.singletonL(D);
        IFuzzySet smjer = KormiloFuzzySystemMin.singletonS(S);
        IFuzzySet lijevoobala = KormiloFuzzySystemMin.singletonL(LK);
        IFuzzySet desnoobala = KormiloFuzzySystemMin.singletonL(DK);

        IFuzzySet z1, z2, z0, z3, z4;

        z0 = KormiloFuzzySystemMin.compositionSingleton(lijevo, db.getRules().get(0).make_R());
        z1 = KormiloFuzzySystemMin.compositionSingleton(desno, db.getRules().get(1).make_R());
        z2 = KormiloFuzzySystemMin.compositionSingleton(smjer, db.getRules().get(2).make_R());
        z3 = KormiloFuzzySystemMin.compositionSingleton(lijevoobala, db.getRules().get(3).make_R());
        z4 = KormiloFuzzySystemMin.compositionSingleton(desnoobala, db.getRules().get(4).make_R());


        IFuzzySet union1 = Operations.binaryOperation(z0, z1, Operations.zadehOr());
        IFuzzySet union2 = Operations.binaryOperation(union1, z2, Operations.zadehOr());
        IFuzzySet union3 = Operations.binaryOperation(union2, z3, Operations.zadehOr());
        IFuzzySet union4 = Operations.binaryOperation(union3, z4, Operations.zadehOr());

        int ret = def.decode(union4);

        System.out.println("Unija svih pravila:");
        System.out.println(union4);
        System.out.println("Dekodirana vrijednost");
        System.out.println(ret);
        System.out.println("Tocnije, vrijednost kuta je:");
        System.out.println(vr_kuta(ret, union4.getDomain().getCardinality()));
    }

    private static int vr_kuta(int ret, int cardinality) {
        if(ret == 0)
            return ret;
        else
            return (ret - (cardinality / 2)) * 10;
    }

    public static void main(String[] args) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException {
        RuleDatabase db = new RuleDatabaseAngle();
        Defuzzifier def = new COADefuzzifier();

        Scanner sc = new Scanner(System.in);

        System.out.println("Upisite vrijednosti za L, D, LK, DK, V i S redom");
        int L = sc.nextInt();
        int D = sc.nextInt();
        int LK = sc.nextInt();
        int DK = sc.nextInt();
        int V = sc.nextInt();
        int S = sc.nextInt();

        zakljuci(L, D, LK, DK, V, S, db, def);

    }
}
