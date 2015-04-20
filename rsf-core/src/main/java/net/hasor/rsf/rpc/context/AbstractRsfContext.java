/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.rsf.rpc.context;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.Executor;
import net.hasor.rsf.RsfBindInfo;
import net.hasor.rsf.RsfContext;
import net.hasor.rsf.address.AddressPool;
import net.hasor.rsf.binder.RsfBindCenter;
import net.hasor.rsf.rpc.client.RsfRequestManager;
import net.hasor.rsf.serialize.SerializeFactory;
/**
 * 服务上下文，负责提供 RSF 运行环境的支持。
 * @version : 2014年11月12日
 * @author 赵永春(zyc@hasor.net)
 */
public abstract class AbstractRsfContext implements RsfContext {
    /**
     * 获取{@link Executor}用于安排执行任务。
     * @param serviceName 服务名
     * @return 返回Executor
     */
    public abstract Executor getCallExecute(String serviceName);
    /** @return 获取序列化管理器。*/
    public abstract SerializeFactory getSerializeFactory();
    /** @return 获取Netty事件处理工具*/
    public abstract EventLoopGroup getLoopGroup();
    /** @return 获取服务注册中心*/
    public abstract RsfBindCenter getBindCenter();
    /** @return 获取请求管理中心*/
    public abstract RsfRequestManager getRequestManager();
    //
    public abstract AddressPool getAddressPool();
    //
    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
    //
    //
    //
    /**
     * 获取元信息所描述的服务对象
     * @param bindInfo 元信息所描述对象
     * @return 服务对象
     */
    public <T> T getBean(RsfBindInfo<T> bindInfo) {
        //        //根据bindInfo 的 id 从 BindCenter 中心取得本地  RsfBindInfo
        //        //   （该操作的目的是为了排除传入参数的干扰，确保可以根据BindInfo id 取得本地的BindInfo。因为外部传入进来的RsfBindInfo极有可能是包装过后的）
        //        bindInfo = this.getBindCenter().getServiceByID(bindInfo.getBindID());
        //        if (bindInfo != null && bindInfo instanceof ServiceDefine == true) {
        //            Provider<T> provider = ((ServiceDefine<T>) bindInfo).getCustomerProvider();
        //            if (provider != null)
        //                return provider.get();
        //        }
        return null;
    }
}