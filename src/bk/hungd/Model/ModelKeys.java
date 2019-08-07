package bk.hungd.Model;

public class ModelKeys {

	public static final String ID = "docid";
	public static final String ORDER = "_order";
	public static final String NAME = "_name";
	public static final String ICON = "icon";
	public static final String ICON_PATH = "icon_path";
	public static final String BRAND = "brand";
	public static final String CATEGORY = "category";
	public static final String INFO = "info";
	public static final String IMAGES = "images";
	public static final String PRODUCTS = "products";
	public static final String TAGS = "tags";
	public static final String NOTES = "notes";
	public static final String BRAND_ID = "brand_id";
	public static final String CATEGORY_ID = "category_id";

	public static final String COLUMNS = ORDER + "," + NAME + "," + ICON + "," + ICON_PATH + "," + BRAND + "," + CATEGORY + "," + INFO
			+ "," + IMAGES + "," + PRODUCTS + "," + TAGS + "," + NOTES + "," + BRAND_ID + "," + CATEGORY_ID;
	public static final String ALL_COLUMNS = ID + "," + ORDER + "," + NAME + "," + ICON + "," + ICON_PATH + "," + BRAND + "," + CATEGORY
			+ "," + INFO + "," + IMAGES + "," + PRODUCTS + "," + TAGS + "," + NOTES + "," + BRAND_ID + ","
			+ CATEGORY_ID;

	// value 0 for docid column as default
	public static final int ORDER_INDEX = 1;
	public static final int NAME_INDEX = 2;
	public static final int ICON_INDEX = 3;
	public static final int ICON_PATH_INDEX = 4;
	public static final int BRAND_INDEX = 5;
	public static final int CATEGORY_INDEX = 6;
	public static final int INFO_INDEX = 7;
	public static final int IMAGES_INDEX = 8;
	public static final int PRODUCTS_INDEX = 9;
	public static final int TAGS_INDEX = 10;
	public static final int NOTES_INDEX = 11;
	public static final int BRAND_ID_INDEX = 12;
	public static final int CATEGORY_ID_INDEX = 13;

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
		case BRAND_INDEX:
			return BRAND;
		case CATEGORY_INDEX:
			return CATEGORY;
		case INFO_INDEX:
			return INFO;
		case IMAGES_INDEX:
			return IMAGES;
		case PRODUCTS_INDEX:
			return PRODUCTS;
		case TAGS_INDEX:
			return TAGS;
		case NOTES_INDEX:
			return NOTES;
		case BRAND_ID_INDEX:
			return BRAND_ID;
		case CATEGORY_ID_INDEX:
			return CATEGORY_ID;
		}
		return "";
	}

}
