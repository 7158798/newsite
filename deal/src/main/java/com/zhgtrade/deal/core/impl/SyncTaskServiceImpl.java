package com.zhgtrade.deal.core.impl;

import com.zhgtrade.deal.core.SyncTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 使用一个队列、线程来实现异步任务
 * CopyRight : www.zhgtrade.com
 * Author : 林超（362228416@qq.com）
 * Date： 2016-06-06 12:55
 */
public class SyncTaskServiceImpl implements SyncTaskService, Runnable {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private BlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();

    @PostConstruct
    public void init() {
        new Thread(this).start();
    }

    @Override
    public int count() {
        return queue.size();
    }

    @Override
    public void execute(Runnable runnable) {
        try {
            queue.put(runnable);
        } catch (Exception e) {
            log.error("add sync task error reason " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Runnable runnable = queue.take();
                runnable.run();
            } catch (Exception e) {
                log.error("execute task error reason " + e.getLocalizedMessage());
            }
        }
    }

}
