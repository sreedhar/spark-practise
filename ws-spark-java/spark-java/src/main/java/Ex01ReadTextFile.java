import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Read a text file as an RDD of Strings
 */
public class Ex01ReadTextFile {
    public static void main(String[] args){

        SparkConf conf = new SparkConf();
        conf.setAppName("Application Ex01ReadTextFile");
        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> rdd =
                sc.textFile(ModuleEnv.DATA_ROOT +"\\ml-latest-small\\movies.csv");

        rdd.take(10).forEach(line -> System.out.println(line));
    }
}
