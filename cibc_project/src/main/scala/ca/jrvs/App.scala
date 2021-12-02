// WIP, does not actually take input yet...
package ca.jrvs

import java.io.File
import org.apache.spark.sql.SparkSession
import uk.co.gresearch.spark.diff._

object App {

  def main(args: Array[String]) {
    var srcTbl = "src"
    var backupTbl = "backup"
    if (args.length != 2) {
      println("Expected 2 arguments: source database name, backup database name")
//      ought to quit here but let's go with the default "src" and "backup" for now
    }
    else {
      srcTbl = args(0)
      backupTbl = args(1)
    }

    val warehouseLocation = new File("spark-warehouse").getAbsolutePath
    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .config("spark.master", "local")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
      .getOrCreate()

    import spark.sql

//    Sets up tables for demonstrative purposes
    sql(s"DROP TABLE IF EXISTS $srcTbl")
    sql(s"CREATE TABLE $srcTbl (part STRING, foo STRING, bar LONG, baz DECIMAL(9,3)) USING hive")
    sql(s"LOAD DATA LOCAL INPATH '$srcTbl/main/resources/$srcTbl.txt' INTO TABLE $srcTbl")

    sql(s"DROP TABLE IF EXISTS $backupTbl")
    sql(s"CREATE TABLE $backupTbl (part STRING, foo STRING, bar LONG, baz DECIMAL(9,3)) USING hive")
    sql(s"LOAD DATA LOCAL INPATH '$srcTbl/main/resources/$backupTbl.txt' INTO TABLE $backupTbl")

    val srcDF = sql(s"SELECT * FROM $srcTbl")
    val backupDF = sql(s"SELECT * FROM $backupTbl")

//    delta shows the difference between backup and src
//    It has a diff column that indicates what operation was done to a row to go from backup to src.
//    Possible diff values are 'C' for Changed, 'I' for Inserted, 'D' for Deleted, and 'N' for uNchanged
    val delta = backupDF.diff(srcDF, "foo", "bar", "baz")

//    First, I gather them into separate collections
    val changes = delta.where("diff == 'C'")
    val inserts = delta.where("diff == 'I'")
    val deletes = delta.where("diff == 'D'")

//    Then, I loop through all entries in each and perform the operation on backup to transform to match src
    for (row <- changes.rdd.collect)
    {
      val thisPart = row.get(row.fieldIndex("right_part"))
      val thisFoo = row.get(row.fieldIndex("foo"))
      val thisBar = row.get(row.fieldIndex("bar"))
      val thisBaz = row.get(row.fieldIndex("baz"))
//      Update not supported in this version...
//      sql(s"UPDATE $backupTbl SET part = '$thisPart' WHERE foo = '$thisFoo' AND bar = $thisBar AND baz = $thisBaz;")
    }

    for (row <- inserts.rdd.collect)
    {
      val thisPart = row.get(row.fieldIndex("right_part"))
      val thisFoo = row.get(row.fieldIndex("foo"))
      val thisBar = row.get(row.fieldIndex("bar"))
      val thisBaz = row.get(row.fieldIndex("baz"))
      sql(s"INSERT INTO $backupTbl (part, foo, bar, baz) VALUES ('$thisPart', '$thisFoo', $thisBar, $thisBaz);")
    }

    for (row <- deletes.rdd.collect)
    {
      val thisFoo = row.get(row.fieldIndex("foo"))
      val thisBar = row.get(row.fieldIndex("bar"))
      val thisBaz = row.get(row.fieldIndex("baz"))
//      Delete not supported either...
//      sql(s"DELETE FROM $backupTbl WHERE foo = '$thisFoo' AND bar = $thisBar AND baz = $thisBaz;")
    }

    sql(s"SELECT * FROM $backupTbl").show
  }
}

