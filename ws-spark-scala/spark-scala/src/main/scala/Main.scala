import org.apache.spark.{SparkConf, SparkContext}

/**
  * Start with this class to check if spark core is available in classpath
  */
object Main extends App{
  val conf = new SparkConf()
  conf.setAppName("Spark-Hello-World")
  conf.setMaster("local")
  val sc = new SparkContext(conf);
  println(sc.version)
  println(sc.getConf.toDebugString)
  //difference in API, not intuitive getter for SPARK_HOME
}
