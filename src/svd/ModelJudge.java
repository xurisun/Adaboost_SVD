package svd;

import java.util.ArrayList;
import java.util.List;

public class ModelJudge {
	public static int USERBINNUMBER = 10;
	public static int ITEMBINNUMBER = 10;
	public static int[] userBin;
	public static int[] userBinHigh;
	public static int[][] userBinList;
	public static int[] itemBin;
	public static int[] itemBinHigh;
	public static int[][] itemBinList;

	public static void modelGetBinRmse(RecModel m, Set set) {
		double[] temp = new double[USERBINNUMBER + 1];
		int[] tempCount = new int[USERBINNUMBER + 1];
		for (int i = 0; i < set.getSize(); i++) {
			int user = set.getUsers()[i];
			int item = set.getItems()[i];
			double rating = set.getRatings()[i];
			temp[userBin[user]] += (m.predict(user, item) - rating)
					* (m.predict(user, item) - rating);
			tempCount[userBin[user]]++;
		}
		for (int i = 1; i <= USERBINNUMBER; i++) {
			System.out.println("USERBIN=" + i + " "
					+ Math.sqrt(temp[i] / tempCount[i]));
		}
		temp = new double[ITEMBINNUMBER + 1];
		tempCount = new int[ITEMBINNUMBER + 1];
		for (int i = 0; i < set.getSize(); i++) {
			int user = set.getUsers()[i];
			int item = set.getItems()[i];
			double rating = set.getRatings()[i];
			temp[itemBin[item]] += (m.predict(user, item) - rating)
					* (m.predict(user, item) - rating);
			tempCount[itemBin[item]]++;
		}
		for (int i = 1; i <= ITEMBINNUMBER; i++) {
			System.out.println("ITEMBIN=" + i + " "
					+ Math.sqrt(temp[i] / tempCount[i]));
		}
	}

	public static void calPopularity(Set set, int nusers, int nitems) {
		userBinHigh = new int[USERBINNUMBER + 1];
		itemBinHigh = new int[ITEMBINNUMBER + 1];
		userBin = new int[nusers + 1];
		itemBin = new int[nitems + 1];
		int[][] temp = new int[nusers + 1][nitems + 1];
		for (int i = 0; i < set.getSize(); i++) {
			temp[set.getUsers()[i]][set.getItems()[i]] = 1;
		}
		int[] userP = new int[nusers + 1];
		int[] itemP = new int[nitems + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				if (temp[i][j] > 0) {
					userP[i]++;
					itemP[j]++;
				}
			}
		}
		int[] indexU = new int[nusers + 1];
		for (int i = 1; i <= nusers; i++) {
			indexU[i] = i;
		}
		popQsort(userP, indexU, 1, nusers);
		for (int i = 0; i < userP.length; i++) {
			System.out.println(i + "  " + indexU[i] + "  " + userP[i]);
		}
		int[] indexI = new int[nitems + 1];
		for (int i = 1; i <= nitems; i++) {
			indexI[i] = i;
		}
		popQsort(itemP, indexI, 1, nitems);

