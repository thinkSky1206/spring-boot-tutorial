package com.liuwp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: liuwuping
 * Date: 17/4/30
 * Description:
 */
@RestController
@RequestMapping(value = "/resources")
public class ResourceController {

    @RequestMapping(value = "/api")
    public String api() {
        return "hello world";
    }
}
