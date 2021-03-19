package fer.hr.nenr.zad1;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Operations {

    public static Double round(Double val) {
        return new BigDecimal(val.toString()).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static IFuzzySet unaryOperation(IFuzzySet set, IUnaryFunction function){
        int elem_num = set.getDomain().getCardinality();
        IFuzzySet retSet = new MutableFuzzySet(set.getDomain());

        for(int i=0;i<elem_num;i++){
            retSet.getMemberships()[i]=function.valueAt(set.getMemberships()[i]);
        }

        return retSet;
    }

    public static IFuzzySet binaryOperation(IFuzzySet set1, IFuzzySet set2, IBinaryFunction function){
        IFuzzySet retSet;
        int elem_num = 0;
        boolean set1veci = false;
        boolean set2veci = false;
        int card1 = set1.getDomain().getCardinality();
        int card2 = set2.getDomain().getCardinality();
        if(set1.getDomain().getCardinality() > set2.getDomain().getCardinality()) {
            retSet = new MutableFuzzySet(set1.getDomain());
            elem_num = set1.getDomain().getCardinality();
            set1veci = true;
        }
        else{
            retSet = new MutableFuzzySet(set2.getDomain());
            elem_num = set2.getDomain().getCardinality();
            set2veci = true;
            if(set1.getDomain().getCardinality() == set2.getDomain().getCardinality()) set1veci = true;
        }
        if(set1veci && !set2veci){
            for (int i = 0; i < elem_num; i++) {
                if(i>=card2){
                    retSet.getMemberships()[i] = function.valueAt(set1.getMemberships()[i], 0);
                }
                else {
                    retSet.getMemberships()[i] = function.valueAt(set1.getMemberships()[i], set2.getMemberships()[i]);
                }
            }
        }
        else if(set2veci && !set1veci){
            for (int i = 0; i < elem_num; i++) {
                if(i>=card1){
                    retSet.getMemberships()[i] = function.valueAt(0, set2.getMemberships()[i]);
                }
                else {
                    retSet.getMemberships()[i] = function.valueAt(set1.getMemberships()[i], set2.getMemberships()[i]);
                }
            }
        }
        else {
            for (int i = 0; i < elem_num; i++) {
                retSet.getMemberships()[i] = function.valueAt(set1.getMemberships()[i], set2.getMemberships()[i]);
            }
        }

        return retSet;
    }

    public static IUnaryFunction zadehNot(){
        return x -> round(1-x);
    }

    public static IBinaryFunction zadehAnd(){
        return Math::min;
    }

    public static IBinaryFunction zadehOr(){
        return Math::max;
    }

    public static IBinaryFunction hamacherTNorm(double x){
        return (a, b) -> {
            return round((a * b)/(x+(1-x)*(a+b-a*b)));
        };
    }

    public static IBinaryFunction hamacherSNorm(double x){
        return (a, b) -> {
            return round((a + b - (2-x) * a * b) / (1 - (1-x)*a * b));
        };
    }
}
