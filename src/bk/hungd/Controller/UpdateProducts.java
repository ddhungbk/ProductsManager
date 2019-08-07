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
import bk.hungd.Extra.CellDoubleEditable;
import bk.hungd.Extra.CellIntegerEditable;
import bk.hungd.Extra.CellStringEditable;
import bk.hungd.Extra.ExtensionsFilter;
import bk.hungd.Extra.StatusChecker;
import bk.hungd.Model.Brand;
import bk.hungd.Model.BrandKeys;
import bk.hungd.Model.Category;
import bk.hungd.Model.CategoryKeys;
import bk.hungd.Model.Model;
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

public class UpdateProducts implements Initializable {

	@FXML
	public TableView<Product> tableView;
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
	private ArrayList<Model> models;
	private ObservableList<Product> products;
	private ArrayList<StatusChecker> checkers;

	public UpdateProducts() {

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
			System.out.println("Start connection: View all contents of products");
			connection = dbHelper.createConnection();

			tableView.getColumns().clear();
			tableView.setEditable(true);
			products = FXCollections.observableArrayList();
			query = "SELECT " + ProductKeys.ALL_COLUMNS + " FROM " + DatabaseHelper.TB_PRODUCT + ";";
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				TableColumn<Product, Integer> orderColumn = new TableColumn<Product, Integer>("Order");
				TableColumn<Product, String> nameColumn = new TableColumn<Product, String>("Name");
				TableColumn<Product, String> codeColumn = new TableColumn<Product, String>("Code");
				TableColumn<Product, Image> iconColumn = new TableColumn<Product, Image>("Icon");
				TableColumn<Product, String> iconPathColumn = new TableColumn<Product, String>("Icon Path");
				TableColumn<Product, String> brandColumn = new TableColumn<Product, String>("Brand");
				TableColumn<Product, String> categoryColumn = new TableColumn<Product, String>("Category");
				TableColumn<Product, String> infoColumn = new TableColumn<Product, String>("Information");
				TableColumn<Product, String> imagesColumn = new TableColumn<Product, String>("Images");
				TableColumn<Product, String> specsColumn = new TableColumn<Product, String>("Specification");
				TableColumn<Product, Double> priceColumn = new TableColumn<Product, Double>("Price");
				TableColumn<Product, Integer> quantityColumn = new TableColumn<Product, Integer>("Quantity");
				TableColumn<Product, String> statusColumn = new TableColumn<Product, String>("Status");
				TableColumn<Product, String> tagsColumn = new TableColumn<Product, String>("Tags");
				TableColumn<Product, String> notesColumn = new TableColumn<Product, String>("Notes");

				orderColumn.setCellValueFactory(cellData -> cellData.getValue().orderProperty().asObject());
				nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
				codeColumn.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
				iconColumn.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
				iconPathColumn.setCellValueFactory(cellData -> cellData.getValue().iconPathProperty());
				brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
				categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
				infoColumn.setCellValueFactory(cellData -> cellData.getValue().infoProperty());
				imagesColumn.setCellValueFactory(cellData -> cellData.getValue().imagesProperty());
				specsColumn.setCellValueFactory(cellData -> cellData.getValue().specsProperty());
				priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
				quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
				statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
				tagsColumn.setCellValueFactory(cellData -> cellData.getValue().tagsProperty());
				notesColumn.setCellValueFactory(cellData -> cellData.getValue().notesProperty());

				orderColumn.setCellFactory(CellIntegerEditable.<Product>forTableColumn());
				orderColumn.setOnEditCommit((CellEditEvent<Product, Integer> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setOrder(t.getNewValue());
					productHelper.updateNumber(product.getId(), ProductKeys.ORDER, t.getNewValue());
				});
				nameColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				nameColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setName(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.NAME, t.getNewValue());
				});
				codeColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				codeColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setCode(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.CODE, t.getNewValue());
				});
				iconColumn.setCellFactory(new Callback<TableColumn<Product, Image>, TableCell<Product, Image>>() {
					@Override
					public TableCell<Product, Image> call(TableColumn<Product, Image> param) {
						return new TableCell<Product, Image>() {
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
				iconPathColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				iconPathColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setIconPath(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.ICON_PATH, t.getNewValue());
				});
				brandColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				brandColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setBrand(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.BRAND, t.getNewValue());
				});
				categoryColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				categoryColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setCategory(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.CATEGORY, t.getNewValue());
				});
				infoColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				infoColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setInfo(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.INFO, t.getNewValue());
				});
				imagesColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				imagesColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setImages(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.IMAGES, t.getNewValue());
				});
				specsColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				specsColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setSpecs(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.SPECS, t.getNewValue());
				});
				priceColumn.setCellFactory(CellDoubleEditable.<Product>forTableColumn());
				priceColumn.setOnEditCommit((CellEditEvent<Product, Double> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setPrice(t.getNewValue());
					productHelper.updateNumber(product.getId(), ProductKeys.PRICE, t.getNewValue());
				});
				quantityColumn.setCellFactory(CellIntegerEditable.<Product>forTableColumn());
				quantityColumn.setOnEditCommit((CellEditEvent<Product, Integer> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setQuantity(t.getNewValue());
					productHelper.updateNumber(product.getId(), ProductKeys.QUANTITY, t.getNewValue());
				});
				statusColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				statusColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setStatus(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.STATUS, t.getNewValue());
				});
				tagsColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				tagsColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setTags(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.TAGS, t.getNewValue());
				});
				notesColumn.setCellFactory(CellStringEditable.<Product>forTableColumn());
				notesColumn.setOnEditCommit((CellEditEvent<Product, String> t) -> {
					Product product = (Product) t.getTableView().getItems().get(t.getTablePosition().getRow());
					product.setNotes(t.getNewValue());
					productHelper.updateString(product.getId(), ProductKeys.NOTES, t.getNewValue());
				});

				tableView.getColumns().addAll(orderColumn, nameColumn, codeColumn, iconColumn, iconPathColumn,
						brandColumn, categoryColumn, infoColumn, imagesColumn, specsColumn, priceColumn, quantityColumn,
						statusColumn, tagsColumn, notesColumn);

				while (resultSet.next()) {
					Product product = productHelper.getProduct(resultSet);
					products.add(product);
				}
				tableView.setItems(products);
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
			return ProductKeys.ORDER;
		case 1:
			return ProductKeys.NAME;
		case 2:
			return ProductKeys.CODE;
		case 3:
			return ProductKeys.ICON;
		case 4:
			return ProductKeys.ICON_PATH;
		case 5:
			return ProductKeys.BRAND;
		case 6:
			return ProductKeys.CATEGORY;
		case 7:
			return ProductKeys.INFO;
		case 8:
			return ProductKeys.IMAGES;
		case 9:
			return ProductKeys.SPECS;
		case 10:
			return ProductKeys.PRICE;
		case 11:
			return ProductKeys.QUANTITY;
		case 12:
			return ProductKeys.STATUS;
		case 13:
			return ProductKeys.TAGS;
		case 14:
			return ProductKeys.NOTES;
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
		case ProductKeys.ORDER:
		case ProductKeys.NAME:
		case ProductKeys.CODE:
		case ProductKeys.SPECS:
		case ProductKeys.PRICE:
		case ProductKeys.QUANTITY:
		case ProductKeys.STATUS:
		case ProductKeys.NOTES:
			textEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case ProductKeys.INFO:
			htmlEditor.setVisible(true);
			btApply.setVisible(true);
			break;
		case ProductKeys.ICON:
		case ProductKeys.ICON_PATH:
		case ProductKeys.IMAGES:
			imageEditor.setVisible(true);
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 80d);
			btApply.setVisible(true);
			break;
		case ProductKeys.BRAND:
		case ProductKeys.CATEGORY:
			gridEditor.setVisible(true);
			AnchorPane.setTopAnchor(gridEditor, 0d);
			btApply.setVisible(true);
			break;
		}
	}

	public void editEvent(MouseEvent event) {
		try {
			if (event.getClickCount() == 1) {
				TablePosition<Product, Object> position = tableView.getSelectionModel().getSelectedCells().get(0);
				if (position != null) {
					int column = position.getColumn(); // 0,1,2,3,4,5
					int row = position.getRow();
					TableColumn<Product, Object> tableColumn = position.getTableColumn();
					Product product = tableView.getItems().get(row);
					int productId = product.getId();
					String field = maskToField(column);

					viewEditor(field);

					Connection connection = null;
					Statement statement = null;
					ResultSet resultSet = null;

					switch (field) {
					case ProductKeys.ORDER:
					case ProductKeys.QUANTITY:
						int intValue = Integer
								.valueOf(tableColumn.getCellObservableValue(product).getValue().toString());
						textEditor.setText("" + intValue);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								int data = Integer.valueOf(textEditor.getText());
								product.setNumber(field, data);
								productHelper.updateNumber(productId, field, data);
							}
						});
						break;
					case ProductKeys.PRICE:
						double realValue = Double
								.valueOf(tableColumn.getCellObservableValue(product).getValue().toString());
						textEditor.setText("" + realValue);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								double data = Double.valueOf(textEditor.getText());
								product.setPrice(data);
								productHelper.updateNumber(productId, ProductKeys.PRICE, data);
							}
						});
						break;
					case ProductKeys.NAME:
					case ProductKeys.CODE:
					case ProductKeys.SPECS:
					case ProductKeys.STATUS:
					case ProductKeys.TAGS:
					case ProductKeys.NOTES:
						String text = (String) tableColumn.getCellObservableValue(product).getValue();
						textEditor.setText(text);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = textEditor.getText();
								product.setString(field, data);
								productHelper.updateString(productId, field, data);
							}
						});
						break;
					case ProductKeys.INFO:
						String html = (String) tableColumn.getCellObservableValue(product).getValue();
						htmlEditor.setHtmlText(html);
						btApply.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent arg0) {
								String data = htmlEditor.getHtmlText();
								product.setInfo(data);
								productHelper.updateString(productId, ProductKeys.INFO, data);
							}
						});
						break;
					case ProductKeys.ICON:
					case ProductKeys.ICON_PATH:
						rbSingle.setSelected(true);
						lbCreate.setVisible(false);
						tfPath.setText(session.getWorkspaceDir() + File.separator + product.getIconPath());

						ImageView icon = new ImageView(product.getIcon());
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
									product.setIcon(image);
									productHelper.updateIcon(productId, file);
									String iconPath = AppFunction.getRelativePath(tfPath.getText().trim(),
											session.getWorkspaceDir());
									product.setIconPath(iconPath);
									productHelper.updateString(productId, ProductKeys.ICON_PATH, iconPath);
								} else {
									File folder = new File(tfPath.getText());
									File[] files = folder
											.listFiles(new ExtensionsFilter(new String[] { "jpg", "png", "gif" }));
									for (int i = 0; i < products.size(); i++) {
										if (i < files.length) {
											Product product = products.get(i);
											Image image = AppFunction.getImageFromFile(files[i]);
											product.setIcon(image);
											productHelper.updateIcon(product.getId(), files[i]);
											String iconPath = AppFunction.getRelativePath(files[i].getPath(),
													session.getWorkspaceDir());
											product.setIconPath(iconPath);
											productHelper.updateString(product.getId(), ProductKeys.ICON_PATH,
													iconPath);
										}
									}
								}
							}
						});
						break;
					case ProductKeys.IMAGES:
						rbSingle.setSelected(true);
						lbCreate.setVisible(true);
						tfPath.setText(session.getWorkspaceDir() + File.separator + product.getImages());

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
								for (Product product : products) {
									AppFunction.createFolder(sourcePath, product.getOrder(), product.getName());
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
									product.setImages(images);
									productHelper.updateString(productId, ProductKeys.IMAGES, images);
								} else {
									File folder = new File(tfPath.getText());
									FileFilter dirFilter = new FileFilter() {
										@Override
										public boolean accept(File file) {
											return file.isDirectory();
										}
									};
									File[] subFolders = folder.listFiles(dirFilter);
									for (int i = 0; i < products.size(); i++) {
										if (i < subFolders.length) {
											Product product = products.get(i);
											String images = AppFunction.getRelativePath(subFolders[i].getPath(),
													session.getWorkspaceDir());
											product.setImages(images);
											productHelper.updateString(product.getId(), ProductKeys.IMAGES, images);
										}
									}
								}
							}
						});
						break;
					case ProductKeys.BRAND:
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
									if (brands.get(i).getId() == product.getBrandId()) {
										VBox vbox = createBrandView(brands.get(i), group, true);
										imageViewer.getChildren().add(vbox);
										checkers.get(i).setValues(true, true);
										break;
									}
								}
								for (int i = 0; i < brands.size(); i++) {
									if (brands.get(i).getId() != product.getBrandId()) {
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
									product.setBrand(brands.get(newId).getName());
									product.setBrandId(brands.get(newId).getId());
									productHelper.updateString(productId, ProductKeys.BRAND,
											brands.get(newId).getName());
									productHelper.updateNumber(productId, ProductKeys.BRAND_ID,
											brands.get(newId).getId());
								}
							}
						});
						break;
					case ProductKeys.CATEGORY:
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
									if (categories.get(i).getId() == product.getCategoryId()) {
										VBox vbox = createCategoryView(categories.get(i), group, true);
										imageViewer.getChildren().add(vbox);
										checkers.get(i).setValues(true, true);
										break;
									}
								}
								for (int i = 0; i < categories.size(); i++) {
									if (categories.get(i).getId() != product.getCategoryId()) {
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
									product.setCategory(categories.get(newId).getName());
									product.setCategoryId(categories.get(newId).getId());
									productHelper.updateString(productId, ProductKeys.CATEGORY,
											categories.get(newId).getName());
									productHelper.updateNumber(productId, ProductKeys.CATEGORY_ID,
											categories.get(newId).getId());
								}
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
}
