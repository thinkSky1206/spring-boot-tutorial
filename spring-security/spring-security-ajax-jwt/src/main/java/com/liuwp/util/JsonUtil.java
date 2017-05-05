package com.liuwp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:liuwp
 * Date: 2017/5/5
 * Description:
 */
public class JsonUtil {
    protected static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper jsonMapper = new ObjectMapper();

    public static ObjectMapper getJsonMapper() {
        return jsonMapper;
    }
}
