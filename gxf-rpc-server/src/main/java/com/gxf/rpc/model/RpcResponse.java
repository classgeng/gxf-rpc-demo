package com.gxf.rpc.model;

import lombok.*;

import java.io.Serializable;

/**
 * RPC 响应
 *
 * @author xfenggeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RpcResponse implements Serializable {

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 响应数据类型（预留）
     */
    private Class<?> dataType;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 异常信息
     */
    private Exception exception;

}
