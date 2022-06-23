package com.test.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;


@Data
public class BaseEntity {
    @TableId(
            value = "id",
            type = IdType.AUTO
    )
    public Long id;

    @TableField(
            value = "create_time",
            fill = FieldFill.INSERT
    )
    public Date createTime;

    @TableField(
            value = "update_time",
            fill = FieldFill.INSERT_UPDATE
    )
    public Date updateTime;

    @TableField(
            value = "version",
            fill = FieldFill.INSERT
    )
    public Long version;

    @Override
    public String toString() {
        return "BaseEntity{id=" + this.id + ", createTime=" + this.createTime + ", updatedTime=" + this.updateTime + ", version=" + this.version + '}';
    }

}
