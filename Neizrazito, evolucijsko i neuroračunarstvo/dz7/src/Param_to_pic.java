import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Param_to_pic {
    public static void main(String[] args) throws Exception {
        String arhitektura = "2x6x4x3";
        List<Integer> arch = NeuronskaMreza.getArch(arhitektura);
        List<Double> retList = new LinkedList<>();
        File myObj = new File("./src/param4.txt");
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

        int n = arch.get(1);
        int u = arch.get(0);
        PrintWriter writer = new PrintWriter("./src/param_2x6x4x3.txt", StandardCharsets.UTF_8);

        for(int i = 0; i < n; i++){
            int[] parami = NeuronskaMreza.getParametri(i + u, arch);
            writer.println(Double.toString(retList.get(parami[0])) + " " + Double.toString(retList.get(parami[0] + 2)));
        }
        writer.close();
    }
}
