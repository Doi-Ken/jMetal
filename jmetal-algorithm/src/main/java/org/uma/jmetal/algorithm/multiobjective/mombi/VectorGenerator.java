package org.uma.jmetal.algorithm.multiobjective.mombi;

public abstract class VectorGenerator {
	protected double[][] lambda_;

	public double[][] getVectors() {
		return this.lambda_;
	}


}
