package fer.hr.nenr.zad1;

public class Primjer2 {
    public static void main(String[] args) throws NoElementException, ProductMustHaveAtLeastTwoVariablesException {
        IDomain d = Domain.intRange(0, 11);
        //IFuzzySet set2 = new CalculatedFuzzySet(d, StandardFuzzySets.lambdaFunction(d.indexOfElement(DomainElement.of(2)),
        //        d.indexOfElement(DomainElement.of(5)), d.indexOfElement(DomainElement.of(8))));
        IFuzzySet set1 = new MutableFuzzySet(d)
                .set(DomainElement.of(0), 1.0)
                .set(DomainElement.of(1), 0.8)
                .set(DomainElement.of(2), 0.6)
                .set(DomainElement.of(3), 0.4)
                .set(DomainElement.of(4), 0.2);
        Debug.print(set1, "Set1:");
        IFuzzySet notSet1 = Operations.unaryOperation(
                set1, Operations.zadehNot());
        //IFuzzySet notSet2 = Operations.unaryOperation(
        //        set2, Operations.zadehNot());
        Debug.print(notSet1, "notSet1:");
        IFuzzySet union = Operations.binaryOperation(
                set1, notSet1, Operations.zadehOr());
        Debug.print(union, "Set1 union notSet1:");
        IFuzzySet hinters = Operations.binaryOperation(
                set1, notSet1, Operations.hamacherTNorm(1.0));
        Debug.print(hinters, "Set1 intersection with notSet1 using " +
                "parameterised Hamacher T norm with parameter 1.0:");

        IDomain d2 = Domain.intRange(-5, 6);
        IDomain d3 = Domain.combine(d, d2);
        IFuzzySet set3 = new CalculatedFuzzySet(
                d3,
                StandardFuzzySets.lambdaFunction(
                        d3.indexOfElement(DomainElement.of(0, -4)),
                        d3.indexOfElement(DomainElement.of(2, 0)),
                        d3.indexOfElement(DomainElement.of(4, 4))
                )
        );
        IFuzzySet notSet3 = Operations.unaryOperation(
                set3, Operations.zadehNot());
        Debug.print(set3, "Set3");
        Debug.print(notSet3, "NotSet3");
    }
}
