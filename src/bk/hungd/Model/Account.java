package bk.hungd.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

public class Account {

	private IntegerProperty id;
	private IntegerProperty order;
	private StringProperty userName;
	private StringProperty password;
	private StringProperty userType;
	private StringProperty name;
	private StringProperty code;
	private ObjectProperty<Image> icon;
	private StringProperty iconPath;
	private StringProperty position;

	public Account() {
		this(0, 0, "", "", "", "", "", "", "");
	}

	public Account(int order, String userName, String password, String userType, String name, String code,
			String iconPath, String position) {
		super();
		this.order = new SimpleIntegerProperty(order);
		this.userName = new SimpleStringProperty(userName);
		this.password = new SimpleStringProperty(password);
		this.userType = new SimpleStringProperty(userType);
		this.name = new SimpleStringProperty(name);
		this.code = new SimpleStringProperty(code);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.position = new SimpleStringProperty(position);
	}

	public Account(int id, int order, String userName, String password, String userType, String name, String code,
			String iconPath, String position) {
		super();
		this.id = new SimpleIntegerProperty(id);
		this.order = new SimpleIntegerProperty(order);
		this.userName = new SimpleStringProperty(userName);
		this.password = new SimpleStringProperty(password);
		this.userType = new SimpleStringProperty(userType);
		this.name = new SimpleStringProperty(name);
		this.code = new SimpleStringProperty(code);
		this.icon = new SimpleObjectProperty<Image>();
		this.iconPath = new SimpleStringProperty(iconPath);
		this.position = new SimpleStringProperty(position);
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

	public String getUserName() {
		return userName.get();
	}

	public void setUserName(String userName) {
		this.userName.set(userName);
	}

	public StringProperty userNameProperty() {
		return userName;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(String password) {
		this.password.set(password);
	}

	public StringProperty passwordProperty() {
		return password;
	}

	public String getUserType() {
		return userType.get();
	}

	public void setUserType(String userType) {
		this.userType.set(userType);
	}

	public StringProperty userTypeProperty() {
		return userType;
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

	public String getPosition() {
		return position.get();
	}

	public void setPosition(String position) {
		this.position.set(position);
	}

	public StringProperty positionProperty() {
		return position;
	}
}
