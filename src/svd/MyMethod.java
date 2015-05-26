package svd;

public class MyMethod {
	public static void main(String[] args) {
		int[] temp = bootstrp(71);
		int[] test = new int[71 + 1];
		for (int i = 0; i < 71; i++) {
			// System.out.println(temp[i]);
			test[temp[i]]++;
		}
		for (int i = 0; i <= 71; i++) {
			// System.out.println(test[i]);
		}
		int[] num = new int[15];
		for (int i = 1; i <= 71; i++) {
			num[test[i]]++;
		}
		for (int i = 0; i <= 10; i++) {
			System.out.println(i + " " + num[i]);
		}
	}

	public static double myRound(double x) {
		if (x > 5) {
			return 5;
		}
		if (x < 1) {
			return 1;
		}
		return x;
	}

	public static int[] bootstrp(int number) {
		int[] ans = new int[number];
		int high = 0;
		for (int i = 0; i < number; i++) {
			int ran = (int) (Math.random() * number) + 1;
			ans[high] = ran;
			high++;
		}
		return ans;
	}

	public static Set[] getSample(Set trainSet, int number, double ratio) {
		Set[] ans = new Set[number];
		for (int i = 0; i < number; i++) {
			ans[i] = new Set((int) (trainSet.getSize() * ratio + number),
					trainSet.getNusers(), trainSet.getNitems());
		}
		for (int i = 0; i < trainSet.getSize(); i++) {
			boolean used = false;
			for (int j = 0; j < number; j++) {
				if (Math.random() < ratio) {
					ans[j].add(trainSet.getUsers()[i], trainSet.getItems()[i],
							trainSet.getRatings()[i]);
					used = true;
				}
			}
			if (!used) {
				int which = (int) (Math.random() * number);
				ans[which].add(trainSet.getUsers()[i], trainSet.getItems()[i],
						trainSet.getRatings()[i]);
			}
		}
		return ans;
	}

	public static void myCopy(double[][] here, double[][] there) {
		if (here.length != there.length) {
			System.exit(0);
		}
		for (int i = 0; i < here.length; i++) {
			System.arraycopy(here[i], 0, there[i], 0, here[i].length);
		}
	}

	public static double getMean(double[][] temp) {
		double ans = 0;
		double count = 0;
		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[i].length; j++) {
				if (temp[i][j] != 0) {
					count++;
					ans += Math.abs(temp[i][j]);
				}
			}
		}
		return ans / count;
	}

	public static int[] qrandperm(int n) {
		int[] temp = new int[n];
		for (int i = 0; i < n; i++) {
			temp[i] = i;
		}
		for (int i = 0; i < n; i++) {
			int pos = (int) (Math.random() * (n - i)) + i;
			int t = temp[pos];
			temp[pos] = temp[i];
			temp[i] = t;
		}
		return temp;
	}

	public static double innerProduct(double[] x1, double[] x2) {
		if (x1.length != x2.length) {
			System.out.println("size not match");
			System.exit(0);
		}
		double ans = 0;
		for (int i = 0; i < x1.length; i++) {
			ans += x1[i] * x2[i];
		}
		return ans;
	}

	public static double innerProduct3(double[] x1, int[] x2) {
		if (x1.length != x2.length) {
			System.out.println("size not match");
			System.exit(0);
		}
		double ans = 0;
		for (int i = 0; i < x2.length; i++) {
			ans += x1[i] * x2[i];
		}
		return ans;
	}

	public static int[] randperm(int n) {
		double[] temp = new double[n];
		for (int i = 0; i < n; i++) {
			temp[i] = Math.random();
		}
		int[] ans = new int[n];
		for (int i = 0; i < n; i++) {
			ans[i] = i;
		}
		qsort(temp, ans, 0, n - 1);
		return ans;
	}

	private static void qsort(double[] temp, int[] ans, int x, int y) {
		int i = x;
		int j = y;
		double t = temp[(x + y) / 2];
		do {
			while (temp[i] < t) {
				i++;
			}
			while (temp[j] > t) {
				j--;
			}
			if (i <= j) {
				double ttt = temp[i];
				temp[i] = temp[j];
				temp[j] = ttt;
				int ttt2 = ans[i];
				ans[i] = ans[j];
				ans[j] = ttt2;
				i++;
				j--;
			}
		} while (i <= j);
		if (i < y) {
			qsort(temp, ans, i, y);
		}
		if (x < j) {
			qsort(temp, ans, x, j);
		}
	}

	public static double getM(double[] vector) {
		double temp = 0;
		for (int i = 0; i < vector.length; i++) {
			temp += vector[i] * vector[i];
		}
		return Math.sqrt(temp);
	}
}
