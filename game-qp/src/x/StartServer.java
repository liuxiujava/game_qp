package x;

import jsa.ioc.context.ClassPathApplicationContext;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.utils.RandomUtil;
import ws.nett.AbsServer;
import x.context.NoticeContext;
import x.executor.ExecutorDispatcher;
import x.frame.DispatcherContext;
import x.schedule.ScheduledTask;
import x.utils.ServerConfig;


/**
 * Created by admin on 2015/1/13.
 */
public class StartServer extends AbsServer {
    private static final Logger logger = LoggerFactory.getLogger(StartServer.class, null);

    @Override
    protected void initialize() {


        try {
            ClassPathApplicationContext.getApplicationContext();
            JsaOrmFactory.initDefaultExecutorAdapter();
            IDataSource ds = JsaOrmFactory.getDataSource();
            ds.ensureIndexes();

            NoticeContext.init();
            DispatcherContext.init();
            ScheduledTask.start();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }

       /* String sql = "SELECT a.accountName,a.password,b.sm_name,b.sm_avatarIcon,b.sm_score as score,b.sm_roleType FROM kbe_accountinfos a,tbl_Avatar b WHERE a.entityDBID=b.sm_accountDbid";
        List<NewRank1Vo> list = SqlManager.getNewRank1Vos(sql);
        IDataSource ds = JsaOrmFactory.getDataSource();
        for (NewRank1Vo vo : list) {
            UserVo userVo = new UserVo();"
            userVo.setAccount(vo.getAccountName());
            userVo.setPassWd(vo.getPassword());
            userVo.setUserName(vo.getSm_name());
            userVo.setCreateTime(new Date());
            userVo.setSex(vo.getSm_roleType());
            userVo.setHeadId(vo.getSm_avatarIcon());
            userVo.setDiamond((int) vo.getScore());
            userVo.setLoginTime(new Date());
            ds.save(userVo);

            userVo.setPtId(UserContext.StartId + userVo.getId());
            ds.updateById(userVo, "ptId");
        }*/
        // 启动定时任务，有定时任务的业务，请查看ScheduledTask类实现，把业务加入到里面
//        logger.error("server start success! time:" + DateUtil.getDateFileFormat(new Date()));
    }


    public static void main(String[] args) {
        StartServer startServer = new StartServer();
        startServer.start();
    }

    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }

    @Override
    public int getPort() {
        return ServerConfig.getPort();
    }

    @Override
    protected boolean canShutdown() {
        ScheduledTask.stop();
        boolean canShutDown = ExecutorDispatcher.shutdown();

        System.out.println("canShutdown begin to shut down...Result=" + canShutDown);
        return canShutDown;
    }

}

