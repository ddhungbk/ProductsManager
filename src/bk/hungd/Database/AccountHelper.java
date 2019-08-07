package bk.hungd.Database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.Model.Account;
import bk.hungd.Model.AccountKeys;
import javafx.scene.image.Image;

public class AccountHelper extends DatabaseHelper {

	public AccountHelper(String databasePath) {
		super(databasePath);
	}

	public void createTableAccount() {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = this.createConnection();
			statement = connection.createStatement();
			String query;
			Object[] columns;

			statement.executeUpdate("DROP TABLE IF EXISTS " + TB_ACCOUNT);

			query = "CREATE VIRTUAL TABLE IF NOT EXISTS " + TB_ACCOUNT + " USING FTS3("
					+ "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s BLOB, %s TEXT, %s TEXT);";
			columns = new Object[] { AccountKeys.ORDER, AccountKeys.USER_NAME, AccountKeys.PASSWORD,
					AccountKeys.USER_TYPE, AccountKeys.NAME, AccountKeys.CODE, AccountKeys.ICON, AccountKeys.ICON_PATH,
					AccountKeys.POSITION };
			statement.execute(String.format(query, columns));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void addAccount(Account account) {
		String query = "INSERT INTO " + TB_ACCOUNT + "(" + AccountKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(AccountKeys.ORDER_INDEX, account.getOrder());
			statement.setString(AccountKeys.USER_NAME_INDEX, account.getUserName());
			statement.setString(AccountKeys.PASSWORD_INDEX, account.getPassword());
			statement.setString(AccountKeys.USER_TYPE_INDEX, account.getUserType());
			statement.setString(AccountKeys.NAME_INDEX, account.getName());
			statement.setString(AccountKeys.CODE_INDEX, account.getCode());
			statement.setBytes(AccountKeys.ICON_INDEX, AppFunction.getBytesFromImage(account.getIcon()));
			statement.setString(AccountKeys.ICON_PATH_INDEX, account.getIconPath());
			statement.setString(AccountKeys.POSITION_INDEX, account.getPosition());
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public Account getAccount(ResultSet resultSet) {
		Account account = new Account();
		try {
			account.setId(resultSet.getInt(AccountKeys.ID));
			account.setUserName(resultSet.getString(AccountKeys.USER_NAME));
			account.setPassword(resultSet.getString(AccountKeys.PASSWORD));
			account.setUserType(resultSet.getString(AccountKeys.USER_TYPE));
			account.setName(resultSet.getString(AccountKeys.NAME));
			account.setCode(resultSet.getString(AccountKeys.CODE));
			Image image = null;
			try{
				image = new Image(resultSet.getBinaryStream(AccountKeys.ICON));
			}catch (Exception e) {
				AppDebug.error(e, "");
			}
			account.setIcon(image);
			account.setIconPath(resultSet.getString(AccountKeys.ICON_PATH));
			account.setPosition(resultSet.getString(AccountKeys.POSITION));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		}
		return account;
	}

	public int getMaxOrder(){
		int maxOrder = -1;
		String query = "SELECT " + AccountKeys.ORDER + " FROM " + DatabaseHelper.TB_ACCOUNT + " WHERE " + AccountKeys.ORDER
				+ "=(SELECT MAX(" + AccountKeys.ORDER + ") FROM " + DatabaseHelper.TB_ACCOUNT + ");";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					maxOrder = resultSet.getInt(AccountKeys.ORDER);
					break;
				}
			}
			DatabaseHelper.close(resultSet);
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
		return maxOrder;
	}
}
