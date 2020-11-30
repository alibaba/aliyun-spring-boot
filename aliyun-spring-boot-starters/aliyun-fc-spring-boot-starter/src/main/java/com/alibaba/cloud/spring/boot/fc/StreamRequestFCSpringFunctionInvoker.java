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
import java.io.OutputStream;

import org.springframework.messaging.Message;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public class StreamRequestFCSpringFunctionInvoker extends AbstractAliyunFCInvoker implements StreamRequestHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        this.initialize(context);

        Class<?> paramTtype = getInputType();

        if (InputOutputPair.class.isAssignableFrom(paramTtype)) {
            doInvoke(new InputOutputPair(input, output));
        } else {
            Object result = doInvokeAsStream(input, null);
            if (functionReturnsMessage(result)) {
                Message<?> respMsg = (Message<?>) result;
                output.write(serializeResult(respMsg.getPayload()));
            } else {
                output.write(serializeResult(result));
            }
        }
    }
}
