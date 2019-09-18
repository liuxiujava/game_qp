package x.db;

import jsa.dispatcher.AbsWorker;
import jsa.dispatcher.IDispatcher;
import jsa.log.Logger;
import jsa.log.LoggerFactory;
import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import jsa.orm.vo.JsaVo;
import x.executor.ExecutorDispatcher;


public class SqlSave<T extends JsaVo> extends AbsWorker {

    final static Logger logger = LoggerFactory.getLogger(SqlSave.class, ExecutorDispatcher.LogActor);
    private IDataSource ds = JsaOrmFactory.getDataSource();
    private T data;
    private T[] moreData;
    private OperateType operateType;

    public SqlSave(IDispatcher actor, OperateType operateType) {
        super(actor);
        this.operateType = operateType;
    }

    @Override
    protected void execute() {
        try {
            int num = 0;
            if (operateType == OperateType.SaveOne) {
                num = ds.save(data);
            } else {
                num = ds.save(moreData).length;
            }
            if (num <= 0) {
                logger.error("execute SqlSave error " + data.getClass() + " id" + data.getId() + " OperateType:" + operateType);
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

    public T[] getMoreData() {
        return moreData;
    }

    public void setMoreData(T[] moreData) {
        this.moreData = moreData;
    }
}
