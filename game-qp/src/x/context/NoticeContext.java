package x.context;

import jsa.orm.IDataSource;
import jsa.orm.JsaOrmFactory;
import x.vo.NoticeVo;
import x.vo.ParamsVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoticeContext {

    private static List<NoticeVo> noticeVos = new CopyOnWriteArrayList<>();
    private static Map<Integer, ParamsVo> paramsVoMap = new HashMap<>();


    public static void init() {
        IDataSource ds = JsaOrmFactory.getDataSource();
        noticeVos = ds.getBySql(NoticeVo.class, "select * from NoticeVo", null);

        List<ParamsVo> paramsVoList = ds.getBySql(ParamsVo.class, "select * from ParamsVo", null);

        for (ParamsVo vo : paramsVoList) {
            paramsVoMap.put(vo.getParam_key(), vo);
        }

    }

    public static List<NoticeVo> getNoticeVos() {
        return noticeVos;
    }


}
