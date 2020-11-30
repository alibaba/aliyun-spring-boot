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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.Credentials;
import com.aliyun.fc.runtime.FunctionComputeLogger;
import com.aliyun.fc.runtime.FunctionParam;
import com.google.gson.Gson;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@NotThreadSafe
public class AliCloudFCAdapterTest {

    @Before
    public void init() {
        System.setProperty("MAIN_CLASS", TestMain.class.getName());
    }

    @Test
    public void string2stringWithStreamTest() throws IOException {
        System.setProperty("SCF_FUNC_NAME", "string2string");

        StreamRequestFCSpringFunctionInvoker invoker = new StreamRequestFCSpringFunctionInvoker();
        InputStream input = new ByteArrayInputStream("hello".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        invoker.handleRequest(input, output, noOpContext());
        Assert.assertEquals(new String(output.toByteArray(), "UTF-8"), "receive string:hello");
        invoker.close();
    }

    @Test
    public void string2objectWithStreamTest() throws IOException {
        System.setProperty("SCF_FUNC_NAME", "string2object");

        StreamRequestFCSpringFunctionInvoker invoker = new StreamRequestFCSpringFunctionInvoker();
        InputStream input = new ByteArrayInputStream("hello".getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        invoker.handleRequest(input, output, noOpContext());
        String jsonStr = new String(output.toByteArray(), "UTF-8");

        Gson gson = new Gson();

        User user = gson.fromJson(jsonStr, User.class);
        Assert.assertEquals(user.getName(), "receive string:hello");
        invoker.close();
    }

    @Test
    public void string2objectWithRequestTest() throws IOException {
        System.setProperty("SCF_FUNC_NAME", "string2object");

        HttpRequestFCSpringFunctionInvoker invoker = new HttpRequestFCSpringFunctionInvoker();
        MockHttpServletRequest input = new MockHttpServletRequest();

        input.setContent("hello".getBytes());

        MockHttpServletResponse output = new MockHttpServletResponse();
        invoker.handleRequest(input, output, noOpContext());

        Gson gson = new Gson();

        String jsonStr = output.getContentAsString();
        User user = gson.fromJson(jsonStr, User.class);
        Assert.assertEquals(user.getName(), "receive string:hello");
        invoker.close();
    }

    @Test
    public void string2stringWithRequestTest() throws IOException {
        System.setProperty("SCF_FUNC_NAME", "string2string");

        HttpRequestFCSpringFunctionInvoker invoker = new HttpRequestFCSpringFunctionInvoker();
        MockHttpServletRequest input = new MockHttpServletRequest();

        input.setContent("hello".getBytes());

        MockHttpServletResponse output = new MockHttpServletResponse();
        invoker.handleRequest(input, output, noOpContext());

        Assert.assertEquals(output.getContentAsString(), "receive string:hello");

        invoker.close();
    }

    private Context noOpContext() {
        return new Context() {
            @Override
            public String getRequestId() {
                return null;
            }

            @Override
            public Credentials getExecutionCredentials() {
                return null;
            }

            @Override
            public FunctionParam getFunctionParam() {
                return null;
            }

            @Override
            public FunctionComputeLogger getLogger() {
                return null;
            }
        };
    }

}
