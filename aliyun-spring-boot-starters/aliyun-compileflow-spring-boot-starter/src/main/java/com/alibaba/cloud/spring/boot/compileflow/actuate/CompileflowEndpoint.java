package com.alibaba.cloud.spring.boot.compileflow.actuate;

import com.alibaba.cloud.spring.boot.compileflow.env.CompileflowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yusu
 */
@Endpoint(id = "compileflow")
public class CompileflowEndpoint {

    @Autowired
    private CompileflowProperties compileflowProperties;

    @ReadOperation
    public Map<String, Object> invoke() {
        Map<String, Object> result = new HashMap();
        result.put("version", this.getClass().getPackage().getImplementationVersion());
        result.put("properties", this.compileflowProperties);
        return result;
    }

}
