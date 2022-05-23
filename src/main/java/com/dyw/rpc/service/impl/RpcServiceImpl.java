package com.dyw.rpc.service.impl;

import com.dyw.rpc.service.RpcService;

/**
 * @author Devil
 * @date 2022-05-23-23:15
 */
public class RpcServiceImpl implements RpcService {
    @Override
    public String hello(String name) {
        return "Hello"+name;
    }
}
