package svd;

public interface RecModel {

	public double preTestRmse = 0.0;
	public double testSetRmse = 0.0;
	public double trainSetRmse = 100.0;
	public int timesNotUpdate = 0;

	public double predict(int user, int item);

	public double update(int user, int item, double rating);

	public void batchUpdate(Set trainSet);

	public RecModel myclone();

	double nclUpdate(int user, int item, double rating, double average, int size);

	public double adaboostUpdate(int user, int item, double rating,
			double weight);

}
