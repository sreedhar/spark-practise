import org.apache.spark.{SparkConf, SparkContext}

/**
  * Read a text file as an RDD of Strings
  */
object Ex01ReadTextFile extends App{
  val conf = new SparkConf()
  conf.setAppName("Spark-Hello-World")
  conf.setMaster("local")
  val sc = new SparkContext(conf);
  val rdd = sc.textFile(ModuleEnv.DATA_ROOT+ "\\ml-latest-small\\movies.csv")

  rdd.take(10).foreach(println)
}
