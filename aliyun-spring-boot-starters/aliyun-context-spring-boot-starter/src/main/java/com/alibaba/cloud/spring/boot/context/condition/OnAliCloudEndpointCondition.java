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

package com.alibaba.cloud.spring.boot.context.condition;

import com.alibaba.cloud.spring.boot.context.env.AliCloudProperties;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static com.alibaba.cloud.spring.boot.context.condition.OnRequiredPropertyCondition.doGetMatchOutcome;
import static com.alibaba.cloud.spring.boot.context.env.AliCloudProperties.ACCESS_KEY_PROPERTY;
import static com.alibaba.cloud.spring.boot.context.env.AliCloudProperties.SECRET_KEY_PROPERTY;

/**
 * {@link Condition} that checks whether an endpoint of Alibaba Cloud is available or not
 * if and only if the properties that are "alibaba.cloud.access-key" and
 * "alibaba.cloud.secret-key" must be present.
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 1.0.0
 * @see ConditionalOnAliCloudEndpoint
 * @see AliCloudProperties
 */
class OnAliCloudEndpointCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {

		ConditionOutcome result = doGetMatchOutcome(context, ACCESS_KEY_PROPERTY);

		if (result.isMatch()) {
			result = doGetMatchOutcome(context, SECRET_KEY_PROPERTY);
		}

		return result;
	}

}
