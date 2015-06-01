package svd;

import java.io.IOException;
import java.util.ArrayList;

public class TestMuitModel {
	// public boolean isSample=false;
	public static int nusers = Ssl.nusers;
	public static int nitems = Ssl.nitems;
	public static int itemFeatureNumber = Ssl.itemFeatureNumber;
	public static int userFeatureNumber = Ssl.userFeatureNumber;
	public static int userFeatureNumerSize1 = Ssl.userFeatureNumerSize1;
	public static int userFeatureNumerSize2 = Ssl.userFeatureNumerSize2;
	public static int userFeatureNumerSize3 = Ssl.userFeatureNumerSize3;
	public static boolean printSwitch = false;

	// public static String trainSetName="smallTrainY.txt";
	// public static String trainSetName = "trainY.txt";

	public static void main(String[] args) throws IOException {
		TestMuitModel tmm = new TestMuitModel();
		tmm.myFunction();
	}

	public void myFunction() throws IOException {
		Init init = new Init();
		Set trainSet = init.getTrainSet();
		Set testSet = init.getTestSet();
		int[][] itemFeature = init.getItemFeature();
		int[][] userFeature = init.getUserFeature();

		// ModelJudge.calPopularity(trainSet, nusers, nitems);

		// Adaboost a = new Adaboost();
		// a.train(trainSet, testSet, userFeature, itemFeature);

		for (double beta = 0.0; beta < 1.0; beta += 0.1) {
//			MuitModelTraining m = new MuitModelTraining();
			MuitModelTraining_Top_N m = new MuitModelTraining_Top_N();
			MuitModelTraining.beta = beta;
			System.out.println("beta :" + MuitModelTraining.beta);
			ArrayList<Double> results = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				System.out.println(i);
				results.add(m
						.train(trainSet, testSet, userFeature, itemFeature));
			}
			System.out.println(getAve(results));
			System.out.println("##################################");
		}

	}

	public double getAve(ArrayList<Double> results) {
		double result = 0.0;
		for (Double double1 : results) {
			result += double1;
		}
		return result / results.size();
	}
}
