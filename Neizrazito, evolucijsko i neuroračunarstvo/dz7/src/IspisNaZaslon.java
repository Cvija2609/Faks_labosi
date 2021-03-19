import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class IspisNaZaslon {
    public static void main(String[] args) throws Exception {
        String arhitektura = "2x8x3";
        List<Integer> arch = NeuronskaMreza.getArch(arhitektura);
        List<Double> retList = new LinkedList<>();
        File myObj = new File("./src/param.txt");
        Dataset dataset = new Dataset("./src/zad7-dataset.txt");
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] addto = data.split(", ");
            List<Double> rl = new LinkedList<>();
            for(String s: addto){
                rl.add(Double.parseDouble(s));
            }
            retList.addAll(rl);
        }
        myReader.close();

        for(int i = 0; i < dataset.datasetLen(); i++){
            List<Double> ulaz = dataset.getUlaz(i, 2);
            System.out.println(NeuronskaMreza.calcOutput(arch, ulaz, retList, 2));
        }
    }
}
