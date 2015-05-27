package svd;

import java.io.IOException;

public class TestSingleModel {
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
	public static String trainSetName = "trainY.txt";

	public static void main(String[] args) throws IOException {
		TestSingleModel tsm = new TestSingleModel();
		tsm.myFunction();
	}

	public void myFunction() throws IOException {
		Init init = new Init();
		Set trainSet = init.getTrainSet();
		Set testSet = init.getTestSet();
		int[][] itemFeature = init.getItemFeature();
		int[][] userFeature = init.getUserFeature();

		// ModelJudge.calPopularity(trainSet, nusers, nitems);
		SingleModelTraining m = new SingleModelTraining();
		m.train(trainSet, testSet, userFeature, itemFeature);
		// GeneticModelTraining gmt=new GeneticModelTraining();
		// gmt.train(trainSet, testSet,userFeature,itemFeature);
		// Set[] sets=MyMethod.getSample(trainSet,2,0.8);
		// Model18BGD m=new Model18BGD();
		// m.train(trainSet, trainSet, testSet, userFeature, itemFeature, true);
		// m.train(sets[0],sets[1], testSet, userFeature, itemFeature, false);
		// ContinuousModelTraining m=new ContinuousModelTraining();
		// m.train(trainSet, testSet,userFeature,itemFeature);
		// AdaboostModelTraining m=new AdaboostModelTraining();
		// m.train(trainSet, testSet,userFeature,itemFeature);
	}
}
