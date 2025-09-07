package com.radhesham.ecart.contex;

import com.radhesham.ecart.bean.SystemParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class CommonContext {
    private static final Logger logger = LoggerFactory.getLogger(CommonContext.class);
    private static CommonContext INSTANCE=null;

    private CommonContext(){
    }

    public static CommonContext getInstance(){
        try{
            if(Objects.isNull(CommonContext.INSTANCE)){
                logger.info("CommonContext CommonContext.INSTANCE is initialize by new instance");
                CommonContext.INSTANCE=new CommonContext();
            }
        }catch (Exception e){
            logger.error("Exception in CommonContext::getInstance() :",e);
        }
        return CommonContext.INSTANCE;
    }

    public SystemParameter getSystemParameter(String parameterIndex){
        SystemParameter systemParameter =null;
        try{
            logger.info("Entry in getSystemParameter() of index {}",parameterIndex);

            logger.info("Exit from getSystemParameter()");
        }catch (Exception e){
            logger.error("Exception in getSystemParameter() :",e);
        }
        return null;
    }
}
