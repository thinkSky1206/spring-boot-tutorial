package com.liuwp.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: liuwuping
 * Date: 17/7/23
 * Description:
 */
@Controller
@RequestMapping("/res")
public class ResController {


    @RequestMapping("/{id}")
    @ResponseBody
    private Map<String, String> getRes(@PathVariable String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("name", "liuwuping");
        return map;
    }
}
