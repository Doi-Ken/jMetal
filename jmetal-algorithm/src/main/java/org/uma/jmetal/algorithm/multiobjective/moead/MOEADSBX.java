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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.multiobjective.moead.util.MOEADUtils;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Class implementing the MOEA/D-DE algorithm described in :
 * Hui Li; Qingfu Zhang, "Multiobjective Optimization Problems With Complicated Pareto Sets,
 * MOEA/D and NSGA-II," Evolutionary Computation, IEEE Transactions on , vol.13, no.2, pp.284,302,
 * April 2009. doi: 10.1109/TEVC.2008.925798
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MOEADSBX extends AbstractMOEAD<DoubleSolution> {

  public MOEADSBX(Problem<DoubleSolution> problem,
      int populationSize,
      int resultPopulationSize,
      int maxEvaluations,
      MutationOperator<DoubleSolution> mutation,
      CrossoverOperator<DoubleSolution> crossover,
      FunctionType functionType,
      String dataDirectory,
      double neighborhoodSelectionProbability,
      int maximumNumberOfReplacedSolutions,
      int neighborSize) {
    super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
        dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
        neighborSize);

   }

  public MOEADSBX(Problem<DoubleSolution> problem,
	      int populationSize,
	      int resultPopulationSize,
	      int maxEvaluations,
	      MutationOperator<DoubleSolution> mutation,
	      CrossoverOperator<DoubleSolution> crossover,
	      FunctionType functionType,
	      String dataDirectory,
	      double neighborhoodSelectionProbability,
	      int maximumNumberOfReplacedSolutions,
	      int neighborSize, int h1, int h2) {
	    super(problem, populationSize, resultPopulationSize, maxEvaluations, crossover, mutation, functionType,
	        dataDirectory, neighborhoodSelectionProbability, maximumNumberOfReplacedSolutions,
	        neighborSize, h1, h2);

	  }


  @Override public void run() {
    initializePopulation() ;
    initializeUniformWeight();
    initializeNeighborhood();
    initializeIdealPoint() ;
    computeMaxPoint();

    String filename = "inisol" + ".txt";
    FileOutputStream fos;
	try {
		fos = new FileOutputStream(filename);
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		BufferedWriter bw = new BufferedWriter(osw);
		int numberOfObjectives = population.get(0).getNumberOfObjectives();
		for (int i = 0; i < population.size(); i++) {
			for (int j = 0; j < numberOfObjectives; j++) {
	         try {
				bw.write(population.get(i).getObjective(j) + "\t");
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
	       }
	       try {
			bw.newLine();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	     }
		/* Close the file */
		try {
			bw.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	} catch (FileNotFoundException e) {
		// TODO 自動生成された catch ブロック
		e.printStackTrace();
	}



    evaluations = populationSize ;
    do {
      int[] permutation = new int[populationSize];
      MOEADUtils.randomPermutation(permutation, populationSize);

      if(normalization){
    	  computeMaxPoint();
    	 initializeNadirPoint();
			computeExtremePoints();
			computeIntercepts();
			normalizePopulation();
      }


      for (int i = 0; i < populationSize; i++) {
        int subProblemId = permutation[i];

        NeighborType neighborType = chooseNeighborType() ;
        List<DoubleSolution> parents = parentSelection(subProblemId, neighborType) ;

        List<DoubleSolution> children = crossoverOperator.execute(parents);

        DoubleSolution child = children.get(0) ;
        mutationOperator.execute(child);
        problem.evaluate(child);

        evaluations++;

        updateIdealPoint(child);
        if(normalization){
        //	initializeNormalizedObjectives();
        	normalizeOnlyIndividual(child);
        	normalizePopulation();
        }
        updateNeighborhood(child, subProblemId, neighborType);
      }
    } while (evaluations < maxEvaluations);

  }

  protected void initializePopulation() {
    population = new ArrayList<>(populationSize);
    for (int i = 0; i < populationSize; i++) {
      DoubleSolution newSolution = (DoubleSolution)problem.createSolution();

      problem.evaluate(newSolution);
      population.add(newSolution);
    }
  }

  @Override public String getName() {
    return "MOEAD" ;
  }

  @Override public String getDescription() {
    return "Multi-Objective Evolutionary Algorithm based on Decomposition" ;
  }
}
