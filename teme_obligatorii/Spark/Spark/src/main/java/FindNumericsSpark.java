import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;

public class FindNumericsSpark {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("FindNumericsSpark");
        JavaSparkContext sc = new JavaSparkContext(conf);

        String inputFile = "src/main/resources/numeric.txt";
        JavaRDD<String> textRDD = sc.textFile(inputFile);

        JavaRDD<String> numericValuesRDD = textRDD.flatMap(line -> Arrays.asList(line.split("\\s+")).iterator())
                .filter(value -> value.matches("\\d+"));

        numericValuesRDD.collect().forEach(System.out::println);

        sc.stop();
    }
}