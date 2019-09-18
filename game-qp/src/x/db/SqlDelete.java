package x.db;

import jsa.dispatcher.AbsWorker;
import jsa.dispatcher.IDispatcher;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.orm.vo.JsaVo;
import x.executor.ExecutorDispatcher;


public class SqlDelete<T extends JsaVo> extends AbsWorker {

    final static Logger logger = LoggerFactory.getLogger(SqlDelete.class, ExecutorDispatcher.LogActor);
    private IDataSource ds = JsaOrmFactory.getDataSource();
    private T data;
    private OperateType operateType;
    private String sql;
    private String[] value;

    public SqlDelete(IDispatcher actor, OperateType operateType) {
        super(actor);
        this.operateType = operateType;
    }

    @Override
    protected void execute() {
        try {
            int num = 0;
            if (operateType == OperateType.ById) {
                num = ds.deleteById(data);
            } else if (operateType == OperateType.ByValue) {
                num = ds.deleteByValue(data, value);
            } else if (operateType == OperateType.BySql) {
//                ds.deleteByValue(data, sql);
                num = ds.deleteBySql(sql, data);
            } else {
                logger.error("execute " + data.getClass() + " OperateType:" + operateType);
            }
            if (num <= 0) {
                logger.error("execute SqlDelete error " + num + "  " + data.getClass() + " id" + data.getId() + " OperateType:" + operateType + " value=" + value);
            }

        } catch (Exception e) {
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

    public void setValue(String[] value) {
        this.value = value;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
