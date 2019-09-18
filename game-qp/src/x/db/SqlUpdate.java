package x.db;

import jsa.dispatcher.AbsWorker;
import jsa.dispatcher.IDispatcher;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.orm.vo.JsaVo;
import x.executor.ExecutorDispatcher;
import x.utils.StringUtil;

import java.util.Arrays;


public class SqlUpdate<T extends JsaVo> extends AbsWorker {

    final static Logger logger = LoggerFactory.getLogger(SqlUpdate.class, ExecutorDispatcher.LogActor);
    private IDataSource ds = JsaOrmFactory.getDataSource();
    private T data;
    private OperateType operateType;
    private String sql;
    private String[] value;

    public SqlUpdate(IDispatcher actor, OperateType operateType, T data) {
        super(actor);
        this.operateType = operateType;
        this.data = data;
    }

    @Override
    protected void execute() {

        try {
            int num = 0;
            if (operateType == OperateType.ById) {
                num = ds.updateById(data);
            } else if (operateType == OperateType.ByValue) {
                num = ds.updateById(data, value);
            } else if (operateType == OperateType.BySql) {
                num = ds.updateBySql(sql, data);
            } else {
                logger.error("execute " + data.getClass() + " OperateType:" + operateType);
            }
            if (num <= 0) {
                logger.error("execute SqlUpdate error " + data.getClass() + " id" + data.getId() + " OperateType:" + operateType + " value=" + StringUtil.mergeString2(Arrays.asList(value), ","));
            }


        } catch (Exception e) {
            for (String string : value) {
                logger.error("sqlUpdate 报错信息 value：" + string);
            }
            logger.error("sqlUpdate 记录下报错信息，sql：" + sql);
            logger.error("execute " + data.getClass(), e);
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String[] getValue() {
        return value;
    }

    public void setValue(String... value) {
        this.value = value;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
