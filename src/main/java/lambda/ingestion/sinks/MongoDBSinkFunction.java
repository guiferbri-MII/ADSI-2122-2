package lambda.ingestion.sinks;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import data.streaming.dto.TweetDTO;
import lambda.layer.data.access.MongoDBDAO;
import lambda.layer.data.access.SingletonMongoDBDAO;
import lambda.utils.Utils;

public class MongoDBSinkFunction implements SinkFunction<String> {

	private static final long serialVersionUID = 1L;
	private static List<String> tweets;
	private static MongoDBDAO dao;

	public MongoDBSinkFunction() {
		tweets = new LinkedList<>();
		dao = SingletonMongoDBDAO.getMongoDBDAO();
		dao.deleteDataLake();
	}

	public void invoke(String value) {
		save(value);
	}

	synchronized private void save(String value) {
		tweets.add(value);
		if (tweets.size() >= Utils.PACKAGE_SIZE) {
			Morphia m = dao.getMorphia();
			Datastore ds = dao.getDatastore();

			List<TweetDTO> jsons = tweets.stream()
					.map((String s) -> m.fromDBObject(ds, TweetDTO.class, (DBObject) BasicDBObject.parse(s)))
					.collect(Collectors.toList());
			ds.save(jsons);
			tweets.clear();
		}
	}

}
