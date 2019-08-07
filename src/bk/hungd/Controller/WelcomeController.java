package bk.hungd.Controller;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import bk.hungd.App.AppController;
import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.App.AppResource;
import bk.hungd.App.AppSession;
import bk.hungd.Database.AccountHelper;
import bk.hungd.Database.DatabaseHelper;
import bk.hungd.Model.Account;
import bk.hungd.Model.AccountKeys;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class WelcomeController implements Initializable {

	public static final int SET_PATH = 0;
	public static final int LOG_IN = 1;
	
	@FXML
	public Label lbInput1, lbInput2;
	public TextField tfInput1, tfInput2;
	public Button btBrowse1, btBrowse2;
	public PasswordField tfPassword;
	public Button btAction, btExit, btBack;
	public Label lbStatus;
	public CheckBox cbSaveInfo;

	private DatabaseHelper dbHelper;
	private AccountHelper accountHelper;
	private AppSession session;

	private String workspaceDir = "";
	private String databasePath = "";
	private String saveWorkspace = "";
	private String accountName = "";
	private String accountPassword = "";
	private String saveAccount = "";
	private ArrayList<Account> accounts;
	private int currentType;

	public WelcomeController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void login(final AppController appController, int type) {
		currentType = type;
		readLogFile();
		session = new AppSession(workspaceDir, databasePath, 0);
		dbHelper = new DatabaseHelper(session.getDatabasePath());
		accountHelper = new AccountHelper(session.getDatabasePath());
		accounts = this.getAccounts();
		viewItems(currentType);

		btBrowse1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				DirectoryChooser chooser = new DirectoryChooser();
				chooser.setTitle("Select Workspace Directory");
				File folder;
				try {
					chooser.setInitialDirectory(new File(session.getWorkspaceDir()));
					folder = chooser.showDialog(btBrowse1.getScene().getWindow());
				} catch (Exception e) {
					chooser.setInitialDirectory(new File("."));
					folder = chooser.showDialog(btBrowse1.getScene().getWindow());
				}
				if (folder != null) {
					tfInput1.setText(folder.getPath());
					session.setWorkspaceDir(folder.getPath());
					lbStatus.setText("");
				}
			}
		});

		btBrowse2.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select Database File");
				fileChooser.setInitialDirectory(new File(tfInput1.getText()));
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Database (*.db)", "*.db"));

				File file = fileChooser.showOpenDialog(btBrowse2.getScene().getWindow());
				if (file != null) {
					tfInput2.setText(file.getPath());
					session.setDatabasePath(file.getPath());
					dbHelper.setDatabasePath(file.getPath());
					accounts = getAccounts();
					lbStatus.setText("");
				}
			}
		});

		btAction.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (currentType == WelcomeController.SET_PATH) {
					File workspaceFile = new File(tfInput1.getText());
					File databaseFile = new File(tfInput2.getText());
					if (!workspaceFile.exists() || !workspaceFile.isDirectory()) {
						lbStatus.setText("Invalid workspace directory!");
					} else if (!databaseFile.exists() || !databaseFile.isFile()
							|| !databaseFile.getName().endsWith(".db")) {
						lbStatus.setText("Invalid database file!");
					} else {
						currentType = WelcomeController.LOG_IN;
						File logFile = new File(AppResource.LOG_FILE_PATH);
						AppFunction.writeFileFilter(logFile, tfInput1.getText(), AppResource.LOG_WORKSPACE_DIR);
						AppFunction.writeFileFilter(logFile, tfInput2.getText(), AppResource.LOG_DATABASE_PATH);
						AppFunction.writeFileFilter(logFile, "" + cbSaveInfo.isSelected(),
								AppResource.LOG_SAVE_WORKSPACE);
						viewItems(1);
					}
				} else if (currentType == WelcomeController.LOG_IN) {
					String userName = tfInput1.getText();
					String password = tfPassword.getText();

					if ((userName.equals("admin") && password.equals("00000"))) {
						session.setAccountId(AppResource.ROOT_ACCOUNT_ID);
						appController.authenticated(session);
						return;
					} else {
						for (int i = 0; i < accounts.size(); i++) {
							Account account = accounts.get(i);
							if (userName.equals(account.getUserName()) && password.equals(account.getPassword())) {
								File logFile = new File(AppResource.LOG_FILE_PATH);
								AppFunction.writeFileFilter(logFile, tfInput1.getText(), AppResource.LOG_ACCOUNT_NAME);
								AppFunction.writeFileFilter(logFile, tfPassword.getText(),
										AppResource.LOG_ACCOUNT_PASSWORD);
								AppFunction.writeFileFilter(logFile, "" + cbSaveInfo.isSelected(),
										AppResource.LOG_SAVE_ACCOUNT);
								session.setAccountId(account.getId());
								appController.authenticated(session);
								return;
							}
						}
					}
					lbStatus.setText("Incorrect username or password!");
				}
			}
		});

		btBack.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentType = WelcomeController.SET_PATH;
				readLogFile();
				viewItems(WelcomeController.SET_PATH);
			}
		});

		btExit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public void readLogFile() {
		System.out.println("Read default log file:");
		File logFile = new File(AppResource.LOG_FILE_PATH);
		ArrayList<String> logData = AppFunction.readFile(logFile);
		if (logData != null) {
			for (int i = 0; i < logData.size(); i++) {
				String s = logData.get(i);
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
				String query = "SELECT " + AccountKeys.ID + "," + AccountKeys.COLUMNS + " FROM " + DatabaseHelper.TB_ACCOUNT
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

	private void viewItems(int type) {
		if (type == WelcomeController.SET_PATH) {
			lbInput1.setText("Workspace");
			lbInput2.setText("Database");
			btBrowse1.setVisible(true);
			btBrowse2.setVisible(true);
			btBack.setVisible(false);
			tfInput2.setVisible(true);
			tfPassword.setVisible(false);
			lbStatus.setText("");
			btAction.setText("Next");
			tfInput1.setText(session.getWorkspaceDir());
			tfInput2.setText(session.getDatabasePath());
			cbSaveInfo.setText("Save information!");
			cbSaveInfo.setSelected(saveWorkspace.trim().equalsIgnoreCase("true"));
		} else if (type == WelcomeController.LOG_IN) {
			lbInput1.setText("User name");
			lbInput2.setText("Password");
			btBrowse1.setVisible(false);
			btBrowse2.setVisible(false);
			btBack.setVisible(true);
			tfInput2.setVisible(false);
			tfPassword.setVisible(true);
			lbStatus.setText("");
			btAction.setText("Login");
			if (saveAccount.equalsIgnoreCase("true")) {
				tfInput1.setText(accountName);
				tfPassword.setText(accountPassword);
			} else {
				tfInput1.setText("");
				tfPassword.setText("");
			}
			cbSaveInfo.setText("Remember account!");
			cbSaveInfo.setSelected(saveAccount.trim().equalsIgnoreCase("true"));
		}

	}
}
