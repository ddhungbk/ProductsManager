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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
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

public class UpdateModels implements Initializable {

	@FXML
	public TableView<Model> tableView;
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

	private ArrayList<Brand> brands;
	private ArrayList<Category> categories;
	private ObservableList<Model> models;
	private ArrayList<Product> products;
	private ArrayList<StatusChecker> checkers;

	public UpdateModels() {

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
			System.out.println("Start connection: View all contents of models");
			connection = dbHelper.createConnection();

			tableView.getColumns().clear();
			tableView.setEditable(true);
			models = FXCollections.observableArrayList();
			query = "SELECT " + ModelKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_MODEL + ";";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				TableColumn<Model, Integer> orderColumn = new TableColumn<Model, Integer>("Order");
				TableColumn<Model, String> nameColumn = new TableColumn<Model, String>("Name");
				TableColumn<Model, Image> iconColumn = new TableColumn<Model, Image>("Icon");
				TableColumn<Model, String> iconPathColumn = new TableColumn<Model, String>("Icon Path");
				TableColumn<Model, String> brandColumn = new TableColumn<Model, String>("Brand");
				TableColumn<Model, String> categoryColumn = new TableColumn<Model, String>("Category");
				TableColumn<Model, String> infoColumn = new TableColumn<Model, String>("Information");
				TableColumn<Model, String> imagesColumn = new TableColumn<Model, String>("Images");
				TableColumn<Model, String> productsColumn = new TableColumn<Model, String>("Products");
				TableColumn<Model, String> tagsColumn = new TableColumn<Model, String>("Tags");
				TableColumn<Model, String> notesColumn = new TableColumn<Model, String>("Notes");

				orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty().asObject());
				nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
				iconColumn.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
				iconPathColumn.setCellValueFactory(cellData -> cellData.getValue().iconPathProperty());
				brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
				categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
				infoColumn.setCellValueFactory(cellData -> cellData.getValue().infoProperty());
				imagesColumn.setCellValueFactory(cellData -> cellData.getValue().imagesProperty());
				productsColumn.setCellValueFactory(cellData -> cellData.getValue().productsProperty());
				tagsColumn.setCellValueFactory(cellData -> cellData.getValue().tagsProperty());
				notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

