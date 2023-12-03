import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class NoBoringWordsHere {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("SOME APP NAME").setMaster("local").set("spark.executor.memory","1g");
        JavaSparkContext sc = new JavaSparkContext(conf);

        String file1Path = "src/main/resources/article.txt";
        JavaRDD<String> wordsFile1 = sc.textFile(file1Path).flatMap(line -> Arrays.asList(line.split("\\s+")).iterator());

        String file2Path = "src/main/resources/boring.txt";
        JavaRDD<String> boringWords = sc.textFile(file2Path);

        Set<String> boringWordsSet = new HashSet<>(boringWords.collect());

        JavaRDD<String> filteredWords = wordsFile1.filter(word -> !boringWordsSet.contains(word));

        String outputPath = "src/main/resources/boring_output.txt";
        filteredWords.saveAsTextFile(outputPath);

        sc.stop();
    }
}