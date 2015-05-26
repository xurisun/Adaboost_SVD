package svd;

public class Set {
	private int[] users;
	private int[] items;
	private double[] ratings;
	private int size;
	private int capacity;
	private int nusers;
	private int nitems;
	private double[] weights;

	public Set(int capacity, int nusers, int nitems) {
		this.capacity = capacity;
		size = 0;
		users = new int[capacity];
		items = new int[capacity];
		ratings = new double[capacity];
		this.nusers = nusers;
		this.nitems = nitems;
	}

	public double getMean() {
		double temp = 0;
		for (int i = 0; i < size; i++) {
			temp += ratings[i];
		}
		return temp / size;
	}

	public void add(int user, int item, double rating) {
		if (size >= capacity) {
			reSize();
		}
		users[size] = user;
		items[size] = item;
		ratings[size] = rating;
		size++;
	}

	public void add(Set set) {
		while (set.size > (capacity - size)) {
			reSize();
		}
		System.arraycopy(set.getUsers(), 0, users, size, set.getSize());
		System.arraycopy(set.getItems(), 0, items, size, set.getSize());
		System.arraycopy(set.getRatings(), 0, ratings, size, set.getSize());
		size = size + set.getSize();
	}

	public int getSize() {
		return size;
	}

	public int[] getUsers() {
		return users;
	}

	public int[] getItems() {
		return items;
	}

	public double[] getRatings() {
		return ratings;
	}

	public int getNusers() {
		return nusers;
	}

	public int getNitems() {
		return nitems;
	}

	public void reSize() {
		capacity = capacity * 2;
		int[] newUsers = new int[capacity];
		int[] newItems = new int[capacity];
		double[] newRatings = new double[capacity];
		System.arraycopy(users, 0, newUsers, 0, size);
		System.arraycopy(items, 0, newItems, 0, size);
		System.arraycopy(ratings, 0, newRatings, 0, size);
		users = newUsers;
		items = newItems;
		ratings = newRatings;
	}

	public double[] getWeights() {
		return weights;
	}

	public void setWeights(double[] weights) {
		this.weights = weights;
	}
}
