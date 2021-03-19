package fer.hr.nenr.zad1;

public class Debug {
    public static void print(IDomain domain, String headingText) {
        if(headingText!=null) {
            System.out.println(headingText);
        }

        for (DomainElement e : domain) {
            System.out.println("Element domene: " + e);
        }
        System.out.println("Kardinalitet domene je: " + domain.getCardinality());
        System.out.println();
    }

    public static void print(IFuzzySet set, String headingText){
        if(headingText!=null) {
            System.out.println(headingText);
        }

        int cnt = 0;
        for(DomainElement e : set.getDomain()){
            System.out.println("d(" + e.toString() + ")=" + Double.toString(set.getMemberships()[cnt++]));
        }
        System.out.println();
    }
}
