import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Simple example of a PairRDD
 */
public class Ex03PairRDD implements Serializable{
    public static void main(String[] args){

        SparkConf conf = new SparkConf();
        conf.setAppName("Spark-Hello-World");
        conf.setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // Lets try to find the count of ratings available for each movie
        //  userId,movieId,rating,timestamp
        //  1,16,4.0,1217897793
        JavaRDD<String> ratingRdd = sc.textFile(ModuleEnv.FILE_RATINGS);

        JavaPairRDD<Integer,Integer> ratingPairRdd = ratingRdd.filter(r -> Ex03PairRDD.isNotRatingHeader(r))
                                                                            .mapToPair(r -> Ex03PairRDD.getTupleOfMovieIdAndCount(r));
        System.out.println(ratingPairRdd.toDebugString());
        JavaPairRDD<Integer,Integer> ratingCountPairRdd = ratingPairRdd.reduceByKey((a,b) -> (a + b));

        ratingCountPairRdd.take(10).forEach(t -> System.out.println(t));

        //  movieId,title,genres
        //  1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy
        JavaRDD<String> movieRdd = sc.textFile(ModuleEnv.FILE_MOVIES);

        JavaPairRDD<Integer,String> movieNamePairRdd = movieRdd.filter(r -> Ex03PairRDD.isNotMovieHeader(r))
                                                                            .mapToPair(r -> Ex03PairRDD.getTupleOfMovieIdAndName(r));
        movieNamePairRdd.take(10).forEach(t -> System.out.println(t));

        movieNamePairRdd.join(ratingCountPairRdd).map( (x) -> x._2)
                                                .takeOrdered(10, new MovieRatingTupleComparator())
                                                .forEach(t -> System.out.println(t));
    }

    private static Boolean isNotRatingHeader(String ratingRecord){
        return !(ratingRecord.charAt(0) == 'u');
    }

    private static Tuple2<Integer,Integer> getTupleOfMovieIdAndCount(String ratingRecord){
        String[] columns = ratingRecord.split(",");
        return new Tuple2( Integer.parseInt(columns[1]), 1);
    }

    private static Boolean isNotMovieHeader(String movieRecord){
        return !(movieRecord.charAt(0) == 'm');
    }

    private static Tuple2<Integer, String> getTupleOfMovieIdAndName(String movieRecord){
        String[] columns = movieRecord.split(",");
        return new Tuple2(Integer.parseInt(columns[0]), columns[1]);
    }

    public static class MovieRatingTupleComparator implements Comparator<Tuple2<String, Integer>>,Serializable {

        @Override
        public int compare(Tuple2<String, Integer> x, Tuple2<String, Integer> y) {
            return x._2 - y._2;
        }
    }
}
