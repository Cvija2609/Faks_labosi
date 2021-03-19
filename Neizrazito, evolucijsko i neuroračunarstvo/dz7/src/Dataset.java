import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Dataset {
    private List<List<Double>> dataset = new LinkedList();

    public Dataset(String filename) throws FileNotFoundException {
        List<List<Double>> retList = new LinkedList<>();
        File myObj = new File(filename);
        Scanner myReader = new Scanner(myObj);

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] addto = data.split("\t");
            List<Double> rl = new LinkedList<>();
            for(String s: addto){
                rl.add(Double.parseDouble(s));
            }
            retList.add(rl);
        }
        myReader.close();

        this.dataset = retList;
    }

    public List<List<Double>> getDataset(){
        return this.dataset;
    }

    public Integer datasetLen(){
        return this.dataset.size();
    }

    public List<Double> getFromDataset(int index){
        return this.dataset.get(index);
    }

    public List<Double> getIzlaz(int index, int M){
        int n = this.dataset.get(0).size();
        return this.dataset.get(index).subList(n - M, n);
    }

    public List<Double> getUlaz(int index, int M){
        return this.dataset.get(index).subList(0, M);
    }
}
