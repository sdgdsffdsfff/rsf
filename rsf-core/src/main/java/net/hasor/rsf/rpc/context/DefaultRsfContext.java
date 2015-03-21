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
import io.netty.channel.nio.NioEventLoopGroup;
import java.io.IOException;
import java.util.concurrent.Executor;
import net.hasor.core.Settings;
import net.hasor.core.binder.InstanceProvider;
import net.hasor.rsf.RsfClient;
import net.hasor.rsf.RsfSettings;
import net.hasor.rsf.adapter.AbstracAddressCenter;
import net.hasor.rsf.adapter.AbstractBindCenter;
import net.hasor.rsf.adapter.AbstractRequestManager;
import net.hasor.rsf.adapter.AbstractRsfContext;
import net.hasor.rsf.remoting.address.DefaultAddressCenter;
import net.hasor.rsf.remoting.binder.DefaultBindCenter;
import net.hasor.rsf.remoting.transport.customer.RsfRequestManager;
import net.hasor.rsf.rpc.executes.ExecutesManager;
import net.hasor.rsf.rpc.executes.NameThreadFactory;
import net.hasor.rsf.rpc.warp.InnerLocalWarpRsfFilter;
import net.hasor.rsf.serialize.SerializeFactory;
/**
 * 
 * @version : 2014年11月12日
 * @author 赵永春(zyc@hasor.net)
 */
public class DefaultRsfContext extends AbstractRsfContext {
    private final AbstractBindCenter     bindCenter;
    private final AbstracAddressCenter   addressCenter;
    private final AbstractRequestManager requestManager;
    //
    private final SerializeFactory       serializeFactory;
    private final ExecutesManager        executesManager;
    private final EventLoopGroup         loopGroup;
    private final RsfSettings            rsfSettings;
    //
    //
    public DefaultRsfContext(Settings settings) throws IOException {
        this(new DefaultRsfSettings(settings));
    }
    public DefaultRsfContext(RsfSettings settings) {
        this.rsfSettings = settings;
        this.bindCenter = new DefaultBindCenter(this);
        this.addressCenter = new DefaultAddressCenter();
        this.requestManager = new RsfRequestManager(this);
        //
        this.serializeFactory = SerializeFactory.createFactory(this.rsfSettings);
        //
        int queueSize = this.rsfSettings.getQueueMaxSize();
        int minCorePoolSize = this.rsfSettings.getQueueMinPoolSize();
        int maxCorePoolSize = this.rsfSettings.getQueueMaxPoolSize();
        long keepAliveTime = this.rsfSettings.getQueueKeepAliveTime();
        this.executesManager = new ExecutesManager(minCorePoolSize, maxCorePoolSize, queueSize, keepAliveTime);
        //
        int workerThread = this.rsfSettings.getNetworkWorker();
        this.loopGroup = new NioEventLoopGroup(workerThread, new NameThreadFactory("RSF-Nio-%s"));
        //
        this.bindCenter.bindFilter(InnerLocalWarpRsfFilter.class.getName(),//
                new InstanceProvider<InnerLocalWarpRsfFilter>(new InnerLocalWarpRsfFilter()));
    }
    //
    public void shutdown() {
        this.loopGroup.shutdownGracefully();
    }
    /**获取使用的{@link EventLoopGroup}*/
    public EventLoopGroup getLoopGroup() {
        return this.loopGroup;
    }
    /**获取配置*/
    public RsfSettings getSettings() {
        return this.rsfSettings;
    }
    /**获取{@link Executor}用于安排执行任务。*/
    public Executor getCallExecute(String serviceName) {
        return this.executesManager.getExecute(serviceName);
    }
    /**获取序列化管理器。*/
    public SerializeFactory getSerializeFactory() {
        return this.serializeFactory;
    }
    /**获取注册中心。*/
    public AbstractBindCenter getBindCenter() {
        return this.bindCenter;
    }
    /**获取注册中心。*/
    public AbstracAddressCenter getAddressCenter() {
        return this.addressCenter;
    }
    /**获取请求管理中心*/
    public AbstractRequestManager getRequestManager() {
        return this.requestManager;
    }
    /**获取客户端*/
    public RsfClient getRsfClient() {
        return new RsfClientFacade(this);
    }
}