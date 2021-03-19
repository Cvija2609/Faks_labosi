package fer.hr.nenr.zad3;
import fer.hr.nenr.zad1.NoElementException;
import fer.hr.nenr.zad1.ProductMustHaveAtLeastTwoVariablesException;
import fer.hr.nenr.zad2.NonMatchingDomainsException;

import java.io.*;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) throws IOException, NoElementException, ProductMustHaveAtLeastTwoVariablesException, NonMatchingDomainsException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Defuzzifier def = new COADefuzzifier();

        //RuleDatabase dbangle = new RuleDatabaseAngle();
        //RuleDatabase dbacc = new RuleDatabaseAcc();

        FuzzySystem fsAkcel = new AkcelFuzzySystemMin(def);
        FuzzySystem fsKormilo = new KormiloFuzzySystemMin(def);

        int L=0,D=0,LK=0,DK=0,V=0,S=0,akcel,kormilo;
        String line = null;
        while(true){
            if((line = input.readLine())!=null){
                if(line.charAt(0)=='K') break;
                Scanner s = new Scanner(line);
                L = s.nextInt();
                D = s.nextInt();
                LK = s.nextInt();
                DK = s.nextInt();
                V = s.nextInt();
                S = s.nextInt();
            }

            akcel = fsAkcel.zakljuci(L, D, LK, DK, V, S);
            kormilo = fsKormilo.zakljuci(L, D, LK, DK, V, S);

            System.out.println(akcel + " " + kormilo);
            System.out.flush();
        }
    }

}