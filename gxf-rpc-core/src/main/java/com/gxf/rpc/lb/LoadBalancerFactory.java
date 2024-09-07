package com.gxf.rpc.lb;

/**
 * 负载均衡器工厂（工厂模式，用于获取负载均衡器对象）
 * @author classgeng
 */
public class LoadBalancerFactory {

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取实例
     *
     * @param lbType
     * @return
     */
    public static LoadBalancer getInstance(LbType lbType) {
        LoadBalancer lb = null;
        switch (lbType) {
            case ROUND_ROBIN:
                lb = new RoundRobinLoadBalancer();
                break;
            case RANDOM:
                lb = new RandomLoadBalancer();
                break;
            case CONSISTENT_HASH:
                lb = new ConsistentHashLoadBalancer();
                break;
            default:
                throw new RuntimeException("暂不支持轮询类型：" + lbType.name());
        }
        return lb;
    }

}
