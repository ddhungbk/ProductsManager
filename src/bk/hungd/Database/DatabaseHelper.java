package bk.hungd.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.App.AppResource;
import bk.hungd.Model.AccountKeys;
import bk.hungd.Model.BrandKeys;
import bk.hungd.Model.CategoryKeys;
import bk.hungd.Model.ModelKeys;
import bk.hungd.Model.ProductKeys;

public class DatabaseHelper {

	public static final String SQLITE_URL = "jdbc:sqlite:";
	public static final String DRIVER_CLASS = "org.sqlite.JDBC";

	public static final String TB_BRAND = "BRANDS";
	public static final String TB_CATEGORY = "CATEGORIES";
	public static final String TB_MODEL = "MODELS";
	public static final String TB_PRODUCT = "PRODUCTS";
	public static final String TB_ACCOUNT = "ACCOUNT";

	private String databasePath;

	public DatabaseHelper(String databasePath) {
		this.databasePath = databasePath;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	public Connection createConnection() {
		Connection connection = null;
		File file = null;
		try {
			file = new File(databasePath);
			if (!file.exists()) {
				try {
					file.getParentFile().mkdir();
					file.createNewFile();
				} catch (Exception e) {
					AppDebug.error(e, "Cannot create database!");
				}
			}
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(SQLITE_URL + databasePath);
			System.out.println(" -> Connected to " + SQLITE_URL + databasePath);
		} catch (SQLException e) {
			AppDebug.error(e, "Unable to connect to database!");
		} catch (ClassNotFoundException e) {
			AppDebug.error(e, "Unable to create a connection!");
		}
		return connection;
	}

	public void createDatabase() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = this.createConnection();
			if (connection != null) {
				statement = connection.createStatement();
				String query;
				Object[] columns;

				query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_BRAND + " USING FTS3("
						+ "%s INTEGER, %s TEXT, %s BLOB, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);";
				columns = new Object[] { BrandKeys.ORDER, BrandKeys.NAME, BrandKeys.ICON, BrandKeys.ICON_PATH,
						BrandKeys.INFO, BrandKeys.IMAGES, BrandKeys.CATEGORIES, BrandKeys.PRODUCTS, BrandKeys.WEBSITE,
						BrandKeys.TAGS, BrandKeys.NOTES };
				statement.execute(String.format(query, columns));

				query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_CATEGORY + " USING FTS3("
						+ "%s INTEGER, %s TEXT, %s BLOB, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);";
				columns = new String[] { CategoryKeys.ORDER, CategoryKeys.NAME, CategoryKeys.ICON,
						CategoryKeys.ICON_PATH, CategoryKeys.INFO, CategoryKeys.IMAGES, CategoryKeys.BRANDS,
						CategoryKeys.PRODUCTS, CategoryKeys.TAGS, CategoryKeys.NOTES };
				statement.execute(String.format(query, columns));

				query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_MODEL + " USING FTS3("
						+ "%s INTEGER, %s TEXT, %s BLOB, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, "
						+ "%s INTEGER NOT NULL CONSTRAINT %s REFERENCES " + TB_BRAND + "(docid) ON DELETE CASCADE, "
						+ "%s INTEGER NOT NULL CONSTRAINT %s REFERENCES " + TB_CATEGORY + "(docid) ON DELETE CASCADE);";
				columns = new String[] { ModelKeys.ORDER, ModelKeys.NAME, ModelKeys.ICON, ModelKeys.ICON_PATH,
						ModelKeys.BRAND, ModelKeys.CATEGORY, ModelKeys.INFO, ModelKeys.IMAGES, ModelKeys.PRODUCTS,
						ModelKeys.TAGS, ModelKeys.NOTES, ModelKeys.BRAND_ID, ModelKeys.BRAND_ID, ModelKeys.CATEGORY_ID,
						ModelKeys.CATEGORY_ID };
				statement.execute(String.format(query, columns));

				query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_PRODUCT + " USING FTS3("
						+ "%s INTEGER, %s TEXT, %s TEXT, %s BLOB, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, "
						+ "%s REAL, %s INTEGER, %s TEXT, %s TEXT, %s TEXT, "
						+ "%s INTEGER NOT NULL CONSTRAINT %s REFERENCES " + TB_BRAND + "(docid) ON DELETE CASCADE, "
						+ "%s INTEGER NOT NULL CONSTRAINT %s REFERENCES " + TB_CATEGORY + "(docid) ON DELETE CASCADE);";
				columns = new String[] { ProductKeys.ORDER, ProductKeys.NAME, ProductKeys.CODE, ProductKeys.ICON,
						ProductKeys.ICON_PATH, ProductKeys.BRAND, ProductKeys.CATEGORY, ProductKeys.INFO,
						ProductKeys.IMAGES, ProductKeys.SPECS, ProductKeys.PRICE, ProductKeys.QUANTITY,
						ProductKeys.STATUS, ProductKeys.TAGS, ProductKeys.NOTES, ProductKeys.BRAND_ID,
						ProductKeys.BRAND_ID, ProductKeys.CATEGORY_ID, ProductKeys.CATEGORY_ID };
				statement.execute(String.format(query, columns));

				query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_ACCOUNT + " USING FTS3("
						+ "%s INTEGER, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s BLOB, %s TEXT, %s TEXT);";
				columns = new Object[] { AccountKeys.ORDER, AccountKeys.USER_NAME, AccountKeys.PASSWORD,
						AccountKeys.USER_TYPE, AccountKeys.NAME, AccountKeys.CODE, AccountKeys.ICON,
						AccountKeys.ICON_PATH, AccountKeys.POSITION };
				statement.execute(String.format(query, columns));

				SQLWarning warning = statement.getWarnings();
				if (warning != null) {
					System.out.println("Warning: " + warning.getMessage());
				}
			}
		} catch (SQLException e) {
			AppDebug.error(e, "Fail to create database!");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void upgradeDatabase(int oldVersion, int newVersion) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = this.createConnection();
			if (connection != null) {
				statement = connection.createStatement();
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_BRAND);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_CATEGORY);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_MODEL);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_PRODUCT);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_ACCOUNT);
				createDatabase();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void deleteDatabase() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = this.createConnection();
			if (connection != null) {
				statement = connection.createStatement();
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_BRAND);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_CATEGORY);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_MODEL);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_PRODUCT);
				statement.executeUpdate("DROP TABLE IF EXISTS " + TB_ACCOUNT);
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public boolean checkExistedDatabase() {
		if (new File(databasePath).exists()) {
			return true;
		}
		return false;
	}

	public boolean removeDatabase() {
		return new File(databasePath).delete();
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
				System.out.println(" -> Closed connection!");
			} catch (SQLException e) {
				AppDebug.error(e, "");
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				AppDebug.error(e, "");
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				AppDebug.error(e, "");
			}
		}
	}
}
