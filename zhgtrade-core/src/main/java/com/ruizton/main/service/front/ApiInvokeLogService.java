package com.ruizton.main.service.front;

import com.ruizton.main.dao.ApiInvokeLogDao;
import com.ruizton.main.model.ApiInvokeLog;
import com.ruizton.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 招股金服
 * CopyRight : www.zhgtrade.com
 * Author : xuelin
 * Date： 2016/7/22
 */
@Service
public class ApiInvokeLogService {
    @Autowired
    private ApiInvokeLogDao apiInvokeLogDao;

    public void saveLog(ApiInvokeLog log){
        log.setCreateTime(Utils.getTimestamp());
        apiInvokeLogDao.save(log);
    }

    public ApiInvokeLog get(int id){
        return apiInvokeLogDao.get(id);
    }

    public List<ApiInvokeLog> findByUser(int userId, int firstResult, int maxResult){
        return apiInvokeLogDao.findByUser(userId, firstResult, maxResult);
    }

    public List<ApiInvokeLog> findByApi(int apiId, int firstResult, int maxResult){
        return apiInvokeLogDao.findByApi(apiId, firstResult, maxResult);
    }

    public int count (String keyword, Integer isSucceed){
        return apiInvokeLogDao.count(keyword, isSucceed);
    }

    public List<Map<String, Object>> list(String keyword, Integer isSucceed, int firstResult, int maxResult){
        return apiInvokeLogDao.list(keyword, isSucceed, firstResult, maxResult);
    }
}
