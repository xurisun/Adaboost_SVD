package svd;

import java.util.ArrayList;

//rui=mu+bu+bi+pq
public class Model10 implements RecModel {
	public double lambda = 0.0;// 0.02 //0.05
	public double alpha = 0.015;// 0.005 //0.001

	public static double beta = 0.5;
	int nusers;
	int nitems;
	public static int k = 20;
	double uu;
	double[] Bu;
	double[] Bi;
	double[][] P;
	double[][] Q;
	//
	public double preTestRmse = 0.0;
	public double testSetRmse = 0.0;
	public double trainSetRmse = 100.0;
	public int timesNotUpdate = 0;

	public Model10(int nusers, int nitems, double uu) {
		this.nusers = nusers;
		this.nitems = nitems;
		this.uu = uu;
		Bu = new double[nusers + 1];
		Bi = new double[nitems + 1];
		P = new double[nusers + 1][k];
		Q = new double[nitems + 1][k];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 0; j < k; j++) {
				P[i][j] = Math.random() / 100;
			}
		}
		for (int i = 1; i <= nitems; i++) {
			for (int j = 0; j < k; j++) {
				Q[i][j] = Math.random() / 100;
			}
		}
	}

	public Model10(int nusers, int nitems, double uu, double alpha,
			double lambda) {
		this.nusers = nusers;
		this.nitems = nitems;
		this.uu = uu;
		Bu = new double[nusers + 1];
		Bi = new double[nitems + 1];
		P = new double[nusers + 1][k];
		Q = new double[nitems + 1][k];
		for (int i = 1; i <= nusers; i++) {
			for (int j = 0; j < k; j++) {
				P[i][j] = Math.random() / 100;
			}
		}
		for (int i = 1; i <= nitems; i++) {
			for (int j = 0; j < k; j++) {
				Q[i][j] = Math.random() / 100;
			}
		}
		
		this.alpha = alpha;
		this.lambda = lambda;
	}

	public double[][] getP() {
		return P;
	}

	public double[][] getQ() {
		return Q;
	}

	public double[] getBu() {
		return Bu;
	}

	public double[] getBi() {
		return Bi;
	}

	public double getPreTestRmse() {
		return preTestRmse;
	}

	public void setPreTestRmse(double preTestRmse) {
		this.preTestRmse = preTestRmse;
	}

	public double getTestSetRmse() {
		return testSetRmse;
	}

	public void setTestSetRmse(double testSetRmse) {
		this.testSetRmse = testSetRmse;
	}

	public double getTrainSetRmse() {
		return trainSetRmse;
	}

	public void setTrainSetRmse(double trainSetRmse) {
		this.trainSetRmse = trainSetRmse;
	}

	public int getTimesNotUpdate() {
		return timesNotUpdate;
	}

	public void setTimesNotUpdate(int timesNotUpdate) {
		this.timesNotUpdate = timesNotUpdate;
	}

	public RecModel myclone() {
		Model10 newModel = new Model10(nusers, nitems, uu);
		System.arraycopy(Bu, 0, newModel.Bu, 0, Bu.length);
		System.arraycopy(Bi, 0, newModel.Bi, 0, Bi.length);
		MyMethod.myCopy(P, newModel.P);
		MyMethod.myCopy(Q, newModel.Q);
		return newModel;
	}

	@Override
	public double predict(int user, int item) {
		double ans = uu + Bu[user] + Bi[item]
				+ MyMethod.innerProduct(P[user], Q[item]);
		// double ans=MyMethod.innerProduct(P[user],Q[item]);
		if (ans > 5) {
			ans = 5;
		}
		if (ans < 1) {
			ans = 1;
		}
		return ans;
	}

	public double adaboostUpdate(int user, int item, double rating,
			double weight) {
		double predictRating = predict(user, item);
		double err = rating - predictRating;
		Bu[user] = Bu[user] + alpha * weight * (err - lambda * Bu[user]);
		Bi[item] = Bi[item] + alpha * weight * (err - lambda * Bi[item]);
		for (int i = 0; i < k; i++) {
			P[user][i] = P[user][i] + alpha * weight
					* (err * Q[item][i] - lambda * P[user][i]);
		}
		for (int i = 0; i < k; i++) {
			Q[item][i] = Q[item][i] + alpha * weight
					* (err * P[user][i] - lambda * Q[item][i]);
		}
		return predict(user, item);
	}

	@Override
	public double update(int user, int item, double rating) {
		double predictRating = predict(user, item);
		double err = rating - predictRating;
		Bu[user] = Bu[user] + alpha * (err - lambda * Bu[user]);
		Bi[item] = Bi[item] + alpha * (err - lambda * Bi[item]);
		for (int i = 0; i < k; i++) {
			P[user][i] = P[user][i] + alpha
					* (err * Q[item][i] - lambda * P[user][i]);
		}
		for (int i = 0; i < k; i++) {
			Q[item][i] = Q[item][i] + alpha
					* (err * P[user][i] - lambda * Q[item][i]);
		}
		return predict(user, item);
	}

	public double nclUpdate(int user, int item, double rating, double average,
			int size) {
		double predictRating = predict(user, item);
		// double err = rating - predictRating;
		// double average = getAve(user, item, models);
		// NCL
		double err = predictRating - rating - MuitModelTraining.beta
				* (predictRating - average);

		Bu[user] = Bu[user] - alpha * (err + lambda * Bu[user]);
		Bi[item] = Bi[item] - alpha * (err + lambda * Bi[item]);
		for (int i = 0; i < k; i++) {
			P[user][i] = P[user][i] - alpha
					* (err * Q[item][i] + lambda * P[user][i]);
		}
		for (int i = 0; i < k; i++) {
			Q[item][i] = Q[item][i] - alpha
					* (err * P[user][i] + lambda * Q[item][i]);
		}
		return predict(user, item);
	}

	@Override
	public void batchUpdate(Set trainSet) {
		double[] tempErr = new double[trainSet.getSize()];
		for (int i = 0; i < trainSet.getSize(); i++) {
			int user = trainSet.getUsers()[i];
			int item = trainSet.getItems()[i];
			double rating = trainSet.getRatings()[i];
			tempErr[i] = rating - predict(user, item);
		}
		for (int i = 0; i < trainSet.getSize(); i++) {
			int user = trainSet.getUsers()[i];
			int item = trainSet.getItems()[i];
			double err = tempErr[i];
			Bu[user] = Bu[user] + alpha * (err - lambda * Bu[user]);
			Bi[item] = Bi[item] + alpha * (err - lambda * Bi[item]);
			for (int j = 0; j < k; j++) {
				P[user][j] = P[user][j] + alpha
						* (err * Q[item][j] - lambda * P[user][j]);
			}
			for (int j = 0; j < k; j++) {
				Q[item][j] = Q[item][j] + alpha
						* (err * P[user][j] - lambda * Q[item][j]);
			}
		}
	}

	public static double getAve(int user, int item, ArrayList<RecModel> models) {
		double result = 0.0;
		for (RecModel recModel : models) {
			result += recModel.predict(user, item);
		}
		return result / models.size();
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}
}
