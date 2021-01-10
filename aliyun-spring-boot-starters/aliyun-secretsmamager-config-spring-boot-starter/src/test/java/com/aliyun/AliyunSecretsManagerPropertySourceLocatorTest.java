
package com.aliyun;

import com.alibaba.cloud.spring.boot.secretsmanager.config.exception.AliyunSecretsManagerPropertySourceNotFoundException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.kms.model.v20160120.GetSecretValueRequest;
import com.aliyuncs.kms.model.v20160120.GetSecretValueResponse;
import org.junit.jupiter.api.Test;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerProperties;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerPropertySourceLocator;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerPropertySources;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.mock.env.MockEnvironment;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AliyunSecretsManagerPropertySourceLocatorTest {

	private IAcsClient client = mock(DefaultAcsClient.class);

	private MockEnvironment env = new MockEnvironment();

	@Test
	void locate_nameSpecifiedInConstructor_returnsPropertySourceWithSpecifiedName() throws ClientException {
		GetSecretValueResponse secretValueResponse = new GetSecretValueResponse();
		secretValueResponse.setSecretData("{\"key1\": \"value1\", \"key2\": \"value2\"}");
		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenReturn(secretValueResponse);

		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator("my-name", client,
				properties);

		PropertySource propertySource = locator.locate(env);

		assertThat(propertySource.getName()).isEqualTo("my-name");
	}

	@Test
	void locate_nameNotSpecifiedInConstructor_returnsPropertySourceWithDefaultName() throws ClientException {
		GetSecretValueResponse secretValueResult = new GetSecretValueResponse();
		secretValueResult.setSecretData("{\"key1\": \"value1\", \"key2\": \"value2\"}");
		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenReturn(secretValueResult);

		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator(client,
				properties);
		PropertySource propertySource = locator.locate(env);

		assertThat(propertySource.getName()).isEqualTo("aliyun-secrets-manager");
	}

	@Test
	void contextExpectedToHave2Elements() throws ClientException {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerPropertiesBuilder()
				.withName("application").build();

		GetSecretValueResponse secretValueResult = new GetSecretValueResponse();
		secretValueResult.setSecretData("{\"key1\": \"value1\", \"key2\": \"value2\"}");
		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenReturn(secretValueResult);

		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator(client,
				properties);
		env.setActiveProfiles("test");
		locator.locate(env);

		assertThat(locator.getSecretNames()).hasSize(2);
	}

	@Test
	void contextExpectedToHave4Elements() throws ClientException {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerPropertiesBuilder()
				.withName("messaging-service").build();

		GetSecretValueResponse secretValueResult = new GetSecretValueResponse();
		secretValueResult.setSecretData("{\"key1\": \"value1\", \"key2\": \"value2\"}");
		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenReturn(secretValueResult);

		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator(client,
				properties);
		env.setActiveProfiles("test");
		locator.locate(env);

		assertThat(locator.getSecretNames()).hasSize(4);
	}

	@Test
	void whenFailFastIsTrueAndSecretDoesNotExistThrowsException() throws ClientException {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		properties.setFailFast(true);

		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenThrow(ClientException.class);

		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator(client,
				properties);
		assertThatThrownBy(() -> locator.locate(env))
				.isInstanceOf(AliyunSecretsManagerPropertySourceNotFoundException.class);
	}

	@Test
	void whenFailFastIsFalseAndSecretDoesNotExistReturnsEmptyPropertySource() throws ClientException {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		properties.setFailFast(false);

		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenThrow(ClientException.class);

		AliyunSecretsManagerPropertySourceLocator locator = new AliyunSecretsManagerPropertySourceLocator(client,
				properties);

		CompositePropertySource result = (CompositePropertySource) locator.locate(env);

		assertThat(result.getPropertySources()).isEmpty();
	}

	private final static class AliyunSecretsManagerPropertiesBuilder {

		private final AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();

		private AliyunSecretsManagerPropertiesBuilder() {
		}

		public AliyunSecretsManagerPropertiesBuilder withName(String name) {
			this.properties.setSecretName(name);
			return this;
		}

		public AliyunSecretsManagerProperties build() {
			return this.properties;
		}

	}

}
