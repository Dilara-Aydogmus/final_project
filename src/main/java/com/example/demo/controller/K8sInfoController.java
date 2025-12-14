package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class K8sInfoController {

    @Value("${POD_NAME:unknown}")
    private String podName;

    @Value("${POD_NAMESPACE:unknown}")
    private String namespace;

    @Value("${POD_IP:unknown}")
    private String podIp;

    @Value("${NODE_NAME:unknown}")
    private String nodeName;

    @GetMapping("/k8s-info")
    public Map<String, String> getInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("podName", podName);
        map.put("namespace", namespace);
        map.put("podIp", podIp);
        map.put("nodeName", nodeName);
        return map;
    }
}
