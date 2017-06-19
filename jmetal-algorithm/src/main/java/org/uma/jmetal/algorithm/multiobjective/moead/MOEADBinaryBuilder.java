//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.algorithm.multiobjective.moead;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.UniformedCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmBuilder;

/**
 * Builder class for algorithm MOEA/D and variants
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
public class MOEADBinaryBuilder implements AlgorithmBuilder<AbstractMOEAD<BinarySolution>> {
  public enum Variant {MOEAD, ConstraintMOEAD, MOEADDRA, MOEADSTM} ;

  protected Problem<BinarySolution> problem ;

  /** T in Zhang & Li paper */
  protected int neighborSize;
  /** Delta in Zhang & Li paper */
  protected double neighborhoodSelectionProbability;
  /** nr in Zhang & Li paper */
  protected int maximumNumberOfReplacedSolutions;

  protected MOEAD.FunctionType functionType;

  protected CrossoverOperator<BinarySolution> crossover;
  protected MutationOperator<BinarySolution> mutation;
  protected String dataDirectory;

  protected int populationSize;
  protected int resultPopulationSize ;

  protected int maxEvaluations;

  protected int numberOfThreads ;

  protected Variant moeadVariant ;

  protected int H1_;

  protected int H2_;

  /** Constructor */
  public MOEADBinaryBuilder(Problem<BinarySolution> problem, Variant variant) {
    this.problem = problem ;
    populationSize = 300 ;
    resultPopulationSize = 300 ;
    maxEvaluations = 150000 ;
    crossover = new UniformedCrossover(1.0) ;
    mutation = new BitFlipMutation(1.0/problem.getNumberOfVariables());
    functionType = MOEAD.FunctionType.TCHE ;
    neighborhoodSelectionProbability = 0.1 ;
    maximumNumberOfReplacedSolutions = 2 ;
    dataDirectory = "" ;
    neighborSize = 20 ;
    numberOfThreads = 1 ;
    moeadVariant = variant ;
    H1_ = 99;
    H2_ = 0;
  }

  /* Getters/Setters */
  public int getNeighborSize() {
    return neighborSize;
  }

  public int getMaxEvaluations() {
    return maxEvaluations;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public int getResultPopulationSize() {
    return resultPopulationSize;
  }

  public String getDataDirectory() {
    return dataDirectory;
  }

  public MutationOperator<BinarySolution> getMutation() {
    return mutation;
  }


  public CrossoverOperator<BinarySolution> getCrossover() {
    return crossover;
  }

  public MOEAD.FunctionType getFunctionType() {
    return functionType;
  }

  public int getMaximumNumberOfReplacedSolutions() {
    return maximumNumberOfReplacedSolutions;
  }

  public double getNeighborhoodSelectionProbability() {
    return neighborhoodSelectionProbability;
  }

  public int getNumberOfThreads() {
    return numberOfThreads ;
  }

  public MOEADBinaryBuilder setPopulationSize(int populationSize) {
    this.populationSize = populationSize;

    return this;
  }

  public MOEADBinaryBuilder setResultPopulationSize(int resultPopulationSize) {
    this.resultPopulationSize = resultPopulationSize;

    return this;
  }

  public MOEADBinaryBuilder setMaxEvaluations(int maxEvaluations) {
    this.maxEvaluations = maxEvaluations;

    return this;
  }

  public MOEADBinaryBuilder setNeighborSize(int neighborSize) {
    this.neighborSize = neighborSize ;

    return this ;
  }

  public MOEADBinaryBuilder setNeighborhoodSelectionProbability(double neighborhoodSelectionProbability) {
    this.neighborhoodSelectionProbability = neighborhoodSelectionProbability ;

    return this ;
  }

  public MOEADBinaryBuilder setFunctionType(MOEAD.FunctionType functionType) {
    this.functionType = functionType ;

    return this ;
  }

  public MOEADBinaryBuilder setMaximumNumberOfReplacedSolutions(int maximumNumberOfReplacedSolutions) {
    this.maximumNumberOfReplacedSolutions = maximumNumberOfReplacedSolutions ;

    return this ;
  }

  public MOEADBinaryBuilder setCrossover(CrossoverOperator<BinarySolution> crossover) {
    this.crossover = crossover ;

    return this ;
  }

  public MOEADBinaryBuilder setMutation(MutationOperator<BinarySolution> mutation) {
    this.mutation = mutation ;

    return this ;
  }

  public MOEADBinaryBuilder setDataDirectory(String dataDirectory) {
    this.dataDirectory = dataDirectory ;

    return this ;
  }

  public MOEADBinaryBuilder setNumberOfThreads(int numberOfThreads) {
    this.numberOfThreads = numberOfThreads ;

    return this ;
  }

  public AbstractMOEAD<BinarySolution> build() {
    AbstractMOEAD<BinarySolution> algorithm = null ;
    if (moeadVariant.equals(Variant.MOEAD)) {
    	System.out.println(Variant.MOEAD);
      algorithm = new MOEADBinary(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
          crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
          maximumNumberOfReplacedSolutions, neighborSize, H1_, H2_);
    }else if (moeadVariant.equals(Variant.MOEADDRA)) {
    	System.out.println(Variant.MOEADDRA);
        algorithm =  new MOEADDRABinary(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
            crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
            maximumNumberOfReplacedSolutions, neighborSize, H1_, H2_);
      }else if (moeadVariant.equals(Variant.MOEADSTM)) {
    	System.out.println(Variant.MOEADSTM);
        algorithm =  new MOEADSTMBinary(problem, populationSize, resultPopulationSize, maxEvaluations, mutation,
                crossover, functionType, dataDirectory, neighborhoodSelectionProbability,
                maximumNumberOfReplacedSolutions, neighborSize, H1_, H2_);
    }
    return algorithm ;
  }


  public MOEADBinaryBuilder setH1(int H1_){
  	this.H1_ = H1_;

  	return this;
  }

  public MOEADBinaryBuilder setH2(int H2_){
	  	this.H2_ = H2_;

	  	return this;
  }

}
