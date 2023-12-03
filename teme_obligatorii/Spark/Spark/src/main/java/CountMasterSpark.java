import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class CountMasterSpark {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("CountMasterOccurrences");
        JavaSparkContext sc = new JavaSparkContext(conf);

        String inputFile = "src/main/resources/master_file.txt";
        JavaRDD<String> textRDD = sc.textFile(inputFile);

        long masterCount = textRDD.flatMap(line -> Arrays.asList(line.split(" ")).iterator())
                .filter(word -> word.toLowerCase().equals("master"))
                .count();

        System.out.println("Number of occurence of master: " + masterCount);

        sc.stop();
    }
}
