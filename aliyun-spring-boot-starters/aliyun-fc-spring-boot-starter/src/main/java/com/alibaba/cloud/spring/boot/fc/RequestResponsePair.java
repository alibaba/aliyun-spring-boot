package com.alibaba.cloud.spring.boot.fc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
class RequestResponsePair extends Pair<HttpServletRequest, HttpServletResponse> {

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public RequestResponsePair(HttpServletRequest key, HttpServletResponse value) {
        super(key, value);
    }
}
