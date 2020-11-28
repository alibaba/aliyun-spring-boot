package com.alibaba.cloud.spring.boot.fc;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
class InputOutputPair extends Pair<InputStream, OutputStream> {

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public InputOutputPair(InputStream key, OutputStream value) {
        super(key, value);
    }
}
