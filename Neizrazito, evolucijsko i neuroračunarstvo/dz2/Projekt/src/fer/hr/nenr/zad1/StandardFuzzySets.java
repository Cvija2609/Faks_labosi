package fer.hr.nenr.zad1;

public class StandardFuzzySets {
    public static IIntUnaryFunction lambdaFunction(int alpha, int beta, int gamma){
        return index -> {
            if(index>=alpha && index<beta){
                return Operations.round(((double)(index-alpha)/(beta-alpha)));
            }
            else if(index>=beta && index<gamma){
                return Operations.round(((double)(gamma-index)/(gamma-beta)));
            }
            return 0;
        };
    }
    public static IIntUnaryFunction gammaFunction(int alpha, int beta){
        return index -> {
            if(index < alpha) return 0;
            else if(index >= alpha && index < beta){
                return Operations.round((double)(index-alpha)/(beta-alpha));
            }
            return 1;
        };
    }

    public static IIntUnaryFunction lFunction(int alpha, int beta){
        return index -> {
            if(index < alpha) return 1;
            else if(index >= alpha && index < beta){
                return Operations.round((double)(beta-index)/(beta-alpha));
            }
            return 0;
        };
    }
}
