package bk.hungd.Extra;

public class StatusChecker {
	int id;
	boolean first;
	boolean last;

	public StatusChecker(boolean first, boolean last) {
		this.first = first;
		this.last = last;
	}

	public StatusChecker(int id, boolean first, boolean last) {
		super();
		this.id = id;
		this.first = first;
		this.last = last;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public boolean getLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public void setValues(boolean first, boolean last) {
		this.first = first;
		this.last = last;
	}

}
