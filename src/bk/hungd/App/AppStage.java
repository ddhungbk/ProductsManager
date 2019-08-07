package bk.hungd.App;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bk.hungd.Controller.WelcomeController;
import bk.hungd.Database.AccountHelper;
import bk.hungd.Database.DatabaseHelper;
import bk.hungd.Model.Account;
import bk.hungd.Model.AccountKeys;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class AppStage extends Application {

	private AppSession session;
	private DatabaseHelper dbHelper;
	private AccountHelper accountHelper;
	
	private String workspaceDir = "";
	private String databasePath = "";
	private String saveWorkspace = "";
	private String accountName = "";
	private String accountPassword = "";
	private String saveAccount = "";
	private ArrayList<Account> accounts;

	public void readLogFile() {
		System.out.println("Read default log file: " + AppResource.LOG_FILE_PATH);
		File logFile = new File(AppResource.LOG_FILE_PATH);
		ArrayList<String> logData = AppFunction.readFile(logFile);
		if (logData != null) {
			for (String s : logData) {
				if (s.startsWith(AppResource.LOG_SAVE_WORKSPACE)) {
					saveWorkspace = s.substring(
							s.indexOf(AppResource.LOG_SAVE_WORKSPACE) + AppResource.LOG_SAVE_WORKSPACE.length());
				}
				if (s.startsWith(AppResource.LOG_WORKSPACE_DIR)) {
					workspaceDir = s.substring(
							s.indexOf(AppResource.LOG_WORKSPACE_DIR) + AppResource.LOG_WORKSPACE_DIR.length());
				}
				if (s.startsWith(AppResource.LOG_DATABASE_PATH)) {
					databasePath = s.substring(
							s.indexOf(AppResource.LOG_DATABASE_PATH) + AppResource.LOG_DATABASE_PATH.length());
				}
				if (s.startsWith(AppResource.LOG_SAVE_ACCOUNT)) {
					saveAccount = s
							.substring(s.indexOf(AppResource.LOG_SAVE_ACCOUNT) + AppResource.LOG_SAVE_ACCOUNT.length());
				}
				if (s.startsWith(AppResource.LOG_ACCOUNT_NAME)) {
					accountName = s
							.substring(s.indexOf(AppResource.LOG_ACCOUNT_NAME) + AppResource.LOG_ACCOUNT_NAME.length());
				}
				if (s.startsWith(AppResource.LOG_ACCOUNT_PASSWORD)) {
					accountPassword = s.substring(
							s.indexOf(AppResource.LOG_ACCOUNT_PASSWORD) + AppResource.LOG_ACCOUNT_PASSWORD.length());
				}
			}
		}
	}

	private ArrayList<Account> getAccounts() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		ArrayList<Account> accounts = new ArrayList<>();
		try {
			System.out.println("Start connection: Get accounts information:");
			connection = dbHelper.createConnection();
			if (connection != null) {
				String query = "SELECT " + AccountKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_ACCOUNT
						+ ";";
				statement = connection.createStatement();
				resultSet = statement.executeQuery(query);
				if (resultSet != null) {
					while (resultSet.next()) {
						Account account = accountHelper.getAccount(resultSet);
						accounts.add(account);
					}
				}
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
		return accounts;
	}

	@Override
	public void start(Stage primaryStage) {
		Scene appScene = new Scene(new StackPane());
		AppController appController = new AppController(primaryStage, appScene);
		
		readLogFile();
		session = new AppSession(workspaceDir, databasePath, 0);
		if (!saveWorkspace.trim().equalsIgnoreCase("true")) {
			appController.showWelcomeScene(WelcomeController.SET_PATH);
		} else if (!saveAccount.trim().equalsIgnoreCase("true")) {
			appController.showWelcomeScene(WelcomeController.LOG_IN);
		} else {
			dbHelper = new DatabaseHelper(session.getDatabasePath());
			accountHelper = new AccountHelper(session.getDatabasePath());
			accounts = this.getAccounts();
			if (accounts.size() > 0) {
				boolean validAccount = false;
				for (Account account : accounts) {
					if (account.getUserName().equals(accountName) && account.getPassword().equals(accountPassword)) {
						validAccount = true;
						session.setAccountId(account.getId());
						break;
					}
				}
				if (validAccount) {
					appController.authenticated(session);
				} else {
					appController.showWelcomeScene(WelcomeController.LOG_IN);
				}
			} else {
				appController.showWelcomeScene(WelcomeController.SET_PATH);
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
