package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.IFuzzySet;
import fer.hr.nenr.zad1.NoElementException;
import fer.hr.nenr.zad1.Operations;
import fer.hr.nenr.zad1.ProductMustHaveAtLeastTwoVariablesException;
import fer.hr.nenr.zad2.NonMatchingDomainsException;

import java.util.Scanner;

public class UserProgram3 {

    public static void zakljuci(int L, int D, int LK, int DK, int V, int S, RuleDatabase db, Defuzzifier def) throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        IFuzzySet brzina = AkcelFuzzySystemMin.singletonV(V);

        Rule pravilo0 = db.getRules().get(0);
        Rule pravilo1 = db.getRules().get(1);

        IFuzzySet z0 = KormiloFuzzySystemMin.compositionSingleton(brzina, pravilo0.make_R());
        IFuzzySet z1 = KormiloFuzzySystemMin.compositionSingleton(brzina, pravilo1.make_R());

        IFuzzySet union = Operations.binaryOperation(z0, z1, Operations.zadehOr());

        int ret = def.decode(union);

        System.out.println("Unija svih pravila je:");
        System.out.println(union);
        System.out.println("Prva dekodirana vrijednost je:");
        System.out.println(ret);
        System.out.println("Dekodirana vrijednost je:");
        System.out.println(AkcelFuzzySystemMin.vr_akcel(ret, union.getDomain().getCardinality()));
    }

    public static void main(String[] args) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException {
        RuleDatabase db = new RuleDatabaseAcc();
        Defuzzifier def = new COADefuzzifier();

        Scanner sc = new Scanner(System.in);

        System.out.println("Upisite vrijednosti za L, D, LK, DK, V i S redom");
        //int L = sc.nextInt();
        //int D = sc.nextInt();
        //int LK = sc.nextInt();
        //int DK = sc.nextInt();
        //int V = sc.nextInt();
        //int S = sc.nextInt();
        int L = 120;
        int D = 1200;
        int LK = 120;
        int DK = 130;
        int V = 40;
        int S = 1;

        //IDomain d = new SimpleDomain(0, 3);
        //IDomain d2 = new SimpleDomain(0, 4);
        //IDomain obje = Domain.combine(d, d2);
        //MutableFuzzySet s = new MutableFuzzySet(d);
        //MutableFuzzySet ss = new MutableFuzzySet(d2);
        //MutableFuzzySet s2 = new MutableFuzzySet(obje);
        //s.set(d.elementForIndex(1), 1);
        //ss.set(d2.elementForIndex(0), 0.5);
        //ss.set(d2.elementForIndex(1), 0.7);

        //s2.set(obje.elementForIndex(5), 0.5);
        //s2.set(obje.elementForIndex(6), 0.3);
        //s2.set(obje.elementForIndex(7), 0.2);
        //KormiloFuzzySystemMin.compositionSingleton(s, s2);
        //Rule r = new Rule(s, ss);
        //r.make_R();
        zakljuci(L, D, LK, DK, V, S, db, def);

    }
}
