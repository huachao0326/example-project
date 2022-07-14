package com.test.validate.group;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @Description: 分组排序
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
@GroupSequence({Default.class, MyValidateGroup.class})
public interface SortValidateGroup {
}
