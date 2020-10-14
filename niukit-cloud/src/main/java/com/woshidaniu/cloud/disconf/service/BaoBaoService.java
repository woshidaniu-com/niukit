package com.woshidaniu.cloud.disconf.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.disconf.client.common.annotations.DisconfItem;
import com.woshidaniu.cloud.disconf.config.Coefficients;

/**
 * 金融宝服务，计算一天赚多少钱
 *
 * @author liaoqiqi
 * @version 2014-5-16
 */
@Service
public class BaoBaoService {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BaoBaoService.class);

    public static final String key = "moneyInvest";

    @Value(value = "2000d")
    private Double moneyInvest;

    @Autowired
    private Coefficients coefficients;

    /**
     * 计算百发一天赚多少钱
     *
     * @return
     */
    public double calcBaiFa() {
        return coefficients.getBaiFaCoe() * coefficients.getDiscount() * getMoneyInvest();
    }

    /**
     * k 计算余额宝一天赚多少钱
     *
     * @return
     */
    public double calcYuErBao() {
        return coefficients.getYuErBaoCoe() * coefficients.getDiscount() * getMoneyInvest();
    }

    /**
     * 投资的钱，分布式配置 <br/>
     * <br/>
     * 这里切面无法生效，因为SpringAOP不支持。<br/>
     * 但是这里还是正确的，因为我们会将值注入到Bean的值里.
     *
     * @return
     */
    @DisconfItem(key = key)
    public Double getMoneyInvest() {
        return moneyInvest;
    }

    public void setMoneyInvest(Double moneyInvest) {
        this.moneyInvest = moneyInvest;
        LOGGER.info("i' m here: setting moneyInvest");
    }

}
