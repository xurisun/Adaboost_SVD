package svd;

import java.io.*;

public class Ssl {
	// public boolean isSample=false;
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
/*
	public static void main(String[] args) throws IOException {
		Ssl ssl = new Ssl();
		ssl.myFunction();
	}

	public void myFunction() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("trainY.txt"));
		int nTrainY = Integer.parseInt(br.readLine());
		Set trainSet = new Set(nTrainY, nusers, nitems);
		for (int i = 0; i < nTrainY; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			trainSet.add(Integer.parseInt(tempSS[0]),
					Integer.parseInt(tempSS[1]), Integer.parseInt(tempSS[2]));
		}
		br.close();
		br = new BufferedReader(new FileReader("testY.txt"));
		int nTestY = Integer.parseInt(br.readLine());
		Set testSet = new Set(nTestY, nusers, nitems);
		for (int i = 0; i < nTestY; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			testSet.add(Integer.parseInt(tempSS[0]),
					Integer.parseInt(tempSS[1]), Integer.parseInt(tempSS[2]));
		}
		br.close();
		br = new BufferedReader(new FileReader("X.txt"));
		int nX = Integer.parseInt(br.readLine());
		int[][] itemFeature = new int[nX + 1][itemFeatureNumber];
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
		int[][] userFeature = new int[nZ + 1][userFeatureNumber];
		for (int i = 0; i < nZ; i++) {
			String tempS = br.readLine();
			String[] tempSS = tempS.split(" ");
			for (int j = 0; j < userFeatureNumber; j++) {
				userFeature[i + 1][j] = Integer.parseInt(tempSS[j]);
			}
		}
		br.close();
		Model18Training m = new Model18Training();

		// Model18BGD m=new Model18BGD();
		Set trainSet1 = new Set(trainSet.getSize(), nusers, nitems);
		trainSet1.add(trainSet);
		Set trainSet2 = new Set(trainSet.getSize(), nusers, nitems);
		trainSet2.add(trainSet);
		RecModel[] models = m.train(trainSet1, trainSet2, testSet, userFeature,
				itemFeature);
		// RecModel[] models=m.train(trainSet1,trainSet2,testSet, userFeature,
		// itemFeature,true);
		RecModel ma = models[0];
		RecModel mb = models[1];
		RecModel me = new EnsembleModel(ma, mb);
		double[][] data = new double[nusers + 1][nitems + 1];
		for (int i = 0; i < trainSet.getSize(); i++) {
			data[trainSet.getUsers()[i]][trainSet.getItems()[i]] = trainSet
					.getRatings()[i];
		}
		for (int i = 0; i < testSet.getSize(); i++) {
			data[testSet.getUsers()[i]][testSet.getItems()[i]] = testSet
					.getRatings()[i];
		}
		for (int t = 1; t <= T; t++) {
			System.out.println("Semi-Round=" + t);
			int timesNotFind = 0;
			Set teachingSet1 = new Set(teachingSetSize, nusers, nitems);
			Set teachingSet2 = new Set(teachingSetSize, nusers, nitems);
			for (int i = 1; i <= teachingSetSize * 2; i++) {
				boolean endLabel = false;
				while (!endLabel) {
					timesNotFind++;
					if (timesNotFind >= teachingSetSize * 2 * thoDecrease) {
						tho = tho * 0.8;
						System.out.println(timesNotFind
								+ " times has tried, only " + i
								+ " has in, tho decrease " + tho);
						timesNotFind = 0;
					}
					int tempU = trainSet.getUsers()[(int) (Math.random() * (trainSet
							.getSize()))];
					int tempI = trainSet.getItems()[(int) (Math.random() * (trainSet
							.getSize()))];
					if (data[tempU][tempI] > 0) {
						continue;
					}
					if (Math.abs(ma.predict(tempU, tempI)
							- mb.predict(tempU, tempI)) < tho) {
						continue;
					}
					if (i % 2 == 1) {
						teachingSet1
								.add(tempU, tempI, me.predict(tempU, tempI));
					} else {
						teachingSet2
								.add(tempU, tempI, me.predict(tempU, tempI));
					}
					break;
				}
			}
			trainSet1.add(teachingSet2);
			trainSet2.add(teachingSet1);
			models = m.train(trainSet1, trainSet2, testSet, userFeature,
					itemFeature);
			// models=m.train(trainSet1,trainSet2, testSet, userFeature,
			// itemFeature,false);
		}
	}
	*/
}
