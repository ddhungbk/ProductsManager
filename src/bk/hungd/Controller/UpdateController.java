package bk.hungd.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppSession;
import bk.hungd.Database.BrandHelper;
import bk.hungd.Database.CategoryHelper;
import bk.hungd.Database.DatabaseHelper;
import bk.hungd.Database.ModelHelper;
import bk.hungd.Database.ProductHelper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class UpdateController implements Initializable {

	@FXML
	public TabPane tabContents;
	public Tab tabBrands, tabCategories, tabModels, tabProducts;
	public Button btRefresh;

	private AppSession session;

	private DatabaseHelper dbHelper;
	private BrandHelper brandHelper;
	private CategoryHelper categoryHelper;
	private ModelHelper modelHelper;
	private ProductHelper productHelper;

	private UpdateBrands updateBrands;
	private UpdateCategories updateCategories;
	private UpdateModels updateModels;
	private UpdateProducts updateProducts;
	private boolean firstBrand, firstCategory, firstModel, firstProduct;

	public UpdateController() {
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void initSession(final MainController mainController, AppSession session) {
		this.session = session;

		dbHelper = new DatabaseHelper(session.getDatabasePath());
		brandHelper = new BrandHelper(session.getDatabasePath());
		categoryHelper = new CategoryHelper(session.getDatabasePath());
		modelHelper = new ModelHelper(session.getDatabasePath());
		productHelper = new ProductHelper(session.getDatabasePath());

		firstBrand = false;
		firstCategory = false;
		firstModel = false;
		firstProduct = false;

		if (firstBrand == false) {
			firstBrand = true;
			openBrandEditor();
		}

		tabContents.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				if (newTab.equals(tabBrands)) {
					if (firstBrand == false) {
						firstBrand = true;
						openBrandEditor();
					}
				} else if (newTab.equals(tabCategories)) {
					if (firstCategory == false) {
						firstCategory = true;
						openCategoryEditor();
					}
				} else if (newTab.equals(tabModels)) {
					if (firstModel == false) {
						firstModel = true;
						openModelEditor();
					}
				} else if (newTab.equals(tabProducts)) {
					if (firstProduct == false) {
						firstProduct = true;
						openProductEditor();
					}
				}
			}
		});

		btRefresh.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Tab selectedTab = tabContents.getSelectionModel().getSelectedItem();
				if (selectedTab.equals(tabBrands)) {
					updateBrands.loadDatabase();
				}
				if (selectedTab.equals(tabCategories)) {
					updateCategories.loadDatabase();
				}
				if (selectedTab.equals(tabModels)) {
					updateModels.loadDatabase();
				}
				if (selectedTab.equals(tabProducts)) {
					updateProducts.loadDatabase();
				}
			}
		});
	}

	private void openBrandEditor() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/UpdateBrand.fxml"));
			Node node = (AnchorPane) loader.load();
			updateBrands = loader.<UpdateBrands>getController();
			updateBrands.initSession(UpdateController.this, session);

			AnchorPane.setLeftAnchor(node, 0d);
			AnchorPane.setRightAnchor(node, 0d);
			AnchorPane.setTopAnchor(node, 0d);
			AnchorPane.setBottomAnchor(node, 0d);

			tabBrands.setContent(node);
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	private void openCategoryEditor() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/UpdateCategory.fxml"));
			Node node = (AnchorPane) loader.load();
			updateCategories = loader.<UpdateCategories>getController();
			updateCategories.initSession(UpdateController.this, session);

			AnchorPane.setLeftAnchor(node, 0d);
			AnchorPane.setRightAnchor(node, 0d);
			AnchorPane.setTopAnchor(node, 0d);
			AnchorPane.setBottomAnchor(node, 0d);

			tabCategories.setContent(node);
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	private void openModelEditor() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/UpdateModel.fxml"));
			Node node = (AnchorPane) loader.load();
			updateModels = loader.<UpdateModels>getController();
			updateModels.initSession(UpdateController.this, session);

			AnchorPane.setLeftAnchor(node, 0d);
			AnchorPane.setRightAnchor(node, 0d);
			AnchorPane.setTopAnchor(node, 0d);
			AnchorPane.setBottomAnchor(node, 0d);

			tabModels.setContent(node);
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

	private void openProductEditor() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bk/hungd/Layout/UpdateProduct.fxml"));
			Node node = (AnchorPane) loader.load();
			updateProducts = loader.<UpdateProducts>getController();
			updateProducts.initSession(UpdateController.this, session);

			AnchorPane.setLeftAnchor(node, 0d);
			AnchorPane.setRightAnchor(node, 0d);
			AnchorPane.setTopAnchor(node, 0d);
			AnchorPane.setBottomAnchor(node, 0d);

			tabProducts.setContent(node);
		} catch (Exception e) {
			AppDebug.error(e, "");
		}
	}

}
