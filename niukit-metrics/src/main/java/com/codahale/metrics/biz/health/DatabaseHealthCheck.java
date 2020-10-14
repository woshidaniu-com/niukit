package com.codahale.metrics.biz.health;

import com.codahale.metrics.health.HealthCheck;

public class DatabaseHealthCheck extends HealthCheck {

	private final Database database;

	public DatabaseHealthCheck(Database database) {
		this.database = database;
	}

	@Override
	protected Result check() throws Exception {
		if (database.ping()) {
			return Result.healthy();
		}
		return Result.unhealthy("Can't ping database");
	}

	public static interface Database {

		boolean ping();

	}

}