		int nowBinIndex = 1;
		int nowBinNumber = 0;
		int binCapacity = nusers / USERBINNUMBER;
		userBinList = new int[USERBINNUMBER + 1][];
		for (int i = 1; i < USERBINNUMBER; i++) {
			userBinList[i] = new int[binCapacity];
		}
		userBinList[USERBINNUMBER] = new int[binCapacity + nusers
				% USERBINNUMBER];
		for (int i = 1; i <= nusers; i++) {
			userBin[indexU[i]] = nowBinIndex;
			userBinList[nowBinIndex][userBinHigh[nowBinIndex]] = indexU[i];
			userBinHigh[nowBinIndex]++;
			nowBinNumber++;
			if (nowBinNumber == binCapacity) {
				if (nowBinIndex == USERBINNUMBER) {
					continue;
				}
				nowBinNumber = 0;
				nowBinIndex++;
			}
		}
		nowBinIndex = 1;
		nowBinNumber = 0;
		binCapacity = nitems / ITEMBINNUMBER;
		itemBinList = new int[ITEMBINNUMBER + 1][];
		for (int i = 1; i < ITEMBINNUMBER; i++) {
			itemBinList[i] = new int[binCapacity];
		}
		itemBinList[ITEMBINNUMBER] = new int[binCapacity + nitems
				% ITEMBINNUMBER];
		for (int i = 1; i <= nitems; i++) {
			itemBin[indexI[i]] = nowBinIndex;
			itemBinList[nowBinIndex][itemBinHigh[nowBinIndex]] = indexI[i];
			itemBinHigh[nowBinIndex]++;
			nowBinNumber++;
			if (nowBinNumber == binCapacity) {
				if (nowBinIndex == ITEMBINNUMBER) {
					continue;
				}
				nowBinNumber = 0;
				nowBinIndex++;
			}
		}
	}

	private static void popQsort(int[] a, int[] index, int x, int y) {
		int i = x;
		int j = y;
		int t = a[(i + j) / 2];
		do {
			while (a[i] > t) {
				i++;
			}
			while (a[j] < t) {
				j--;
			}
			if (i <= j) {
				int temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				temp = index[i];
				index[i] = index[j];
				index[j] = temp;
				i++;
				j--;
			}
		} while (i <= j);
		if (i < y) {
			popQsort(a, index, i, y);
		}
		if (x < j) {
			popQsort(a, index, x, j);
		}
	}

	private static void doublepopQsort(double[] a, int[] index, int x, int y) {
		int i = x;
		int j = y;
		double t = a[(i + j) / 2];
		do {
			while (a[i] > t) {
				i++;
			}
			while (a[j] < t) {
				j--;
			}
			if (i <= j) {
				double temp = a[i];
				a[i] = a[j];
				a[j] = temp;
				int temp_index = index[i];
				index[i] = index[j];
				index[j] = temp_index;
				i++;
				j--;
			}
		} while (i <= j);
		if (i < y) {
			doublepopQsort(a, index, i, y);
		}
		if (x < j) {
			doublepopQsort(a, index, x, j);
		}
	}

	public static double modelGetRmse(RecModel m, Set set) {
		double temp = 0;
		for (int i = 0; i < set.getSize(); i++) {
			int user = set.getUsers()[i];
			int item = set.getItems()[i];
			double rating = set.getRatings()[i];
			double predict = m.predict(user, item);
			temp += (predict - rating) * (predict - rating);
		}
		return Math.sqrt(temp / set.getSize());
	}

	public static double ensembleModelGetRmse(ArrayList<RecModel> models,
			Set set) {
		double temp = 0;
		for (int i = 0; i < set.getSize(); i++) {
			int user = set.getUsers()[i];
			int item = set.getItems()[i];
			double rating = set.getRatings()[i];
			// double predict = m.predict(user, item);
			double predict = Model10.getAve(user, item, models);
			temp += (predict - rating) * (predict - rating);
		}
		return Math.sqrt(temp / set.getSize());
	}

	public static double adaboostEnsembleModelGetRmse(List<RecModel> models,
			List<Double> weights, Set set) {
		double temp = 0;
		double weight = 0.0;
		for (int j = 0; j < models.size(); j++) {
			weight += weights.get(j);
		}
		for (int i = 0; i < set.getSize(); i++) {
			int user = set.getUsers()[i];
			int item = set.getItems()[i];
			double rating = set.getRatings()[i];
			// double predict = m.predict(user, item);
			double predict = 0.0;

			for (int j = 0; j < models.size(); j++) {
				predict += models.get(j).predict(user, item) * weights.get(j);
			}
			predict = predict / weight;
			temp += (predict - rating) * (predict - rating);
		}
		return Math.sqrt(temp / set.getSize());
	}

	public static void modelRandomShow(RecModel m, Set set, int number) {
		boolean[] temp = new boolean[set.getSize()];
		for (int i = 0; i < number; i++) {
			int random = (int) (Math.random() * set.getSize());
			if (temp[random]) {
				i--;
				continue;
			}
			temp[random] = true;
			System.out.println(i + " user:" + set.getUsers()[random] + " item:"
					+ set.getItems()[random]);
			System.out
					.println(i
							+ "                             real:"
							+ set.getRatings()[random]
							+ " predict:"
							+ m.predict(set.getUsers()[random],
									set.getItems()[random]));
		}
	}

	public static void modelDiversity(RecModel m1, RecModel m2, Set trainSet1,
			Set trainSet2, Set testSet, Set unlabeledSet) {
		double temp = 0;
		for (int i = 0; i < trainSet1.getSize(); i++) {
			temp += Math.abs(m1.predict(trainSet1.getUsers()[i],
					trainSet1.getItems()[i])
					- m2.predict(trainSet1.getUsers()[i],
							trainSet1.getItems()[i]));
		}
		temp = temp / trainSet1.getSize();
		System.out.println("model diversity on trainSet1=" + temp);
		temp = 0;
		for (int i = 0; i < trainSet2.getSize(); i++) {
			temp += Math.abs(m1.predict(trainSet2.getUsers()[i],
					trainSet2.getItems()[i])
					- m2.predict(trainSet2.getUsers()[i],
							trainSet2.getItems()[i]));
		}
		temp = temp / trainSet1.getSize();
		System.out.println("model diversity on trainSet2=" + temp);
		temp = 0;
		for (int i = 0; i < testSet.getSize(); i++) {
			temp += Math.abs(m1.predict(testSet.getUsers()[i],
					testSet.getItems()[i])
					- m2.predict(testSet.getUsers()[i], testSet.getItems()[i]));
		}
		temp = temp / testSet.getSize();
		System.out.println("model diversity on testSet=" + temp);
		if (unlabeledSet != null) {
			temp = 0;
			for (int i = 0; i < unlabeledSet.getSize(); i++) {
				temp += Math.abs(m1.predict(unlabeledSet.getUsers()[i],
						unlabeledSet.getItems()[i])
						- m2.predict(unlabeledSet.getUsers()[i],
								unlabeledSet.getItems()[i]));
			}
			temp = temp / unlabeledSet.getSize();
			System.out.println("model diversity on unlabeledSet=" + temp);
		}
	}

	public static void getRecall() {

	}

	public static void getPrecision() {

	}

	public static int modelGetPrecisionAndRecall(RecModel m, Set trainSet,
			Set testSet, int n) {
		int nitems = Ssl.nitems;
		int nusers = Ssl.nusers;

		int[][] trainSet_temp = new int[nusers + 1][nitems + 1];
		for (int i = 0; i < trainSet.getSize(); i++) {
			trainSet_temp[trainSet.getUsers()[i]][trainSet.getItems()[i]] = 1;
		}
		int[][] testSet_temp = new int[nusers + 1][nitems + 1];
		for (int i = 0; i < testSet.getSize(); i++) {
			testSet_temp[testSet.getUsers()[i]][testSet.getItems()[i]] = 1;
		}

		int[][] indexI = new int[nusers + 1][nitems + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				indexI[i][j] = j;
			}
		}

		double[][] record = new double[nusers + 1][nitems + 1];
		// double[] record = new double[nitems + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				record[i][j] = m.predict(i, j);

			}
			doublepopQsort(record[i], indexI[i], 1, nitems);
		}

		int[][] result = new int[nusers + 1][n];
		doublepopQsort(record[1], indexI[1], 1, nitems);
		// for (int i = 1; i <= nitems; i++) {
		// System.out.println(indexI[1][i] + "  " + record[1][i]);
		// }
		int count = 0;
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				if (!contains(indexI[i][j], trainSet_temp[i])) {
					result[i][count] = indexI[i][j];
					// System.out.print(indexI[i][j] + "|");
					count++;
					if (count == n) {
						count = 0;
						break;
					}
				}
			}
			// System.out.println();
		}
		int[] num = new int[nusers + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 0; j < n; j++) {
				if (contains(result[i][j], testSet_temp[i])) {
					num[i]++;
				}
			}

		}
		count = 0;
		for (int i = 1; i <= nusers; i++) {
			// System.out.println(i + " " + num[i]);
			count += num[i];
		}
