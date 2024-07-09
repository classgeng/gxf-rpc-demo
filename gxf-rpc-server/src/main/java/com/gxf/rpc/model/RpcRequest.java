package com.gxf.rpc.model;

import lombok.*;

import java.io.Serializable;

/**
 * RPC 请求
 * @author xfenggeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcRequest implements Serializable {

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 参数列表
     */
    private Object[] args;

}
