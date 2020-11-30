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
package com.alibaba.cloud.spring.boot.fc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.context.AbstractSpringFunctionAdapterInitializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.Credentials;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.FunctionParam;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public abstract class AbstractAliyunFCInvoker extends AbstractSpringFunctionAdapterInitializer<Context> {

    private final static ExecutionContextDelegate EXECUTION_CTX_DELEGATE = new ExecutionContextDelegate();

    @Autowired(required = false)
    protected ObjectMapper mapper;

    public AbstractAliyunFCInvoker() {
        super();
        preInit();
    }

    public AbstractAliyunFCInvoker(Class<?> configurationClass) {
        super(configurationClass);
        preInit();
    }

    private void preInit() {
        System.setProperty("spring.http.converters.preferred-json-mapper", "gson");
        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
        this.initialize(null);
    }

    @Override
    public final void initialize(Context context) {
        if (EXECUTION_CTX_DELEGATE.target == null) {
            synchronized (EXECUTION_CTX_DELEGATE) {
                if (EXECUTION_CTX_DELEGATE.target == null) {
                    EXECUTION_CTX_DELEGATE.target = context;
                }
            }
            String functionName = null;
            if (System.getenv().containsKey("SCF_FUNC_NAME")) {
                functionName = System.getenv("SCF_FUNC_NAME");
            }
            if (functionName == null && System.getProperties().containsKey("SCF_FUNC_NAME")) {
                functionName = System.getProperty("SCF_FUNC_NAME");
            }
            if (!StringUtils.isEmpty(functionName)) {
                System.setProperty("function.name", functionName);
            }
        }
        super.initialize(EXECUTION_CTX_DELEGATE);
        if (this.mapper == null) {
            this.mapper = new ObjectMapper();
        }
    }

    @Override
    public void close() {
        EXECUTION_CTX_DELEGATE.target = null;
        this.mapper = null;
        super.close();
    }

    protected Object doInvokeAsStream(InputStream input, Map<String, Object> headers) throws IOException {

        Class<?> type = getInputType();

        Object param;
        if (type == Void.class) {
            param = null;
        } else if (InputStream.class.isAssignableFrom(type)) {
            param = input;
        } else {
            byte[] body = StreamUtils.copyToByteArray(input);
            MessageBuilder<byte[]> msg = MessageBuilder.withPayload(body);
            if (headers != null) {
                msg.copyHeaders(headers);
            }
            param = msg.build();
        }

        return doInvoke(param);
    }

    protected Object doInvoke(Object param) {

        Publisher<?> input = param == null ? Mono.just("null") : extract(param);

        Publisher<?> output = apply(input);
        Object realInput = param;
        if (param instanceof Pair) {
            realInput = ((Pair<?, ?>) param).getKey();
        }
        Object result = result(realInput, output);
        return result;
    }

    protected boolean functionReturnsMessage(Object output) {
        return output instanceof Message;
    }


    protected String serializeBody(Object body) {
        try {
            if (body instanceof CharSequence) {
                return String.valueOf(body);
            }
            return this.mapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert output", e);
        }
    }

    protected byte[] serializeResult(Object body) {
        if (body instanceof byte[]) {
            return (byte[]) body;
        } else if (body instanceof String) {
            String seq = (String) body;
            return seq.getBytes();
        }
        try {
            return this.mapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot convert output", e);
        }
    }

    protected Flux<?> extract(Object input) {
        return Flux.just(input);
    }

    protected static class ExecutionContextDelegate implements Context {

        private Context target;

        @Override
        public String getRequestId() {
            return target.getRequestId();
        }

        @Override
        public Credentials getExecutionCredentials() {
            return target.getExecutionCredentials();
        }

        @Override
        public FunctionParam getFunctionParam() {
            return target.getFunctionParam();
        }

        @Override
        public FunctionComputeLogger getLogger() {
            return target.getLogger();
        }
    }
}