//		System.out.println(count);
//		System.out.println("precision:" + (1.0 * count) / (n * nusers));
//		System.out.println("recall:" + (1.0 * count) / testSet.getSize());
		// precision
		return count;

	}

	public static int modelGetPrecisionAndRecall(ArrayList<RecModel> models,
			Set trainSet, Set testSet, int n) {
		int nitems = Ssl.nitems;
		int nusers = Ssl.nusers;

		int[][] trainSet_temp = new int[nusers + 1][nitems + 1];
		for (int i = 0; i < trainSet.getSize(); i++) {
			trainSet_temp[trainSet.getUsers()[i]][trainSet.getItems()[i]] = 1;
		}
		int[][] testSet_temp = new int[nusers + 1][nitems + 1];
		for (int i = 0; i < testSet.getSize(); i++) {
			testSet_temp[testSet.getUsers()[i]][testSet.getItems()[i]] = 1;
		}

		int[][] indexI = new int[nusers + 1][nitems + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				indexI[i][j] = j;
			}
		}

		double[][] record = new double[nusers + 1][nitems + 1];
		// double[] record = new double[nitems + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				record[i][j] = MuitModelTraining.getAve(i, j, models);// m.predict(i,
																		// j);

			}
			doublepopQsort(record[i], indexI[i], 1, nitems);
		}

		int[][] result = new int[nusers + 1][n];
		doublepopQsort(record[1], indexI[1], 1, nitems);
		// for (int i = 1; i <= nitems; i++) {
		// System.out.println(indexI[1][i] + "  " + record[1][i]);
		// }
		int count = 0;
		for (int i = 1; i <= nusers; i++) {
			for (int j = 1; j <= nitems; j++) {
				if (!contains(indexI[i][j], trainSet_temp[i])) {
					result[i][count] = indexI[i][j];
					// System.out.print(indexI[i][j] + "|");
					count++;
					if (count == n) {
						count = 0;
						break;
					}
				}
			}
			// System.out.println();
		}
		int[] num = new int[nusers + 1];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 0; j < n; j++) {
				if (contains(result[i][j], testSet_temp[i])) {
					num[i]++;
				}
			}

		}
		int sum = 0;
		for (int i = 1; i <= nusers; i++) {
			// System.out.println(i + " " + num[i]);
			sum += num[i];
		}
		System.out.println(sum);
		System.out.println("precision:" + (1.0 * sum) / (n * nusers));
		System.out.println("recall:" + (1.0 * sum) / testSet.getSize());
		// precision
		return sum;

	}

	public static boolean contains(int a, int[] list) {
		if (list[a] != 0)
			return true;

		return false;
	}
}
