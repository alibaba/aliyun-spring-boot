package com.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.kms.model.v20160120.GetSecretValueRequest;
import com.aliyuncs.kms.model.v20160120.GetSecretValueResponse;
import org.junit.jupiter.api.Test;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AliyunSecretsManagerPropertySourceTest {

	private IAcsClient client = mock(DefaultAcsClient.class);

	private AliyunSecretsManagerPropertySource propertySource = new AliyunSecretsManagerPropertySource("/config/myservice",
			client);

	@Test
	void shouldParseSecretValue() throws ClientException {
		GetSecretValueResponse secretValueResponse = new GetSecretValueResponse();
		secretValueResponse.setSecretData("{\"key1\": \"value1\", \"key2\": \"value2\"}");

		when(client.getAcsResponse(any(GetSecretValueRequest.class))).thenReturn(secretValueResponse);

		propertySource.init();

		assertThat(propertySource.getPropertyNames()).containsExactly("key1", "key2");
		assertThat(propertySource.getProperty("key1")).isEqualTo("value1");
		assertThat(propertySource.getProperty("key2")).isEqualTo("value2");
	}

	@Test
	void throwsExceptionWhenSecretNotFound() throws ClientException {
		when(client.getAcsResponse(any(GetSecretValueRequest.class)))
				.thenThrow(new ClientException("secret not found"));

		assertThatThrownBy(() -> propertySource.init()).hasCauseInstanceOf(ClientException.class);
	}

}
