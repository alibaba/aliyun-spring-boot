/*
 * Copyright 2013-2021 the original author or authors.
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

package com.alibaba.cloud.spring.boot.secretsmanager.config;

import com.aliyuncs.IAcsClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.*;

public class AliyunSecretsManagerPropertySourceLocator implements PropertySourceLocator {

private final String propertySourceName;

private final IAcsClient acsClient;

private final AliyunSecretsManagerProperties properties;

private final Set<String> secretNames = new LinkedHashSet<>();

private Log logger = LogFactory.getLog(getClass());

public AliyunSecretsManagerPropertySourceLocator(String propertySourceName, IAcsClient acsClient,
AliyunSecretsManagerProperties properties) {
this.propertySourceName = propertySourceName;
this.acsClient = acsClient;
this.properties = properties;
}

public AliyunSecretsManagerPropertySourceLocator(IAcsClient acsClient, AliyunSecretsManagerProperties properties) {
this("aliyun-secrets-manager", acsClient, properties);
}

public List<String> getSecretNames() {
return new ArrayList<>(secretNames);
}

@Override
public PropertySource<?> locate(Environment environment) {
if (!(environment instanceof ConfigurableEnvironment)) {
return null;
}

ConfigurableEnvironment env = (ConfigurableEnvironment) environment;

AliyunSecretsManagerPropertySources sources = new AliyunSecretsManagerPropertySources(properties, logger);

List<String> profiles = Arrays.asList(env.getActiveProfiles());
this.secretNames.addAll(sources.getAutomaticSecretNames(profiles));

CompositePropertySource composite = new CompositePropertySource(this.propertySourceName);

for (String propertySourceContext : this.secretNames) {
PropertySource<IAcsClient> propertySource = sources.createPropertySource(propertySourceContext,
!this.properties.isFailFast(), this.acsClient);
if (propertySource != null) {
composite.addPropertySource(propertySource);
}
}

return composite;
}

}