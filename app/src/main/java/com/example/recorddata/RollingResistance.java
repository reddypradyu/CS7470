import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class RollingResistance {
    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args) throws IOException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Pablo\\Desktop\\M2\\CsvTest\\src\\Tiles_trialtest.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        }
        String[] timeStamps = new String[records.size() - 27];
        Double[] xVal = new Double[records.size() - 27];
        Double[] yVal = new Double[records.size() - 27];
        Double[] zVal = new Double[records.size() - 27];
        for (int i = 27; i < records.size(); i++) {
            timeStamps[i - 27] = records.get(i).get(0);
            xVal[i - 27] = Double.parseDouble(records.get(i).get(1));
            yVal[i - 27] = Double.parseDouble(records.get(i).get(2));
            zVal[i - 27] = Double.parseDouble(records.get(i).get(3));
        }
//        System.out.println(Arrays.toString(xVal));
        // get these from user input
        double wheelDiam = 0.1412;
        String wheel_side = "left";

        double weightLoad = 100;
        double sampFreq = 51.2;
        // filter the relevant accelerations
        smoothArray(xVal, 2);
        smoothArray(yVal, 2);
        smoothArray(zVal, 2);

        DrawGraph xGraph = new DrawGraph(Arrays.asList(xVal));
        xGraph.setPreferredSize(new Dimension(2000, 600));
        JFrame frame = new JFrame("DrawGraph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(xGraph);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // http://phrogz.net/js/framerate-independent-low-pass-filter.html
    public static void smoothArray(Double[] values, int smoothing){
        Double value = values[0]; // start with the first input
        for (int i=1; i < values.length; i++){
            Double currentValue = values[i];
            value += (currentValue - value) / smoothing;
            values[i] = value;
        }
    }
}




