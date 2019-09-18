package x.schedule;

import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.task.TaskExecutor;
import x.executor.ExecutorDispatcher;
import x.frame.FrameAct;

import java.util.concurrent.TimeUnit;

public class ScheduledTask {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class, ExecutorDispatcher.LogActor);
    //帧频检测
    public static final int FrameTime = 1;// 秒

    private static FrameAct frameAct = new FrameAct();

    private static TaskExecutor taskExecutor = new TaskExecutor();
    private static TaskExecutor commonExecutor = new TaskExecutor();

    /**
     * 把所有的需要定时的业务写到这里
     */
    public static void start() {
        logger.debug("ScheduledTask reset...");

        taskExecutor.startAtFixedRate(FrameTime, FrameTime, TimeUnit.SECONDS, frameAct);
        // 定时5s检测所有过期的称号，时装等
//        commonExecutor.startWithFixedDelay(ExpireActTime, ExpireActTime, TimeUnit.SECONDS, expireAct);
    }

    public static TaskExecutor getTaskExecutor() {
        return commonExecutor;
    }

    /**
     * 中断定时任务
     *
     * @param taskId
     */
    public static void cancelTask(String taskId) {
        commonExecutor.cancelTask(taskId);
    }

    public static void stop() {
        shutdownExecutors(taskExecutor, "taskExecutor");
        shutdownExecutors(commonExecutor, "commonExecutor");
    }


    private static void shutdownExecutors(TaskExecutor executors, String type) {
        System.out.println(type + " begin to shut down...");
        boolean isResult = executors.shutdown();
        System.out.println(type + " begin to shut down...Result=" + isResult);

    }

}