				orderColumn.setCellFactory(CellIntegerEditable.<Model>forTableColumn());
				orderColumn.setOnEditCommit((CellEditEvent<Model, Integer> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setOrder(t.getNewValue());
					modelHelper.updateNumber(model.getId(), ModelKeys.ORDER, t.getNewValue());
				});
				nameColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				nameColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setName(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.NAME, t.getNewValue());
				});
				iconColumn.setCellFactory(new Callback<TableColumn<Model, Image>, TableCell<Model, Image>>() {
					@Override
					public TableCell<Model, Image> call(TableColumn<Model, Image> param) {
						return new TableCell<Model, Image>() {
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
				iconPathColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				iconPathColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					File file = new File(session.getWorkspaceDir() + File.separator + t.getNewValue());
					Image image = AppFunction.getImageFromFile(file);
					model.setIcon(image);
					modelHelper.updateIcon(model.getId(), file);
					model.setIconPath(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.ICON_PATH, t.getNewValue());
				});
				brandColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				brandColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setBrand(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.BRAND, t.getNewValue());
				});
				categoryColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				categoryColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setCategory(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.CATEGORY, t.getNewValue());
				});
				infoColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				infoColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setInfo(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.INFO, t.getNewValue());
				});
				imagesColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				imagesColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setImages(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.IMAGES, t.getNewValue());
				});
				productsColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				productsColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setProducts(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.PRODUCTS, t.getNewValue());
				});
				tagsColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				tagsColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setTags(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.TAGS, t.getNewValue());
				});
				notesColumn.setCellFactory(CellStringEditable.<Model>forTableColumn());
				notesColumn.setOnEditCommit((CellEditEvent<Model, String> t) -> {
					Model model = (Model) t.getTableView().getItems().get(t.getTablePosition().getRow());
					model.setNotes(t.getNewValue());
					modelHelper.updateString(model.getId(), ModelKeys.NOTES, t.getNewValue());
				});

				tableView.getColumns().addAll(orderColumn, nameColumn, iconColumn, iconPathColumn, brandColumn,
						categoryColumn, infoColumn, imagesColumn, productsColumn, tagsColumn, notesColumn);

				while (resultSet.next()) {
					Model model = modelHelper.getModel(resultSet);
					models.add(model);
				}
				tableView.setItems(models);
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
			return ModelKeys.ORDER;
		case 1:
			return ModelKeys.NAME;
		case 2:
			return ModelKeys.ICON;
		case 3:
			return ModelKeys.ICON_PATH;
		case 4:
			return ModelKeys.BRAND;
		case 5:
			return ModelKeys.CATEGORY;
		case 6:
			return ModelKeys.INFO;
		case 7:
			return ModelKeys.IMAGES;
		case 8:
			return ModelKeys.PRODUCTS;
		case 9:
			return ModelKeys.TAGS;
		case 10:
			return ModelKeys.NOTES;
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
		case ModelKeys.ORDER:
		case ModelKeys.NAME:
		case ModelKeys.TAGS:
		case ModelKeys.NOTES:
			textEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case ModelKeys.INFO:
			htmlEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case ModelKeys.ICON:
		case ModelKeys.ICON_PATH:
		case ModelKeys.IMAGES:
			imageEditor.setVisible(true);
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 80d);
			btApply.setVisible(true);
			break;
		case ModelKeys.BRAND:
		case ModelKeys.CATEGORY:
		case ModelKeys.PRODUCTS:
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 0d);
			btApply.setVisible(true);
			break;
		}
	}

	public void editEvent(MouseEvent event) {
		try {
			if (event.getClickCount() == 1) {
				TablePosition<Model, Object> position = tableView.getSelectionModel().getSelectedCells().get(0);
				if (position != null) {
					int column = position.getColumn(); // 0,1,2,3,4,5
					int row = position.getRow();

					TableColumn<Model, Object> tableColumn = position.getTableColumn();
					Model model = tableView.getItems().get(row);
					int modelId = model.getId();
					String field = maskToField(column);

					viewEditor(field);

					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;

					switch (field) {
					case ModelKeys.ORDER:
						int number = Integer.valueOf(tableColumn.getCellObservableValue(model).getValue().toString());
						textEditor.setText("" + number);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								int data = Integer.valueOf(textEditor.getText());
								model.setOrder(data);
								modelHelper.updateNumber(modelId, ModelKeys.ORDER, data);
							}
						});
						break;
					case ModelKeys.NAME:
					case ModelKeys.TAGS:
					case ModelKeys.NOTES:
						String text = (String) tableColumn.getCellObservableValue(model).getValue();
						textEditor.setText(text);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = textEditor.getText();
								model.setString(field, data);
								modelHelper.updateString(modelId, field, data);
							}
						});
						break;
					case ModelKeys.INFO:
						String html = (String) tableColumn.getCellObservableValue(model).getValue();
						htmlEditor.setHtmlText(html);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = htmlEditor.getHtmlText();
								model.setInfo(data);
								modelHelper.updateString(modelId, ModelKeys.INFO, data);
							}
						});
						break;
					case ModelKeys.ICON:
					case ModelKeys.ICON_PATH:
						rbSingle.setSelected(true);
						lbCreate.setVisible(false);
						tfPath.setText(session.getWorkspaceDir() + File.separator + model.getIconPath());

						ImageView icon = new ImageView(model.getIcon());
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
									model.setIcon(image);
									modelHelper.updateIcon(modelId, file);
									String iconPath = AppFunction.getRelativePath(tfPath.getText().trim(),
											session.getWorkspaceDir());
									model.setIconPath(iconPath);
									modelHelper.updateString(modelId, ModelKeys.ICON_PATH, iconPath);
								} else {
									File folder = new File(tfPath.getText());
									File[] files = folder
											.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
									for (int i = 0; i < models.size(); i++) {
										if (i < files.length) {
											Model model = models.get(i);
											Image image = AppFunction.getImageFromFile(files[i]);
											model.setIcon(image);
											modelHelper.updateIcon(model.getId(), files[i]);
											String iconPath = AppFunction.getRelativePath(files[i].getPath(),
													session.getWorkspaceDir());
											model.setIconPath(iconPath);
											modelHelper.updateString(model.getId(), ModelKeys.ICON_PATH, iconPath);
										}
									}
								}
							}
						});
						break;
					case ModelKeys.IMAGES:
						rbSingle.setSelected(true);
						lbCreate.setVisible(true);
						tfPath.setText(session.getWorkspaceDir() + File.separator + model.getImages());

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
								for (Model model : models) {
									AppFunction.createFolder(sourcePath, model.getOrder(), model.getName());
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
									model.setImages(images);
									modelHelper.updateString(modelId, ModelKeys.IMAGES, images);
								} else {
									File folder = new File(tfPath.getText());
									FileFilter dirFilter = new FileFilter() {
										@Override
										public boolean accept(File file) {
											return file.isDirectory();
										}
									};
									File[] subFolders = folder.listFiles(dirFilter);
									for (int i = 0; i < models.size(); i++) {
										if (i < subFolders.length) {
											Model model = models.get(i);
											String images = AppFunction.getRelativePath(subFolders[i].getPath(),
													session.getWorkspaceDir());
											model.setImages(images);
											modelHelper.updateString(model.getId(), ModelKeys.IMAGES, images);
										}
									}
								}
							}
						});
						break;
					case ModelKeys.BRAND:
						try {
							connection = dbHelper.createConnection();
							if (connection != null) {
								String query = "SELECT " + BrandKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_BRAND
										+ ";";
								statement = connection.createStatement();
								resultSet = statement.executeQuery(query);
								brands = new ArrayList<>();
								checkers = new ArrayList<>();
								if (resultSet != null) {
									while (resultSet.next()) {
										Brand brand = brandHelper.getBrand(resultSet);
										brands.add(brand);
										checkers.add(new StatusChecker(brand.getId(), false, false));
									}
								}

								final ToggleGroup group = new ToggleGroup();
								imageViewer.getChildren().clear();
								for (int i = 0; i < brands.size(); i++) {
									if (brands.get(i).getId() == model.getBrandId()) {
										VBox vbox = createBrandView(brands.get(i), group, true);
										imageViewer.getChildren().add(vbox);
										checkers.get(i).setValues(true, true);
										break;
									}
								}
								for (int i = 0; i < brands.size(); i++) {
									if (brands.get(i).getId() != model.getBrandId()) {
										VBox vbox = createBrandView(brands.get(i), group, false);
										imageViewer.getChildren().add(vbox);
									}
								}

								group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
									@Override
									public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
											Toggle newValue) {
										if (group.getSelectedToggle() != null) {
											int brandId = Integer
													.valueOf(group.getSelectedToggle().getUserData().toString());
											for (int i = 0; i < checkers.size(); i++) {
												if (checkers.get(i).getId() == brandId) {
													checkers.get(i).setLast(true);
												} else {
													checkers.get(i).setLast(false);
												}
											}
										}
									}
								});
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
								int oldId = 0, newId = 0;
								for (int i = 0; i < checkers.size(); i++) {
									if (checkers.get(i).getFirst()) {
										oldId = i;
										checkers.get(i).setFirst(false);
										break;
									}
								}
								for (int i = 0; i < checkers.size(); i++) {
									if (checkers.get(i).getLast()) {
										newId = i;
										checkers.get(i).setFirst(true);
										break;
									}
								}
								if (newId != oldId) {
									model.setBrand(brands.get(newId).getName());
									model.setBrandId(brands.get(newId).getId());
									modelHelper.updateString(modelId, ModelKeys.BRAND, brands.get(newId).getName());
									modelHelper.updateNumber(modelId, ModelKeys.BRAND_ID, brands.get(newId).getId());
								}
							}
						});
						break;
					case ModelKeys.CATEGORY:
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

								final ToggleGroup group = new ToggleGroup();
								imageViewer.getChildren().clear();
								for (int i = 0; i < categories.size(); i++) {
									if (categories.get(i).getId() == model.getCategoryId()) {
										VBox vbox = createCategoryView(categories.get(i), group, true);
										imageViewer.getChildren().add(vbox);
										checkers.get(i).setValues(true, true);
										break;
									}
								}
								for (int i = 0; i < categories.size(); i++) {
									if (categories.get(i).getId() != model.getCategoryId()) {
										VBox vbox = createCategoryView(categories.get(i), group, false);
										imageViewer.getChildren().add(vbox);
									}
								}

								group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
									@Override
									public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
											Toggle newValue) {
										if (group.getSelectedToggle() != null) {
											int categoryId = Integer
													.valueOf(group.getSelectedToggle().getUserData().toString());
											for (int i = 0; i < checkers.size(); i++) {
												if (checkers.get(i).getId() == categoryId) {
													checkers.get(i).setLast(true);
												} else {
													checkers.get(i).setLast(false);
												}
											}
										}
									}
								});
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
								int oldId = 0, newId = 0;
								for (int i = 0; i < checkers.size(); i++) {
									if (checkers.get(i).getFirst()) {
										oldId = i;
										checkers.get(i).setFirst(false);
										break;
									}
								}
								for (int i = 0; i < checkers.size(); i++) {
									if (checkers.get(i).getLast()) {
										newId = i;
										checkers.get(i).setFirst(true);
										break;
									}
								}
								if (newId != oldId) {
									model.setCategory(categories.get(newId).getName());
									model.setCategoryId(categories.get(newId).getId());
									modelHelper.updateString(modelId, ModelKeys.CATEGORY,
											categories.get(newId).getName());
									modelHelper.updateNumber(modelId, ModelKeys.CATEGORY_ID,
											categories.get(newId).getId());
								}
							}
						});
						break;
					case ModelKeys.PRODUCTS:
						try {
							connection = dbHelper.createConnection();
							if (connection != null) {
								String query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM "
										+ DatabaseHelper.TB_PRODUCT + " WHERE " + ProductKeys.BRAND_ID + "="
										+ model.getBrandId() + " AND " + ProductKeys.CATEGORY_ID + "="
										+ model.getCategoryId() + ";";
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
									// // Add an item
									// if (!checkers.get(i).getFirst() &&
									// checkers.get(i).getLast()) {
									// Product product = products.get(i);
									// productHelper.updateNumber(product.getId(),
									// ProductKeys.BRAND_ID, brandId);
									// checkers.get(i).setFirst(true);
									// } // remove an item
									// else if (checkers.get(i).getFirst() &&
									// !checkers.get(i).getLast()) {
									// Product product = products.get(i);
									// productHelper.updateNumber(product.getId(),
									// ProductKeys.BRAND_ID, 0);
									// checkers.get(i).setFirst(false);
									// }
									if (checkers.get(i).getLast()) {
										sProducts += products.get(i).getName() + "; ";
									}
								}
								model.setProducts(sProducts);
								modelHelper.updateString(modelId, ModelKeys.PRODUCTS, sProducts);
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

	public VBox createBrandView(Brand brand, final ToggleGroup group, boolean selected) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #00ff00; ");

		Button button = new Button();
		ImageView imageView = new ImageView(brand.getIcon());
		imageView.setFitWidth(132);
		imageView.setFitHeight(120);
		button.setGraphic(imageView);
		button.setPadding(new Insets(5));

		RadioButton radio = new RadioButton(" " + brand.getName());
		radio.setToggleGroup(group);
		radio.setSelected(selected);
		radio.setUserData("" + brand.getId());

		vbox.getChildren().addAll(button, radio);

		return vbox;
	}

	public VBox createCategoryView(Category category, final ToggleGroup group, boolean selected) {
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color: #00ff00; ");

		Button button = new Button();
		ImageView imageView = new ImageView(category.getIcon());
		imageView.setFitWidth(132);
		imageView.setFitHeight(120);
		button.setGraphic(imageView);
		button.setPadding(new Insets(5));

		RadioButton radio = new RadioButton(" " + category.getName());
		radio.setToggleGroup(group);
		radio.setSelected(selected);
		radio.setUserData("" + category.getId());

		vbox.getChildren().addAll(button, radio);

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
