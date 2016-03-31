/*
 *     Copyright 2016 IBM Corp.
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.ibm.mobilefirstplatform.clientsdk.android.analytics.api;

import android.app.Application;

import com.ibm.mobilefirstplatform.clientsdk.android.analytics.internal.AnalyticsDelegate;
import com.ibm.mobilefirstplatform.clientsdk.android.analytics.internal.NoOpAnalyticsDelegate;
import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONObject;

import java.lang.reflect.Method;

public class Analytics {
    public enum DeviceEvent {
        NONE,
        ALL,
        LIFECYCLE
//        NETWORK //Note: Temporarily disabled
    }

    protected static AnalyticsDelegate analyticsDelegate = new NoOpAnalyticsDelegate();
    protected static Logger analyticsLogger = Logger.getLogger(Logger.INTERNAL_PREFIX + "analytics");

    public static void setAnalyticsDelegate(AnalyticsDelegate analyticsDelegate){
        Analytics.analyticsDelegate = analyticsDelegate;
    }

    public static void init(Application application, String applicationName, String clientApiKey, DeviceEvent... contexts){
        Class analyticsClass;

        try {
            analyticsClass = Class.forName("com.ibm.mobilefirstplatform.clientsdk.android.analytics.internal.BMSAnalytics");

            Class stringClass = String.class;
            Class deviceEventClass = DeviceEvent[].class;

            Method initMethod = analyticsClass.getMethod("init", Application.class, stringClass, stringClass, deviceEventClass);

            initMethod.invoke(null, new Object[] {application, applicationName, clientApiKey, contexts});
        } catch (Throwable e) {
            analyticsLogger.warn("Nothing will happen. In order to properly initialize the Analytics SDK and get all features, first include the Analytics SDK as a dependency for your application.", e);
        }
    }

    public static void enable(){
        if(analyticsDelegate != null){
            analyticsDelegate.enable();
        }
    }

    public static void disable(){
        if(analyticsDelegate != null){
            analyticsDelegate.disable();
        }
    }

    public static boolean isEnabled() {
        return analyticsDelegate != null && analyticsDelegate.isEnabled();
    }

    public static void send(){
        if(analyticsDelegate != null){
            analyticsDelegate.send();
        }
    }

    public static void send(Object responseListener){
        if(analyticsDelegate != null){
            analyticsDelegate.send(responseListener);
        }
    }

    public static void log(JSONObject eventMetadata){
        if(analyticsDelegate != null){
            analyticsDelegate.log(eventMetadata);
        }
    }

    public static void setUserIdentity(String username){
        if(analyticsDelegate != null){
            analyticsDelegate.setUserIdentity(username);
        }
    }

    public static void clearUserIdentity(){
        if(analyticsDelegate != null){
            analyticsDelegate.clearUserIdentity();
        }
    }

    public static String getClientAPIKey(){
        if(analyticsDelegate != null){
            return analyticsDelegate.getClientAPIKey();
        }
        else{
            return null;
        }
    }

    public static String getAppName(){
        if(analyticsDelegate != null){
            return analyticsDelegate.getAppName();
        }
        else{
            return null;
        }
    }
}
