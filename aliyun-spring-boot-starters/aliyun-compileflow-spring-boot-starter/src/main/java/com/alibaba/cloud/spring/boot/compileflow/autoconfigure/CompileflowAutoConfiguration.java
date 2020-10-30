/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.cloud.spring.boot.compileflow.autoconfigure;

import com.alibaba.cloud.spring.boot.compileflow.env.CompileflowProperties;
import com.alibaba.compileflow.engine.ProcessEngine;
import com.alibaba.compileflow.engine.ProcessEngineFactory;
import com.alibaba.compileflow.engine.common.constants.FlowModelType;
import com.alibaba.compileflow.engine.common.utils.ClassLoaderUtils;
import com.alibaba.compileflow.engine.process.preruntime.generator.bean.SpringApplicationContextProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author yusu
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ProcessEngine.class)
@ConditionalOnProperty(name = "alibaba.cloud.compileflow.enabled", matchIfMissing = true)
@EnableConfigurationProperties({CompileflowProperties.class})
public class CompileflowAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CompileflowAutoConfiguration.class);

    @Autowired
    private CompileflowProperties compileflowProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        long startTime = System.currentTimeMillis();
        SpringApplicationContextProvider.applicationContext = applicationContext;

        if (!compileflowProperties.isPrecompile()) {
            return;
        }

        if (!"file".equals(compileflowProperties.getFlowSourceType())) {
            throw new RuntimeException("Flow can only be loaded from files in the current version");
        }

        try {
            precompile();
        } catch (Exception e) {
            throw new RuntimeException("Precompile flow failed", e);
        } finally {
            log.info("Precompile flow cost {} ms", System.currentTimeMillis() - startTime);
        }
    }

    private void precompile() {
        Set<String> precompileProcessFiles = new HashSet<>();

        if (CollectionUtils.isNotEmpty(compileflowProperties.getPrecompileProcessFiles())) {
            for (String precompileProcessFile : compileflowProperties.getPrecompileProcessFiles()) {
                if (precompileProcessFile.endsWith(".bpm") || precompileProcessFile.endsWith(".bpmn20")) {
                    precompileProcessFiles.add(precompileProcessFile);
                } else {
                    precompileProcessFiles.add(precompileProcessFile + ".bpm");
                }
            }
        }

        if (CollectionUtils.isNotEmpty(compileflowProperties.getPrecompileProcessDirs())) {
            for (String precompileProcessDir : compileflowProperties.getPrecompileProcessDirs()) {
                precompileProcessFiles.addAll(getProcessFileInDir(precompileProcessDir));
            }
        }

        doPrecompile(precompileProcessFiles);
    }

    private void doPrecompile(Set<String> precompileProcessFiles) {
        if (CollectionUtils.isEmpty(precompileProcessFiles)) {
            return;
        }

        log.info("Begin to compile flow [" + String.join(", ", precompileProcessFiles) + "]");
        long startTime = System.currentTimeMillis();
        Map<String, List<String>> precompileProcessFileGroup = precompileProcessFiles.stream()
            .collect(Collectors.groupingBy(e -> e.substring(e.lastIndexOf(".") + 1)));

        List<String> tbbpmPrecompileProcessFiles = precompileProcessFileGroup.get("bpm");
        precompileBpmProcess(tbbpmPrecompileProcessFiles);

        List<String> bpmn20PrecompileProcessFiles = precompileProcessFileGroup.get("bpmn20");
        precompileBpmn20Process(bpmn20PrecompileProcessFiles, ProcessEngineFactory.getStatelessProcessEngine(FlowModelType.BPMN));
        log.info("Compile flow finished, cost {} ms", System.currentTimeMillis() - startTime);
    }

    private void precompileBpmProcess(List<String> tbbpmPrecompileProcessFiles) {
        if (CollectionUtils.isNotEmpty(tbbpmPrecompileProcessFiles)) {
            tbbpmPrecompileProcessFiles = convertToProcessCode(tbbpmPrecompileProcessFiles);
            ProcessEngine processEngine = ProcessEngineFactory.getProcessEngine();
            processEngine.preCompile(tbbpmPrecompileProcessFiles.toArray(new String[0]));
        }
    }

    private void precompileBpmn20Process(List<String> bpmn20PrecompileProcessFiles, ProcessEngine statelessProcessEngine) {
        if (CollectionUtils.isNotEmpty(bpmn20PrecompileProcessFiles)) {
            bpmn20PrecompileProcessFiles = convertToProcessCode(bpmn20PrecompileProcessFiles);
            ProcessEngine processEngine = statelessProcessEngine;
            processEngine.preCompile(bpmn20PrecompileProcessFiles.toArray(new String[0]));
        }
    }

    private List<String> convertToProcessCode(List<String> tbbpmPrecompileProcessFiles) {
        tbbpmPrecompileProcessFiles = tbbpmPrecompileProcessFiles.stream()
            .map(e -> e.substring(0, e.lastIndexOf(".")))
            .map(e -> e.replace("/", "."))
            .collect(Collectors.toList());
        return tbbpmPrecompileProcessFiles;
    }

    private Set<String> getProcessFileInDir(String precompileProcessDir) {
        Set<String> processFiles = new HashSet<>();

        URL resource = ClassLoaderUtils.getResource(precompileProcessDir);
        if (resource == null) {
            log.error("Directory resource does not exist, dir name is " + precompileProcessDir);
            return processFiles;
        }

        scanProcessFileInDir(processFiles, resource.getFile(), precompileProcessDir, true);
        return processFiles;
    }

    private void scanProcessFileInDir(Set<String> processFiles, String dirPath,
                                      String relativePath, boolean recursive) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        File[] files = dir.listFiles(file -> (recursive && file.isDirectory())
            || (file.getName().endsWith(".bpm") || (file.getName().endsWith(".bpmn20"))));
        if (files == null || files.length == 0) {
            return;
        }
        for (File file : files) {
            String subRelativePath = relativePath + "/" + file.getName();
            if (file.isDirectory()) {
                scanProcessFileInDir(processFiles, file.getAbsolutePath(), subRelativePath, recursive);
            } else {
                processFiles.add(subRelativePath);
            }
        }
    }

}