package com.alibaba.cloud.spring.boot.fc;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public interface InputOutputFunction extends Consumer<Pair<InputStream, OutputStream>> {

    @Override
    default void accept(Pair<InputStream, OutputStream> pair) {
        apply(pair.getKey(), pair.getValue());
    }

    void apply(InputStream in, OutputStream out);
}
