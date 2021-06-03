package com.chen.ignite;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.lifecycle.LifecycleBean;
import org.apache.ignite.lifecycle.LifecycleEventType;
import org.apache.ignite.resources.IgniteInstanceResource;

/**
 * 节点生命周期事件
 * <p>
 *
 * @Author LeifChen
 * @Date 2021-06-03
 */
public class MyLifecycleBean implements LifecycleBean {

    @IgniteInstanceResource
    public Ignite ignite;

    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();
        cfg.setClientMode(true);

        // Specify a lifecycle bean in the node configuration.
        cfg.setLifecycleBeans(new MyLifecycleBean());

        Ignite ignite = Ignition.start(cfg);
        ignite.close();
    }

    @Override
    public void onLifecycleEvent(LifecycleEventType evt) {
        if (evt == LifecycleEventType.BEFORE_NODE_START) {
            System.out.println(">>>  Before node start.");
        } else if (evt == LifecycleEventType.AFTER_NODE_START) {
            System.out.format(">>>  After the node (consistentId = %s) starts.\n", ignite.cluster().node().consistentId());
        } else if (evt == LifecycleEventType.BEFORE_NODE_STOP) {
            System.out.println(">>>  Before node stop.");
        } else if (evt == LifecycleEventType.AFTER_NODE_STOP) {
            System.out.println(">>>  After node stop.");
        }
    }
}
