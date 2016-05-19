import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.broadcast.Broadcast;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Simple BroadCast example, where the Broadcast value is an Int
 * Broadcast example where the Broadcast value is a Dataset
 */
public class Ex02Broadcast {
    public static void main(String[] args){

        SparkConf conf = new SparkConf();
        conf.setAppName("Spark-Hello-World");
        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        //example, where the Broadcast value is an Int
        JavaRDD<Integer> data = sc.parallelize(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,15));
        Broadcast<Integer> FACTOR = sc.broadcast(3);
        JavaRDD<Integer> dataScaledUp = data.map(n -> n * FACTOR.getValue());
        dataScaledUp.take(10).forEach(n -> System.out.println(n));

        //example where the Broadcast value is a RDD
        List<Integer> divisorList = Arrays.asList(2, 5);
        JavaRDD<Integer> divisors = sc.parallelize(divisorList);
//        Broadcast<JavaRDD<Integer>> FILTER_FACTORS = sc.broadcast(divisors);
        Broadcast<List<Integer>> FILTER_FACTORS = sc.broadcast(divisorList);
        //TODO: Use the broadcast value
    }
}
