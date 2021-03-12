package cn.kth;

import cn.kth.common.bean.Topic;
import cn.kth.common.bean.Topics;
import cn.kth.common.conf.ConfReload;
import cn.kth.common.conf.Env;
import cn.kth.common.tasks.RefreshTasks;
import cn.kth.common.tasks.StreamTasks;
import cn.kth.common.util.JsonUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.rapidoid.setup.App;

import java.util.HashMap;
import java.util.Map;

public class Launcher {
    public static void main(String[] args) throws Exception {


        System.setProperty("HADOOP_USER_NAME", "oozie");
        // 更新环境变量
        refreshEnv(args);
        // 启动容器
        App.bootstrap(args).jpa().auth();

        // 添加定时任务
        Map<Class<? extends Job>, String> tasks = new HashMap<>();

        tasks.put(StreamTasks.class, "0 5 /1 * * ? ");
        tasks.put(RefreshTasks.class, "0 15 /1 * * ? ");

        // 启动定时任务
        startTasks(tasks);
        StreamHandler.init();
        Topics topics = JsonUtil.fromString(ConfReload.getProperty("topics"), Topics.class);
        for (Topic topic : topics.getTopics()) {
            new KTH().start(topic);
        }

    }


    private static void startTasks(Map<Class<? extends Job>, String> tasks) throws
            SchedulerException {

        // 创建调度器Schedule
        SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        Scheduler scheduler = schedulerFactory.getScheduler();
        for (Map.Entry<Class<? extends Job>, String> entry : tasks.entrySet()) {
            String jobName = entry.getKey().getName();
            CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(entry.getValue());

            JobDetail jobDetail = JobBuilder.newJob(entry.getKey()).withIdentity(jobName,
                    jobName + "-JobGroup").build();
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName + "-Trigger",
                    jobName + "-TriggerGroup").startNow().withSchedule(cron).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }

        scheduler.start();

    }

    /**
     * 根据命令行参数刷新环境变量。默认DEV环境
     *
     * @param args
     */
    private static void refreshEnv(String[] args) {
        for (String item : args) {
            String neat = item.trim().toLowerCase();
            if (neat.startsWith("profiles")) {
                Env.environment = neat.replace("profiles", "").replace("=", "").trim();
                break;
            }
        }
    }

}
