package com.woshidaniu.qa.listener;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import com.woshidaniu.qa.annotation.DbPrepare;
import com.woshidaniu.qa.core.DbPrepareRunner;

/**
 * 
 * @author Administrator
 *
 */
public class DbPrepareTestExecutionListener implements TestExecutionListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.context.TestExecutionListener#beforeTestClass(
	 * org.springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		DbPrepare dbPrepare = testContext.getTestClass().getAnnotation(DbPrepare.class);
		if (dbPrepare == null) {
			return;
		}
		Class<?> testedClazz = testContext.getTestClass();
		String[] sqlFiles = dbPrepare.before();
		runSql(testedClazz, sqlFiles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.test.context.TestExecutionListener#
	 * prepareTestInstance(org.springframework.test.context.TestContext)
	 */
	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.context.TestExecutionListener#beforeTestMethod(
	 * org.springframework.test.context.TestContext)
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		DbPrepare dbPrepare = testContext.getTestClass().getAnnotation(DbPrepare.class);
		if (dbPrepare == null) {
			return;
		}
		Class<?> testedClazz = testContext.getTestClass();
		String[] sqlFiles = dbPrepare.after();
		runSql(testedClazz, sqlFiles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.context.TestExecutionListener#afterTestMethod(
	 * org.springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		DbPrepare dbPrepare = testContext.getTestClass().getAnnotation(DbPrepare.class);
		if (dbPrepare == null) {
			return;
		}
		Class<?> testedClazz = testContext.getTestClass();
		String[] sqlFiles = dbPrepare.after();
		runSql(testedClazz, sqlFiles);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.test.context.TestExecutionListener#afterTestClass(org
	 * .springframework.test.context.TestContext)
	 */
	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		// TODO Auto-generated method stub
		DbPrepare dbPrepare = testContext.getTestClass().getAnnotation(DbPrepare.class);
		if (dbPrepare == null) {
			return;
		}
		Class<?> testedClazz = testContext.getTestClass();
		String[] sqlFiles = dbPrepare.after();
		runSql(testedClazz, sqlFiles);

	}

	private static void runSql(Class<?> testClazz, String[] sqlFiles) {
		for (String sqlFile : sqlFiles) {
			DbPrepareRunner.runDbPrepare(testClazz, sqlFile);
		}
	}

}
