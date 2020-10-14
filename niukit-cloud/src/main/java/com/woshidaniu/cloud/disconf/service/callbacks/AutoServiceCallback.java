package com.woshidaniu.cloud.disconf.service.callbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfUpdateService;
import com.baidu.disconf.client.common.update.IDisconfUpdate;
import com.woshidaniu.cloud.disconf.service.AutoService;

/**
 * 这是 autoconfig.properties 的回调函数类
 * <p/>
 * Created by knightliao on 15/3/21.
 */
@Service
@DisconfUpdateService(confFileKeys = {"autoconfig.properties", "autoconfig2.properties"})
public class AutoServiceCallback implements IDisconfUpdate {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AutoServiceCallback.class);

    @Autowired
    private AutoService autoService;

    public void reload() throws Exception {

        LOGGER.info("reload callback " + "autoconfig.properties or autoconfig2.properties" + autoService.getAuto());

    }
}
