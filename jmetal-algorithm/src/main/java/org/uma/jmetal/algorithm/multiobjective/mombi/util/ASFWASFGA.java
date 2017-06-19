package org.uma.jmetal.algorithm.multiobjective.mombi.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;

/**
 * @author Juan J. Durillo
 * Modified by Antonio J. Nebro
 *
 * @param <S>
 */
@SuppressWarnings("serial")
public class ASFWASFGA<S extends Solution<?>> extends AbstractUtilityFunctionsSet<S> {

	private final List<Double> interestPoint;
	private double augmentationCoefficient = 0.001;
	private List<Double> utopia = null;
	private List<Double> nadir  = null;


	public ASFWASFGA(double [][] weights, List<Double> interestPoint) {
		super(weights);
		this.interestPoint = interestPoint;
	}

	public ASFWASFGA(double [][] weights) {
		super(weights);
		this.interestPoint = new ArrayList<>(this.getVectorSize());
		for (int i = 0; i < this.getVectorSize(); i++)
			this.interestPoint.add(0.0);
	}

	public ASFWASFGA(String file_path, List<Double> interestPoint) {
		super(file_path);
		this.interestPoint = interestPoint;
	}

	public ASFWASFGA(String file_path) {
		super(file_path);
		this.interestPoint = new ArrayList<>(this.getVectorSize());
		for (int i = 0; i < this.getVectorSize(); i++)
			this.interestPoint.add(0.0);
	}

	public void updatePointOfInterest(List<Double> newInterestPoint ) {
		if (this.interestPoint.size()!=newInterestPoint.size())
			throw new JMetalException("Wrong dimension of the interest point vector");

		for (int i = 0; i < newInterestPoint.size(); i++) {
            this.interestPoint.set(i,newInterestPoint.get(i));
		}
	}


	@Override
	public Double evaluate(S solution, int vector) {
		if ((vector < 0) || (vector >= this.getSize())) {
			throw new JMetalException("Vector value " + vector + " invalid") ;
		}



		List<Double> weightVector 	 =  this.getWeightVector(vector);
		List<Double> objectiveValues =  new ArrayList<>(solution.getNumberOfObjectives());
		for (int i = 0; i < solution.getNumberOfObjectives();i++)
				objectiveValues.add(solution.getObjective(i));

		double result = -1e10;
		double secondSum = 0.0;
		if(this.getScalarizingFunction() == "pbi"){
			return normalizedPBI(weightVector, objectiveValues);
		}
		else if(this.getScalarizingFunction() == "invertedpbi"){
			return normalizedInvertedPBI(weightVector, objectiveValues);
		}
		else{
		for (int i = 0; i < weightVector.size(); i++) {

			double temp = objectiveValues.get(i) - this.interestPoint.get(i);


			if (nadir!=null && utopia!=null) {
				temp = temp / (this.nadir.get(i) - this.utopia.get(i));
			}

			double temp_product = temp * weightVector.get(i);

			if (temp_product > result)
				result = temp_product;

			secondSum += temp_product;
		}

		return result + (secondSum * this.augmentationCoefficient);
		}
	}


	public double normalizedPBI(List<Double> weightVector, List<Double> objectiveValues){
		double lambda_norm = 0.0;
		double d_t = 0.0;
		double d_n = 0.0;
		double z_minus_fit[] = new double [weightVector.size()];
		double PENALTY = 5.0;

		for (int o = 0; o < weightVector.size(); o++){
			lambda_norm += weightVector.get(o) * weightVector.get(o);
		}
		lambda_norm = Math.sqrt(lambda_norm);

		for (int o = 0; o < weightVector.size(); o++){
			z_minus_fit[o] = (objectiveValues.get(o) - this.interestPoint.get(o));
			if (nadir!=null && utopia!=null) {
				z_minus_fit[o] = z_minus_fit[o] / (this.nadir.get(o) - this.utopia.get(o));
			}
		}

		//d_t
		for (int o = 0; o < weightVector.size(); o++){
			d_t += z_minus_fit[o] * weightVector.get(o);
		}
		d_t /= lambda_norm;

		//d_n
		for (int o = 0; o < weightVector.size(); o++){
			d_n += (z_minus_fit[o] - d_t * weightVector.get(o) / lambda_norm) * (z_minus_fit[o] - d_t * weightVector.get(o) / lambda_norm);
		}
		d_n = Math.sqrt(d_n);

		return (d_t + PENALTY * d_n);
	}


	public double normalizedInvertedPBI(List<Double> weightVector, List<Double> objectiveValues){
		double lambda_norm = 0.0;
		double d_t = 0.0;
		double d_n = 0.0;
		double z_minus_fit[] = new double [weightVector.size()];
		double PENALTY = 5.0;

		for (int o = 0; o < weightVector.size(); o++){
			lambda_norm += weightVector.get(o) * weightVector.get(o);
		}
		lambda_norm = Math.sqrt(lambda_norm);

		for (int o = 0; o < weightVector.size(); o++){
			z_minus_fit[o] = (this.interestPoint.get(o) - objectiveValues.get(o));
			if (nadir!=null && utopia!=null) {
				z_minus_fit[o] = z_minus_fit[o] / (this.nadir.get(o) - this.utopia.get(o));
			}
		}
		//d_t
		for (int o = 0; o < weightVector.size(); o++){
			d_t += z_minus_fit[o] * weightVector.get(o);
		}
		d_t /= lambda_norm;

		//d_n
		for (int o = 0; o < weightVector.size(); o++){
			d_n += (z_minus_fit[o] - d_t * weightVector.get(o) / lambda_norm) * (z_minus_fit[o] - d_t * weightVector.get(o) / lambda_norm);
		}
		d_n = Math.sqrt(d_n);

		return (d_t - PENALTY * d_n);
	}

	public void setNadir(List<Double> nadir) {
		this.nadir = nadir;

	}
	public void setUtopia(List<Double> utopia) {
		this.utopia = utopia;
	}


}
