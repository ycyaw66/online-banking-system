package com.zjuse.bankingsystem.service.foreignCurrency;

import java.time.LocalDateTime;
import java.util.List;

import com.zjuse.bankingsystem.model.Currency;

public interface CurrencyManagerService {

    /**
    * 查询所有货币信息。
    *   
    * @return 返回所有货币信息。
    */
    public List<Currency> selectAllCurrency();

    /**
    * 查询指定货币信息。
    *   
    * @param currency_name 货币的名称。
    * @return 返回指定货币信息。
    */
    public List<Currency> selectCurrency(String currency_name);

    public double selectRecentRate(String id) throws Exception;

    public double getCurrencyRate(String currency_name, LocalDateTime date);

    /**
    * 添加货币汇率信息。
    *   

    * @param rate 指定货币的汇率。
    * @param currency_name 货币的名称。
    * @param data_operator_id 操作该数据的用户ID。
    * @return 如果添加成功返回true，否则返回false。
     * @throws Exception 
    */
    public boolean addCurrencyRate(double rate, String currency_name, String data_operator_id) throws Exception;

    /**
    * 更新货币汇率信息。
    *   
    * @param date 汇率生效的日期和时间，使用LocalDateTime表示。
    * @param rate 指定货币的汇率。
    * @param currency_name 货币的名称。
    * @param data_operator_id 操作该数据的用户ID。
    * @return 如果更新成功返回true，否则返回false。
    */
    public boolean updateCurrencyRate(LocalDateTime date, double rate, String currency_name, String data_operator_id) throws Exception;

}
