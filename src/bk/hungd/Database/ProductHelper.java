package bk.hungd.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.Model.Product;
import bk.hungd.Model.ProductKeys;
import javafx.scene.image.Image;

public class ProductHelper extends DatabaseHelper {

	public ProductHelper(String databasePath) {
		super(databasePath);
	}

	public void addProduct(Product product, int brandId, int categoryId) {
		String query = "INSERT INTO " + TB_PRODUCT + "(" + ProductKeys.COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(ProductKeys.ORDER_INDEX, product.getOrder());
			statement.setString(ProductKeys.NAME_INDEX, product.getName());
			statement.setString(ProductKeys.CODE_INDEX, product.getCode());
			statement.setBytes(ProductKeys.ICON_INDEX, AppFunction.getBytesFromImage(product.getIcon()));
			statement.setString(ProductKeys.ICON_PATH_INDEX, product.getIconPath());
			statement.setString(ProductKeys.BRAND_INDEX, product.getBrand());
			statement.setString(ProductKeys.CATEGORY_INDEX, product.getCategory());
			statement.setString(ProductKeys.INFO_INDEX, product.getInfo());
			statement.setString(ProductKeys.IMAGES_INDEX, product.getImages());
			statement.setString(ProductKeys.SPECS_INDEX, product.getSpecs());
			statement.setDouble(ProductKeys.PRICE_INDEX, product.getPrice());
			statement.setInt(ProductKeys.QUANTITY_INDEX, product.getQuantity());
			statement.setString(ProductKeys.STATUS_INDEX, product.getStatus());
			statement.setString(ProductKeys.TAGS_INDEX, product.getTags());
			statement.setString(ProductKeys.NOTES_INDEX, product.getNotes());
			statement.setInt(ProductKeys.BRAND_ID_INDEX, brandId);
			statement.setInt(ProductKeys.CATEGORY_ID_INDEX, categoryId);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void addProducts(ArrayList<Product> products, int brandId, int categoryId) {
		String query = "INSERT INTO " + TB_PRODUCT + "(" + ProductKeys.COLUMNS
				+ ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			for (int i = 0; i < products.size(); i++) {
				statement.setInt(ProductKeys.ORDER_INDEX, products.get(i).getOrder());
				statement.setString(ProductKeys.NAME_INDEX, products.get(i).getName());
				statement.setString(ProductKeys.CODE_INDEX, products.get(i).getCode());
				statement.setBytes(ProductKeys.ICON_INDEX, AppFunction.getBytesFromImage(products.get(i).getIcon()));
				statement.setString(ProductKeys.ICON_PATH_INDEX, products.get(i).getIconPath());
				statement.setString(ProductKeys.BRAND_INDEX, products.get(i).getBrand());
				statement.setString(ProductKeys.CATEGORY_INDEX, products.get(i).getCategory());
				statement.setString(ProductKeys.INFO_INDEX, products.get(i).getInfo());
				statement.setString(ProductKeys.IMAGES_INDEX, products.get(i).getImages());
				statement.setString(ProductKeys.SPECS_INDEX, products.get(i).getSpecs());
				statement.setDouble(ProductKeys.PRICE_INDEX, products.get(i).getPrice());
				statement.setInt(ProductKeys.QUANTITY_INDEX, products.get(i).getQuantity());
				statement.setString(ProductKeys.STATUS_INDEX, products.get(i).getStatus());
				statement.setString(ProductKeys.TAGS_INDEX, products.get(i).getTags());
				statement.setString(ProductKeys.NOTES_INDEX, products.get(i).getNotes());
				statement.setInt(ProductKeys.BRAND_ID_INDEX, brandId);
				statement.setInt(ProductKeys.CATEGORY_ID_INDEX, categoryId);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateIcon(int productId, File file) {
		String query = "UPDATE " + TB_PRODUCT + " SET " + ProductKeys.ICON + "=? WHERE " + ProductKeys.ID + " = ?"
				+ ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.setBytes(1, AppFunction.getBytesFromImageFile(file));
				statement.setInt(2, productId);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateString(int productId, String columnName, String value) {
		String query = "UPDATE " + TB_PRODUCT + " SET " + columnName + "=\"" + value + "\" WHERE " + ProductKeys.ID
				+ "=" + productId + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateNumber(int productId, String columnName, int value) {
		String query = "UPDATE " + TB_PRODUCT + " SET " + columnName + "=" + value + " WHERE " + ProductKeys.ID + "="
				+ productId + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}
	
	public void updateNumber(int productId, String columnName, double value) {
		String query = "UPDATE " + TB_PRODUCT + " SET " + columnName + "=" + value + " WHERE " + ProductKeys.ID + "="
				+ productId + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public Product getProduct(ResultSet resultSet) {
		Product product = new Product();
		try {
			product.setId(resultSet.getInt(ProductKeys.ID));
			product.setOrder(resultSet.getInt(ProductKeys.ORDER));
			product.setName(resultSet.getString(ProductKeys.NAME));
			product.setCode(resultSet.getString(ProductKeys.CODE));
			Image image = null;
			try{
				image = new Image(resultSet.getBinaryStream(ProductKeys.ICON));
			}catch (Exception e) {
				AppDebug.error(e, "");
			}
			product.setIcon(image);
			product.setIconPath(resultSet.getString(ProductKeys.ICON_PATH));
			product.setBrand(resultSet.getString(ProductKeys.BRAND));
			product.setCategory(resultSet.getString(ProductKeys.CATEGORY));
			product.setInfo(resultSet.getString(ProductKeys.INFO));
			product.setImages(resultSet.getString(ProductKeys.IMAGES));
			product.setSpecs(resultSet.getString(ProductKeys.SPECS));
			product.setPrice(resultSet.getDouble(ProductKeys.PRICE));
			product.setQuantity(resultSet.getInt(ProductKeys.QUANTITY));
			product.setStatus(resultSet.getString(ProductKeys.STATUS));
			product.setTags(resultSet.getString(ProductKeys.TAGS));
			product.setNotes(resultSet.getString(ProductKeys.NOTES));
			product.setBrandId(resultSet.getInt(ProductKeys.BRAND_ID));
			product.setCategoryId(resultSet.getInt(ProductKeys.CATEGORY_ID));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		}
		return product;
	}

	public int getMaxOrder(){
		int maxOrder = -1;
		String query = "SELECT " + ProductKeys.ORDER + " FROM " + DatabaseHelper.TB_PRODUCT + " WHERE " + ProductKeys.ORDER
				+ "=(SELECT MAX(" + ProductKeys.ORDER + ") FROM " + DatabaseHelper.TB_PRODUCT + ");";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					maxOrder = resultSet.getInt(ProductKeys.ORDER);
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
