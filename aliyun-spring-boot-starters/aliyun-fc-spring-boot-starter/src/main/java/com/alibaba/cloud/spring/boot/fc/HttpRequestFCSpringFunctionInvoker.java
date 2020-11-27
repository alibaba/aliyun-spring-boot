/*
 * Copyright (C) 2018 the original author or authors.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.HttpRequestHandler;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
public class HttpRequestFCSpringFunctionInvoker extends AbstractAliyunFCInvoker implements HttpRequestHandler {

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response, Context context)
            throws IOException, ServletException {
        this.initialize(context);

        context.getLogger().info("invoke fc as httpRequest!!!!");

        SimpleFunctionRegistry.FunctionInvocationWrapper wrapper = (SimpleFunctionRegistry.FunctionInvocationWrapper) function();

        if (wrapper.getTarget() instanceof RequestResponseFunction) {
            doInvoke(new Pair<>(request, response));
        } else {

            Map<String, Object> map = generateHeader(request);

            Message<BufferedReader> reqMsg = getInputType() == Void.class ? null
                    : MessageBuilder.withPayload(new BufferedReader(new InputStreamReader(request.getInputStream()))).copyHeaders(map).build();

            Object result = doInvoke(reqMsg);

            if (functionReturnsMessage(result)) {
                Message<?> respMsg = (Message<?>) result;
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(serializeBody(respMsg.getPayload()));
                for (Entry<String, Object> header : respMsg.getHeaders().entrySet()) {
                    response.setHeader(header.getKey(), header.getValue().toString());
                }
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(serializeBody(result));
            }
        }

    }

    private Map<String, Object> generateHeader(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();

        Enumeration<String> enumeration = request.getHeaderNames();

        while (enumeration.hasMoreElements()) {
            String key = enumeration.nextElement();
            map.put(key, request.getHeader(key));
        }
        return map;
    }
}
