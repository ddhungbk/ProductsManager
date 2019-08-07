package bk.hungd.Controller;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.App.AppSession;
import bk.hungd.Database.BrandHelper;
import bk.hungd.Database.CategoryHelper;
import bk.hungd.Database.DatabaseHelper;
import bk.hungd.Database.ModelHelper;
import bk.hungd.Database.ProductHelper;
import bk.hungd.Extra.CellIntegerEditable;
import bk.hungd.Extra.CellStringEditable;
import bk.hungd.Extra.ExtensionsFilter;
import bk.hungd.Extra.StatusChecker;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class UpdateBrands implements Initializable {

	@FXML
	public TableView<Brand> tableView;
	public TextArea textEditor;
	public HTMLEditor htmlEditor;
	public AnchorPane imageEditor;
	public Button btBrowse;
	public RadioButton rbSingle, rbMultiple;
	public Label lbCreate;
	public TextField tfPath;
	public ScrollPane gridEditor;
	public TilePane imageViewer;
	public ListView<String> listEditor;
	public Button btApply;

	private AppSession session;
	private DatabaseHelper dbHelper;
	private BrandHelper brandHelper;
	private CategoryHelper categoryHelper;
	private ModelHelper modelHelper;
	private ProductHelper productHelper;

	private ObservableList<Brand> brands;
	private ArrayList<Category> categories;
	private ArrayList<Model> models;
	private ArrayList<Product> products;
	private ArrayList<StatusChecker> checkers;

	public UpdateBrands() {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void initSession(final UpdateController mainController, AppSession session) {
		this.session = session;
		dbHelper = new DatabaseHelper(session.getDatabasePath());
		brandHelper = new BrandHelper(session.getDatabasePath());
		categoryHelper = new CategoryHelper(session.getDatabasePath());
		modelHelper = new ModelHelper(session.getDatabasePath());
		productHelper = new ProductHelper(session.getDatabasePath());

		loadDatabase();

		gridEditor.setFitToWidth(true);
		gridEditor.setFitToHeight(true);
		gridEditor.setContent(imageViewer);
	}

	public void loadDatabase() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		String query;
		try {
			System.out.println("Start connection: View all contents of brands");
			connection = dbHelper.createConnection();

			tableView.getColumns().clear();
			tableView.setEditable(true);
			brands = FXCollections.observableArrayList();
			query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND + ";";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				TableColumn<Brand, Integer> orderColumn = new TableColumn<Brand, Integer>("Order");
				TableColumn<Brand, String> nameColumn = new TableColumn<Brand, String>("Name");
				TableColumn<Brand, Image> iconColumn = new TableColumn<Brand, Image>("Logo");
				TableColumn<Brand, String> iconPathColumn = new TableColumn<Brand, String>("Logo Path");
				TableColumn<Brand, String> infoColumn = new TableColumn<Brand, String>("Information");
				TableColumn<Brand, String> imagesColumn = new TableColumn<Brand, String>("Images");
				TableColumn<Brand, String> categoriesColumn = new TableColumn<Brand, String>("Categories");
				TableColumn<Brand, String> productsColumn = new TableColumn<Brand, String>("Products");
				TableColumn<Brand, String> websiteColumn = new TableColumn<Brand, String>("Website");
				TableColumn<Brand, String> tagsColumn = new TableColumn<Brand, String>("Tags");
				TableColumn<Brand, String> notesColumn = new TableColumn<Brand, String>("Notes");

				orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty().asObject());
				nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
				iconColumn.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
				iconPathColumn.setCellValueFactory(cellData -> cellData.getValue().iconPathProperty());
				infoColumn.setCellValueFactory(cellData -> cellData.getValue().infoProperty());
				imagesColumn.setCellValueFactory(cellData -> cellData.getValue().imagesProperty());
				categoriesColumn.setCellValueFactory(cellData -> cellData.getValue().categoriesProperty());
				productsColumn.setCellValueFactory(cellData -> cellData.getValue().productsProperty());
				websiteColumn.setCellValueFactory(cellData -> cellData.getValue().websiteProperty());
				tagsColumn.setCellValueFactory(cellData -> cellData.getValue().tagsProperty());
				notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

				orderColumn.setCellFactory(CellIntegerEditable.<Brand>forTableColumn());
				orderColumn.setOnEditCommit((CellEditEvent<Brand, Integer> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setOrder(t.getNewValue());
					brandHelper.updateNumber(brand.getId(), BrandKeys.ORDER, t.getNewValue());
				});
				nameColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				nameColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setName(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.NAME, t.getNewValue());
				});
				iconColumn.setCellFactory(new Callback<TableColumn<Brand, Image>, TableCell<Brand, Image>>() {
					@Override
					public TableCell<Brand, Image> call(TableColumn<Brand, Image> param) {
						return new TableCell<Brand, Image>() {
							protected void updateItem(Image item, boolean empty) {
								if (item != null) {
									ImageView imageView = new ImageView();
									imageView.setFitHeight(64);
									imageView.setFitWidth(64);
									imageView.setImage(item);
									setGraphic(imageView);
								}
							};
						};
					}
				});
				iconPathColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				iconPathColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					File file = new File(session.getWorkspaceDir() + File.separator + t.getNewValue());
					Image image = AppFunction.getImageFromFile(file);
					brand.setIcon(image);
					brandHelper.updateIcon(brand.getId(), file);
					brand.setIconPath(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.ICON_PATH, t.getNewValue());
				});
				infoColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				infoColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setInfo(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.INFO, t.getNewValue());
				});
				imagesColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				imagesColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setImages(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.IMAGES, t.getNewValue());
				});
				categoriesColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				categoriesColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setCategories(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.CATEGORIES, t.getNewValue());
				});
				productsColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				productsColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setProducts(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.PRODUCTS, t.getNewValue());
				});
				websiteColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				websiteColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setWebsite(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.WEBSITE, t.getNewValue());
				});
				tagsColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				tagsColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setTags(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.TAGS, t.getNewValue());
				});
				notesColumn.setCellFactory(CellStringEditable.<Brand>forTableColumn());
				notesColumn.setOnEditCommit((CellEditEvent<Brand, String> t) -> {
					Brand brand = (Brand) t.getTableView().getItems().get(t.getTablePosition().getRow());
					brand.setNotes(t.getNewValue());
					brandHelper.updateString(brand.getId(), BrandKeys.NOTES, t.getNewValue());
				});

				tableView.getColumns().addAll(orderColumn, nameColumn, iconColumn, iconPathColumn, infoColumn,
						imagesColumn, categoriesColumn, productsColumn, websiteColumn, tagsColumn, notesColumn);

				while (resultSet.next()) {
					Brand brand = brandHelper.getBrand(resultSet);
					brands.add(brand);
				}
				tableView.setItems(brands);
			}
			DatabaseHelper.close(statement);
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	private String maskToField(int column) {
		switch (column) {
		case 0:
			return BrandKeys.ORDER;
		case 1:
			return BrandKeys.NAME;
		case 2:
			return BrandKeys.ICON;
		case 3:
			return BrandKeys.ICON_PATH;
		case 4:
			return BrandKeys.INFO;
		case 5:
			return BrandKeys.IMAGES;
		case 6:
			return BrandKeys.CATEGORIES;
		case 7:
			return BrandKeys.PRODUCTS;
		case 8:
			return BrandKeys.WEBSITE;
		case 9:
			return BrandKeys.TAGS;
		case 10:
			return BrandKeys.NOTES;
		default:
			return "";
		}
	}

	public void viewEditor(String field) {
		textEditor.setVisible(false);
		htmlEditor.setVisible(false);
		imageEditor.setVisible(false);
		gridEditor.setVisible(false);
		listEditor.setVisible(false);
		btApply.setVisible(false);
		switch (field) {
		case BrandKeys.ORDER:
		case BrandKeys.NAME:
		case BrandKeys.WEBSITE:
		case BrandKeys.TAGS:
		case BrandKeys.NOTES:
			textEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case BrandKeys.INFO:
			htmlEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case BrandKeys.ICON:
		case BrandKeys.ICON_PATH:
		case BrandKeys.IMAGES:
			imageEditor.setVisible(true);
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 80d);
			btApply.setVisible(true);
			break;
		case BrandKeys.CATEGORIES:
		case BrandKeys.PRODUCTS:
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 0d);
			btApply.setVisible(true);
			break;
		}
	}

	public void editEvent(MouseEvent event) {
		try {
			if (event.getClickCount() == 1) {
				TablePosition<Brand, Object> position = tableView.getSelectionModel().getSelectedCells().get(0);
				if (position != null) {
					int column = position.getColumn(); // 0,1,2,3,4,5
					int row = position.getRow();

					TableColumn<Brand, Object> tableColumn = position.getTableColumn();
					Brand brand = tableView.getItems().get(row);
					int brandId = brand.getId();
					String field = maskToField(column);

					viewEditor(field);

					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;

					switch (field) {
					case BrandKeys.ORDER:
						int number = Integer.valueOf(tableColumn.getCellObservableValue(brand).getValue().toString());
						textEditor.setText("" + number);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								int data = Integer.valueOf(textEditor.getText());
								brand.setOrder(data);
								brandHelper.updateNumber(brandId, BrandKeys.ORDER, data);
							}
						});
						break;
					case BrandKeys.NAME:
					case BrandKeys.WEBSITE:
					case BrandKeys.TAGS:
					case BrandKeys.NOTES:
						String text = (String) tableColumn.getCellObservableValue(brand).getValue();
						textEditor.setText(text);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = textEditor.getText();
								brand.setString(field, data);
								brandHelper.updateString(brandId, field, data);
							}
						});
						break;
					case BrandKeys.INFO:
						String html = (String) tableColumn.getCellObservableValue(brand).getValue();
						htmlEditor.setHtmlText(html);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = htmlEditor.getHtmlText();
								brand.setInfo(data);
								brandHelper.updateString(brandId, BrandKeys.INFO, data);
							}
						});
						break;
					case BrandKeys.ICON:
					case BrandKeys.ICON_PATH:
						rbSingle.setSelected(true);
						lbCreate.setVisible(false);
						tfPath.setText(session.getWorkspaceDir() + File.separator + brand.getIconPath());

						ImageView icon = new ImageView(brand.getIcon());
						imageViewer.getChildren().clear();
						imageViewer.getChildren().add(icon);

						btBrowse.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								if (rbSingle.isSelected()) {
									FileChooser fileChooser = AppFunction.fileImageChooser("Select An Icon",
											session.getWorkspaceDir());
									File file = fileChooser.showOpenDialog(btBrowse.getScene().getWindow());
									if (file != null) {
										tfPath.setText(file.getPath());
										Image image = AppFunction.getImageFromFile(file);
										ImageView imageView = new ImageView(image);
										imageViewer.getChildren().clear();
										imageViewer.getChildren().add(imageView);
									}
								} else {
									DirectoryChooser chooser = AppFunction.directoryChooser("Select Icon Folder",
											session.getWorkspaceDir());
									File folder = chooser.showDialog(btBrowse.getScene().getWindow());
									if (folder != null) {
										tfPath.setText(folder.getPath());
										File[] files = folder
												.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
										imageViewer.getChildren().clear();
										for (File file : files) {
											if (file.isFile()) {
												Image image = AppFunction.getImageFromFile(file);
												ImageView imageView = new ImageView(image);
												imageViewer.getChildren().add(imageView);
											}
										}
									}
								}
							}
						});

						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								if (rbSingle.isSelected()) {
									File file = new File(tfPath.getText());
									Image image = AppFunction.getImageFromFile(file);
									brand.setIcon(image);
									brandHelper.updateIcon(brandId, file);
									String iconPath = AppFunction.getRelativePath(tfPath.getText().trim(),
											session.getWorkspaceDir());
									brand.setIconPath(iconPath);
									brandHelper.updateString(brandId, BrandKeys.ICON_PATH, iconPath);
								} else {
									File folder = new File(tfPath.getText());
									File[] files = folder
											.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
									for (int i = 0; i < brands.size(); i++) {
										if (i < files.length) {
											Brand brand = brands.get(i);
											Image image = AppFunction.getImageFromFile(files[i]);
											brand.setIcon(image);
											brandHelper.updateIcon(brand.getId(), files[i]);
											String iconPath = AppFunction.getRelativePath(files[i].getPath(),
													session.getWorkspaceDir());
											brand.setIconPath(iconPath);
											brandHelper.updateString(brand.getId(), BrandKeys.ICON_PATH, iconPath);
										}
									}
								}
							}
						});
						break;
					case BrandKeys.IMAGES:
						rbSingle.setSelected(true);
						lbCreate.setVisible(true);
						tfPath.setText(session.getWorkspaceDir() + File.separator + brand.getImages());

						File[] files = new File(tfPath.getText())
								.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
						imageViewer.getChildren().clear();
						for (File file : files) {
							if (file.isFile()) {
								Image image = AppFunction.getImageFromFile(file);
								VBox imageView = createImageView(image);
								imageViewer.getChildren().add(imageView);
							}
						}

						lbCreate.setOnMouseClicked(new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent event) {
								String sourcePath = tfPath.getText().trim();
								for (Brand brand : brands) {
									AppFunction.createFolder(sourcePath, brand.getOrder(), brand.getName());
								}
							}
						});

						btBrowse.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								DirectoryChooser chooser = AppFunction.directoryChooser("Select Images Folder",
										session.getWorkspaceDir());
								File folder = chooser.showDialog(btBrowse.getScene().getWindow());
								if (folder != null) {
									tfPath.setText(folder.getPath());
									File[] files = folder
											.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
									imageViewer.getChildren().clear();
									for (File file : files) {
										Image image = AppFunction.getImageFromFile(file);
										VBox imageView = createImageView(image);
										imageViewer.getChildren().add(imageView);
									}
								}
							}
						});

						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								if (rbSingle.isSelected()) {
									String images = AppFunction.getRelativePath(tfPath.getText().trim(),
											session.getWorkspaceDir());
									brand.setImages(images);
									brandHelper.updateString(brandId, BrandKeys.IMAGES, images);
								} else {
									File folder = new File(tfPath.getText());
									FileFilter dirFilter = new FileFilter() {
										@Override
										public boolean accept(File file) {
											return file.isDirectory();
										}
									};
									File[] subFolders = folder.listFiles(dirFilter);
									for (int i = 0; i < brands.size(); i++) {
										if (i < subFolders.length) {
											Brand brand = brands.get(i);
											String images = AppFunction.getRelativePath(subFolders[i].getPath(),
													session.getWorkspaceDir());
											brand.setImages(images);
											brandHelper.updateString(brand.getId(), BrandKeys.IMAGES, images);
										}
									}
								}
							}
						});
						break;
					case BrandKeys.CATEGORIES:
						try {
							connection = dbHelper.createConnection();
							if (connection != null) {
								String query = "SELECT " + CategoryKeys.ALL_COLUMNS + " FROM "
										+ DatabaseHelper.TB_CATEGORY + ";";
								statement = connection.createStatement();
								resultSet = statement.executeQuery(query);
								categories = new ArrayList<>();
								checkers = new ArrayList<>();
								if (resultSet != null) {
									while (resultSet.next()) {
										Category category = categoryHelper.getCategory(resultSet);
										categories.add(category);
										checkers.add(new StatusChecker(category.getId(), false, false));
									}
								}
								DatabaseHelper.close(resultSet);

								query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL
										+ " WHERE " + ModelKeys.BRAND_ID + "=" + brandId + ";";
								resultSet = statement.executeQuery(query);
								models = new ArrayList<>();
								if (resultSet != null) {
									while (resultSet.next()) {
										Model model = modelHelper.getModel(resultSet);
										models.add(model);
										for (int i = 0; i < checkers.size(); i++) {
											if (checkers.get(i).getId() == model.getCategoryId()) {
												checkers.get(i).setValues(true, true);
												break;
											}
										}
									}
								}
								DatabaseHelper.close(resultSet);

								imageViewer.getChildren().clear();
								for (int i = 0; i < categories.size(); i++) {
									if (checkers.get(i).getFirst()) {
										VBox vbox = createCategoryView(categories.get(i), true);
										imageViewer.getChildren().add(vbox);
									}
								}
								for (int i = 0; i < categories.size(); i++) {
									if (!checkers.get(i).getFirst()) {
										VBox vbox = createCategoryView(categories.get(i), false);
										imageViewer.getChildren().add(vbox);
									}
								}
							}
						} catch (SQLException e) {
							AppDebug.error(e, "");
						} finally {
							DatabaseHelper.close(statement);
							DatabaseHelper.close(connection);
						}

						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								String sCategories = "";
								for (int i = 0; i < checkers.size(); i++) {
									// Add an item
									if (!checkers.get(i).getFirst() && checkers.get(i).getLast()) {
										Category category = categories.get(i);
										Model model = new Model();
										model.setOrder(modelHelper.getMaxOrder() + 1);
										model.setName(brand.getName() + "-" + category.getName());
										model.setIconPath(category.getIconPath());
										model.setBrand(brand.getName());
										model.setCategory(category.getName());
										modelHelper.addModel(model, brandId, category.getId());
										checkers.get(i).setFirst(true);
									} // remove an item
									else if (checkers.get(i).getFirst() && !checkers.get(i).getLast()) {
										Category category = categories.get(i);
										modelHelper.removeModel(brandId, category.getId());
										checkers.get(i).setFirst(false);
									}
									if (checkers.get(i).getLast()) {
										sCategories += categories.get(i).getName() + "; ";
									}
								}
								brand.setCategories(sCategories);
								brandHelper.updateString(brandId, BrandKeys.CATEGORIES, sCategories);
							}
						});
						break;
					case BrandKeys.PRODUCTS:
						try {
							connection = dbHelper.createConnection();
							if (connection != null) {
								String query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM "
										+ DatabaseHelper.TB_PRODUCT + " WHERE " + ProductKeys.BRAND_ID + "=" + brandId
										+ ";";
								statement = connection.createStatement();
								resultSet = statement.executeQuery(query);
								products = new ArrayList<>();
								checkers = new ArrayList<>();
								imageViewer.getChildren().clear();
								if (resultSet != null) {
									while (resultSet.next()) {
										Product product = productHelper.getProduct(resultSet);
										products.add(product);
										checkers.add(new StatusChecker(product.getId(), true, true));
										VBox vbox = createProductView(product, true);
										imageViewer.getChildren().add(vbox);
									}
								}
								DatabaseHelper.close(resultSet);
							}
						} catch (SQLException e) {
							AppDebug.error(e, "");
						} finally {
							DatabaseHelper.close(statement);
							DatabaseHelper.close(connection);
						}

						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								String sProducts = "";
								for (int i = 0; i < checkers.size(); i++) {
									// Add an item
									if (!checkers.get(i).getFirst() && checkers.get(i).getLast()) {
										Product product = products.get(i);
										productHelper.updateNumber(product.getId(), ProductKeys.BRAND_ID, brandId);
										checkers.get(i).setFirst(true);
									} // remove an item
									else if (checkers.get(i).getFirst() && !checkers.get(i).getLast()) {
										Product product = products.get(i);
										productHelper.updateNumber(product.getId(), ProductKeys.BRAND_ID, 0);
										checkers.get(i).setFirst(false);
									}
									if (checkers.get(i).getLast()) {
										sProducts += products.get(i).getName() + "; ";
									}
								}
								brand.setProducts(sProducts);
								brandHelper.updateString(brandId, BrandKeys.PRODUCTS, sProducts);
							}
						});
						break;
					default:
						break;
					}
				}
			} else if (event.getClickCount() > 1) {

			}
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	public VBox createImageView(Image image) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #BA55D3; ");

		Button button = new Button();
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(200);
		imageView.setFitHeight(200);
		button.setGraphic(imageView);
		button.setPadding(new Insets(5));

		vbox.getChildren().addAll(button);

		return vbox;
	}

	public VBox createCategoryView(Category category, boolean selected) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #00ff00; ");

		Button button = new Button();
		ImageView imageView = new ImageView(category.getIcon());
		imageView.setFitWidth(132);
		imageView.setFitHeight(120);
		button.setGraphic(imageView);
		button.setPadding(new Insets(5));

		CheckBox checkBox = new CheckBox(" " + category.getName());
		checkBox.setSelected(selected);
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				for (int i = 0; i < checkers.size(); i++) {
					if (checkers.get(i).getId() == category.getId()) {
						checkers.get(i).setLast(newValue);
						break;
					}
				}
			}
		});

		vbox.getChildren().addAll(button, checkBox);

		return vbox;
	}

	public VBox createProductView(Product product, boolean selected) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #00ff00; ");

		Button button = new Button();
		ImageView imageView = new ImageView(product.getIcon());
		imageView.setFitWidth(132);
		imageView.setFitHeight(120);
		button.setGraphic(imageView);
		button.setPadding(new Insets(5));

		CheckBox checkBox = new CheckBox(" " + product.getName());
		checkBox.setSelected(selected);
		checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				for (int i = 0; i < checkers.size(); i++) {
					if (checkers.get(i).getId() == product.getId()) {
						checkers.get(i).setLast(newValue);
						break;
					}
				}
			}
		});

		vbox.getChildren().addAll(button, checkBox);

		return vbox;
	}

}
