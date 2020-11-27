package com.alibaba.cloud.spring.boot.fc;

import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
public interface RequestResponseFunction extends Consumer<Pair<HttpServletRequest, HttpServletResponse>> {

    @Override
    default void accept(Pair<HttpServletRequest, HttpServletResponse> pair) {
        apply(pair.getKey(), pair.getValue());
    }

    void apply(HttpServletRequest in, HttpServletResponse out);
}
