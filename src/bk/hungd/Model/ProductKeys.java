
package bk.hungd.Model;

public class ProductKeys {

	public static final String ID = "docid";
	public static final String ORDER = "_order";
	public static final String NAME = "_name";
	public static final String CODE = "code";
	public static final String ICON = "icon";
	public static final String ICON_PATH = "icon_path";
	public static final String BRAND = "brand";
	public static final String CATEGORY = "category";
	public static final String INFO = "info";
	public static final String IMAGES = "images";
	public static final String SPECS = "specs";
	public static final String PRICE = "price";
	public static final String QUANTITY = "quantity";
	public static final String STATUS = "status";
	public static final String TAGS = "tags";
	public static final String NOTES = "notes";
	public static final String BRAND_ID = "brand_id";
	public static final String CATEGORY_ID = "category_id";

	public static final String COLUMNS = ORDER + "," + NAME + "," + CODE + "," + ICON + "," + ICON_PATH + "," + BRAND + "," + CATEGORY
			+ "," + INFO + "," + IMAGES + "," + SPECS + "," + PRICE + "," + QUANTITY + "," + STATUS + "," + TAGS + ","
			+ NOTES + "," + BRAND_ID + "," + CATEGORY_ID;
	public static final String ALL_COLUMNS = ID + "," + ORDER + "," + NAME + "," + CODE + "," + ICON + "," + ICON_PATH + "," + BRAND
			+ "," + CATEGORY + "," + INFO + "," + IMAGES + "," + SPECS + "," + PRICE + "," + QUANTITY + "," + STATUS
			+ "," + TAGS + "," + NOTES + "," + BRAND_ID + "," + CATEGORY_ID;

	// value 0 for docid column as default
	public static final int ORDER_INDEX = 1;
	public static final int NAME_INDEX = 2;
	public static final int CODE_INDEX = 3;
	public static final int ICON_INDEX = 4;
	public static final int ICON_PATH_INDEX = 5;
	public static final int BRAND_INDEX = 6;
	public static final int CATEGORY_INDEX = 7;
	public static final int INFO_INDEX = 8;
	public static final int IMAGES_INDEX = 9;
	public static final int SPECS_INDEX = 10;
	public static final int PRICE_INDEX = 11;
	public static final int QUANTITY_INDEX = 12;
	public static final int STATUS_INDEX = 13;
	public static final int TAGS_INDEX = 14;
	public static final int NOTES_INDEX = 15;
	public static final int BRAND_ID_INDEX = 16;
	public static final int CATEGORY_ID_INDEX = 17;

	public static String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case ORDER_INDEX:
			return ORDER;
		case NAME_INDEX:
			return NAME;
		case CODE_INDEX:
			return CODE;
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
		case SPECS_INDEX:
			return SPECS;
		case PRICE_INDEX:
			return PRICE;
		case QUANTITY_INDEX:
			return QUANTITY;
		case STATUS_INDEX:
			return STATUS;
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
