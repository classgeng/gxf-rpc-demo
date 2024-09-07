package com.gxf.rpc.registry.timer;

import com.gxf.rpc.registry.core.DefaultRegistry;

import java.util.Timer;
import java.util.TimerTask;

public class RegistreTimer {

    public static void task(){
        DefaultRegistry instance = new DefaultRegistry();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("当前注册服务数量: " + instance.size());
            }
        },5000L,10000L); // 执行程序，会发现5秒之后开始每隔10秒执行一次。

    }

}
