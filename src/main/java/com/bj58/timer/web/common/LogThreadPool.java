package com.bj58.timer.web.common;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;
@Component
public class LogThreadPool {

    private static AtomicInteger i = new AtomicInteger(0);

    public static ThreadPoolExecutor showExec = new ThreadPoolExecutor(30, 50, 30l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(20),
            new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    System.out.print("createThread-showExec" + i.incrementAndGet());
                    return new Thread(r, "cateExec-threadPool");
                }
            });

}