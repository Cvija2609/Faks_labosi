package fer.hr.nenr.zad2;

import fer.hr.nenr.zad1.*;

public class Relations {

    public static boolean isUtimesURelation(IFuzzySet relation){
        int numOfSimples = relation.getDomain().getNumberOfComponents();
        if(numOfSimples != 2) return false;

        int card = relation.getDomain().getCardinality();
        for(int i=0;i<card;i++){
            if(relation.getDomain().elementForIndex(i).getNumberOfComponents() != 2){
                return false;
            }
        }

        if(!relation.getDomain().getComponent(0).equals(relation.getDomain().getComponent(1))){
            return false;
        }

        return true;
    }

    public static boolean isSymmetric(IFuzzySet relation){
        if(!isUtimesURelation(relation)) return false;
        int card = relation.getDomain().getComponent(0).getCardinality();

        // Izracunao sam da su simetrični elementi oni s indexima i*card+j i j*card+i
        for(int i=0;i<card;i++){
            for(int j=0;j<=i;j++){
                if(relation.getMemberships()[i*card+j] != relation.getMemberships()[j*card+i])
                    return false;
            }
        }
        return true;
    }

    public static boolean isReflexive(IFuzzySet relation){
        if(!isUtimesURelation(relation)) return false;
        int card1 = relation.getDomain().getComponent(0).getCardinality();
        int card = relation.getDomain().getCardinality();

        for(int i=0; i<card; i+=card1+1){
            if(relation.getMemberships()[i] != 1){
                return false;
            }
        }
        return true;
    }

    public static boolean isMaxMinTransitive(IFuzzySet relation) throws NoElementException {
        if(!isUtimesURelation(relation)) return false;
        int card = relation.getDomain().getComponent(0).getCardinality();

        // mi(x, z)<= min(mi(x, y), mi(y, z)) za svaki x, y, z element U
        for(int i=0; i<card; i++){  //svi x-evi
            for(int j=0; j<card; j++){      // svi z-ovi
                double mixz = relation.getMemberships()[i*card+j];
                double min;
                for(int k=0; k<card; k++){  // svi y-oni
                    double mixy = relation.getMemberships()[i*card+k];
                    double miyz = relation.getMemberships()[k*card+j];
                    min = Math.min(mixy, miyz);
                    if(mixz<min) return false;
                }
            }
        }
        return true;
    }

    public static IFuzzySet compositionOfBinaryRelations(IFuzzySet relation1, IFuzzySet relation2) throws NonMatchingDomainsException, ProductMustHaveAtLeastTwoVariablesException, NoElementException {
        //Provjeri poklapaju li se UxY kompozicija sa YxW = UxW
        if(!relation1.getDomain().getComponent(1).equals(relation2.getDomain().getComponent(0))){
            throw new NonMatchingDomainsException("Domains " + relation1.getDomain().toString() +
                    " and " + relation2.getDomain().toString() + "are not matching");
        }

        int cardy = relation1.getDomain().getComponent(1).getCardinality();

        //daj mi prvi i zadnji element jer stupac i redak u ciljnoj matrici (domeni) mi govori
        // koji je stupac i redak koristen za njeno dobivanje iz relation1/relation2
        int first = relation1.getDomain().getComponent(1).elementForIndex(0).getValues()[0];
        int last = relation1.getDomain().getComponent(1).elementForIndex(cardy-1).getValues()[0];

        CompositeDomain newDomain = new CompositeDomain(relation1.getDomain().getComponent(0),
                relation2.getDomain().getComponent(1));
        MutableFuzzySet retSet = new MutableFuzzySet(newDomain);

        for(DomainElement e: newDomain){
            int redak = e.getValues()[0];
            int stupac = e.getValues()[1];

            double max = 0;
            for(int i=first;i<=last;i++){
                int indexel1 = relation1.getIndexOf(new DomainElement(new int[]{redak, i}));
                double min1 = relation1.getMemberships()[indexel1];
                int indexel2 = relation2.getIndexOf(new DomainElement(new int[]{i, stupac}));
                double min2 = relation2.getMemberships()[indexel2];
                double min = Math.min(min1, min2);
                if(min > max){
                    max = min;
                }
            }
            retSet.set(e, max);
        }

        return retSet;
    }

    public static boolean isFuzzyEquivalence(IFuzzySet relation) throws NoElementException {
        return isSymmetric(relation) && isReflexive(relation) && isMaxMinTransitive(relation);
    }
}
