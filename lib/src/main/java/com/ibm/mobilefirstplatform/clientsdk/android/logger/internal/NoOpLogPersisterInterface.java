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

package com.ibm.mobilefirstplatform.clientsdk.android.logger.internal;

import android.util.Log;

import com.ibm.mobilefirstplatform.clientsdk.android.logger.api.Logger;

import org.json.JSONObject;

public class NoOpLogPersisterInterface implements LogPersisterInterface{
    protected Logger.LEVEL level;
    protected boolean shouldStoreLogs;

    @Override
    public void setLogLevel(Logger.LEVEL level) {
        this.level = level;
    }

    @Override
    public Logger.LEVEL getLogLevel() {
        return level;
    }

    @Override
    public Logger.LEVEL getLogLevelSync() {
        return level;
    }

    @Override
    public void storeLogs(boolean shouldStoreLogs) {
        this.shouldStoreLogs = shouldStoreLogs;
    }

    @Override
    public boolean isStoringLogs() {
        return shouldStoreLogs;
    }

    @Override
    public void setMaxLogStoreSize(int bytes) {
        //no op
    }

    @Override
    public int getMaxLogStoreSize() {
        //no op
        return -1;
    }

    @Override
    public void send(Object responseListener) {
        //no op
    }

    @Override
    public boolean isUncaughtExceptionDetected() {
        return false;
    }

    @Override
    public void doLog(Logger.LEVEL calledLevel, String message, long timestamp, Throwable t, JSONObject additionalMetadata, Logger logger) {
        boolean canLog = (calledLevel != null) && calledLevel.isLoggable();

        if (canLog || (calledLevel == Logger.LEVEL.ANALYTICS)) {
            message = (null == message) ? "(null)" : message;  // android.util.Log can't handle null, so protect it
            switch (calledLevel) {
                case FATAL:
                case ERROR:
                    if (null == t) { Log.e(logger.getName(), message); } else { Log.e(logger.getName(), message, t); }
                    break;
                case WARN:
                    if (null == t) { Log.w(logger.getName(), message); } else { Log.w(logger.getName(), message, t); }
                    break;
                case INFO:
                    if (null == t) { Log.i(logger.getName(), message); } else { Log.i(logger.getName(), message, t); }
                    break;
                case DEBUG:
                    if(!Logger.isInternalLogger(logger) || Logger.isSDKDebugLoggingEnabled()){
                        if (null == t) { Log.d(logger.getName(), message); } else { Log.d(logger.getName(), message, t); }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
