package com.zjuse.bankingsystem.entity.deposite;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.utils.DepositCardType;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

@Data
@TableName("deposite_card")
public class DepositeCard {
    @JsonProperty("id")
    @TableId(type = IdType.AUTO)
    Long id;//卡ID
    @JsonProperty("type")
    @NonNull
    DepositCardType type;//卡类型（借记卡或存折）
    @JsonProperty("accountid")
    @NonNull
    Long accountid;//绑定的账户ID

    public Long getCardId() {return id;}
    public void setCardId(Long cardId) {this.id = cardId;}
    public DepositCardType getType() {return type;}
    public void setType(DepositCardType type) {this.type = type;}
    public Long getAccountid() {return accountid;}
    public void setAccountid(Long accountid) {this.accountid = accountid;}
}
