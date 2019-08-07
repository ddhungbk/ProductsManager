package bk.hungd.Database;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import bk.hungd.App.AppDebug;
import bk.hungd.App.AppFunction;
import bk.hungd.Model.CategoryKeys;
import bk.hungd.Model.Model;
import bk.hungd.Model.ModelKeys;
import javafx.scene.image.Image;

public class ModelHelper extends DatabaseHelper {

	public ModelHelper(String databasePath) {
		super(databasePath);
	}

	public void addModel(Model model, int brandId, int catrgoryId) {
		String query = "INSERT INTO " + TB_MODEL + "(" + ModelKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(ModelKeys.ORDER_INDEX, model.getOrder());
			statement.setString(ModelKeys.NAME_INDEX, model.getName());
			statement.setBytes(ModelKeys.ICON_INDEX, AppFunction.getBytesFromImage(model.getIcon()));
			statement.setString(ModelKeys.ICON_PATH_INDEX, model.getIconPath());
			statement.setString(ModelKeys.BRAND_INDEX, model.getBrand());
			statement.setString(ModelKeys.CATEGORY_INDEX, model.getCategory());
			statement.setString(ModelKeys.INFO_INDEX, model.getInfo());
			statement.setString(ModelKeys.IMAGES_INDEX, model.getImages());
			statement.setString(ModelKeys.PRODUCTS_INDEX, model.getProducts());
			statement.setString(ModelKeys.TAGS_INDEX, model.getTags());
			statement.setString(ModelKeys.NOTES_INDEX, model.getNotes());
			statement.setInt(ModelKeys.BRAND_ID_INDEX, brandId);
			statement.setInt(ModelKeys.CATEGORY_ID_INDEX, catrgoryId);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateIcon(int modelId, File file) {
		String query = "UPDATE " + TB_MODEL + " SET " + ModelKeys.ICON + "=? WHERE "
				+ ModelKeys.ID + " = ?" + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.setBytes(1, AppFunction.getBytesFromImageFile(file));
			statement.setInt(2, modelId);
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateString(int modelId, String columnName, String value) {
		String query = "UPDATE " + TB_MODEL + " SET " + columnName + "=\"" + value + "\" WHERE " + ModelKeys.ID + "="
				+ modelId + ";";
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

	public void updateNumber(int modelId, String columnName, int value) {
		String query = "UPDATE " + TB_MODEL + " SET " + columnName + "=" + value + " WHERE " + ModelKeys.ID + "="
				+ modelId + ";";
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
	
	public Model getModel(ResultSet resultSet) {
		Model model = new Model();
		try {
			model.setId(resultSet.getInt(ModelKeys.ID));
			model.setOrder(resultSet.getInt(ModelKeys.ORDER));
			model.setName(resultSet.getString(ModelKeys.NAME));
			Image image = null;
			try{
				image = new Image(resultSet.getBinaryStream(ModelKeys.ICON));
			}catch (Exception e) {
				AppDebug.error(e, "");
			}
			model.setIcon(image);
			model.setIconPath(resultSet.getString(ModelKeys.ICON_PATH));
			model.setBrand(resultSet.getString(ModelKeys.BRAND));
			model.setCategory(resultSet.getString(ModelKeys.CATEGORY));
			model.setInfo(resultSet.getString(ModelKeys.INFO));
			model.setImages(resultSet.getString(ModelKeys.IMAGES));
			model.setProducts(resultSet.getString(ModelKeys.PRODUCTS));
			model.setTags(resultSet.getString(ModelKeys.TAGS));
			model.setNotes(resultSet.getString(ModelKeys.NOTES));

			model.setBrandId(resultSet.getInt(ModelKeys.BRAND_ID));
			model.setCategoryId(resultSet.getInt(ModelKeys.CATEGORY_ID));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		}
		return model;
	}

	public void removeModel(int brandId, int categoryId) {
		String query = "DELETE FROM " + DatabaseHelper.TB_MODEL + " WHERE " + ModelKeys.BRAND_ID + "=" + brandId
				+ " AND " + ModelKeys.CATEGORY_ID + "=" + categoryId + ";";
		Connection connection = null;
		Statement statement = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public int getMaxOrder() {
		int maxOrder = -1;
		String query = "SELECT " + ModelKeys.ORDER + " FROM " + DatabaseHelper.TB_MODEL + " WHERE " + ModelKeys.ORDER
				+ "=(SELECT MAX(" + ModelKeys.ORDER + ") FROM " + DatabaseHelper.TB_MODEL + ");";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					maxOrder = resultSet.getInt(ModelKeys.ORDER);
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
