package fer.hr.nenr.zad3;

import fer.hr.nenr.zad1.*;
import fer.hr.nenr.zad2.NonMatchingDomainsException;
import fer.hr.nenr.zad2.Relations;

import java.util.Scanner;

public class UserProgram {
    public static void zakljuci(int L, int D, int LK, int DK, int V, int S, int pravilo, RuleDatabase db, Defuzzifier def) throws NoElementException, NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException {
        IFuzzySet lijevo = KormiloFuzzySystemMin.singletonL(L);
        IFuzzySet desno = KormiloFuzzySystemMin.singletonL(D);
        IFuzzySet smjer = KormiloFuzzySystemMin.singletonS(S);
        IFuzzySet lijevoobala = KormiloFuzzySystemMin.singletonL(LK);
        IFuzzySet desnoobala = KormiloFuzzySystemMin.singletonL(DK);

        Rule pravilo0 = db.getRules().get(pravilo);
        IFuzzySet z;

        if(pravilo == 0) {
            z = KormiloFuzzySystemMin.compositionSingleton(lijevo, pravilo0.make_R());
        }
        else if(pravilo == 1) {
            z = KormiloFuzzySystemMin.compositionSingleton(desno, pravilo0.make_R());
        }
        else if(pravilo == 2){
            z = KormiloFuzzySystemMin.compositionSingleton(smjer, pravilo0.make_R());
        }
        else if(pravilo == 3){
            z = KormiloFuzzySystemMin.compositionSingleton(lijevoobala, pravilo0.make_R());
        }
        else{
            z = KormiloFuzzySystemMin.compositionSingleton(desnoobala, pravilo0.make_R());
        }

        int ret = def.decode(z);

        System.out.println("Neizraziti skup koji je zakljucak pravila " + pravilo + ":");
        System.out.println(z);
        System.out.println("Dekodirana vrijednost:");
        System.out.println(ret);
        System.out.println("Tocnije, kut od:");
        System.out.println(vr_kuta(ret, z.getDomain().getCardinality()));
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
        // Tri su pravila:
        // 0 ako ti je slijeva zid, skreni desno oštro
        // 1 ako ti je zdesna zid, skreni lijevo oštro
        // 2 ako si u krivom smjeru, okreni se
        // 3 ako ti je slijeva pod kute zid, skreni desno
        // 4 ako ti je zdesna pod kutem zid, skreni lijevo
        System.out.println("Dostupna pravila su:");
        System.out.println("0 ako ti je slijeva zid, skreni desno oštro");
        System.out.println("1 ako ti je zdesna zid, skreni lijevo oštro");
        System.out.println("2 ako si u krivom smjeru, okreni se");
        System.out.println("3 ako ti je slijeva pod kute zid, skreni desno");
        System.out.println("4 ako ti je zdesna pod kutem zid, skreni lijevo");

        System.out.println("Odaberite jedno od pravila tako da unesete broj pravila");
        Scanner sc = new Scanner(System.in);
        int a = -1;
        while(a != 1 && a != 2 && a != 0 && a!= 3 && a != 4){
            a = sc.nextInt();
        }
        System.out.println("Upisite vrijednosti za L, D, LK, DK, V i S redom");
        int L = sc.nextInt();
        int D = sc.nextInt();
        int LK = sc.nextInt();
        int DK = sc.nextInt();
        int V = sc.nextInt();
        int S = sc.nextInt();

        zakljuci(L, D, LK, DK, V, S, a, db, def);

    }
}
