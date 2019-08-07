package bk.hungd.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Brand {

	private IntegerProperty id;
	private IntegerProperty order;
	private StringProperty name;
	private ObjectProperty<Image> icon;
	private StringProperty iconPath;
	private StringProperty info;
	private StringProperty images;
	private StringProperty categories;
	private StringProperty products;
	private StringProperty website;
	private StringProperty tags;
	private StringProperty notes;

	public Brand() {
		this(0, 0, "", "", "", "", "", "", "", "", "");
	}

	public Brand(int order, String name, String iconPath, String info, String images, String products, String categories,
		String website, String tags, String notes) {
		super();
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.products = new SimpleStringProperty(products);
		this.categories = new SimpleStringProperty(categories);
		this.website = new SimpleStringProperty(website);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
	}

	public Brand(int id, int order, String name, String iconPath, String info, String images, String products,
		String categories, String website, String tags, String notes) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.name = new SimpleStringProperty(name);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.info = new SimpleStringProperty(info);
		this.images = new SimpleStringProperty(images);
		this.products = new SimpleStringProperty(products);
		this.categories = new SimpleStringProperty(categories);
		this.website = new SimpleStringProperty(website);
		this.tags = new SimpleStringProperty(tags);
		this.notes = new SimpleStringProperty(notes);
	}

	public void setString(String field, String valie) {
		switch (field) {
			case BrandKeys.NAME:
			setName(valie);
			break;
			case BrandKeys.ICON_PATH:
			setIconPath(valie);
			break;
			case BrandKeys.INFO:
			setInfo(valie);
			break;
			case BrandKeys.IMAGES:
			setImages(valie);
			break;
			case BrandKeys.PRODUCTS:
			setProducts(valie);
			break;
			case BrandKeys.CATEGORIES:
			setCategories(valie);
			break;
			case BrandKeys.WEBSITE:
			setWebsite(valie);
			break;
			case BrandKeys.TAGS:
			setTags(valie);
			break;
			case BrandKeys.NOTES:
			setNotes(valie);
			break;
			default:
			break;
		}
	}
	
	public void setNumber(String field, int value) {
		switch (field) {
			case BrandKeys.ORDER:
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

	public ObjectProperty<Image> iconProperty(){
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

	public String getCategories() {
		return categories.get();
	}

	public void setCategories(String categories) {
		this.categories.set(categories);
	}

	public StringProperty categoriesProperty() {
		return categories;
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

	public String getWebsite() {
		return website.get();
	}

	public void setWebsite(String website) {
		this.website.set(website);
	}

	public StringProperty websiteProperty() {
		return website;
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
