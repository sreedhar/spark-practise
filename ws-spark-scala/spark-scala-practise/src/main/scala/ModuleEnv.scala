/**
  * Define the constants for Paths etc to be used in code
  */
object ModuleEnv {
  val SPARK_PRACTISE_HOME: String = "<Put absolute path here>\\spark-practise"
  val DATA_ROOT: String = SPARK_PRACTISE_HOME + "\\ws-data"
  val FILE_RATINGS = ModuleEnv.DATA_ROOT + "\\ml-latest-small\\ratings.csv"
  val FILE_MOVIES = ModuleEnv.DATA_ROOT + "\\ml-latest-small\\movies.csv"
}
