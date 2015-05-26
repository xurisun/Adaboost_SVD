package svd;

import java.util.ArrayList;

public class MuitModelTraining {
	public static int numPasses = 100000;
	public static boolean Stochastic = true;
	public static int sum = 5;
	public static double beta = 0.5;

	public double train(Set trainSet, Set testSet, int[][] userFeature,
			int[][] itemFeature) {
		ArrayList<RecModel> models = new ArrayList<RecModel>();
		ArrayList<RecModel> bests = new ArrayList<RecModel>();
		ArrayList<Double> trainSetRmses = new ArrayList<>();
		ArrayList<Double> testSetRmses = new ArrayList<>();
		ArrayList<Double> preTestRmses = new ArrayList<>();
		ArrayList<Integer> timesNotUpdates = new ArrayList<>();
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<String> results1 = new ArrayList<String>();
		RecModel[] best = new RecModel[sum];
		ArrayList<RecModel> models1 = new ArrayList<RecModel>();
		ArrayList<RecModel> bests1 = new ArrayList<RecModel>();
		ArrayList<Double> trainSetRmses1 = new ArrayList<>();
		ArrayList<Double> testSetRmses1 = new ArrayList<>();
		ArrayList<Double> preTestRmses1 = new ArrayList<>();
		ArrayList<Integer> timesNotUpdates1 = new ArrayList<>();
		RecModel[] best1 = new RecModel[sum];

		for (int i = 0; i < sum; i++) {
			RecModel m = new Model10(trainSet.getNusers(),
					trainSet.getNitems(), trainSet.getMean());
			
			RecModel m1 = m.myclone();

			models.add(m);
			best[i] = m;
			trainSetRmses.add(0.0);
			testSetRmses.add(0.0);
			preTestRmses.add(100.0);
			timesNotUpdates.add(0);

			models1.add(m1);
			best1[i] = m1;
			trainSetRmses1.add(0.0);
			testSetRmses1.add(0.0);
			preTestRmses1.add(100.0);
			timesNotUpdates1.add(0);

		}
		
		for (int pass = 1; pass <= numPasses; pass++) {
			int[] prm = MyMethod.qrandperm(trainSet.getSize());
			for (int i = 0; i < trainSet.getSize(); i++) {
				int user = trainSet.getUsers()[prm[i]];
				int item = trainSet.getItems()[prm[i]];
				double average = getAve(user, item, models);
				double rating = trainSet.getRatings()[prm[i]];
				for (int j = 0; j < models.size(); j++) {
					models.get(j).nclUpdate(user, item, rating, average,
							models.size());
				}
			}
			for (int i = 0; i < models.size(); i++) {
				RecModel m = models.get(i);
				trainSetRmses.remove(i);
				trainSetRmses.add(i, ModelJudge.modelGetRmse(m, trainSet));
				testSetRmses.remove(i);
				testSetRmses.add(i, ModelJudge.modelGetRmse(m, testSet));

				if (testSetRmses.get(i) < preTestRmses.get(i)) {
					preTestRmses.remove(i);
					preTestRmses.add(i, testSetRmses.get(i));
					timesNotUpdates.remove(i);
					timesNotUpdates.add(i, 0);
					best[i] = m.myclone();
				} else {
					int timesNotUpdate = timesNotUpdates.get(i);
					timesNotUpdates.remove(i);
					timesNotUpdates.add(i, ++timesNotUpdate);
					if (timesNotUpdates.get(i) == 10) {
						String result = "Stop updating: model " + i + " rmse= "
								+ preTestRmses.get(i);
						results.add(result);
						bests.add(best[i]);
						
						models.remove(i);
						preTestRmses.remove(i);
						timesNotUpdates.remove(i);
						trainSetRmses.remove(i);
						testSetRmses.remove(i);
						break;
					}
				}
			}
			if (models.size() == 0)
				break;
		}
		results.add("After training,  final model       rmse on trainSet= "
				+ ModelJudge.ensembleModelGetRmse(bests, trainSet));
		results.add("After training, final model       rmse on testSet= "
				+ ModelJudge.ensembleModelGetRmse(bests, testSet));
/*
		for (int pass = 1; pass <= numPasses; pass++) {
			int[] prm = MyMethod.qrandperm(trainSet.getSize());
			for (int i = 0; i < trainSet.getSize(); i++) {
				int user = trainSet.getUsers()[prm[i]];
				int item = trainSet.getItems()[prm[i]];
				double rating = trainSet.getRatings()[prm[i]];
				for (int j = 0; j < models1.size(); j++) {
					// models.get(j).nclUpdate(user, item, rating, models);
					models1.get(j).update(user, item, rating);
				}
			}
			for (int i = 0; i < models1.size(); i++) {
				RecModel m = models1.get(i);
				trainSetRmses1.remove(i);
				trainSetRmses1.add(i, ModelJudge.modelGetRmse(m, trainSet));
				testSetRmses1.remove(i);
				testSetRmses1.add(i, ModelJudge.modelGetRmse(m, testSet));

				if (testSetRmses1.get(i) < preTestRmses1.get(i)) {
					preTestRmses1.remove(i);
					preTestRmses1.add(i, testSetRmses1.get(i));
					timesNotUpdates1.remove(i);
					timesNotUpdates1.add(i, 0);
					best1[i] = m.myclone();
				} else {
					int timesNotUpdate = timesNotUpdates1.get(i);
					timesNotUpdates1.remove(i);
					timesNotUpdates1.add(i, ++timesNotUpdate);
					if (timesNotUpdates1.get(i) == 10) {
						String result = "Stop updating: model " + i + " rmse= "
								+ preTestRmses1.get(i);
						results1.add(result);
						models1.remove(i);
						bests1.add(best1[i]);
						preTestRmses1.remove(i);
						timesNotUpdates1.remove(i);
						trainSetRmses1.remove(i);
						testSetRmses1.remove(i);
						break;
					}
				}
			}
			if (models1.size() == 0)
				break;
		}
		results1.add("After training,  final model       rmse on trainSet= "
				+ ModelJudge.ensembleModelGetRmse(bests1, trainSet));
		
		results1.add("After training, final model       rmse on testSet= "
				+ ModelJudge.ensembleModelGetRmse(bests1, testSet));
*/
		for (int i = 0; i < results.size(); i++) {
			System.out.println(results.get(i));
		}
//		for (int i = 0; i < results1.size(); i++) {
//			System.out.println(results1.get(i));
//		}
		results.clear();
//		results1.clear();
		double a = ModelJudge.ensembleModelGetRmse(bests, testSet);
//		double b = ModelJudge.ensembleModelGetRmse(bests1, testSet);
//		System.out.println((b - a) / b * 100);
//		return (b - a) / b * 100;
		return a;
	}

	public static double getAve(int user, int item, ArrayList<RecModel> models) {
		double result = 0.0;
		for (RecModel recModel : models) {
			result += recModel.predict(user, item);
		}
		return result / models.size();
	}
}
