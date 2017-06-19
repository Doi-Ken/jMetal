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

package org.uma.jmetal.operator.impl.mutation;

import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;
import org.uma.jmetal.util.pseudorandom.RandomGenerator;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 * @version 1.0
 *
 * This class implements a bit flip mutation operator.
 */
@SuppressWarnings("serial")
public class DoubleBitFlipMutation implements MutationOperator<DoubleSolution>  {
  private double mutationProbability ;
  private RandomGenerator<Double> randomGenerator ;

  /** Constructor */
  public DoubleBitFlipMutation(double mutationProbability) {
	  this(mutationProbability, () -> JMetalRandom.getInstance().nextDouble());
  }

  /** Constructor */
  public DoubleBitFlipMutation(double mutationProbability, RandomGenerator<Double> randomGenerator) {
    if (mutationProbability < 0) {
      throw new JMetalException("Mutation probability is negative: " + mutationProbability) ;
    }
    this.mutationProbability = mutationProbability;
    this.randomGenerator = randomGenerator ;
  }

  /* Getter */
  public double getMutationProbability() {
    return mutationProbability;
  }

  /* Setters */
  public void setMutationProbability(double mutationProbability) {
    this.mutationProbability = mutationProbability;
  }

  /** Execute() method */
  @Override
  public DoubleSolution execute(DoubleSolution solution) throws JMetalException  {
    if (null == solution) {
      throw new JMetalException("Null parameter") ;
    }

    doMutation(mutationProbability, solution);
    return solution;
  }

  /**
   * Perform the mutation operation
   *
   * @param probability Mutation setProbability
   * @param solution    The solution to mutate
   */
  public void doMutation(double probability, DoubleSolution solution)  {
    for (int i = 0; i < solution.getNumberOfVariables(); i++) {

        if (randomGenerator.getRandomValue() <= probability) {
          if(solution.getVariableValue(i) == 1.0){
        	solution.setVariableValue(i, 0.0);;
          }
          else{
        	  solution.setVariableValue(i, 1.0);
          }
        }

    }
  }
}
