package bk.hungd.App;

import java.io.File;

public class AppResource {

	public static final String USER_HOME_DIR = System.getProperty("user.home");
	public static final String LOG_FILE_NAME = "ProductsManager.log";
	public static final String LOG_FILE_PATH = USER_HOME_DIR + File.separator + AppResource.LOG_FILE_NAME;
	public static final int ROOT_ACCOUNT_ID = -1;
	
	
	public static final String LOG_WORKSPACE_DIR = "@workspace: ";
	public static final String LOG_DATABASE_PATH = "@database: ";
	public static final String LOG_SAVE_WORKSPACE = "@saveWorkspace: ";
	public static final String LOG_ACCOUNT_NAME = "@userName: ";
	public static final String LOG_ACCOUNT_PASSWORD = "@password: ";
	public static final String LOG_SAVE_ACCOUNT = "@saveAccount: ";

}
