
package lambda.ingestion;

import java.util.Properties;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;

import data.streaming.sources.ADSITwitterSource;
import lambda.ingestion.sinks.MongoDBSinkFunction;
import lambda.utils.Utils;

public class DataIngestor implements Runnable {

	private static final Integer PARALLELISM = 1;

	@Override
	public void run() {
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		env.setParallelism(PARALLELISM);

		SourceFunction<String> simpleSource = new ADSITwitterSource();

		DataStream<String> stream = env.addSource(simpleSource);
		stream.addSink(new MongoDBSinkFunction());

		Properties props = Utils.PROPIEDADES;
		FlinkKafkaProducer010.writeToKafkaWithTimestamps(stream, props.getProperty("topic"), new SimpleStringSchema(), props );
		stream.print();

		try {
			env.execute("Twitter Streaming Data Producer");
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}

	}

}
