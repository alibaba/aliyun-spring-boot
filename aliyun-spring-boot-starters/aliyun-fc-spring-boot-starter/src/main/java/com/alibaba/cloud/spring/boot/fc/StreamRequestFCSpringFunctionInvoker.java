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
