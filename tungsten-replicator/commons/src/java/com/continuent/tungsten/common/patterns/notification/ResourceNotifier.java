/**
 * VMware Continuent Tungsten Replicator
 * Copyright (C) 2015 VMware, Inc. All rights reserved.
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
 * limitations under the License.
 *
 * Initial developer(s): Edward Archibald
 * Contributor(s): Robert Hodges
 */

package com.continuent.tungsten.common.patterns.notification;

import java.util.Map;

import com.continuent.tungsten.common.cluster.resource.notification.ClusterResourceNotification;

public interface ResourceNotifier extends Runnable
{
    public abstract void prepare() throws Exception;

    public abstract void addListener(ResourceNotificationListener listener);

    public abstract void notifyListeners(
            ClusterResourceNotification notification)
            throws ResourceNotificationException;

    public abstract Map<String, NotificationGroupMember> getNotificationGroupMembers();
}
