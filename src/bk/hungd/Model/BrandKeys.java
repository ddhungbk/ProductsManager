package bk.hungd.Model;

public class BrandKeys {

	public static final String ID = "docid"; // default
	public static final String ORDER = "_order";
	public static final String NAME = "_name";
	public static final String ICON = "icon";
	public static final String ICON_PATH = "icon_path";
	public static final String INFO = "info";
	public static final String IMAGES = "images";
	public static final String CATEGORIES = "categories";
	public static final String PRODUCTS = "products";
	public static final String WEBSITE = "website";
	public static final String TAGS = "tags";
	public static final String NOTES = "notes";

	public static final String COLUMNS = ORDER + "," + NAME + "," + ICON + "," + ICON_PATH + "," + INFO + "," + IMAGES + "," + CATEGORIES
			+ "," + PRODUCTS + "," + WEBSITE + "," + TAGS + "," + NOTES;
	public static final String ALL_COLUMNS = ID + "," + ORDER + "," + NAME + "," + ICON + "," + ICON_PATH + "," + INFO + "," + IMAGES
			+ "," + CATEGORIES + "," + PRODUCTS + "," + WEBSITE + "," + TAGS + "," + NOTES;

	// value 0 for docid column as default
	public static final int ORDER_INDEX = 1;
	public static final int NAME_INDEX = 2;
	public static final int ICON_INDEX = 3;
	public static final int ICON_PATH_INDEX = 4;
	public static final int INFO_INDEX = 5;
	public static final int IMAGES_INDEX = 6;
	public static final int CATEGORIES_INDEX = 7;
	public static final int PRODUCTS_INDEX = 8;
	public static final int WEBSITE_INDEX = 9;
	public static final int TAGS_INDEX = 10;
	public static final int NOTES_INDEX = 11;

	public static String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case ORDER_INDEX:
			return ORDER;
		case NAME_INDEX:
			return NAME;
		case ICON_INDEX:
			return ICON;
		case ICON_PATH_INDEX:
			return ICON_PATH;
		case INFO_INDEX:
			return INFO;
		case IMAGES_INDEX:
			return IMAGES;
		case CATEGORIES_INDEX:
			return CATEGORIES;
		case PRODUCTS_INDEX:
			return PRODUCTS;
		case WEBSITE_INDEX:
			return WEBSITE;
		case TAGS_INDEX:
			return TAGS;
		case NOTES_INDEX:
			return NOTES;
		}
		return "";
	}

	public static int getIndex(String columnName) {
		switch (columnName) {
		case ORDER:
			return ORDER_INDEX;
		case NAME:
			return NAME_INDEX;
		case ICON:
			return ICON_INDEX;
		case ICON_PATH:
			return ICON_PATH_INDEX;
		case INFO:
			return INFO_INDEX;
		case IMAGES:
			return IMAGES_INDEX;
		case CATEGORIES:
			return CATEGORIES_INDEX;
		case PRODUCTS:
			return PRODUCTS_INDEX;
		case WEBSITE:
			return WEBSITE_INDEX;
		case TAGS:
			return TAGS_INDEX;
		case NOTES:
			return NOTES_INDEX;
		}
		return 0;
	}
}
