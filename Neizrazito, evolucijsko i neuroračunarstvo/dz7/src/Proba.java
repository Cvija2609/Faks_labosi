public class Proba {
    public static void main(String[] args) throws Exception {
        double[] range = new double[]{-1., 1.};
        int vel_pop = 50;
        int max_iter = 200000;
        Dataset dataset = new Dataset("./src/zad7-dataset.txt");
        double M = 0.4;
        int k = 3;
        String arhitektura = "2x6x4x3";
        double pm1 = 0.02;
        double pm2 = 0.05;
        double pm3 = 0.1;
        int t1 = 6;
        int t2 = 4;
        int t3 = 3;
        double sigma1 = 0.1;
        double sigma2 = 0.2;
        double sigma3 = 1.;
        double pj = 0.1;
        double alpha = 0.5;

        GeneticAlgorithm ga = new GeneticAlgorithm(vel_pop, max_iter, dataset, M, k, range, arhitektura, pm1, pm2, pm3, t1, t2, t3, sigma1, sigma2, sigma3, pj, alpha);

        System.out.println(ga.geneticAlgorithm());


    }
}
