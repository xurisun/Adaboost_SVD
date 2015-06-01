package svd;

public class SingleModelTraining_Top_N {
	public static int numPasses = 100000;
	public static boolean Stochastic = true;
	public static int n = 10;

	public RecModel train(Set trainSet, Set testSet, int[][] userFeature,
			int[][] itemFeature) {
		double preTestRmse = 0;
		RecModel best = null;
		int timesNotUpdate = 0;

		RecModel m = new Model10(trainSet.getNusers(), trainSet.getNitems(),
				trainSet.getMean());

		// numPasses=0;
		// m2.batchUpdate(trainSet);
		// best=m;

		for (int pass = 1; pass <= numPasses; pass++) {
			if (Stochastic) {
				int[] prm = MyMethod.qrandperm(trainSet.getSize());
				for (int i = 0; i < trainSet.getSize(); i++) {
					int user = trainSet.getUsers()[prm[i]];
					int item = trainSet.getItems()[prm[i]];
					double rating = trainSet.getRatings()[prm[i]];
					m.update(user, item, rating);
				}
			} else {
				m.batchUpdate(trainSet);
			}
			double trainSetRmse = ModelJudge.modelGetRmse(m, trainSet);
			double testSetRmse = ModelJudge.modelGetRmse(m, testSet);
			int num = ModelJudge.modelGetPrecisionAndRecall(m, trainSet,
					testSet, n);
			System.out.println("pass=" + pass);
			System.out.println("model TrainSet Rmse=" + trainSetRmse);
			System.out.println("model TestSet                   Rmse="
					+ testSetRmse);

			if (num > preTestRmse) {
				preTestRmse = num;
				timesNotUpdate = 0;
				best = m.myclone();
			} else {
				timesNotUpdate++;
				if (timesNotUpdate == 10) {
					System.out.println("Stop updating: model hit= "
							+ preTestRmse);
					break;
				}
			}
		}
		m = best;

		System.out.println("After training,  model       rmse on trainSet= "
				+ ModelJudge.modelGetRmse(m, trainSet));
		System.out.println("After training,  model       rmse on testSet= "
				+ ModelJudge.modelGetRmse(m, testSet));

		System.out.println("-----------------------------");
		// ModelJudge.calPopularity(testSet, 943, 1682);
		// ModelJudge.modelGetBinRmse(m, testSet);
		ModelJudge.modelGetPrecisionAndRecall(m, trainSet, testSet, n);

		// Model2 m2=new
		// Model2(trainSet.getNusers(),trainSet.getNitems(),trainSet.getMean());
		// m2.setModel((Model10)m);
		// m2.batchUpdate(trainSet);
		// System.out.println("After training,  model m2      rmse on trainSet= "+ModelJudge.modelGetRmse(m2,
		// trainSet));
		// System.out.println("After training,  model m2      rmse on testSet= "+ModelJudge.modelGetRmse(m2,
		// testSet));

		// Model3 m3=new
		// Model3(trainSet.getNusers(),trainSet.getNitems(),trainSet.getMean());
		// m3.setModel((Model10)m);
		// m3.batchUpdate(trainSet);
		// System.out.println("After training,  model m3      rmse on trainSet= "+ModelJudge.modelGetRmse(m3,
		// trainSet));
		// System.out.println("After training,  model m3      rmse on testSet= "+ModelJudge.modelGetRmse(m3,
		// testSet));
		//
		//
		// ModelJudge.modelRandomShow(m, testSet, 100);
		// RecModel mu=new Model1(trainSet.getMean());
		// System.out.println("After training, mu model       rmse on trainSet= "+ModelJudge.modelGetRmse(mu,
		// trainSet));
		// System.out.println("After training, mu model                  rmse on testSet= "+ModelJudge.modelGetRmse(mu,
		// testSet));
		// ModelJudge.modelRandomShow(mu, testSet, 100);
		// System.out.println("bioMean="+MyMethod.getMean(((Model14)m).getBio()));
		// System.out.println("biaMean="+MyMethod.getMean(((Model14)m).getBia()));
		// System.out.println("bisMean="+MyMethod.getMean(((Model14)m).getBis()));
		// System.out.println("bugMean="+MyMethod.getMean(((Model14)m).getBug()));
		return best;

	}
}
