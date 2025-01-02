package org.example;

import com.clickhouse.client.api.Client;
import com.clickhouse.client.api.insert.InsertResponse;
import com.clickhouse.client.api.insert.InsertSettings;
import com.clickhouse.client.api.metrics.ServerMetrics;
import com.clickhouse.data.ClickHouseFormat;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

public class Main {

    public static void main(String[] args) throws IOException {

         Client client = new Client.Builder()
                .addEndpoint("http://localhost:18123")
                .setUsername("test")
                .setPassword("test")
                .compressServerResponse(true)
                .setDefaultDatabase("test")
                .build();

         InsertSettings insertSettings = new InsertSettings()
                 .serverSetting("format_csv_delimiter", "|");

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        GZIPInputStream inputStream = new GZIPInputStream(classloader.getResourceAsStream("data.csv.gz"));

         try (
                 InsertResponse response = client.insert("test", inputStream, ClickHouseFormat.CSVWithNames,
                         insertSettings).get(5, TimeUnit.SECONDS)

                 ) {
             System.out.println("Inserted "+ response.getMetrics().getMetric(ServerMetrics.NUM_ROWS_WRITTEN).getLong() +" rows into table" );

         } catch (
                 Exception e
         ){
             System.out.println("Failed to write CSV to database");
             throw new RuntimeException(e);
         }

    }
}