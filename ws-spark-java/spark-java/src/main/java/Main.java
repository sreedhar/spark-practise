import org.apache.spark.api.java.JavaSparkContext;

/**
 * Start with this class to check if spark core is available in classpath
 */
public class Main {
    public static void main(String[] args){
        JavaSparkContext sc = new JavaSparkContext("local", "Spark-Hello-World");
        System.out.println(sc.version());
        System.out.println(sc.getSparkHome());//I removed SPARK_HOME from my env variables
    }
}
