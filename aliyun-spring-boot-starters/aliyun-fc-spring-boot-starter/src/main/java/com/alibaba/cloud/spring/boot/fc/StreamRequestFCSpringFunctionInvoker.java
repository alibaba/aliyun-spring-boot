package com.alibaba.cloud.spring.boot.fc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StreamUtils;

import com.aliyun.fc.runtime.Context;
import com.aliyun.fc.runtime.StreamRequestHandler;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public class StreamRequestFCSpringFunctionInvoker extends AbstractAliyunFCInvoker implements StreamRequestHandler {

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        this.initialize(context);

        SimpleFunctionRegistry.FunctionInvocationWrapper wrapper = (SimpleFunctionRegistry.FunctionInvocationWrapper) function();

        if (wrapper.getTarget() instanceof InputOutputFunction) {
            doInvoke(new Pair<>(input, output));
        } else {
            Object param = input;
            if (!InputStream.class.isAssignableFrom(getInputType())) {
                byte[] body = StreamUtils.copyToByteArray(input);
                param = MessageBuilder.withPayload(body).build();
            }

            Object result = doInvoke(param);
            if (functionReturnsMessage(result)) {
                Message<?> respMsg = (Message<?>) result;
                output.write(serializeResult(respMsg.getPayload()));
            } else {
                output.write(serializeResult(result));
            }
        }
    }

}
