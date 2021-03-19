import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {
    private final double minimum = 1e-7;
    private int vel_pop;
    private int max_iter;
    private Dataset dataset;
    private double M;
    private int k;
    private int param_len;
    private double[] param_range;
    private List<Integer> arhitektura;
    private double pm1;
    private double pm2;
    private double pm3;
    private double v1;
    private double v2;
    private double v3;
    private double sigma1;
    private double sigma2;
    private double sigma3;
    private double pj;
    private double alpha;

    public GeneticAlgorithm(int vel_pop, int max_iter, Dataset dataset, double M, int k, double[] param_range, String arhitektra, double pm1, double pm2, double pm3, int t1, int t2, int t3, double sigma1, double sigma2, double sigma3, double pj, double alpha){
        this.vel_pop = vel_pop;
        this.max_iter = max_iter;
        this.dataset = dataset;
        this.param_len = NeuronskaMreza.getUkParametara(NeuronskaMreza.getArch(arhitektra));
        this.param_range = param_range;
        this.arhitektura = NeuronskaMreza.getArch(arhitektra);
        this.pm1 = pm1;
        this.pm2 = pm2;
        this.pm3 = pm3;
        this.v1 = (double) t1 / (t1 + t2 + t3);
        this.v2 = (double) t2 / (t1 + t2 + t3);
        this.v3 = (double) t3 / (t1 + t2 + t3);
        this.sigma1 = sigma1;
        this.sigma2 = sigma2;
        this.sigma3 = sigma3;
        this.pj = pj;
        this.alpha = alpha;
        this.M = M;
        this.k = k;
    }

    public List<Double> geneticAlgorithm() throws Exception {
        int generation = 0;
        List<List<Double>> populacija = generate(this.param_len, this.param_range, this.vel_pop);
        List<Double> fitnesses = fitness(populacija, this.dataset, this.arhitektura);
        List<Double> best = new LinkedList<>();

        for(int i = 0; i< this.max_iter; i++){
            if(generation % 100 == 0) {
                System.out.println("Generacija " + Integer.toString(generation));
            }

            selection(populacija, fitnesses);

            double rnd = randomNumber(0., 1.);

            if(rnd < (1. / 3.)){
                crossing1(populacija, fitnesses);
            }
            else if((1. / 3.) < rnd && rnd < (2. / 3.)){
                crossing2(populacija, fitnesses);
            }
            else{
                crossing3(populacija, fitnesses);
            }

            double rnd2 = randomNumber(0., 1.);

            if(rnd2 < this.v1){
                mutation(populacija, fitnesses, this.sigma1, this.pm1);
            }
            else if(rnd2 >= this.v1 && rnd2 < this.v2 + this.v1){
                mutation(populacija, fitnesses, this.sigma2, this.pm2);
            }
            else{
                mutation2(populacija, fitnesses, this.sigma3, this.pm3);
            }

            generation += 1;

            best = best(populacija, fitnesses);

            double bf = best_fitness(fitnesses);

            if(bf < minimum){
                break;
            }
            //System.out.println(best);
            //System.out.println();
            if(generation%100 == 0) {
                System.out.println(bf);
            }

        }

        return best;
    }

    private double best_fitness(List<Double> fitnesses) {
        Double min = fitnesses.get(0);

        for(int i = 0; i < fitnesses.size(); i++){
            if(fitnesses.get(i) < min){
                min = fitnesses.get(i);
            }
        }

        return min;
    }

    private List<Double> best(List<List<Double>> populacija, List<Double> fitnesses) {
        Double min = fitnesses.get(0);
        int index = 0;

        for(int i = 0; i < fitnesses.size(); i++){
            if(fitnesses.get(i) < min){
                min = fitnesses.get(i);
                index = i;
            }
        }

        return populacija.get(index);
    }

    private void mutation2(List<List<Double>> populacija, List<Double> fitnesses, double sigma1, double pm1) throws Exception {
        int hml = (int) (populacija.size() * this.M);
        for(int i = hml; i < populacija.size(); i++){
            double rnd = randomNumber(0., 1.);
            if(rnd > this.pj){
                continue;
            }
            else{
                for(int j = 0; j < this.param_len; j++){
                    double rnd2 = randomNumber(0., 1.);
                    if(rnd2 < pm1){
                        populacija.get(i).set(j, getGaussian(0, sigma1));
                    }
                }
                fitnesses.set(i, evaluate(populacija.get(i), arhitektura, dataset));
            }
        }
    }

    private void mutation(List<List<Double>> populacija, List<Double> fitnesses, double sigma1, double pm1) throws Exception {
        int hml = (int) (populacija.size() * this.M);
        for(int i = hml; i < populacija.size(); i++){
            double rnd = randomNumber(0., 1.);
            if(rnd > this.pj){
                continue;
            }
            else{
                for(int j = 0; j < this.param_len; j++){
                    double rnd2 = randomNumber(0., 1.);
                    if(rnd2 < pm1){
                        double b = populacija.get(i).get(j) + getGaussian(0, sigma1);
                        populacija.get(i).set(j, b);
                    }
                }
                fitnesses.set(i, evaluate(populacija.get(i), arhitektura, dataset));
            }
        }
    }

    private double getGaussian(double aMean, double aVariance){
        Random fRandom = new Random();
        return aMean + fRandom.nextGaussian() * aVariance;
    }

    public void crossing3(List<List<Double>> populacija, List<Double> fitnesses) throws Exception {
        while(populacija.size() != this.vel_pop){
            int prvi = randInt(0, populacija.size() - 1);
            int drugi = randInt(0, populacija.size() - 1);

            while(prvi == drugi){
                drugi = randInt(0, populacija.size() - 1);
            }

            List<Double> parent1 = populacija.get(prvi);
            List<Double> parent2 = populacija.get(drugi);

            List<Double> child = new LinkedList<>();

            for(int i = 0; i < this.param_len; i++){
                double cimin = Math.min(parent1.get(i), parent2.get(i));
                double cimax = Math.max(parent1.get(i), parent2.get(i));

                double Ii = cimax - cimin;

                double a = cimin - Ii * this.alpha;
                double b = cimax + Ii * this.alpha;

                child.add(randomNumber(a, b));
            }

            populacija.add(child);
            fitnesses.add(evaluate(child, arhitektura, dataset));
        }
    }

    /**public void crossing2(List<List<Double>> populacija, List<Double> fitnesses) throws Exception {
        int num = NeuronskaMreza.getUkNeurona(this.arhitektura);

        while(populacija.size() != this.vel_pop){
            List<Double> parent1 = populacija.get(randInt(0, populacija.size() - 1));
            List<Double> parent2 = populacija.get(randInt(0, populacija.size() - 1));

            List<Double> child1 = new LinkedList<>();
            List<Double> child2 = new LinkedList<>();

            for(int i = 0; i < num; i++){
                if(i < this.arhitektura.get(0)){
                    continue;
                }
                double rnd = randomNumber(0., 1.);
                int[] parami = NeuronskaMreza.getParametri(i, this.arhitektura);

                if(rnd < 0.5){
                    child1.addAll(parent1.subList(parami[0], parami[1] + 1));
                    child2.addAll(parent2.subList(parami[0], parami[1] + 1));
                }
                else{
                    child1.addAll(parent2.subList(parami[0], parami[1] + 1));
                    child2.addAll(parent1.subList(parami[0], parami[1] + 1));
                }
            }
            populacija.add(child1);
            fitnesses.add(evaluate(child1, this.arhitektura, this.dataset));

            if(populacija.size() == this.vel_pop){
                break;
            }
            else{
                populacija.add(child2);
                fitnesses.add(evaluate(child2, this.arhitektura, this.dataset));
            }
        }
    }**/

    public void crossing2(List<List<Double>> populacija, List<Double> fitnesses) throws Exception{
        while(populacija.size() != this.vel_pop){
            int prvi = randInt(0, populacija.size() - 1);
            int drugi = randInt(0, populacija.size() - 1);

            while(prvi == drugi){
                drugi = randInt(0, populacija.size() - 1);
            }

            List<Double> parent1 = populacija.get(prvi);
            List<Double> parent2 = populacija.get(drugi);

            double fitness1 = fitnesses.get(prvi);
            double fitness2 = fitnesses.get(drugi);

            List<Double> bolji;
            List<Double> losiji;

            if(fitness1 < fitness2){
                bolji = parent1;
                losiji = parent2;
            }
            else{
                bolji = parent2;
                losiji = parent1;
            }

            double a = randomNumber(0., 1.);

            List<Double> child = new LinkedList<>();

            for(int i = 0; i < this.param_len; i++){
                double b = a * (bolji.get(i) - losiji.get(i)) + bolji.get(i);
                if(b > this.param_range[1]){
                    b = param_range[1];
                }
                else if(b < this.param_range[0]){
                    b = param_range[0];
                }
                child.add(b);
            }

            populacija.add(child);
            fitnesses.add(evaluate(child, arhitektura, dataset));
        }
    }

    public void crossing1(List<List<Double>> populacija, List<Double> fitnesses) throws Exception {
        while(populacija.size() != this.vel_pop){
            List<Double> parent1 = populacija.get(randInt(0, populacija.size() - 1));
            List<Double> parent2 = populacija.get(randInt(0, populacija.size() - 1));

            List<Double> child1 = new LinkedList<>();
            List<Double> child2 = new LinkedList<>();

            for(int i = 0; i < this.param_len; i++){
                double rnd = randomNumber(0., 1.);
                if(rnd < 0.5){
                    child1.add(parent1.get(i));
                    child2.add(parent2.get(i));
                }
                else{
                    child1.add(parent2.get(i));
                    child2.add(parent1.get(i));
                }
            }

            populacija.add(child1);
            fitnesses.add(evaluate(child1, this.arhitektura, this.dataset));

            if(populacija.size() == this.vel_pop){
                break;
            }
            else{
                populacija.add(child2);
                fitnesses.add(evaluate(child2, this.arhitektura, this.dataset));
            }
        }
    }

    private Double evaluate(List<Double> jedinka, List<Integer> arhitektura, Dataset dataset) throws Exception {
        return NeuronskaMreza.calcError(arhitektura, dataset, jedinka);
    }

    public void selection(List<List<Double>> populacija, List<Double> fitnesses) {
        int how_many_left = (int) (this.vel_pop * this.M);

        while(populacija.size() > how_many_left){
            List<Double> k_par_fit = new LinkedList<>();
            List<Integer> k_par_idx = new LinkedList<>();

            for(int i = 0;i < this.k; i++){
                int rnd = randInt(0, populacija.size() - 1);

                while(k_par_idx.contains(rnd)){
                    rnd = randInt(0, populacija.size() - 1);
                }

                k_par_idx.add(rnd);
                k_par_fit.add(fitnesses.get(rnd));
            }

            int worst = getWorst(k_par_fit, k_par_idx);

            populacija.remove(worst);
            fitnesses.remove(worst);
        }
    }

    private static int randInt(int i, int size) {
        return (int) (Math.random() * (size - i)) + i;
    }

    private int getWorst(List<Double> k_par_fit, List<Integer> k_par_idx) {
        double max = k_par_fit.get(0);

        for(double m: k_par_fit){
            if(m > max){
                max = m;
            }
        }

        int wrst = k_par_fit.indexOf(max);

        return k_par_idx.get(wrst);
    }

    private List<Double> fitness(List<List<Double>> populacija, Dataset dataset, List<Integer> arhitektura) throws Exception {
        List<Double> retList = new LinkedList<>();

        for(List<Double> jedinka: populacija){
            retList.add(NeuronskaMreza.calcError(arhitektura, dataset, jedinka));
        }

        return retList;
    }

    private static List<List<Double>> generate(int param_len, double[] param_range, int vel_pop) {
        List<List<Double>> retList = new LinkedList<>();

        for(int i = 0; i < vel_pop; i++){
            List<Double> p = new LinkedList<>();
            for(int j = 0; j < param_len; j++){
                p.add(randomNumber(param_range[0], param_range[1]));
            }
            retList.add(p);
        }

        return retList;
    }

    private static Double randomNumber(double min, double max) {
        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }
}
