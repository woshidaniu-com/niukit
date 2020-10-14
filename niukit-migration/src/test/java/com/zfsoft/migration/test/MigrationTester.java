/**
 * 
 */
package com.woshidaniu.migration.test;

import org.junit.Assert;
import org.junit.Test;

import com.woshidaniu.migration.woshidaniuMigration;

/**
 * @author xiaobin.zhang
 *
 */
public class MigrationTester {

	@Test
	public void test() {
		
		woshidaniuMigration migration  = new woshidaniuMigration("classpath:.");
		
		int migrate = migration.migrate();
		
		Assert.assertEquals(migrate, 1);
	}

}
