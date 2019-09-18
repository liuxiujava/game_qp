package x.db;

import jsa.dispatcher.IDispatcher;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.orm.vo.JsaVo;
import x.executor.ExecutorDispatcher;
import x.vo.NewRankVo;
import x.vo.RankInfoVo;
import x.vo.UserVo;

import java.util.List;

/**
 * User: LiuXiu
 * Date: 2015/9/22
 * Time: 15:02
 * Description:
 */
public class SqlManager {
    private static final Logger logger = LoggerFactory.getLogger(SqlManager.class, ExecutorDispatcher.LogActor);


    public static List<NewRankVo> getNewRankVos(String sql) {
        IDataSource ds = JsaOrmFactory.getDataSource();
        NewRankVo rankVo = new NewRankVo();
        return ds.getBySql(NewRankVo.class, sql, rankVo);
    }

    public static UserVo getUserVoByPtIdFromDb(String ptId) {
        IDataSource ds = JsaOrmFactory.getDataSource();
        UserVo userVo = new UserVo();
        userVo.setPtId(Integer.parseInt(ptId));
        return ds.getOneByValue(userVo, "ptId");
    }


    public static List<RankInfoVo> getRankInfo(String ptId, int num, int page) {
        IDataSource ds = JsaOrmFactory.getDataSource();
        int start = num * page;
        int end = num * (page + 1);
        String sql = "";
        if (ptId.equals("0")) {
            sql = "select * from RankInfoVo order by time desc limit " + start + "," + end + " ";
        } else {
            sql = "select * from RankInfoVo where ptId='" + ptId + "' order by time desc limit " + start + "," + end + " ";
        }
        logger.error(sql);
        return ds.getBySql(RankInfoVo.class, sql, null);
    }

    public static List<UserVo> getRankInfo(int type, int num, int page) {
        IDataSource ds = JsaOrmFactory.getDataSource();
        int start = num * page;
        int end = num * (page + 1);
        String sql = "";
        if (type == 1) {
            sql = "select * from UserVo order by integral desc limit " + start + "," + end;
        } else {
            sql = "select * from UserVo order by win desc limit " + start + "," + end;
        }
        return ds.getBySql(UserVo.class, sql, null);
    }


    public static UserVo getUserVoByRoleNameFromDb(String roleName) {
        IDataSource ds = JsaOrmFactory.getDataSource();
        UserVo userVo = new UserVo();
        userVo.setUserName(roleName);
        return ds.getOneByValue(userVo, "userName");
    }


    public static <T extends JsaVo> void updateBySql(T vo, IDispatcher dispatcher, String sql) {
        SqlUpdate<T> dbUpdate = new SqlUpdate<>(dispatcher,
                OperateType.BySql, vo);
        dbUpdate.setSql(sql);
        dbUpdate.executeByQueue();
    }

    public static <T extends JsaVo> void deleteVo(T vo, IDispatcher dispatcher) {
        SqlDelete<T> dbDelete = new SqlDelete<>(dispatcher, OperateType.ById);
        dbDelete.setData(vo);
        dbDelete.executeByQueue();
    }


    public static <T extends JsaVo> void deleteBatchBySql(String sql, T vo, IDispatcher dispatcher) {
        SqlDelete<T> dbDelete = new SqlDelete<>(dispatcher, OperateType.BySql);
        dbDelete.setSql(sql);
        dbDelete.setData(vo);
        dbDelete.executeByQueue();
    }

    public static <T extends JsaVo> void updateVo(T vo, IDispatcher dispatcher, String... value) {
        SqlUpdate<T> dbUpdate = new SqlUpdate<>(dispatcher, OperateType.ByValue, vo);
        dbUpdate.setValue(value);
        dbUpdate.executeByQueue();
    }

    public static <T extends JsaVo> void saveVo(T vo, IDispatcher dispatcher) {
        SqlSave<T> sqlSave = new SqlSave<>(dispatcher, OperateType.SaveOne);
        sqlSave.setData(vo);
        sqlSave.executeByQueue();
    }


}
