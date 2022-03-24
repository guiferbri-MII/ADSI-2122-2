package lambda.layer.batch;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import lambda.utils.Utils;

public class BatchLayerLauncher implements Runnable {

	public void run() {

		// JobDetail
		JobDetail job = JobBuilder.newJob(BatchJob.class).withIdentity("BatchJob", "group1").build();

		// Trigger
		Trigger trigger = TriggerBuilder.newTrigger().forJob("BatchJob", "group1").withSchedule(
			SimpleScheduleBuilder.simpleSchedule()
			.withIntervalInSeconds(Utils.SECONDS_TO_NEW_OFFLINE).repeatForever())
			.build();

		// Scheduler
		Scheduler scheduler;
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			throw new IllegalStateException(e);
		}
	}
}