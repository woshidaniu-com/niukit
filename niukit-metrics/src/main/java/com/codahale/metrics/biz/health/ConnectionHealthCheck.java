/*
 * Copyright (c) 2010-2020, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.codahale.metrics.biz.health;

import java.sql.Connection;

import com.codahale.metrics.health.HealthCheck;

public class ConnectionHealthCheck extends HealthCheck{
	
	protected Connection connection;
    protected int timeout = 5000;

    public ConnectionHealthCheck(Connection connection,int timeout) {
        this.connection = connection;
    }

    @Override
    protected Result check() throws Exception {
        if (connection.isValid(timeout)) {
            return Result.healthy();
        }
        return Result.unhealthy(" Connection Timeout.");
    }


}
