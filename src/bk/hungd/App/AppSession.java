package bk.hungd.App;

public class AppSession {

	private String workspaceDir;
	private String databasePath;
	private int accountId;
	
	public AppSession(String workspaceDir, String databasePath, int accountId) {
		super();
		this.workspaceDir = workspaceDir;
		this.databasePath = databasePath;
		this.accountId = accountId;
	}

	public String getWorkspaceDir() {
		return workspaceDir;
	}

	public void setWorkspaceDir(String workspaceDir) {
		this.workspaceDir = workspaceDir;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public void setDatabasePath(String databasePath) {
		this.databasePath = databasePath;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	
}
