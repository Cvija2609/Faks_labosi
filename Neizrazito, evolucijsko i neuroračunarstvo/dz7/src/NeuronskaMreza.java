import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NeuronskaMreza {
    private String arhitektura = "";
    private List<Integer> list_arhitektura = new LinkedList<>();

    public NeuronskaMreza(String arhitektura) throws Exception {
        this.arhitektura = arhitektura;
        this.list_arhitektura = getArch(arhitektura);

        if(this.list_arhitektura.get(0) != 2){
            throw new Exception("Krivi broj ulaza");
        }
        if(this.list_arhitektura.get(this.list_arhitektura.size() - 1) != 3){
            throw new Exception("Krivi broj izlaza");
        }
    }

    public static List<Integer> getArch(String arhitektura) {
        String[] lst = arhitektura.split("x");
        List<String> l = new LinkedList<>(Arrays.asList(lst));

        List<Integer> retList = new LinkedList<>();

        for(String s: l){
            retList.add(Integer.parseInt(s));
        }

        return retList;
    }

    public List<Integer> getArhitektura(){
        return this.list_arhitektura;
    }

    public static Integer getUkParametara(List<Integer> arhitektura){
        int suma = 0;

        for(int i = 0; i < arhitektura.size(); i++){
            if(i == 0){
                continue;
            }
            else if(i == 1){
                suma += arhitektura.get(i - 1) * arhitektura.get(i) * 2;
            }
            else{
                suma += (arhitektura.get(i - 1) + 1) * arhitektura.get(i);
            }
        }

        return suma;
    }

    public static Integer getUkNeurona(List<Integer> arhitektura){
        int suma = 0;

        for (Integer integer : arhitektura) {
            suma += integer;
        }

        return suma;
    }

    /** Metoda koja vraca koliko parametara pojedini neuron treba **/
    public static Integer getBrParametara(Integer index_neurona, List<Integer> arhitektura) throws Exception {
        Integer ukNeurona = getUkNeurona(arhitektura);

        if(index_neurona > ukNeurona - 1){
            throw new Exception("Neispravan index neurona " + Integer.toString(index_neurona) + " > " + Integer.toString(ukNeurona));
        }

        if(index_neurona < arhitektura.get(0)) {
            return 0;
        }
        else if(arhitektura.get(0) <= index_neurona && index_neurona < arhitektura.get(0) + arhitektura.get(1)){
            return arhitektura.get(0) * 2;
        }
        else {
            int suma = 0;
            for(int i = 0; i < arhitektura.size(); i++){
                if(i == 0 || i == 1){
                    suma += arhitektura.get(i);
                }
                else {
                    if(suma <= index_neurona && index_neurona < arhitektura.get(i) + suma){
                        return arhitektura.get(i - 1) + 1;
                    }
                    else{
                        suma += arhitektura.get(i);
                    }
                }
            }
        }
        return -1;
    }

    public static int[] getParametri(Integer index_neurona, List<Integer> arhitektura) throws Exception {
        Integer ukNeurona = getUkNeurona(arhitektura);

        if(index_neurona > ukNeurona - 1){
            throw new Exception("Neispravan index neurona " + Integer.toString(index_neurona) + " > " + Integer.toString(ukNeurona));
        }
        int suma = 0;

        for(int i = 0; i < index_neurona; i++){
            suma += getBrParametara(i, arhitektura);
        }

        return new int[]{suma, suma + getBrParametara(index_neurona, arhitektura) - 1};
    }

    public static List<Double> calcOutput(List<Integer> arhitektura, List<Double> ulaz, List<Double> parametri, int zastavica) throws Exception {
        int n = arhitektura.get(0);

        if(ulaz.size() != n){
            throw new Exception("Kriva velicina ulaza");
        }

        int brojac = 0;
        List<Double> izlaz = new LinkedList<>();

        for(double i: ulaz){
            izlaz.add(i);
        }
        for(int i = 0; i < arhitektura.size(); i++){
            if(i == 0){
                brojac += arhitektura.get(i);
                continue;
            }
            else if(i == 1){
                for(int j = brojac; j < arhitektura.get(i) + arhitektura.get(i - 1); j++){
                    double suma = 0.;
                    int[] parami = getParametri(j, arhitektura);

                    for(int k = 0; k < n; k++){
                        suma += Math.abs((ulaz.get(k) - parametri.get(parami[0] + k * n)) / parametri.get(parami[0] + k * n + 1));
                    }

                    izlaz.add(1 / (1 + suma));
                }
                brojac += arhitektura.get(i);
            }
            else{
                for(int j = brojac; j < arhitektura.get(i) + brojac; j++){
                    int[] parami = getParametri(j, arhitektura);
                    double suma = 0.;
                    int N = arhitektura.get(i - 1);
                    int sma = 0;
                    for(int lj = 0; lj < i - 1; lj++){
                        sma += arhitektura.get(lj);
                    }

                    for(int k = 0; k < N; k++){
                        suma += parametri.get(parami[0] + k) * izlaz.get(k + sma);
                    }
                    izlaz.add(1 / (1 + Math.exp(-(suma + parametri.get(parami[1])))));
                }
                brojac += arhitektura.get(i);
            }
        }

        List<Double> retList = izlaz.subList(izlaz.size() - arhitektura.get(arhitektura.size() - 1), izlaz.size());

        if(zastavica == 0){
            return retList;
        }
        else if(zastavica == 2){
            double max = retList.get(0);
            for(double e: retList){
                if(e > max){
                    max = e;
                }
            }
            for(int i = 0; i < retList.size(); i++){
                if(retList.get(i) == max){
                    retList.set(i, 1.);
                }
                else{
                    retList.set(i, 0.);
                }
            }

            return retList;
        }
        else{
            return izlaz;
        }
    }

    public static double calcError(List<Integer> arhitektura, Dataset dataset, List<Double> parametri) throws Exception {
        int M = arhitektura.get(arhitektura.size() - 1);
        int N = dataset.datasetLen();
        int u = arhitektura.get(0);
        double mse = 0.;

        for(int s = 0; s < N; s++){
            List<Double> izl = dataset.getIzlaz(s, M);
            List<Double> ulaz = dataset.getUlaz(s, u);
            List<Double> izln = calcOutput(arhitektura, ulaz, parametri, 0);
            for(int o = 0; o < M; o++){
                mse += Math.pow(izl.get(o) - izln.get(o), 2);
            }
        }

        return mse / N;
    }
}
