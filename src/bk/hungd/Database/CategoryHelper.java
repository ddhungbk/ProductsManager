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
import bk.hungd.Model.Category;
import bk.hungd.Model.CategoryKeys;
import javafx.scene.image.Image;

public class CategoryHelper extends DatabaseHelper {

	public CategoryHelper(String databasePath) {
		super(databasePath);
	}

	public void addCategory(Category category) {
		String query = "INSERT INTO " + TB_CATEGORY + "(" + CategoryKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			statement.setInt(CategoryKeys.ORDER_INDEX, category.getOrder());
			statement.setString(CategoryKeys.NAME_INDEX, category.getName());
			statement.setBytes(CategoryKeys.ICON_INDEX, AppFunction.getBytesFromImage(category.getIcon()));
			statement.setString(CategoryKeys.ICON_PATH_INDEX, category.getIconPath());
			statement.setString(CategoryKeys.INFO_INDEX, category.getInfo());
			statement.setString(CategoryKeys.IMAGES_INDEX, category.getImages());
			statement.setString(CategoryKeys.BRANDS_INDEX, category.getBrands());
			statement.setString(CategoryKeys.PRODUCTS_INDEX, category.getProducts());
			statement.setString(CategoryKeys.TAGS_INDEX, category.getTags());
			statement.setString(CategoryKeys.NOTES_INDEX, category.getNotes());
			statement.executeUpdate();
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void addCategories(ArrayList<Category> categories) {
		String query = "INSERT INTO " + TB_CATEGORY + "(" + CategoryKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			statement = connection.prepareStatement(query);
			for (int i = 0; i < categories.size(); i++) {
				statement.setInt(CategoryKeys.ORDER_INDEX, categories.get(i).getOrder());
				statement.setString(CategoryKeys.NAME_INDEX, categories.get(i).getName());
				statement.setBytes(CategoryKeys.ICON_INDEX, AppFunction.getBytesFromImage(categories.get(i).getIcon()));
				statement.setString(CategoryKeys.ICON_PATH_INDEX, categories.get(i).getIconPath());
				statement.setString(CategoryKeys.INFO_INDEX, categories.get(i).getInfo());
				statement.setString(CategoryKeys.IMAGES_INDEX, categories.get(i).getImages());
				statement.setString(CategoryKeys.BRANDS_INDEX, categories.get(i).getBrands());
				statement.setString(CategoryKeys.PRODUCTS_INDEX, categories.get(i).getProducts());
				statement.setString(CategoryKeys.TAGS_INDEX, categories.get(i).getTags());
				statement.setString(CategoryKeys.NOTES_INDEX, categories.get(i).getNotes());
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateIcon(int categoryId, File file) {
		String query = "UPDATE " + TB_CATEGORY + " SET " + CategoryKeys.ICON + "=? WHERE "
				+ CategoryKeys.ID + " = ?" + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.setBytes(1, AppFunction.getBytesFromImageFile(file));
				statement.setInt(2, categoryId);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}
	
	public void updateString(int categoryId, String columnName, String value) {
		String query = "UPDATE " + TB_CATEGORY + " SET " + columnName + "=\"" + value + "\" WHERE " + CategoryKeys.ID
				+ "=" + categoryId + ";";
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

	public void updateNumber(int categoryId, String columnName, int value) {
		String query = "UPDATE " + TB_CATEGORY + " SET " + columnName + "=" + value + " WHERE " + CategoryKeys.ID
				+ "=" + categoryId + ";";
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

	public Category getCategory(ResultSet resultSet) {
		Category category = new Category();
		try {
			category.setId(resultSet.getInt(CategoryKeys.ID));
			category.setOrder(resultSet.getInt(CategoryKeys.ORDER));
			category.setName(resultSet.getString(CategoryKeys.NAME));
			Image image = null;
			try{
				image = new Image(resultSet.getBinaryStream(CategoryKeys.ICON));
			}catch (Exception e) {
				AppDebug.error(e, "");
			}
			category.setIcon(image);
			category.setIconPath(resultSet.getString(CategoryKeys.ICON_PATH));
			category.setInfo(resultSet.getString(CategoryKeys.INFO));
			category.setImages(resultSet.getString(CategoryKeys.IMAGES));
			category.setBrands(resultSet.getString(CategoryKeys.BRANDS));
			category.setProducts(resultSet.getString(CategoryKeys.PRODUCTS));
			category.setTags(resultSet.getString(CategoryKeys.TAGS));
			category.setNotes(resultSet.getString(CategoryKeys.NOTES));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		}
		return category;
	}

	public int getMaxOrder(){
		int maxOrder = -1;
		String query = "SELECT " + CategoryKeys.ORDER + " FROM " + DatabaseHelper.TB_CATEGORY + " WHERE " + CategoryKeys.ORDER
				+ "=(SELECT MAX(" + CategoryKeys.ORDER + ") FROM " + DatabaseHelper.TB_CATEGORY + ");";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					maxOrder = resultSet.getInt(CategoryKeys.ORDER);
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
