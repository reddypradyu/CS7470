import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class RollingResistance {
    private static final String COMMA_DELIMITER = ",";

    public static void main(String[] args) throws IOException, ParseException {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\kushd\\Documents\\cs4605\\Code\\Carpet_trial4 - Carpet_trial4.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        }
        SimpleDateFormat[] timeStamps = new SimpleDateFormat[records.size()];
        Double[] xVal = new Double[records.size()];
        Double[] yVal = new Double[records.size()];
        Double[] zVal = new Double[records.size()];
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mm:ss", Locale.ENGLISH);
        Date[] dateStamp = new Date[records.size()];
        for (int i = 0; i < records.size(); i++) {
            dateStamp[i] = sdf.parse(records.get(i).get(0));
            xVal[i] = Double.parseDouble(records.get(i).get(1));
            yVal[i] = Double.parseDouble(records.get(i).get(2));
            zVal[i] = Double.parseDouble(records.get(i).get(3));
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

        //for finding magnitude
        Double[] magnitude = new Double[xVal.length];
        for (int i = 0; i < xVal.length; i++) {
            Double mag = (xVal[i] * xVal[i]) + (yVal[i] * yVal[i]) + (zVal[i] * zVal[i]);
            magnitude[i] = Math.sqrt(mag);
        }

        smoothArray(magnitude, 2);

        //for graphing magnitude
        DrawGraph magGraph = new DrawGraph(Arrays.asList(magnitude));
        magGraph.setPreferredSize(new Dimension(2000, 600));
        JFrame frame = new JFrame("Magnitude Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(magGraph);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        //for finding velocity

        //finding accurate time difference
        Double counter = 1.0;
        int i = 1;
        Double[] timeDiff = new Double[dateStamp.length - 1];
        while (i < dateStamp.length) {
            int j = i;
            while ((dateStamp[j].getTime() - dateStamp[j - 1].getTime() == 0) && j < dateStamp.length-1) {
                counter++;
                j++;
                //System.out.print(j);
            }
//            if(counter != 1.0) {
//                System.out.println((1 / counter));
//            }


            if (counter == 1.0) {
                timeDiff[i - 1] = 1.0;
            }

            for (int k = i; k <= j && counter != 1.0; k++) {
                timeDiff[k - 1] = (1 / counter);
            }
            i = j + 1;
            counter = 1.0;
        }
        //System.out.print(Arrays.toString(timeDiff));
        System.out.println(dateStamp[1].getTime() - dateStamp[0].getTime());
        System.out.println(Arrays.toString(timeDiff));
    }

    // http://phrogz.net/js/framerate-independent-low-pass-filter.html
    public static void smoothArray(Double[] values, int smoothing) {
        Double value = values[0]; // start with the first input
        for (int i=1; i < values.length; i++){
            Double currentValue = values[i];
            value += (currentValue - value) / smoothing;
            values[i] = value;
        }
    }
}




