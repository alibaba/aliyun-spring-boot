/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.cloud.spring.boot.examples.compileflow;

import com.alibaba.compileflow.engine.ProcessEngineFactory;
import com.alibaba.compileflow.engine.common.constants.FlowModelType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Compileflow Rest Controller
 *
 * @author yusu
 */
@RestController
public class CompileflowController {

    @GetMapping("/getJavaCode/bpm/{code}")
    private String getJavaCodeBpm(@PathVariable String code) {
        return ProcessEngineFactory.getProcessEngine().getJavaCode(code);
    }

    @GetMapping("/getJavaCode/bpmn20/{code}")
    private String getCodeBpmn20(@PathVariable String code) {
        return ProcessEngineFactory.getStatelessProcessEngine(FlowModelType.BPMN).getJavaCode(code);
    }

    @GetMapping("/getTestCode/bpm/{code}")
    private String getTestCodeBpm(@PathVariable String code) {
        return ProcessEngineFactory.getProcessEngine().getJavaCode(code);
    }

    @GetMapping("/getTestCode/bpmn20/{code}")
    private String getTestBpmn20(@PathVariable String code) {
        return ProcessEngineFactory.getStatelessProcessEngine(FlowModelType.BPMN).getTestCode(code);
    }

    @GetMapping("/startKtvProcess/bpm")
    @ResponseBody
    private Map<String, Object> startKtvProcessBpm() {
        final String code = "bpm.ktv.ktvExample";

        final Map<String, Object> context = new HashMap<>();
        List<String> pList = new ArrayList<>();
        pList.add("wuxiang");
        pList.add("yusu");
        context.put("pList", pList);

        return ProcessEngineFactory.getProcessEngine().start(code, context);
    }

    @GetMapping("/startKtvProcess/bpmn20")
    @ResponseBody
    private Map<String, Object> startKtvProcessBpmn20() {
        final String code = "bpmn20.ktv.ktvExample";

        final Map<String, Object> context = new HashMap<>();
        List<String> pList = new ArrayList<>();
        pList.add("wuxiang");
        pList.add("yusu");
        context.put("pList", pList);

        return ProcessEngineFactory.getStatelessProcessEngine(FlowModelType.BPMN).start(code, context);
    }

}
