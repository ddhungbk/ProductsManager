package bk.hungd.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Category {

	private IntegerProperty id;
	private IntegerProperty order;
	private StringProperty name;
	private ObjectProperty<Image> icon;
	private StringProperty iconPath;
	private StringProperty info;
	private StringProperty images;
	private StringProperty brands;
	private StringProperty products;
	private StringProperty tags;
	private StringProperty notes;

	public Category() {
		this(0, 0, "", "", "", "", "", "", "", "");
	}

	public Category(int order, String name, String iconPath, String info, String images, String brands, String products,
			String tags, String notes) {
		super();
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.brands = new SimpleStringProperty(brands);
		this.products = new SimpleStringProperty(products);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
	}

	public Category(int id, int order, String name, String iconPath, String info, String images, String brands,
			String products, String tags, String notes) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.brands = new SimpleStringProperty(brands);
		this.products = new SimpleStringProperty(products);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
	}

	public void setString(String name, String value) {
		switch (name) {
		case CategoryKeys.NAME:
			setName(value);
			break;
		case CategoryKeys.ICON_PATH:
			setIconPath(value);
			break;
		case CategoryKeys.INFO:
			setInfo(value);
			break;
		case CategoryKeys.IMAGES:
			setImages(value);
			break;
		case CategoryKeys.BRANDS:
			setBrands(value);
			break;
		case CategoryKeys.PRODUCTS:
			setProducts(value);
			break;
		case CategoryKeys.TAGS:
			setTags(value);
			break;
		case CategoryKeys.NOTES:
			setNotes(value);
			break;
		default:
			break;
		}
	}

	public void setNumber(int index, int value) {
		switch (index) {
		case CategoryKeys.ORDER_INDEX:
			setOrder(value);
			break;
		default:
			break;
		}
	}	
	public int getId() {
		return id.get();
	}

	public void setId(int id) {
		this.id.set(id);
	}

	public IntegerProperty idProperty() {
		return id;
	}

	public int getOrder() {
		return order.get();
	}

	public void setOrder(int order) {
		this.order.set(order);
	}

	public IntegerProperty orderProperty() {
		return order;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public StringProperty nameProperty() {
		return name;
	}

	public Image getIcon() {
		return icon.get();
	}

	public void setIcon(Image image) {
		this.icon.set(image);
	}

	public ObjectProperty<Image> iconProperty() {
		return icon;
	}

	public String getIconPath() {
		return iconPath.get();
	}

	public void setIconPath(String iconPath) {
		this.iconPath.set(iconPath);
	}

	public StringProperty iconPathProperty() {
		return iconPath;
	}

	public String getInfo() {
		return info.get();
	}

	public void setInfo(String info) {
		this.info.set(info);
	}

	public StringProperty infoProperty() {
		return info;
	}

	public String getImages() {
		return images.get();
	}

	public void setImages(String images) {
		this.images.set(images);
	}

	public StringProperty imagesProperty() {
		return images;
	}

	public String getBrands() {
		return brands.get();
	}

	public void setBrands(String brands) {
		this.brands.set(brands);
	}

	public StringProperty brandsProperty() {
		return brands;
	}

	public String getProducts() {
		return products.get();
	}

	public void setProducts(String products) {
		this.products.set(products);
	}

	public StringProperty productsProperty() {
		return products;
	}

	public String getTags() {
		return tags.get();
	}

	public void setTags(String tags) {
		this.tags.set(tags);
	}

	public StringProperty tagsProperty() {
		return tags;
	}

	public String getNotes() {
		return notes.get();
	}

	public void setNotes(String notes) {
		this.notes.set(notes);
	}

	public StringProperty notesProperty() {
		return notes;
	}
}
