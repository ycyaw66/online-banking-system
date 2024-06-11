package com.zjuse.bankingsystem.mapper.foreignCurrency;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeRecordMapper {
    // Retrieve all trade records for a particular user
    @Select("SELECT * FROM trade_record WHERE user_id = #{user_id, jdbcType=VARCHAR}")
    List<TradeRecord> findTradeRecordsByUserId(@Param("user_id") String user_id);

    // 精确搜索
    @SelectProvider(type = SqlBuilder.class, method = "buildQueryTradeRecords")
    List<TradeRecord> searchTradeRecords(Map<String, Object> params);

    class SqlBuilder {
        public String buildQueryTradeRecords(@Param("params") Map<String, Object> params) {
             System.out.println(params);
            String sql = new SQL() {{
                SELECT("*");
                FROM("trade_record");
                if (params.get("userId") != null & params.get("userId") != "") {
                    WHERE("user_id = '" + params.get("userId") + "'");
                }
                if (params.get("fcId") != null & params.get("fcId") != "") {
                    WHERE("fc_id = '" + params.get("fcId") + "'");
                }
                if (params.get("tradeTimeRange") != null && ((List<?>) params.get("tradeTimeRange")).size() == 2) {
                    WHERE("trade_time BETWEEN '" + ((List<?>) params.get("tradeTimeRange")).get(0).toString().replace('T',' ') + "' AND '" + ((List<?>) params.get("tradeTimeRange")).get(1).toString().replace('T', ' ') + "'");
                }
                if (params.get("amountCNYMin") != null & params.get("amountCNYMin") != "") {
                    WHERE("amount_CNY >= " + params.get("amountCNYMin"));
                }
                if (params.get("amountCNYMax") != null & params.get("amountCNYMax") != "") {
                    WHERE("amount_CNY <= " + params.get("amountCNYMax"));
                }
                if (params.get("amountForeignCurrencyMin") != null & params.get("amountForeignCurrencyMin") != "") {
                    WHERE("amount_foreign_currency >= " + params.get("amountForeignCurrencyMin"));
                }
                if (params.get("amountForeignCurrencyMax") != null & params.get("amountForeignCurrencyMax") != "") {
                    WHERE("amount_foreign_currency <= " + params.get("amountForeignCurrencyMax"));
                }
                if (params.get("creditCardId") != null & params.get("creditCardId") != "") {
                    WHERE("credit_card_id = '" + params.get("creditCardId") + "'" );
                }
                if (params.get("isBuyIn") != null) {
                    WHERE("is_buy_in = " + params.get("isBuyIn"));
                }
            }}.toString();
             System.out.println(sql);
            return sql;
        }
    }

    // Insert a new trade record into the database
    @Insert("INSERT INTO trade_record (trade_id, fc_id, trade_time, amount_cny, amount_foreign_currency, user_id, credit_card_id, is_buy_in) " +
            "VALUES (#{trade_id}, #{fc_id}, #{trade_time}, #{amount_cny}, #{amount_foreign_currency}, #{user_id}, #{credit_card_id}, #{is_buy_in})")
    void insertTradeRecord(TradeRecord tradeRecord);

    // Retrieve a trade record by its ID
    @Select("SELECT * FROM trade_record WHERE trade_id = #{trade_id}")
    TradeRecord findTradeRecordById(@Param("trade_id") String trade_id);

    // Update an existing trade record
    @Update("UPDATE trade_record SET fc_id = #{fc_id}, trade_time = #{trade_time}, amount_cny = #{amount_cny}, " +
            "amount_foreign_currency = #{amount_foreign_currency}, credit_card_id = #{credit_card_id}, is_buy_in = #{is_buy_in} WHERE trade_id = #{trade_id}")
    void updateTradeRecord(TradeRecord tradeRecord);

    // Delete a trade record by its ID
    @Delete("DELETE FROM trade_record WHERE trade_id = #{trade_id}")
    void deleteTradeRecordById(@Param("trade_id") String trade_id);
}
