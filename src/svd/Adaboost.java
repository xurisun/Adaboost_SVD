package svd;

import java.util.ArrayList;
import java.util.List;

public class Adaboost {
	public static int numPasses = 100000;
	public static boolean Stochastic = true;
	public static int ROUND = 10;
	public static final double threshold = 0.4;
	public List<RecModel> models = new ArrayList<>();
	public List<Double> model_weights = new ArrayList<>();
	public Set weightSet;

	// public s
	public RecModel train(Set trainSet, Set testSet, int[][] userFeature,
			int[][] itemFeature) {
		final double orign = 1.0 / trainSet.getSize();
		double preTestRmse = 100;
		RecModel best = null;
		int timesNotUpdate = 0;

		RecModel m = new Model10(trainSet.getNusers(), trainSet.getNitems(),
				trainSet.getMean());

		double[] weights = new double[trainSet.getSize()];
		weightSet = trainSet;
		for (int i = 0; i < trainSet.getSize(); i++) {
			weights[i] = 1.0 / trainSet.getSize();
		}
		weightSet.setWeights(weights);

		for (int round = 0; round < ROUND; round++) {
			for (int pass = 1; pass <= numPasses; pass++) {
				if (Stochastic) {
					int[] prm = MyMethod.qrandperm(trainSet.getSize());
					for (int i = 0; i < trainSet.getSize(); i++) {
						int user = trainSet.getUsers()[prm[i]];
						int item = trainSet.getItems()[prm[i]];
						double rating = trainSet.getRatings()[prm[i]];
						double weight = weightSet.getWeights()[prm[i]];
						m.adaboostUpdate(user, item, rating, weight / orign);
					}
				} else {
					m.batchUpdate(trainSet);
				}
				double trainSetRmse = ModelJudge.modelGetRmse(m, trainSet);
				double testSetRmse = ModelJudge.modelGetRmse(m, testSet);
				System.out.println("round=" + round + " pass=" + pass);
				System.out.println("model TrainSet Rmse=" + trainSetRmse);
				System.out.println("model TestSet                   Rmse="
						+ testSetRmse);
				if (testSetRmse < preTestRmse) {
					preTestRmse = testSetRmse;
					timesNotUpdate = 0;
					best = m.myclone();
				} else {
					timesNotUpdate++;
					if (timesNotUpdate == 10) {
						System.out.println("Stop updating: model rmse= "
								+ preTestRmse);
						break;
					}
				}
			}
			timesNotUpdate = 0;
			preTestRmse = 100;
			m = best;

			// calculate err weight
			double err_weight = 0.0;
			for (int i = 0; i < trainSet.getSize(); i++) {
				int user = trainSet.getUsers()[i];
				int item = trainSet.getItems()[i];
				double rating = trainSet.getRatings()[i];
				double predictRating = m.predict(user, item);
				double err = rating - predictRating;
				if (err > threshold)
					err_weight += weightSet.getWeights()[i];
			}
			System.out.println("err_weight=" + err_weight);
			double alpha = 0.1 * Math.log((1 - err_weight) / err_weight);
			
//			double alpha =  Math.log(1.0 / err_weight);
			
			for (int i = 0; i < trainSet.getSize(); i++) {
				int user = trainSet.getUsers()[i];
				int item = trainSet.getItems()[i];
				double rating = trainSet.getRatings()[i];
				double predictRating = m.predict(user, item);
				double err = rating - predictRating;
				if (Math.abs(err) > threshold)
					weightSet.getWeights()[i] *= Math.exp(alpha);
//					weightSet.getWeights()[i] *= 1;
				else
					weightSet.getWeights()[i] *= Math.exp(-alpha);
//				weightSet.getWeights()[i] *= (err_weight*err_weight);
			}

			System.out
					.println("After training,  model       rmse on trainSet= "
							+ ModelJudge.modelGetRmse(m, trainSet));
			System.out.println("After training,  model       rmse on testSet= "
					+ ModelJudge.modelGetRmse(m, testSet));
			models.add(best);
			model_weights.add(alpha);
			m = new Model10(trainSet.getNusers(), trainSet.getNitems(),
					trainSet.getMean());
		}

		System.out
				.println("After training,  final model       rmse on trainSet= "
						+ ModelJudge.adaboostEnsembleModelGetRmse(models,
								model_weights, trainSet));
		System.out
				.println("After training,  final model       rmse on testSet= "
						+ ModelJudge.adaboostEnsembleModelGetRmse(models,
								model_weights, testSet));
		return best;

	}
}
