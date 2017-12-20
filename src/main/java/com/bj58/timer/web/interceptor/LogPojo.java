package com.bj58.timer.web.interceptor;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class LogPojo{
    
    Logger logger = Logger.getLogger(LogPojo.class);  
    String logStr = null;
    /** 
     * 前置通知：在某连接点之前执行的通知，但这个通知不能阻止连接点前的执行 
     */ 
    public void beforeMethod(JoinPoint jp){   
        logStr = jp.getTarget().getClass().getName() + " 类的 "  
                + jp.getSignature().getName()+" 方法开始执行 ***Start***"  
                + "参数:" + Arrays.toString(jp.getArgs());
        logger.info(logStr);  
    }   
    /** 
     * 后置通知 
     */ 
    public void afterMethod(JoinPoint jp){   
        logStr =jp.getTarget().getClass().getName() + " 类的 "  
                + jp.getSignature().getName() +" 方法执行结束 ***End***";   
        logger.info(logStr);  
    }   
    
    
    public void afterThrowMethod(){
       /* logStr = "发生异常,错误信息如下：["+e+"]";  
        logger.error(logStr);   
        logger.error("堆栈信息：",e); */        
    }
    
    public void afterReturnMethod(){
        
    }
    /** 
     * 环绕通知：包围一个连接点的通知，可以在方法的调用前后完成自定义的行为，也可以选择不执行 
     * 
     */ 
    public Object aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object result=null;  
        try{    
            result = pjp.proceed();    
        }catch(Exception e){ 
            logStr = "方法："+pjp.getTarget().getClass() + "." + pjp.getSignature().getName()+ "() ";  
            logStr = logStr + "参数:" + Arrays.toString(pjp.getArgs());
            logStr = logStr+"错误信息如下：\r\n ["+e+"]";  
            logger.error(logStr);   
            logger.error("堆栈信息：",e);   
            throw e;  //使用这种方法如果捕获到的异常只处理，不抛出，会导致程序hang住
        }  
    
        return result;  
    }
}
