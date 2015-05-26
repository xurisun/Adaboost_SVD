package svd;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Init {
	public static int nusers = 943;
	public static int nitems = 1682;
	public static int itemFeatureNumber = 19;
	public static int userFeatureNumber = 3;
	public static int userFeatureNumerSize1 = 2;
	public static int userFeatureNumerSize2 = 7;
	public static int userFeatureNumerSize3 = 21;
	public static int teachingSetSize = 8000;
	public static double tho = 0.4;
	public static int T = 30;
	public static int thoDecrease = 15;
	public static boolean printSwitch = true;
	public static String trainSetName = "trainY.txt";

	private Set trainSet;
	private Set testSet;
	private int[][] itemFeature;
	private int[][] userFeature;

	public Init() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(trainSetName));
		int nTrainY = Integer.parseInt(br.readLine());
		trainSet = new Set(nTrainY, nusers, nitems);
		for (int i = 0; i < nTrainY; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			trainSet.add(Integer.parseInt(tempSS[0]),
					Integer.parseInt(tempSS[1]), Integer.parseInt(tempSS[2]));
		}
		br.close();
		br = new BufferedReader(new FileReader("testY.txt"));
		int nTestY = Integer.parseInt(br.readLine());
		testSet = new Set(nTestY, nusers, nitems);
		for (int i = 0; i < nTestY; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			testSet.add(Integer.parseInt(tempSS[0]),
					Integer.parseInt(tempSS[1]), Integer.parseInt(tempSS[2]));
		}
		br.close();
		br = new BufferedReader(new FileReader("X.txt"));
		int nX = Integer.parseInt(br.readLine());
		itemFeature = new int[nX + 1][itemFeatureNumber];
		for (int i = 0; i < nX; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			for (int j = 0; j < itemFeatureNumber; j++) {
				itemFeature[i + 1][j] = Integer.parseInt(tempSS[j]);
			}
		}
		br.close();
		br = new BufferedReader(new FileReader("Z.txt"));
		int nZ = Integer.parseInt(br.readLine());
		userFeature = new int[nZ + 1][userFeatureNumber];
		for (int i = 0; i < nZ; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			for (int j = 0; j < userFeatureNumber; j++) {
				userFeature[i + 1][j] = Integer.parseInt(tempSS[j]);
			}
		}
		br.close();
	}

	public Set getTestSet() {
		return testSet;
	}

	public void setTestSet(Set testSet) {
		this.testSet = testSet;
	}

	public Set getTrainSet() {
		return trainSet;
	}

	public void setTrainSet(Set trainSet) {
		this.trainSet = trainSet;
	}

	public int[][] getItemFeature() {
		return itemFeature;
	}

	public void setItemFeature(int[][] itemFeature) {
		this.itemFeature = itemFeature;
	}

	public int[][] getUserFeature() {
		return userFeature;
	}

	public void setUserFeature(int[][] userFeature) {
		this.userFeature = userFeature;
	}

}
