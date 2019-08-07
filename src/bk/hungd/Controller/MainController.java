package bk.hungd.Controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import bk.hungd.App.AppController;
import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.App.AppResource;
import bk.hungd.App.AppSession;
import bk.hungd.Database.AccountHelper;
import bk.hungd.Database.BrandHelper;
import bk.hungd.Database.CategoryHelper;
import bk.hungd.Database.DatabaseHelper;
import bk.hungd.Database.ModelHelper;
import bk.hungd.Database.ProductHelper;
import bk.hungd.Model.Account;
import bk.hungd.Model.AccountKeys;
import bk.hungd.Model.Brand;
import bk.hungd.Model.BrandKeys;
import bk.hungd.Model.Category;
import bk.hungd.Model.CategoryKeys;
import bk.hungd.Model.Model;
import bk.hungd.Model.ModelKeys;
import bk.hungd.Model.Product;
import bk.hungd.Model.ProductKeys;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController implements Initializable {

	@FXML
	public VBox paneBrands;
	public HBox paneCategories;
	public TilePane paneContents;
	public ScrollPane spContents;
	public SplitMenuButton btAccount;
	public SplitPane sppContents;
	public Button btHome;
	public ToggleButton btSoftBrands;
	public ToggleButton btSoftCategories;
	public ProgressBar progress;

	private AppSession session;
	private DatabaseHelper dbHelper;
	private BrandHelper brandHelper;
	private CategoryHelper categoryHelper;
	private ModelHelper modelHelper;
	private ProductHelper productHelper;
	private AccountHelper accountHelper;

	private ArrayList<Brand> brands = null;
	private ArrayList<Category> categories = null;
	private ArrayList<Product> products = null;
	private ArrayList<Model> models = null;
	private ArrayList<ArrayList<Model>> bModels = null;
	private ArrayList<ArrayList<Model>> cModels = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		spContents.setFitToWidth(true);
		spContents.setFitToHeight(true);
		spContents.setContent(paneContents);
		sppContents.setDividerPositions(0.2f, 0.8f);
	}

	public void initSession(final AppController appController, AppSession session) {
		this.session = session;
		dbHelper = new DatabaseHelper(session.getDatabasePath());
		brandHelper = new BrandHelper(session.getDatabasePath());
		categoryHelper = new CategoryHelper(session.getDatabasePath());
		modelHelper = new ModelHelper(session.getDatabasePath());
		productHelper = new ProductHelper(session.getDatabasePath());
		accountHelper = new AccountHelper(session.getDatabasePath());

		showDatabase();
		showAccount(appController, session.getAccountId());

		btHome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				showHomeContents();
			}
		});

		btSoftBrands.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadAllBrands.setSortType(btSoftBrands.isSelected() ? 1 : 0);
				if (!loadAllBrands.isRunning()) {
					loadAllBrands.reset();
					loadAllBrands.start();
				}
				loadAllBrands.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						viewAllBrands();
					}
				});
				progress.progressProperty().bind(loadAllBrands.progressProperty());
			}
		});
		
		btSoftCategories.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				loadAllCategories.setSortType(btSoftCategories.isSelected() ? 1 : 0);
				if (!loadAllCategories.isRunning()) {
					loadAllCategories.reset();
					loadAllCategories.start();
				}
				progress.progressProperty().bind(loadAllCategories.progressProperty());
			}
		});
	}

	private void showAccount(final AppController appController, int accountId) {
		btAccount.getItems().clear();
		btAccount.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//
			}
		});

		MenuItem miSwitchWorkspace = new MenuItem("Switch Workspace");
		miSwitchWorkspace.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				appController.setPath();
			}
		});

		MenuItem miSwitchDatabase = new MenuItem("Switch Database");
		miSwitchDatabase.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				appController.setPath();
			}
		});

		MenuItem miUpdateDatabase = new MenuItem("Update Database");

		MenuItem miProfile = new MenuItem("Account Settings");
		miProfile.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				//
			}
		});
		MenuItem miLogOut = new MenuItem("Log Out");
		miLogOut.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				appController.logout();
			}
		});

		if (accountId == AppResource.ROOT_ACCOUNT_ID) {
			btAccount.setText("ROOT");

			miUpdateDatabase.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					openDbUpdator();
				}
			});

			btAccount.getItems().addAll(miSwitchWorkspace, miUpdateDatabase, miLogOut);
		} else {
			Connection connection = null;
			Statement statement = null;
			ResultSet resultSet = null;
			Account account = null;
			try {
				System.out.println("Start connection: Get info of account #" + accountId);
				connection = dbHelper.createConnection();
				if (connection != null) {
					String query = "SELECT " + AccountKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_ACCOUNT
							+ " WHERE " + AccountKeys.ID + "=" + accountId + ";";
					statement = connection.createStatement();
					resultSet = statement.executeQuery(query);
					if (resultSet != null) {
						while (resultSet.next()) {
							account = accountHelper.getAccount(resultSet);
							break;
						}
					}
				}
			} catch (SQLException e) {
				AppDebug.error(e, "");
			} finally {
				DatabaseHelper.close(statement);
				DatabaseHelper.close(connection);
			}

			if (account != null) {
				final String accountType = account.getUserType();
				miUpdateDatabase.setDisable(!accountType.equals("admin"));
				miUpdateDatabase.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (accountType.equals("admin")) {
							openDbUpdator();
						}
					}
				});
				btAccount.setText(account.getName());
				btAccount.getItems().addAll(miSwitchWorkspace, miSwitchDatabase, miUpdateDatabase,
						new SeparatorMenuItem(), miProfile, miLogOut);
			}
		}

	}

	// ==================================================================================
	class LoadAllBrands extends Service<Void> {
		private int sortType;

		public int getSortType() {
			return sortType;
		}

		public void setSortType(int sortType) {
			this.sortType = sortType;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement bStatement = null;
					Statement mStatement = null;
					ResultSet bResultSet = null;
					ResultSet mResultSet = null;
					try {
						System.out.println("Start connection: Load all brands from database");
						connection = dbHelper.createConnection();
						if (connection != null) {
							bStatement = connection.createStatement();
							bResultSet = bStatement
									.executeQuery("SELECT COUNT(*) AS rowCount FROM " + DatabaseHelper.TB_BRAND + ";");
							int count = bResultSet.getInt("rowCount");

							brands = new ArrayList<>();
							bModels = new ArrayList<>();
							String query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + "";
							if (sortType == 0)
								query += " ORDER BY " + BrandKeys.ORDER + " ASC;";
							if (sortType == 1)
								query += " ORDER BY " + BrandKeys.NAME + " ASC;";
							bResultSet = bStatement.executeQuery(query);
							if (bResultSet != null) {
								int progress = 1;
								while (bResultSet.next()) {
									Brand brand = brandHelper.getBrand(bResultSet);
									brands.add(brand);
									query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL
											+ " WHERE " + ModelKeys.BRAND_ID + "=" + brand.getId() + ";";
									mStatement = connection.createStatement();
									mResultSet = mStatement.executeQuery(query);
									ArrayList<Model> models = new ArrayList<>();
									if (mResultSet != null) {
										while (mResultSet.next()) {
											Model model = modelHelper.getModel(mResultSet);
											models.add(model);
										}
									}
									DatabaseHelper.close(mStatement);
									bModels.add(models);
									updateProgress(progress++, count);
								}
							}
							DatabaseHelper.close(bStatement);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(bStatement);
						DatabaseHelper.close(mStatement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	class LoadAllCategories extends Service<Void> {
		private int sortType;

		public int getSortType() {
			return sortType;
		}

		public void setSortType(int sortType) {
			this.sortType = sortType;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement cStatement = null;
					Statement mStatement = null;
					ResultSet cResultSet = null;
					ResultSet mResultSet = null;
					try {
						System.out.println("Start connection: Load all categories from database");
						connection = dbHelper.createConnection();
						if (connection != null) {
							cStatement = connection.createStatement();
							cResultSet = cStatement
									.executeQuery("SELECT COUNT(*) AS rowCount FROM " + DatabaseHelper.TB_CATEGORY + ";");
							int count = cResultSet.getInt("rowCount");
							
							categories = new ArrayList<>();
							cModels = new ArrayList<>();
							String query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_CATEGORY + "";
							if (sortType == 0)
								query += " ORDER BY " + BrandKeys.ORDER + " ASC;";
							if (sortType == 1)
								query += " ORDER BY " + BrandKeys.NAME + " ASC;";
							cResultSet = cStatement.executeQuery(query);
							if (cResultSet != null) {
								int progress = 1;
								while (cResultSet.next()) {
									Category category = categoryHelper.getCategory(cResultSet);
									categories.add(category);
									query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL
											+ " WHERE " + ModelKeys.CATEGORY_ID + "=" + category.getId() + ";";
									mStatement = connection.createStatement();
									mResultSet = mStatement.executeQuery(query);
									ArrayList<Model> models = new ArrayList<>();
									if (mResultSet != null) {
										while (mResultSet.next()) {
											Model model = modelHelper.getModel(mResultSet);
											models.add(model);
										}
									}
									DatabaseHelper.close(mStatement);
									cModels.add(models);
									updateProgress(progress++, count);
								}
							}
							DatabaseHelper.close(cStatement);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(cStatement);
						DatabaseHelper.close(mStatement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	class LoadAllProducts extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement pStatement = null;
					ResultSet pResultSet = null;
					String query;
					try {
						System.out.println("Start connection: Load all products from database");
						connection = dbHelper.createConnection();
						if (connection != null) {
							products = new ArrayList<>();
							query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT
									+ " WHERE " + ProductKeys.QUANTITY + " >= 90" + ";";
							pStatement = connection.createStatement();
							pResultSet = pStatement.executeQuery(query);
							if (pResultSet != null) {
								while (pResultSet.next()) {
									Product product = productHelper.getProduct(pResultSet);
									products.add(product);
								}
							}
							DatabaseHelper.close(pStatement);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(pStatement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	LoadAllBrands loadAllBrands = new LoadAllBrands();
	LoadAllCategories loadAllCategories = new LoadAllCategories();
	LoadAllProducts loadAllProducts = new LoadAllProducts();

	public void viewAllBrands() {
		paneBrands.getChildren().clear();
		ArrayList<TitledPane> titledPanes = new ArrayList<>();
		for (int i = 0; i < brands.size(); i++) {
			Brand brand = brands.get(i);
			TitledPane titledPane = new TitledPane();
			titledPane.setText(brand.getName());
			ListView<Model> list = new ListView<>();
			for (int j = 0; j < bModels.get(i).size(); j++) {
				Model model = bModels.get(i).get(j);
				list.getItems().add(model);
			}
			list.setPrefHeight(24 * list.getItems().size());
			titledPane.setContent(list);
			titledPane.setExpanded(false);
			setBrandClickListener(titledPane, brand);
			setCategoryClickListener(list);
			titledPanes.add(titledPane);
		}

		Accordion accordion = new Accordion();
		accordion.getPanes().addAll(titledPanes);
		paneBrands.getChildren().add(accordion);
	}

	public void viewAllCategories() {
		paneCategories.getChildren().clear();
		ArrayList<SplitMenuButton> menuButtons = new ArrayList<>();
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			SplitMenuButton menuButton = new SplitMenuButton();
			menuButton.setText(" " + category.getName() + " ");
			ArrayList<MenuItem> menuItems = new ArrayList<>();
			for (int j = 0; j < cModels.get(i).size(); j++) {
				Model model = cModels.get(i).get(j);
				MenuItem menuItem = new MenuItem("   " + model.getBrand() + "   ");
				setBrandClickListener(menuItem, model);
				menuItems.add(menuItem);
			}
			menuButton.getItems().addAll(menuItems);
			menuButton.setPadding(new Insets(0, 0, 0, 5));
			setCategoryClickListener(menuButton, category);
			menuButtons.add(menuButton);
		}
		paneCategories.getChildren().addAll(menuButtons);
	}

	public void viewAllProducts() {
		paneContents.getChildren().clear();
		for (Product product : products) {
			VBox productView = createProductView(product);
			paneContents.getChildren().add(productView);
		}
	}

	public void showDatabase() {
		loadAllBrands.setSortType(btSoftBrands.isSelected() ? 1 : 0);
		if (!loadAllBrands.isRunning()) {
			loadAllBrands.reset();
			loadAllBrands.start();
		}

		loadAllCategories.setSortType(btSoftCategories.isSelected()?1:0);
		if (!loadAllCategories.isRunning()) {
			loadAllCategories.reset();
			loadAllCategories.start();
		}

		if (!loadAllProducts.isRunning()) {
			loadAllProducts.reset();
			loadAllProducts.start();
		}

		loadAllBrands.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				viewAllBrands();
			}
		});
		progress.progressProperty().bind(loadAllBrands.progressProperty());

		loadAllCategories.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				viewAllCategories();
			}
		});

		loadAllProducts.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				viewAllProducts();
			}
		});
	}

	// =================================================================
	class GetHomeContents extends Service<Void> {
		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;
					String query;
					try {
						System.out.println("Start connection: Read database to show to Home");
						connection = dbHelper.createConnection();
						if (connection != null) {
							statement = connection.createStatement();

							int count = 0, progress = 0;
							resultSet = statement
									.executeQuery("SELECT COUNT(*) AS rowCount FROM " + DatabaseHelper.TB_BRAND + ";");
							count += resultSet.getInt("rowCount");
							resultSet = statement.executeQuery(
									"SELECT COUNT(*) AS rowCount FROM " + DatabaseHelper.TB_CATEGORY + ";");
							count += resultSet.getInt("rowCount");
							System.out.println("Count = " + count);

							query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + ";";
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								brands = new ArrayList<>();
								while (resultSet.next()) {
									Brand brand = brandHelper.getBrand(resultSet);
									brands.add(brand);
									// updateProgress(++progress, count);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(1, 3);

							query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_CATEGORY + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								categories = new ArrayList<>();
								while (resultSet.next()) {
									Category category = categoryHelper.getCategory(resultSet);
									categories.add(category);
									// updateProgress(++progress, count);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(2, 3);

							query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT
							/* + " WHERE " + ProductKeys.QUANTITY + " >= 90" */ + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								products = new ArrayList<>();
								while (resultSet.next()) {
									Product product = productHelper.getProduct(resultSet);
									if (product.getQuantity() >= 90) {
										products.add(product);
									}
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(3, 3);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} catch (Exception e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(statement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	public void viewHomeContents() {
		paneContents.getChildren().clear();
		for (Brand brand : brands) {
			VBox brandView = createBrandView(brand);
			paneContents.getChildren().add(brandView);
		}
		for (Category category : categories) {
			VBox categoryView = createCategoryView(category);
			paneContents.getChildren().add(categoryView);
		}
		for (Product product : products) {
			VBox productView = createProductView(product);
			paneContents.getChildren().add(productView);
		}
	}

	GetHomeContents getHomeContents = new GetHomeContents();

	public void showHomeContents() {
		if (!getHomeContents.isRunning()) {
			getHomeContents.reset();
			getHomeContents.start();
		}
		getHomeContents.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				viewHomeContents();
			}
		});
		progress.progressProperty().bind(getHomeContents.progressProperty());
	}

	// ======
	class GetBrandContents extends Service<Void> {
		private Brand brand;

		public Brand getBrand() {
			return brand;
		}

		public void setBrand(Brand brand) {
			this.brand = brand;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;
					String query;
					try {
						System.out.println("Start connection: Read all contents of brand #" + brand.getId());
						connection = dbHelper.createConnection();
						if (connection != null) {
							query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + " WHERE "
									+ BrandKeys.ID + "=" + brand.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							updateProgress(1, 6);
							if (resultSet != null) {
								brands = new ArrayList<>();
								while (resultSet.next()) {
									Brand brand = brandHelper.getBrand(resultSet);
									brands.add(brand);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(2, 6);

							query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL + " WHERE "
									+ ModelKeys.BRAND_ID + "=" + brand.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							updateProgress(3, 6);
							if (resultSet != null) {
								models = new ArrayList<>();
								while (resultSet.next()) {
									Model model = modelHelper.getModel(resultSet);
									models.add(model);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(4, 6);

							query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT
									+ " WHERE " + ProductKeys.BRAND_ID + "=" + brand.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							updateProgress(5, 6);
							if (resultSet != null) {
								products = new ArrayList<>();
								while (resultSet.next()) {
									Product product = productHelper.getProduct(resultSet);
									products.add(product);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(6, 6);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(statement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	public void viewBrandContents() {
		paneContents.getChildren().clear();
		for (Brand brand : brands) {
			VBox brandView = createBrandView(brand);
			paneContents.getChildren().add(brandView);
		}
		for (Model model : models) {
			VBox modelView = createModelView(model);
			paneContents.getChildren().add(modelView);
		}
		for (Product product : products) {
			VBox productView = createProductView(product);
			paneContents.getChildren().add(productView);
		}
	}

	GetBrandContents getBrandContents = new GetBrandContents();

	public void setBrandClickListener(TitledPane pane, Brand brand) {
		pane.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue == true) {
					getBrandContents.setBrand(brand);
					if (!getBrandContents.isRunning()) {
						getBrandContents.reset();
						getBrandContents.start();
					}
					getBrandContents.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
						@Override
						public void handle(WorkerStateEvent event) {
							viewBrandContents();
						}
					});
					progress.progressProperty().bind(getBrandContents.progressProperty());
				}
			}
		});
	}

	// ======
	class GetCategoryContents extends Service<Void> {
		private Category category;

		public Category getCategory() {
			return category;
		}

		public void setCategory(Category category) {
			this.category = category;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;
					String query;
					try {
						System.out.println("Start connection: Read all contents of category #" + category.getId());
						connection = dbHelper.createConnection();
						if (connection != null) {
							query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_CATEGORY
									+ " WHERE " + CategoryKeys.ID + "=" + category.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								categories = new ArrayList<>();
								while (resultSet.next()) {
									Category category = categoryHelper.getCategory(resultSet);
									categories.add(category);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(1, 3);

							query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL + " WHERE "
									+ ModelKeys.CATEGORY_ID + "=" + category.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								brands = new ArrayList<>();
								while (resultSet.next()) {
									Model model = modelHelper.getModel(resultSet);
									query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND
											+ " WHERE " + BrandKeys.ID + "=" + model.getBrandId() + ";";
									Statement st = connection.createStatement();
									ResultSet rs = st.executeQuery(query);
									if (rs != null) {
										if (rs.next()) {
											Brand brand = brandHelper.getBrand(rs);
											brands.add(brand);
										}
									}
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(2, 3);

							query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT
									+ " WHERE " + ProductKeys.CATEGORY_ID + "=" + category.getId() + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								products = new ArrayList<>();
								while (resultSet.next()) {
									Product product = productHelper.getProduct(resultSet);
									products.add(product);
								}
							}
							DatabaseHelper.close(statement);
							updateProgress(3, 3);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(statement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	public void viewCategoryContents() {
		paneContents.getChildren().clear();
		for (Category category : categories) {
			VBox categoryView = createCategoryView(category);
			paneContents.getChildren().add(categoryView);
		}

		for (Brand brand : brands) {
			VBox brandView = createBrandView(brand);
			paneContents.getChildren().add(brandView);
		}
		for (Product product : products) {
			VBox productView = createProductView(product);
			paneContents.getChildren().add(productView);
		}
	}

	GetCategoryContents getCategoryContents = new GetCategoryContents();

	public void setCategoryClickListener(MenuButton menuButton, Category category) {
		menuButton.showingProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue == true) {
					// do something
				}
			}
		});
	}

	public void setCategoryClickListener(SplitMenuButton menuButton, Category category) {
		menuButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getCategoryContents.setCategory(category);
				if (!getCategoryContents.isRunning()) {
					getCategoryContents.reset();
					getCategoryContents.start();
				}

				getCategoryContents.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						viewCategoryContents();
					}
				});
				progress.progressProperty().bind(getCategoryContents.progressProperty());
			}
		});
	}

	// ======
	class GetProductsOfModel extends Service<Void> {
		private Model model;

		public Model getModel() {
			return model;
		}

		public void setModel(Model model) {
			this.model = model;
		}

		@Override
		protected Task<Void> createTask() {
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;
					try {
						int brandId = model.getBrandId();
						int categoryId = model.getCategoryId();
						System.out.println("Start connection: Read products of brand #" + brandId + " and category #"
								+ categoryId);
						connection = dbHelper.createConnection();
						if (connection != null) {
							products = new ArrayList<>();
							String query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT
									+ " WHERE " + ProductKeys.BRAND_ID + "=" + brandId + " AND "
									+ ProductKeys.CATEGORY_ID + "=" + categoryId + ";";
							statement = connection.createStatement();
							resultSet = statement.executeQuery(query);
							if (resultSet != null) {
								while (resultSet.next()) {
									Product product = productHelper.getProduct(resultSet);
									products.add(product);
								}
							}
							DatabaseHelper.close(statement);
						}
					} catch (SQLException e) {
						AppDebug.error(e, "");
					} finally {
						DatabaseHelper.close(statement);
						DatabaseHelper.close(connection);
					}
					return null;
				}
			};
		}
	}

	GetProductsOfModel getProductsOfModel = new GetProductsOfModel();
	
	private void viewProductsOfCategory() {
		paneContents.getChildren().clear();
		for (Product product : products) {
			VBox productView = createProductView(product);
			paneContents.getChildren().add(productView);
		}
	}

	public void setCategoryClickListener(ListView<Model> listView) {
		listView.setCellFactory(param -> new ListCell<Model>() {
			@Override
			protected void updateItem(Model item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null || item.getCategory() == null) {
					setText(null);
				} else {
					setText("     " + item.getCategory());
				}
			}
		});

		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Model>() {
			@Override
			public void changed(ObservableValue<? extends Model> observable, Model oldModel, Model newModel) {
				getProductsOfModel.setModel(newModel);
				if(!getProductsOfModel.isRunning()){
					getProductsOfModel.reset();
					getProductsOfModel.start();
				}
				
				getProductsOfModel.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						viewProductsOfCategory();
					}
				});
			}
		});
	}

	public void setBrandClickListener(MenuItem menuItem, Model model) {
		menuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				getProductsOfModel.setModel(model);
				if(!getProductsOfModel.isRunning()){
					getProductsOfModel.reset();
					getProductsOfModel.start();
				}
				
				getProductsOfModel.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
					@Override
					public void handle(WorkerStateEvent event) {
						viewProductsOfCategory();
					}
				});
			}
		});
	}

	//==============================================================
	public void setBrandClickListener(Button button, int brandId) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Connection connection = null;
				Statement statement = null;
				ResultSet resultSet = null;
				String query;
				try {
					System.out.println("Start connection: Read information of brand #" + brandId);
					connection = dbHelper.createConnection();
					if (connection != null) {
						query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + " WHERE "
								+ BrandKeys.ID + "=" + brandId + ";";
						statement = connection.createStatement();
						resultSet = statement.executeQuery(query);
						if (resultSet != null) {
							while (resultSet.next()) {
								button.setText(button.getText() + ": " + resultSet.getString(BrandKeys.NAME));
							}
						}
						DatabaseHelper.close(statement);
					}
				} catch (SQLException e) {
					AppDebug.error(e, "");
				} finally {
					DatabaseHelper.close(statement);
					DatabaseHelper.close(connection);
				}
			}
		});
	}

	public void setCategoryClickListener(Button button, int categoryId) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Connection connection = null;
				Statement statement = null;
				ResultSet resultSet = null;
				String query;
				try {
					System.out.println("Start connection: Read information of category #" + categoryId);
					connection = dbHelper.createConnection();
					if (connection != null) {
						query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_CATEGORY + " WHERE "
								+ CategoryKeys.ID + "=" + categoryId + ";";
						statement = connection.createStatement();
						resultSet = statement.executeQuery(query);
						if (resultSet != null) {
							while (resultSet.next()) {
								button.setText(button.getText() + ": " + resultSet.getString(CategoryKeys.NAME));
							}
						}
					}
				} catch (SQLException e) {
					AppDebug.error(e, "");
				} finally {
					DatabaseHelper.close(statement);
					DatabaseHelper.close(connection);
				}
			}
		});
	}

	public void setProductClickListener(Button button, int productId) {
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Connection connection = null;
				Statement statement = null;
				ResultSet resultSet = null;
				String query;
				try {
					System.out.println("Start connection: Read information of product #" + productId);
					connection = dbHelper.createConnection();
					if (connection != null) {
						query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT + " WHERE "
								+ ProductKeys.ID + "=" + productId + ";";
						statement = connection.createStatement();
						resultSet = statement.executeQuery(query);
						if (resultSet != null) {
							while (resultSet.next()) {
								Product product = new Product();
								product.setName(resultSet.getString(ProductKeys.NAME));
								product.setPrice(resultSet.getDouble(ProductKeys.PRICE));
								product.setQuantity(resultSet.getInt(ProductKeys.QUANTITY));
								button.setText("" + product.getName() + "\n" + product.getPrice());
							}
						}
					}
				} catch (SQLException e) {
					AppDebug.error(e, "");
				} finally {
					DatabaseHelper.close(statement);
					DatabaseHelper.close(connection);
				}
			}
		});
	}

	//===============================================================
	public VBox createBrandView(Brand brand) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #BA55D3; ");

		Button button = new Button();
		ImageView imageView = new ImageView(brand.getIcon());
		// imageView.setFitWidth(200);
		// imageView.setFitHeight(200);
		button.setGraphic(imageView);
		button.setPadding(new Insets(10));
		Label label = new Label(brand.getName());
		vbox.getChildren().addAll(button, label);

		return vbox;
	}

	public VBox createCategoryView(Category category) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #00ff00; ");

		Button button = new Button();
		ImageView imageView = new ImageView(category.getIcon());
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		button.setGraphic(imageView);
		button.setPadding(new Insets(10));
		vbox.getChildren().addAll(button);

		return vbox;
	}

	public VBox createModelView(Model model) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #002200; ");

		Button button = new Button();
		ImageView imageView = new ImageView(model.getIcon());
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		button.setGraphic(imageView);
		button.setPadding(new Insets(10));
		vbox.getChildren().addAll(button);

		return vbox;
	}

	public VBox createProductView(Product product) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #1E90FF; ");

		Button button = new Button();
		ImageView imageView = new ImageView(product.getIcon());
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		button.setGraphic(imageView);
		button.setPadding(new Insets(10));
		Label label = new Label("" + product.getPrice());
		vbox.getChildren().addAll(button, label);

		return vbox;
	}

	// ===============================================================
	public void createDatabase() {
		dbHelper.deleteDatabase();
		dbHelper.createDatabase();
		Random rand = new Random();

		AccountHelper accountHelper = new AccountHelper(session.getDatabasePath());
		accountHelper.addAccount(new Account(1, "admin", "00000", "admin", "Admin", "ADMIN-000", "", ""));
		accountHelper.addAccount(new Account(2, "hungd", "12345", "admin", "Hung", "ADMIN-001", "", ""));
		accountHelper.addAccount(new Account(3, "red", "123", "user", "Red", "USER-010", "", ""));
		accountHelper.addAccount(new Account(4, "bomb", "abc", "user", "Bomb", "USER-011", "", ""));
		accountHelper.addAccount(new Account(5, "chuck", "xyz", "user", "Chuck", "USER-012", "", ""));

		BrandHelper brandHelper = new BrandHelper(session.getDatabasePath());
		ArrayList<Brand> brands = getBrands();

		CategoryHelper categoryHelper = new CategoryHelper(session.getDatabasePath());
		ArrayList<Category> categories = getCategories();

		ModelHelper modelHelper = new ModelHelper(session.getDatabasePath());
		ArrayList<Model> models = new ArrayList<>();

		ProductHelper productHelper = new ProductHelper(session.getDatabasePath());
		ArrayList<Product> products = getProducts();

		brandHelper.addBrands(brands);

		categoryHelper.addCategories(categories);

		int modelOrder = 0;
		for (int b = 0; b < brands.size(); b++) {
			Brand brand = brands.get(b);
			int cCount = rand.nextInt(5) + 4; // must < categories.size()
			int[] rands = AppFunction.filteredRandom(cCount, categories.size());
			for (int c = 0; c < rands.length; c++) {
				Category category = categories.get(rands[c]);
				Model model = new Model();
				model.setOrder(++modelOrder);
				model.setName(brand.getName() + "-" + category.getName());
				model.setIcon(category.getIcon());
				model.setIconPath(category.getIconPath());
				model.setBrand(brand.getName());
				model.setCategory(category.getName());
				model.setInfo(category.getInfo());
				model.setImages(category.getImages());
				model.setProducts("");
				model.setTags(category.getTags());
				model.setNotes(category.getNotes());
				models.add(model);
				modelHelper.addModel(model, brand.getId(), category.getId());
			}
		}

		for (int p = 0; p < products.size(); p++) {
			int brandId = rand.nextInt(brands.size()) + 1;
			int categoryId = rand.nextInt(categories.size()) + 1;
			productHelper.addProduct(products.get(p), brandId, categoryId);
		}
	}

	public ArrayList<Brand> getBrands() {
		File file = new File(session.getWorkspaceDir() + "/Brands/brands.txt");
		ArrayList<String> list = AppFunction.readFile(file);

		ArrayList<Brand> brands = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			brands.add(new Brand(i + 1, i + 1, list.get(i), "", "", "", "", "", "", "", ""));
		}

		file = new File(session.getWorkspaceDir() + "/Brands/icons");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			Image image = AppFunction.getImageFromFile(files[i]);
			brands.get(i).setIcon(image);
			String iconPath = AppFunction.getRelativePath(files[i].getPath(), session.getWorkspaceDir());
			brands.get(i).setIconPath(iconPath);
		}

		return brands;
	}

	public ArrayList<Category> getCategories() {
		File file = new File(session.getWorkspaceDir() + "/Categories/categories.txt");
		ArrayList<String> list = AppFunction.readFile(file);

		ArrayList<Category> categories = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			categories.add(new Category(i + 1, (i + 1), list.get(i), "", "", "", "", "", "", ""));
		}

		file = new File(session.getWorkspaceDir() + "/Categories/icons");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			Image image = AppFunction.getImageFromFile(files[i]);
			categories.get(i).setIcon(image);
			String iconPath = AppFunction.getRelativePath(files[i].getPath(), session.getWorkspaceDir());
			categories.get(i).setIconPath(iconPath);
		}
		return categories;
	}

	public ArrayList<Product> getProducts() {
		File file = new File(session.getWorkspaceDir() + "/Products/products.txt");
		ArrayList<String> list = AppFunction.readFile(file);

		Random rand = new Random();

		ArrayList<Product> products = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			products.add(new Product(i + 1, (i + 1), list.get(i), "", "", "", "", "", "", "",
					(100000 * rand.nextDouble()), (rand.nextInt(100)), "", "", ""));
		}

		file = new File(session.getWorkspaceDir() + "/Products/icons");
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			Image image = AppFunction.getImageFromFile(files[i]);
			products.get(i).setIcon(image);
			String iconPath = AppFunction.getRelativePath(files[i].getPath(), session.getWorkspaceDir());
			products.get(i).setIconPath(iconPath);
		}
		return products;
	}

	public void loadDataFromDb() {
		Connection connection = null;
		Statement bStatement = null;
		Statement cStatement = null;
		Statement mStatement = null;
		Statement pStatement = null;
		ResultSet bResultSet = null;
		ResultSet cResultSet = null;
		ResultSet mResultSet = null;
		ResultSet pResultSet = null;
		String query;
		try {
			System.out.println("Start connection: Load all contents from database");
			connection = dbHelper.createConnection();
			if (connection != null) {
				paneBrands.getChildren().clear();
				ArrayList<TitledPane> titledPanes = new ArrayList<>();
				query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + ";";
				bStatement = connection.createStatement();
				bResultSet = bStatement.executeQuery(query);
				if (bResultSet != null) {
					while (bResultSet.next()) {
						Brand brand = brandHelper.getBrand(bResultSet);
						TitledPane titledPane = new TitledPane();
						titledPane.setText(brand.getName());
						ListView<Model> list = new ListView<>();
						query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL + " WHERE "
								+ ModelKeys.BRAND_ID + "=" + brand.getId() + ";";
						mStatement = connection.createStatement();
						mResultSet = mStatement.executeQuery(query);
						if (mResultSet != null) {
							while (mResultSet.next()) {
								Model model = modelHelper.getModel(mResultSet);
								list.getItems().add(model);
							}
						}
						DatabaseHelper.close(mStatement);
						list.setPrefHeight(24 * list.getItems().size());
						titledPane.setContent(list);
						titledPane.setExpanded(false);
						setBrandClickListener(titledPane, brand);
						setCategoryClickListener(list);
						titledPanes.add(titledPane);
					}
				}
				DatabaseHelper.close(bStatement);

				Accordion accordion = new Accordion();
				accordion.getPanes().addAll(titledPanes);
				paneBrands.getChildren().add(accordion);

				paneCategories.getChildren().clear();
				ArrayList<SplitMenuButton> menuButtons = new ArrayList<>();
				query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_CATEGORY + ";";
				cStatement = connection.createStatement();
				cResultSet = cStatement.executeQuery(query);
				if (cResultSet != null) {
					while (cResultSet.next()) {
						Category category = categoryHelper.getCategory(cResultSet);
						SplitMenuButton menuButton = new SplitMenuButton();
						menuButton.setText(" " + category.getName() + " ");
						ArrayList<MenuItem> menuItems = new ArrayList<>();
						query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL + " WHERE "
								+ ModelKeys.CATEGORY_ID + "=" + category.getId() + ";";
						mStatement = connection.createStatement();
						mResultSet = mStatement.executeQuery(query);
						if (mResultSet != null) {
							while (mResultSet.next()) {
								Model model = modelHelper.getModel(mResultSet);
								MenuItem menuItem = new MenuItem("   " + model.getBrand() + "   ");
								setBrandClickListener(menuItem, model);
								menuItems.add(menuItem);
							}
						}
						DatabaseHelper.close(mStatement);
						menuButton.getItems().addAll(menuItems);
						menuButton.setPadding(new Insets(0, 0, 0, 5));
						setCategoryClickListener(menuButton, category);
						menuButtons.add(menuButton);
					}
				}
				DatabaseHelper.close(cStatement);
				paneCategories.getChildren().addAll(menuButtons);

				paneContents.getChildren().clear();
				query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT + " WHERE "
						+ ProductKeys.QUANTITY + " >= 90" + ";";
				pStatement = connection.createStatement();
				pResultSet = pStatement.executeQuery(query);
				if (pResultSet != null) {
					while (pResultSet.next()) {
						Product product = productHelper.getProduct(pResultSet);
						VBox productView = createProductView(product);
						paneContents.getChildren().add(productView);
					}
				}
				DatabaseHelper.close(pStatement);
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(bStatement);
			DatabaseHelper.close(cStatement);
			DatabaseHelper.close(mStatement);
			DatabaseHelper.close(pStatement);
			DatabaseHelper.close(connection);
		}
	}

	public void openDbUpdator() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/UpdateScene.fxml"));
			Parent root = loader.load();

			UpdateController updateController = loader.<UpdateController>getController();
			updateController.initSession(this, session);

			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/bk/hungd/Layout/application.css").toExternalForm());
			Stage stage = new Stage();
			stage.setMaximized(true);
			stage.setResizable(true);
			stage.setTitle("Update Database");
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			AppDebug.error(e, "");
		}
	}

	public void setSplitPaneDivider() {
		sppContents.setDividerPositions(0.15f, 0.85f);
	}

}
