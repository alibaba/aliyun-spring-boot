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

package com.alibaba.cloud.spring.boot.compileflow.env;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author yusu
 */
@ConfigurationProperties("alibaba.cloud.compileflow")
public class CompileflowProperties {

    private String flowSourceType = "file";

    private boolean precompile = true;

    private List<String> precompileProcessFiles;

    private List<String> precompileProcessDirs;

    public String getFlowSourceType() {
        return flowSourceType;
    }

    public void setFlowSourceType(String flowSourceType) {
        this.flowSourceType = flowSourceType;
    }

    public boolean isPrecompile() {
        return precompile;
    }

    public void setPrecompile(boolean precompile) {
        this.precompile = precompile;
    }

    public List<String> getPrecompileProcessFiles() {
        return precompileProcessFiles;
    }

    public void setPrecompileProcessFiles(List<String> precompileProcessFiles) {
        this.precompileProcessFiles = precompileProcessFiles;
    }

    public List<String> getPrecompileProcessDirs() {
        return precompileProcessDirs;
    }

    public void setPrecompileProcessDirs(List<String> precompileProcessDirs) {
        this.precompileProcessDirs = precompileProcessDirs;
    }

}
