package rddfirst;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;


public class IoTErzurum {

    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        SparkSession sparkSession = SparkSession.builder()
                .appName("SparkMessageListener")
                .master("local")
                .getOrCreate();

        StructType weatherType = new StructType()
                .add("ilceler","string")
                .add("havasembol","string")
                .add("sicaklik","integer")
                .add("ruzgaryon","string");

        Dataset<Row> rowData = sparkSession.readStream().schema(weatherType)
                .option("sep", ",")
                .csv("C:\\Users\\akifk\\Desktop\\datasets\\*");

        Dataset<Row> sicaklikData = rowData.select("ilceler", "sicaklik")
                .where("sicaklik<0");

        StreamingQuery start = sicaklikData.writeStream().outputMode("complete").format("console").start();

        start.awaitTermination();

    }
}
