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

package com.mindorks.jpost.channels;

import com.mindorks.jpost.core.*;
import com.mindorks.jpost.exceptions.InvalidPropertyException;
import com.mindorks.jpost.exceptions.NullObjectException;

import java.lang.ref.WeakReference;

/**
 * Created by janisharali on 22/09/16.
 */
public class PrivateChannel extends PublicChannel{

    private WeakReference<Object> channelOwnerRef;

    public PrivateChannel(Integer channelId, ChannelType type, ChannelState state, WeakReference<Object> channelOwnerRef) {
        super(channelId, type, state);
        this.channelOwnerRef = channelOwnerRef;
    }

    public WeakReference<Object> getChannelOwnerRef() {
        return channelOwnerRef;
    }

    public synchronized void removeSubscriber(Integer subscriberId) throws NullObjectException,InvalidPropertyException {
        if(subscriberId == null){
            throw new NullObjectException("subscriberId is null");
        }
        if(getSubscriberMap().containsKey(subscriberId)){
            getSubscriberMap().remove(subscriberId);
        }else{
            throw new InvalidPropertyException("Subscriber with subscriberId " + subscriberId + " do not exists");
        }
    }
}
