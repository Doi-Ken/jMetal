//  OneZeroMax.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2012 Antonio J. Nebro
//
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
//  along with this program.  If not, see <http://www.gnu.org/licenses/>. * OneZeroMax.java

package org.uma.jmetal.problem.multiobjective ;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

/**
 * Class representing problem OneZeroMax. The problem consist of maximizing the
 * number of '1's and '0's in a binary string.
 */
public class DMP extends AbstractDoubleProblem {

	double [] c;
	double r;
	double [][] A;
	double [][] B;

	 /**
	  * Creates a new OneZeroMax problem instance
  * @param solutionType Solution type
	 * @throws ClassNotFoundException
	  */
  public DMP(String solutionType) throws ClassNotFoundException {
	  this(2, 3);
  }

 /**
  * Creates a new OneZeroMax problem instance
  * @param solutionType Solution type
  * @param numberOfBits Length of the problem
  */
  public DMP(Integer numberOfVariables, Integer numberOfObjectives) {
	  	setNumberOfVariables(numberOfVariables);
	  	 setNumberOfObjectives(numberOfObjectives);


	     List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
	     List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

		for (int var = 0; var < numberOfVariables; var++) {
			 lowerLimit.add(-50.0);
		     upperLimit.add(50.0);
		} // for
	    setLowerLimit(lowerLimit);
	    setUpperLimit(upperLimit);

		r = 25;
		c = new double [2];
		for(int i = 0;i < 2;i++){
			c[i] = 0.0;
		}

		A = new double [getNumberOfObjectives()][2];
		B = new double [getNumberOfObjectives()][getNumberOfVariables()];

		for (int o = 0; o < getNumberOfObjectives(); o++){
			A[o][0] = -1.0 * Math.sin(2.0 * Math.PI * o / getNumberOfObjectives()) * r + c[0];
			A[o][1] = Math.cos(2.0 * Math.PI * o / getNumberOfObjectives()) * r + c[1];
		}

		//basis only (1, 0, 1, 0, ..., 1, 0, 1, 0), (0, 1, 0, 1, ..., 0, 1, 0, 1)
		for (int o = 0; o < getNumberOfObjectives(); o++){
			for (int i = 0; i <getNumberOfVariables(); i++){
				B[o][i] = A[o][i % 2];
			}
		}

  } // OneZeroMax

 /**
  * Evaluates a solution
  * @param solution The solution to evaluate
  */
  public void evaluate(DoubleSolution solution){

		double[] x = new double[getNumberOfVariables()];
		double[] f = new double[getNumberOfObjectives()];

		for (int i = 0; i < getNumberOfVariables(); i++)
			x[i] = solution.getVariableValue(i);

		for (int o = 0; o < getNumberOfObjectives(); o++){
			double distance = 0.0;
			for (int i = 0; i < getNumberOfVariables(); i++){
				distance += (x[i] - B[o][i]) * (x[i] - B[o][i]);
			}
			distance = Math.sqrt(distance);
			f[o] = distance;
		}

		for (int i = 0; i < getNumberOfObjectives(); i++)
			solution.setObjective(i, f[i]);
  } // evaluate
} // OneZeroMax
