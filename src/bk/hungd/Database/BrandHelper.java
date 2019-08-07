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
import bk.hungd.Model.Brand;
import bk.hungd.Model.BrandKeys;
import javafx.scene.image.Image;

public class BrandHelper extends DatabaseHelper {

	public BrandHelper(String databasePath) {
		super(databasePath);
	}

	public void addBrand(Brand brand) {
		String query = "INSERT INTO " + TB_BRAND + "(" + BrandKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.setInt(BrandKeys.ORDER_INDEX, brand.getOrder());
				statement.setString(BrandKeys.NAME_INDEX, brand.getName());
				statement.setBytes(BrandKeys.ICON_INDEX, AppFunction.getBytesFromImage(brand.getIcon()));
				statement.setString(BrandKeys.ICON_PATH_INDEX, brand.getIconPath());
				statement.setString(BrandKeys.INFO_INDEX, brand.getInfo());
				statement.setString(BrandKeys.IMAGES_INDEX, brand.getImages());
				statement.setString(BrandKeys.CATEGORIES_INDEX, brand.getCategories());
				statement.setString(BrandKeys.PRODUCTS_INDEX, brand.getProducts());
				statement.setString(BrandKeys.WEBSITE_INDEX, brand.getWebsite());
				statement.setString(BrandKeys.TAGS_INDEX, brand.getTags());
				statement.setString(BrandKeys.NOTES_INDEX, brand.getTags());
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void addBrands(ArrayList<Brand> brands) {
		String query = "INSERT INTO " + TB_BRAND + "(" + BrandKeys.COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?,?);";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				for (int i = 0; i < brands.size(); i++) {
					statement.setInt(BrandKeys.ORDER_INDEX, brands.get(i).getOrder());
					statement.setString(BrandKeys.NAME_INDEX, brands.get(i).getName());
					statement.setBytes(BrandKeys.ICON_INDEX, AppFunction.getBytesFromImage(brands.get(i).getIcon()));
					statement.setString(BrandKeys.ICON_PATH_INDEX, brands.get(i).getIconPath());
					statement.setString(BrandKeys.INFO_INDEX, brands.get(i).getInfo());
					statement.setString(BrandKeys.IMAGES_INDEX, brands.get(i).getImages());
					statement.setString(BrandKeys.CATEGORIES_INDEX, brands.get(i).getCategories());
					statement.setString(BrandKeys.PRODUCTS_INDEX, brands.get(i).getProducts());
					statement.setString(BrandKeys.WEBSITE_INDEX, brands.get(i).getWebsite());
					statement.setString(BrandKeys.TAGS_INDEX, brands.get(i).getTags());
					statement.setString(BrandKeys.NOTES_INDEX, brands.get(i).getTags());
					statement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateIcon(int brandId, File file) {
		String query = "UPDATE " + TB_BRAND + " SET " + BrandKeys.ICON + "=? WHERE "
				+ BrandKeys.ID + " = ?" + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.setBytes(1, AppFunction.getBytesFromImageFile(file));
				statement.setInt(2, brandId);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateString(int brandId, String columnName, String value) {
		String query = "UPDATE " + TB_BRAND + " SET " + columnName + "=\"" + value + "\" WHERE " + BrandKeys.ID + "="
				+ brandId + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}

	public void updateNumber(int brandId, String columnName, int value) {
		String query = "UPDATE " + TB_BRAND + " SET " + columnName + "=" + value + " WHERE " + BrandKeys.ID + "="
				+ brandId + ";";
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = createConnection();
			if (connection != null) {
				statement = connection.prepareStatement(query);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			AppDebug.error(e, "");
		} finally {
			DatabaseHelper.close(statement);
			DatabaseHelper.close(connection);
		}
	}
	
	public Brand getBrand(ResultSet resultSet) {
		Brand brand = new Brand();
		try {
			brand.setId(resultSet.getInt(BrandKeys.ID));
			brand.setOrder(resultSet.getInt(BrandKeys.ORDER));
			brand.setName(resultSet.getString(BrandKeys.NAME));
			Image image = null;
			try{
				image = new Image(resultSet.getBinaryStream(BrandKeys.ICON));
			}catch (Exception e) {
				AppDebug.error(e, "");
			}
			brand.setIcon(image);
			brand.setIconPath(resultSet.getString(BrandKeys.ICON_PATH));
			brand.setInfo(resultSet.getString(BrandKeys.INFO));
			brand.setImages(resultSet.getString(BrandKeys.IMAGES));
			brand.setCategories(resultSet.getString(BrandKeys.CATEGORIES));
			brand.setProducts(resultSet.getString(BrandKeys.PRODUCTS));
			brand.setWebsite(resultSet.getString(BrandKeys.WEBSITE));
			brand.setTags(resultSet.getString(BrandKeys.TAGS));
			brand.setNotes(resultSet.getString(BrandKeys.NOTES));
		} catch (SQLException e) {
			AppDebug.error(e, "");
		}
		return brand;
	}

	public int getMaxOrder(){
		int maxOrder = -1;
		String query = "SELECT " + BrandKeys.ORDER + " FROM " + DatabaseHelper.TB_BRAND + " WHERE " + BrandKeys.ORDER
				+ "=(SELECT MAX(" + BrandKeys.ORDER + ") FROM " + DatabaseHelper.TB_BRAND + ");";
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			connection = createConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					maxOrder = resultSet.getInt(BrandKeys.ORDER);
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
