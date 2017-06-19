package org.uma.jmetal.algorithm.multiobjective.gwasfga;

public abstract class VectorGenerator {
	protected double[][] lambda_;

	public double[][] getVectors() {
		return this.lambda_;
	}


}
