// WIP, If you'd like, I'd love to talk this project over with you.
package ca.jrvs

import java.io.File
import org.apache.spark.sql.SparkSession
import io.delta.tables._

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
    sql(s"CREATE TABLE $srcTbl (part STRING, foo STRING, bar BIGINT NOT NULL, baz DECIMAL)")
    sql(s"LOAD DATA LOCAL INPATH '$srcTbl/main/resources/$srcTbl.txt' INTO TABLE $srcTbl")

    sql(s"DROP TABLE IF EXISTS $backupTbl")
    sql(s"CREATE TABLE $backupTbl (part STRING, foo STRING, bar BIGINT NOT NULL, baz DECIMAL)")
    sql(s"LOAD DATA LOCAL INPATH '$srcTbl/main/resources/$backupTbl.txt' INTO TABLE $backupTbl")

    val srcDF = sql(s"SELECT * FROM $srcTbl")
    val backupDF = sql(s"SELECT * FROM $backupTbl")

//    This would work, but if the tables are extremely large and changes are few, it doesn't make much sense to overwrite the entire table
//    sql(s"INSERT OVERWRITE TABLE $backupTbl FROM $srcTbl SELECT *")

//    This would perform better, but can only use if the tables use id columns, which wasn't specified in the project description
    sql(s"""
      MERGE INTO $backupTbl
      USING $srcTbl
      ON $srcTbl.id = $backupTbl.id
      WHEN MATCHED THEN
      UPDATE SET *
      WHEN NOT MATCHED
      THEN INSERT (part, foo, bar, baz) VALUES (part, foo, bar, baz)
      """)


//      Below is my initial over-engineered "solution" that doesn't even work
//
//      import uk.co.gresearch.spark.diff._
//
//
////    delta shows the difference between backup and src
////    It has a diff column that indicates what operation was done to a row to go from backup to src.
////    Possible diff values are 'C' for Changed, 'I' for Inserted, 'D' for Deleted, and 'N' for uNchanged
//    val delta = backupDF.diff(srcDF, "foo", "bar", "baz")
//
////    First, I gather them into separate collections
//    val changes = delta.where("diff == 'C'")
//    val inserts = delta.where("diff == 'I'")
//    val deletes = delta.where("diff == 'D'")
//
////    Then, I loop through all entries in each and perform the operation on backup to transform to match src
//    for (row <- changes.rdd.collect)
//    {
//      val thisPart = row.get(row.fieldIndex("right_part"))
//      val thisFoo = row.get(row.fieldIndex("foo"))
//      val thisBar = row.get(row.fieldIndex("bar"))
//      val thisBaz = row.get(row.fieldIndex("baz"))
////      Update not supported in this version...
////      sql(s"UPDATE $backupTbl SET part = '$thisPart' WHERE foo = '$thisFoo' AND bar = $thisBar AND baz = $thisBaz;")
//    }
//
//    for (row <- inserts.rdd.collect)
//    {
//      val thisPart = row.get(row.fieldIndex("right_part"))
//      val thisFoo = row.get(row.fieldIndex("foo"))
//      val thisBar = row.get(row.fieldIndex("bar"))
//      val thisBaz = row.get(row.fieldIndex("baz"))
//      sql(s"INSERT INTO $backupTbl (part, foo, bar, baz) VALUES ('$thisPart', '$thisFoo', $thisBar, $thisBaz);")
//    }
//
//    for (row <- deletes.rdd.collect)
//    {
//      val thisFoo = row.get(row.fieldIndex("foo"))
//      val thisBar = row.get(row.fieldIndex("bar"))
//      val thisBaz = row.get(row.fieldIndex("baz"))
////      Delete not supported either...
////      sql(s"DELETE FROM $backupTbl WHERE foo = '$thisFoo' AND bar = $thisBar AND baz = $thisBaz;")
//    }

    sql(s"SELECT * FROM $backupTbl").show
  }
}

