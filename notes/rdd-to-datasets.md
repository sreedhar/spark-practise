Spark RDD to Dataset
====================

### Today's Talk
#### First 45 min RDD to Dataset
#### How to start learning spark, simple sample problems


## RDD

* A Resilient Distributed Dataset (RDD), the basic abstraction in Spark. Represents an immutable, partitioned collection of elements that can be operated on in parallel.
### Properties
* A list of partitions
* A function for computing each split
* A list of dependencies on other RDDs
* Optionally, a Partitioner for key-value RDDs (e.g. to say that the RDD is hash-partitioned)
* Optionally, a list of preferred locations to compute each split on (e.g. block locations for an HDFS file)
### Ways to create an RDD
* RDDs can only be created through deterministic
operations on either (1) data in stable storage or (2)
other RDDs
	* Reading from source (files, database, custom)
	* From a collection in Driver program
	* By transforming an existing RDD
	* By Materializing persisted RDD

### Other Notes
* RDD is immutable not its contents
* Although individual RDDs are immutable, it is possible to implement
mutable state by having multiple RDDs to represent multiple versions
of a dataset. We made RDDs immutable to make it easier to describe
lineage graphs, but it would have been equivalent to have our
abstraction be versioned datasets and track versions in lineage graphs.


## PairRDD
* It's not a separate DataType, but a use case where the RDD consists of 2 element Tuples (Tuple2). The first element of the tuple is treated as a key and the second element is a key. Special functions are provided to work with such RDDs
* Pair RDDs allow you to act on each key in parallel or
regroup data across the network. This is where Shuffle comes into picture.
* Depending on the operation, data in an RDD may have to be shuffled
among worker nodes, using worker-worker communication.
* Pair RDD are created from other RDD's by transformation.

## Examples

## Broadcast
* If a large read-only piece of data
(e.g., a lookup table) is used in multiple parallel operations,
it is preferable to distribute it to the workers
only once instead of packaging it with every closure.
Spark lets the programmer create a “broadcast variable”
object that wraps the value and ensures that it
is only copied to each worker once.

### Are they really immutable?
* Immutable only means that the mutable state will not move out of a single worker node.

### How can we safely update the broadcast value?
* Possible steps to update broadcast:
	* In the driver program
	* call unpersist(true) -- blocking call
	* once the call returns , modify(not reassign) the value of the broadcast variable
	* Next time the broadcast variable is used on any node it will be resent

### Is there a size limit on broadcast value?
* The value should fit in memory
* Very large size could increase the broadcast time


## Accumulator
*  These are variables that workers can
only “add” to using an associative operation, and that
only the driver can read. They can be used to implement
counters as in MapReduce and to provide a
more imperative syntax for parallel sums. Accumulators
can be defined for any type that has an “add”
operation and a “zero” value. Due to their “add-only”
semantics, they are easy to make fault-tolerant.

### What happens to accumulator value when a task is rerun?
* "For accumulator updates performed inside actions only, Spark guarantees that each task’s update to the accumulator will only be applied once, i.e. restarted tasks will not update the value. In transformations, users should be aware of that each task’s update may be applied more than once if tasks or job stages are re-executed."


## Dataset
### Why Dataset is a better abstraction than RDD or Dataframe ?
* With RDD the API is more like Collections with map and filter.
* Getting a simple max of 2 separate fields/columns from an RDD requires 2 transformations or a complex single one.
* Dataframes give the SQL like syntax. Aggregate operation like max and min on multiple columns is simple with SQL syntax "select max(col1), min(col2) from sometable"
* Dataset give type safety to the column values. This means now Dataframe=Dataset[Row]
	* Dataframe and Dataset gives spark query optimizer room to optimize which is not so simple with RDD's
	* With Dataset and Encoders the spark can optimize to only fetch the columns which are ultimately used. It's like pushing down filters all the way to loading point.
	* Even with Dataset, in some cases of reduce operations it's more performant to use Dataframe API.

### Whats inside Dataset and why do we need to know?
* Knowing how Dataset work internally gives options:
	* To use custom Types and implement custom Encoders which can benifit from the knowledge of data
	* Opens dores to provide custom rules which can be used by the query optimizer

### Should we still be coding with RDD's?
* Only if the solution cannot be expressed in a more SQL like syntax.
* Most of the transformations on Pair RDD are much simpler with SQL syntax on Dataset

## Motivation for implementing custom RDD/Dataframe/Dataset
* With RDD's there was some reason to implement custom SubClass to encapsulate behaviour
* With Dataframe and Dataset it is better to encapsulate business logic in UserDefinedFuntions and register them with Spark Session

## References


* [Resilient Distributed Datasets: A Fault-Tolerant Abstraction for
In-Memory Cluster Computing](http://people.csail.mit.edu/matei/papers/2012/nsdi_spark.pdf)
* [Spark SQL: Relational Data Processing in Spark](http://people.csail.mit.edu/matei/papers/2015/sigmod_spark_sql.pdf)
* [Performance and Scalability of Broadcast in Spark](http://www.cs.berkeley.edu/~agearh/cs267.sp10/files/mosharaf-spark-bc-report-spring10.pdf)
* [JerryLead/SparkInternals](https://github.com/JerryLead/SparkInternals/blob/master/markdown/english/0-Introduction.md)
* [Spark Broadcast Variables - What are they and how do I use them](http://www.sparktutorials.net/Spark+Broadcast+Variables+-+What+are+they+and+how+do+I+use+them)
* [Working with Apache Spark: Or, How I Learned to Stop Worrying and Love the Shuffle](http://blog.cloudera.com/blog/2015/05/working-with-apache-spark-or-how-i-learned-to-stop-worrying-and-love-the-shuffle/)
* [Spark Programming Guide](https://spark.apache.org/docs/1.6.1/programming-guide.html)
* [Tuning Spark](https://spark.apache.org/docs/1.6.1/tuning.html)
* [Spark 2.0 in Spark Summit Feb 2016](https://www.youtube.com/watch?v=ZFBgY0PwUeY&feature=youtu.be)
* [Working with Key-Value Pairs](http://heather.miller.am/teaching/cs212/slides/week19b.pdf)
* [Broadcast and Accumulators](http://heather.miller.am/teaching/cs212/slides/week20.pdf)
* [Mastering Apache Spark](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/content/spark-overview.html)
