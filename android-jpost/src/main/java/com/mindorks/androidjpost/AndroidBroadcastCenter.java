/*
 * Copyright (C) 2016 Janishar Ali Anwar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.mindorks.androidjpost;

import com.mindorks.androidjpost.droid.AndroidPrivateChannel;
import com.mindorks.androidjpost.droid.AndroidPublicChannel;
import com.mindorks.jpost.core.BroadcastCenter;
import com.mindorks.jpost.core.Channel;
import com.mindorks.jpost.core.ChannelPost;
import com.mindorks.jpost.core.ChannelState;
import com.mindorks.jpost.core.ChannelType;
import com.mindorks.jpost.exceptions.*;

import java.lang.ref.WeakReference;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Created by janisharali on 27/09/16.
 */
public class AndroidBroadcastCenter extends BroadcastCenter {

    public AndroidBroadcastCenter(ConcurrentHashMap<Integer,
            Channel<PriorityBlockingQueue<WeakReference<ChannelPost>>, ConcurrentHashMap<Integer,
                    WeakReference<Object>>>> channelMap, ExecutorService executorService) {
        super(channelMap, executorService);
    }

    @Override
    public <T> Channel<PriorityBlockingQueue<WeakReference<ChannelPost>>, ConcurrentHashMap<Integer,
            WeakReference<Object>>> createPrivateChannel(T owner, Integer channelId) throws AlreadyExistsException {
        if(channelId == null){
            System.out.println("channelId is null");
            return null;
        }
        if (getChannelMap().containsKey(channelId)) {
            throw new AlreadyExistsException("Channel with id " + channelId + " already exists");
        }

        try {
            AndroidPrivateChannel privateChannel =
                    new AndroidPrivateChannel(
                            channelId,
                            ChannelState.OPEN,
                            ChannelType.PRIVATE,
                            new PriorityBlockingQueue<>(Channel.MSG_QUEUE_INITIAL_CAPACITY,
                                    new Comparator<WeakReference<ChannelPost>>() {
                                        @Override
                                        public int compare(WeakReference<ChannelPost> o1, WeakReference<ChannelPost> o2) {
                                            ChannelPost post1 = o1.get();
                                            ChannelPost post2 = o2.get();
                                            if(post1 != null || post2 != null){
                                                return post1.getPriority().compareTo(post2.getPriority());
                                            }else{
                                                return 0;
                                            }
                                        }
                                    }),
                            new ConcurrentHashMap<>(Channel.SUBSCRIBER_INITIAL_CAPACITY),
                            new WeakReference<>(owner));

            getChannelMap().put(channelId, privateChannel);
            runPrivateSubscriptionTask(owner, channelId, owner, owner.hashCode());
            return privateChannel;
        }catch (IllegalChannelStateException e){
            e.printStackTrace();
        }catch (PermissionException e){
            e.printStackTrace();
        }catch (NullObjectException e){
            e.printStackTrace();
        }catch (NoSuchChannelException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> Channel<PriorityBlockingQueue<WeakReference<ChannelPost>>, ConcurrentHashMap<Integer,
            WeakReference<Object>>> createPrivateChannel(T owner, Integer channelId, Integer subscriberId)
            throws AlreadyExistsException {
        if(channelId == null){
            System.out.println("channelId is null");
            return null;
        }
        if (getChannelMap().containsKey(channelId)) {
            throw new AlreadyExistsException("Channel with id " + channelId + " already exists");
        }

        try {
            AndroidPrivateChannel privateChannel =
                    new AndroidPrivateChannel(
                            channelId,
                            ChannelState.OPEN,
                            ChannelType.PRIVATE,
                            new PriorityBlockingQueue<>(Channel.MSG_QUEUE_INITIAL_CAPACITY,
                                    new Comparator<WeakReference<ChannelPost>>() {
                                        @Override
                                        public int compare(WeakReference<ChannelPost> o1, WeakReference<ChannelPost> o2) {
                                            ChannelPost post1 = o1.get();
                                            ChannelPost post2 = o2.get();
                                            if(post1 != null || post2 != null){
                                                return post1.getPriority().compareTo(post2.getPriority());
                                            }else{
                                                return 0;
                                            }
                                        }
                                    }),
                            new ConcurrentHashMap<>(Channel.SUBSCRIBER_INITIAL_CAPACITY),
                            new WeakReference<>(owner));

            getChannelMap().put(channelId, privateChannel);
            runPrivateSubscriptionTask(owner, channelId, owner, subscriberId);
            return privateChannel;
        }catch (IllegalChannelStateException e){
            e.printStackTrace();
        }catch (PermissionException e){
            e.printStackTrace();
        }catch (NullObjectException e){
            e.printStackTrace();
        }catch (NoSuchChannelException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Channel<PriorityBlockingQueue<WeakReference<ChannelPost>>, ConcurrentHashMap<Integer,
            WeakReference<Object>>> createPublicChannel(Integer channelId) throws AlreadyExistsException {
        if(channelId == null){
            System.out.println("channelId is null");
            return null;
        }
        if (getChannelMap().containsKey(channelId)) {
            throw new AlreadyExistsException("Channel with id " + channelId + " already exists");
        }
        AndroidPublicChannel publicChannel =
                new AndroidPublicChannel(
                        channelId,
                        ChannelState.OPEN,
                        ChannelType.PUBLIC,
                        new PriorityBlockingQueue<>(Channel.MSG_QUEUE_INITIAL_CAPACITY,
                                new Comparator<WeakReference<ChannelPost>>() {
                                    @Override
                                    public int compare(WeakReference<ChannelPost> o1, WeakReference<ChannelPost> o2) {
                                        ChannelPost post1 = o1.get();
                                        ChannelPost post2 = o2.get();
                                        if(post1 != null || post2 != null){
                                            return post1.getPriority().compareTo(post2.getPriority());
                                        }else{
                                            return 0;
                                        }
                                    }
                                }),
                        new ConcurrentHashMap<>(Channel.SUBSCRIBER_INITIAL_CAPACITY));

        getChannelMap().put(channelId, publicChannel);
        return publicChannel;
    }
}
