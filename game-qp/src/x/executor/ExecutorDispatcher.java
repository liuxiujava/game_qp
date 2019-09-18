package x.executor;

import jsa.dispatcher.IDispatcher;
import jsa.dispatcher.actor.Actor;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import ws.nett.message.IMessage;
import ws.nett.session.IoSession;
import x.context.UserContext;
import x.utils.ServerConfig;
import x.vo.UserVo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ExecutorDispatcher {

    final static Logger logger = LoggerFactory.getLogger(ExecutorDispatcher.class, ExecutorDispatcher.LogActor);
    public static ExecutorService executors;
    private static ExecutorService loginExecutors = Executors.newFixedThreadPool(2);//Executors.newFixedThreadPool(ServerConfig.getIoThreads());
    private static ExecutorService logExecutors = Executors.newSingleThreadExecutor();

    private static final int DbThreadSize = ServerConfig.getBizThreads() / 2;// > 0 ? ServerConfig.getBizThreads() / 2 : 1 + 1;
    private static Map<Integer, IDispatcher> LoginActors = new ConcurrentHashMap<>();
    public static Map<Integer, IDispatcher> BindingActors = new ConcurrentHashMap<>();

    public static Map<Integer, IDispatcher> DbActors = new ConcurrentHashMap<>();
    public static Map<Integer, IDispatcher> SceneActors = new ConcurrentHashMap<>();
    public final static IDispatcher LogActor = new Actor(logExecutors);//日志

    static {
        int size = 16 > ServerConfig.getBizThreads() ? 16 : ServerConfig.getBizThreads();
        executors = Executors.newFixedThreadPool(size);

        for (int i = 0; i < size; i++) {
            SceneActors.put(i, new Actor(executors));
        }

        for (int i = 0; i < ServerConfig.getIoThreads(); i++) {
            LoginActors.put(i, new Actor(loginExecutors));
        }

        for (int i = 0; i < DbThreadSize; i++) {
            DbActors.put(i, new Actor(executors));
        }
    }

    public static IDispatcher getDbActor(int id) {
        if (id < 0) {
            id = -id;
        }
        return DbActors.get(id % DbActors.size());
    }

    public static IDispatcher getSceneActor(int id) {
        return SceneActors.get(id % SceneActors.size());
    }

    public static IDispatcher matchDispatcher(IoSession session, IMessage msg) {

        if (session != null) {
            IDispatcher actor = getBindingActor(session);
            if (actor != null) {
                return actor;
            }
        }


        return LoginActors.get((int) session.getSessionId() % LoginActors.size());
    }

    public static IDispatcher getBindingActor(IoSession session) {
        UserVo user = UserContext.getUser(session);
        if (user != null) {
            IDispatcher actor = BindingActors.get(user.getIndex());
            if (actor != null) {
                return actor;
            }
        }
        return null;
    }

    public static void bindingActor(UserVo userVo, int roomId) {
        BindingActors.put(userVo.getIndex(), getSceneActor(roomId));
    }

    public static void removeBindActor(UserVo userVo) {
        BindingActors.remove(userVo.getIndex());
    }

    public static boolean shutdown() {
        System.out.println("shutdown begin to shut down...");
        shutdownExecutors(loginExecutors, "loginExecutors");
        shutdownExecutors(executors, "executors");
        shutdownExecutors(logExecutors, "logExecutors");
        return true;
    }

    private static void shutdownExecutors(ExecutorService executors, String type) {
        System.out.println(type + " begin to shut down...");
        executors.shutdown();
        while (!executors.isTerminated()) {
            System.out.println(type + " is shutting down...");
            sleep();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("sleep", e);
            e.printStackTrace();
        }
    }

}
