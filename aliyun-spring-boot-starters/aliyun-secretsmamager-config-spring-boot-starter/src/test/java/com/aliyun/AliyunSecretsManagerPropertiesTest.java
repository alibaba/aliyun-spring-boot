package com.aliyun;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.alibaba.cloud.spring.boot.secretsmanager.config.AliyunSecretsManagerProperties;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AliyunSecretsManagerPropertiesTest {

	@Test
	public void validationSucceeds() {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerPropertiesBuilder().withPrefix("/sec")
				.withProfileSeparator("_").build();
		Errors errors = new BeanPropertyBindingResult(properties, "properties");
		properties.validate(properties, errors);
		assertThat(errors.getAllErrors()).isEmpty();
	}

	@ParameterizedTest
	@MethodSource("invalidProperties")
	public void validationFails(AliyunSecretsManagerProperties properties, String field, String errorCode) {
		Errors errors = new BeanPropertyBindingResult(properties, "properties");

		properties.validate(properties, errors);

		assertThat(errors.getFieldError(field)).isNotNull();
		assertThat(errors.getFieldError(field).getCode()).isEqualTo(errorCode);
	}

	@Test
	public void acceptsForwardSlashAsProfileSeparator() {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		properties.setProfileSeparator("/");

		Errors errors = new BeanPropertyBindingResult(properties, "properties");

		properties.validate(properties, errors);

		assertThat(errors.getFieldError("profileSeparator")).isNull();
	}

	@Test
	public void acceptsBackslashAsProfileSeparator() {
		AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();
		properties.setProfileSeparator("\\");

		Errors errors = new BeanPropertyBindingResult(properties, "properties");

		properties.validate(properties, errors);

		assertThat(errors.getFieldError("profileSeparator")).isNull();
	}

	private static Stream<Arguments> invalidProperties() {
		return Stream.of(
				Arguments.of(new AliyunSecretsManagerPropertiesBuilder().withPrefix("").build(), "prefix", "NotEmpty"),
				Arguments.of(new AliyunSecretsManagerPropertiesBuilder().withPrefix("!.").build(), "prefix", "Pattern"),
				Arguments.of(new AliyunSecretsManagerPropertiesBuilder().withProfileSeparator("").build(),
						"profileSeparator", "NotEmpty"),
				Arguments.of(new AliyunSecretsManagerPropertiesBuilder().withProfileSeparator("!_").build(),
						"profileSeparator", "Pattern"));
	}

	private static class AliyunSecretsManagerPropertiesBuilder {

		private final AliyunSecretsManagerProperties properties = new AliyunSecretsManagerProperties();

		AliyunSecretsManagerPropertiesBuilder withPrefix(String prefix) {
			this.properties.setPrefix(prefix);
			return this;
		}

		AliyunSecretsManagerPropertiesBuilder withProfileSeparator(String profileSeparator) {
			this.properties.setProfileSeparator(profileSeparator);
			return this;
		}

		AliyunSecretsManagerProperties build() {
			return this.properties;
		}

	}

}
