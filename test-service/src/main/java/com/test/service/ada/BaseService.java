package com.test.service.ada;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaseService {

    public BaseService(@Value("${param.projectId}") String projectId) {
    }

}
