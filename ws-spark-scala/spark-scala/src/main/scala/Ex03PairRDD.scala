import org.apache.spark.{SparkConf, SparkContext}

/**
  * Simple example of a PairRDD
  */
object Ex03PairRDD extends App {
  val conf = new SparkConf()
  conf.setAppName("Spark-Hello-World")
  conf.setMaster("local")
  val sc = new SparkContext(conf);

  // Lets try to find the count of ratings available for each movie
  //  userId,movieId,rating,timestamp
  //  1,16,4.0,1217897793
  val ratingRdd = sc.textFile(ModuleEnv.FILE_RATINGS)


  def isNotRatingHeader(ratingRecord: String) = {
    !(ratingRecord.charAt(0) == 'u')
  }

  def getTupleOfMovieIdAndCount(ratingRecord: String) = {
    val columns = ratingRecord.split(",")
    (columns(1).toInt, 1)
  }

  val ratingPairRdd = ratingRdd.filter(isNotRatingHeader).map(getTupleOfMovieIdAndCount)
  println(ratingPairRdd.toDebugString)
  val ratingCountPairRdd = ratingPairRdd.reduceByKey((r1: Int, r2: Int) => (r1 + r2))

  //  movieId,title,genres
  //  1,Toy Story (1995),Adventure|Animation|Children|Comedy|Fantasy
  val movieRdd = sc.textFile(ModuleEnv.FILE_MOVIES)

  def isNotMovieHeader(movieRecord: String) = {
    !(movieRecord.charAt(0) == 'm')
  }

  def getTupleOfMovieIdAndName(movieRecord: String) = {
    val columns = movieRecord.split(",")
    (columns(0).toInt, columns(1))
  }

  //11,"American President, The (1995)",Comedy|Drama|Romance
  def getTupleOfMovieIdAndNameV2(movieRecord: String) = {
    (movieRecord.substring(0, movieRecord.indexOf(',')).toInt, movieRecord.substring(movieRecord.indexOf(',')+1, movieRecord.lastIndexOf(',')))
  }

  val movieNamePairRdd = movieRdd.filter(isNotMovieHeader).map(getTupleOfMovieIdAndName)

  movieNamePairRdd.join(ratingCountPairRdd).map( (x) => x._2).takeOrdered(10)(ord=Ordering[(String,Int)]{
    new Ordering[(String, Int)] {
      override def compare(x: (String, Int), y: (String, Int)): Int = x._2 - y._2
    }.reverse
  }).foreach(println)
}


