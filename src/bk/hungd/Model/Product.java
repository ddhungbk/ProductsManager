package bk.hungd.Model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Product {

	private IntegerProperty id;
	private IntegerProperty order;
	private StringProperty name;
	private StringProperty code;
	private ObjectProperty<Image> icon;
	private StringProperty iconPath;
	private StringProperty brand;
	private StringProperty category;
	private StringProperty info;
	private StringProperty images;
	private StringProperty specs;
	private DoubleProperty price;
	private IntegerProperty quantity;
	private StringProperty status;
	private StringProperty tags;
	private StringProperty notes;
	private IntegerProperty brandId;
	private IntegerProperty categoryId;

	public Product() {
		this(0, 0, "", "", "", "", "", "", "", "", 0, 0, "", "", "");
	}

	public Product(int order, String name, String code, String iconPath, String brand, String category, String info,
			String images, String specs, double price, int quantity, String status, String tags, String notes) {
		super();
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.code = new SimpleStringProperty(code);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.brand = new SimpleStringProperty(brand);
		this.category = new SimpleStringProperty(category);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.specs = new SimpleStringProperty(specs);
		this.price = new SimpleDoubleProperty(price);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.status = new SimpleStringProperty(status);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
		this.brandId = new SimpleIntegerProperty();
		this.categoryId = new SimpleIntegerProperty();
	}

	public Product(int id, int order, String name, String code, String iconPath, String brand, String category,
			String info, String images, String specs, double price, int quantity, String status, String tags,
			String notes) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.code = new SimpleStringProperty(code);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.brand = new SimpleStringProperty(brand);
		this.category = new SimpleStringProperty(category);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.specs = new SimpleStringProperty(specs);
		this.price = new SimpleDoubleProperty(price);
		this.quantity = new SimpleIntegerProperty(quantity);
		this.status = new SimpleStringProperty(status);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
		this.brandId = new SimpleIntegerProperty();
		this.categoryId = new SimpleIntegerProperty();
	}

	public void setString(String name, String value) {
		switch (name) {
		case ProductKeys.NAME:
			setName(value);
			break;
		case ProductKeys.CODE:
			setCode(value);
			break;
		case ProductKeys.ICON_PATH:
			setIconPath(value);
			break;
		case ProductKeys.BRAND:
			setBrand(value);
			break;
		case ProductKeys.CATEGORY:
			setCategory(value);
			break;
		case ProductKeys.INFO:
			setInfo(value);
			break;
		case ProductKeys.IMAGES:
			setImages(value);
			break;
		case ProductKeys.SPECS:
			setSpecs(value);
			break;
		case ProductKeys.STATUS:
			setStatus(value);
			break;
		case ProductKeys.TAGS:
			setTags(value);
			break;
		case ProductKeys.NOTES:
			setNotes(value);
			break;
		default:
			break;
		}
	}

	public void setNumber(String name, double value) {
		switch (name) {
		case ProductKeys.ORDER:
			setOrder((int) value);
			break;
		case ProductKeys.PRICE:
			setPrice(value);
			break;
		case ProductKeys.QUANTITY:
			setQuantity((int) value);
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

	public String getCode() {
		return code.get();
	}

	public void setCode(String code) {
		this.code.set(code);
	}

	public StringProperty codeProperty() {
		return code;
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

	public String getBrand() {
		return brand.get();
	}

	public void setBrand(String brand) {
		this.brand.set(brand);
	}

	public StringProperty brandProperty() {
		return brand;
	}

	public String getCategory() {
		return category.get();
	}

	public void setCategory(String category) {
		this.category.set(category);
	}

	public StringProperty categoryProperty() {
		return category;
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

	public String getSpecs() {
		return specs.get();
	}

	public void setSpecs(String specs) {
		this.specs.set(specs);
	}

	public StringProperty specsProperty() {
		return specs;
	}

	public double getPrice() {
		return price.get();
	}

	public void setPrice(double price) {
		this.price.set(price);
	}

	public DoubleProperty priceProperty() {
		return price;
	}

	public int getQuantity() {
		return quantity.get();
	}

	public void setQuantity(int quantity) {
		this.quantity.set(quantity);
	}

	public IntegerProperty quantityProperty() {
		return quantity;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public StringProperty statusProperty() {
		return status;
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

	public int getBrandId() {
		return brandId.get();
	}

	public void setBrandId(int brandId) {
		this.brandId.set(brandId);
	}

	public IntegerProperty brandIdProperty() {
		return brandId;
	}

	public int getCategoryId() {
		return categoryId.get();
	}

	public void setCategoryId(int categoryId) {
		this.categoryId.set(categoryId);
	}

	public IntegerProperty categoryIdProperty() {
		return categoryId;
	}

}
