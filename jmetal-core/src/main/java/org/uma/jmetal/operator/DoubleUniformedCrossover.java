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

package org.uma.jmetal.operator;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 * This class implements a single point crossover operator.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class DoubleUniformedCrossover implements CrossoverOperator<DoubleSolution> {
  private double crossoverProbability ;
  private JMetalRandom randomGenerator ;

  /** Constructor */
  public DoubleUniformedCrossover(double crossoverProbability) {
    if (crossoverProbability < 0) {
      throw new JMetalException("Crossover probability is negative: " + crossoverProbability) ;
    }
    this.crossoverProbability = crossoverProbability;
    randomGenerator = JMetalRandom.getInstance() ;
  }

  /* Getter */
  public double getCrossoverProbability() {
    return crossoverProbability;
  }

  @Override
  public List<DoubleSolution> execute(List<DoubleSolution> solutions) {
    if (solutions == null) {
      throw new JMetalException("Null parameter") ;
    } else if (solutions.size() != 2) {
      throw new JMetalException("There must be two parents instead of " + solutions.size()) ;
    }

    return doCrossover(crossoverProbability, solutions.get(0), solutions.get(1)) ;
  }

  /**
   * Perform the crossover operation.
   *
   * @param probability Crossover setProbability
   * @param parent1     The first parent
   * @param parent2     The second parent
   * @return An array containing the two offspring
   */
  public List<DoubleSolution> doCrossover(double probability, DoubleSolution parent1, DoubleSolution parent2)  {
	  List<DoubleSolution> offspring = new ArrayList<>(2);
    offspring.add((DoubleSolution) parent1.copy()) ;
    offspring.add((DoubleSolution) parent2.copy()) ;

    if (randomGenerator.nextDouble() < probability) {
      // 1. Get the total number of bits
      int totalNumberOfBits = parent1.getNumberOfVariables();

      // 2. Calculate the point to make the crossover
      int[] crossoverMask = new int[parent1.getNumberOfVariables()];


      for(int i = 0; i < totalNumberOfBits; i++){
    	  crossoverMask[i] = randomGenerator.nextInt(0, 1);
      }


      // 4. Apply the crossover to the variable;
      DoubleSolution offspring1, offspring2;
      offspring1 = (DoubleSolution) parent1.copy();
      offspring2 = (DoubleSolution) parent2.copy();

      for (int i = 0; i < offspring1.getNumberOfVariables(); i++) {
        if(crossoverMask[i] == 1){
        	double swap = offspring1.getVariableValue(i);
        	offspring1.setVariableValue(i, offspring2.getVariableValue(i));
        	offspring2.setVariableValue(i, swap);
        }
      }

      // 5. Apply the crossover to the other variables
      for (int i = 0; i < parent1.getNumberOfVariables(); i++) {
        offspring.get(0).setVariableValue(i, parent2.getVariableValue(i));
        offspring.get(1).setVariableValue(i, parent1.getVariableValue(i));
      }

    }
    return offspring ;
  }

  /**
   * Two parents are required to apply this operator.
   * @return
   */
  public int getNumberOfParents() {
    return 2 ;
  }
}
