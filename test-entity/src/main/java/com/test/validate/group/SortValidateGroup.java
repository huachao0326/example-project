package com.test.validate.group;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

/**
 * @Description: εη»ζεΊ
 * @Author: huaChao
 * @Date: 2022/7/14
 * @Version: 1.0.0
 **/
@GroupSequence({Default.class, MyValidateGroup.class})
public interface SortValidateGroup {
}
