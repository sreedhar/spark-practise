import org.apache.spark.{SparkConf, SparkContext}

/**
  * Simple BroadCast example, where the Broadcast value is an Int
  * Broadcast example where the Broadcast value is a Dataset
  */
object Ex02Broadcast extends App{
  val conf = new SparkConf()
  conf.setAppName("Spark-Hello-World")
  conf.setMaster("local")
  val sc = new SparkContext(conf);

  //example, where the Broadcast value is an Int
  val data = sc.parallelize(List(1,2,3,4,5,6,7,8,9,10,11,12,13,15))
  val FACTOR = sc.broadcast(3);
  val dataScaledUp = data.map(n => n * FACTOR.value);
  dataScaledUp.take(20).foreach(println)

  //example where the Broadcast value is a RDD
  val divisorList = List(2,5)
  val divisors = sc.parallelize(divisorList)
//  val FILTER_FACTORS = sc.broadcast(divisors);
  val FILTER_FACTORS = sc.broadcast(divisorList);
  val filteredMultiple = data.flatMap(x => FILTER_FACTORS.value.map(f => (x,f))).filter( p=> p._1 % p._2 == 0).map(p => p._1)
  filteredMultiple.foreach(println)

}
