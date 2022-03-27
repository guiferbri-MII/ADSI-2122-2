package lambda.layer.batch;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mongodb.morphia.Datastore;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import data.streaming.dto.TweetDTO;
import lambda.layer.data.access.MongoDBDAO;
import lambda.layer.data.access.SingletonMongoDBDAO;
import lambda.layer.data.access.dto.OfflineModelDTO;
import lambda.utils.Utils;

public class BatchJob implements Job {

	private static MongoDBDAO dao;

	public BatchJob() {
		dao = SingletonMongoDBDAO.getMongoDBDAO();
	}

	public void execute(JobExecutionContext context) throws JobExecutionException {
		process();
	}

	synchronized public void process() {
		List<String> hashtags = new LinkedList<>();
		List<String> idiomas = new LinkedList<>();
		Datastore datastore = dao.getDatastore();

		List<TweetDTO> datos = dao.getDataLake();
		List<String> tags = datos.stream()
		.flatMap(x -> Stream.of(x.getText().split(Utils.WHITESPACE)))
		.filter(x -> x.startsWith(Utils.HASH))
		.collect(Collectors.toList());
		hashtags.addAll(tags);
		idiomas = datos.stream().flatMap(x -> Stream.of(x.getLanguage())).collect(Collectors.toList());

		Map<String, Long> freqs = hashtags.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
		Map<String, Long> freqIdiomas = idiomas.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
		datastore.save(new OfflineModelDTO(freqs, freqIdiomas));
	}

}
