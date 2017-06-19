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
public class PDMP extends AbstractDoubleProblem {

	double [][] c;
	double  r;
	double [][][] A;
	double [][][] B;
	int polygonsize;

	 /**
	  * Creates a new OneZeroMax problem instance
  * @param solutionType Solution type
	 * @throws ClassNotFoundException
	  */
  public PDMP(String solutionType) throws ClassNotFoundException {
	  this(2, 3);
  }

 /**
  * Creates a new OneZeroMax problem instance
  * @param solutionType Solution type
  * @param numberOfBits Length of the problem
  */
  public PDMP(Integer numberOfVariables,Integer numberOfObjectives) {

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

		polygonsize = 4;
		r = 10;

		c = new double [polygonsize][2];
		c[0][0] = 30.0; c[0][1] = 30.0;
		c[1][0] = 30.0; c[1][1] = -30.0;
		c[2][0] = -30.0; c[2][1] = -30.0;
		c[3][0] = -30.0; c[3][1] = 30.0;

		A = new double [polygonsize][getNumberOfObjectives()][2];
		B = new double [polygonsize][getNumberOfObjectives()][getNumberOfVariables()];

		for(int p = 0;p < polygonsize;p++){
			for (int o = 0; o < getNumberOfObjectives(); o++){
				A[p][o][0] = -1.0 * Math.sin(2.0 * Math.PI * o / getNumberOfObjectives()) * r + c[p][0];
				A[p][o][1] = Math.cos(2.0 * Math.PI * o / getNumberOfObjectives()) * r + c[p][1];
			}
		}
		//basis only (1, 0, 1, 0, ..., 1, 0, 1, 0), (0, 1, 0, 1, ..., 0, 1, 0, 1)
		for(int p = 0;p < polygonsize;p++){
			for (int o = 0; o < getNumberOfObjectives(); o++){
				for (int i = 0; i < getNumberOfVariables(); i++){
					B[p][o][i] = A[p][o][i % 2];
				}
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
		double[] min_dis = new double [getNumberOfObjectives()];
		for(int o = 0;o < getNumberOfObjectives();o++){
			min_dis[o] = 10000000000000.0;
		}

		for (int i = 0; i < getNumberOfVariables(); i++)
			x[i] = solution.getVariableValue(i);

		for (int p = 0; p < polygonsize; p++){
			for (int o = 0; o < getNumberOfObjectives(); o++){
				double distance = 0.0;
				for (int i = 0; i < getNumberOfVariables(); i++){
					distance += (x[i] - B[p][o][i]) * (x[i] - B[p][o][i]);
				}
				distance = Math.sqrt(distance);
				if (min_dis[o] > distance){
					min_dis[o] = distance;
				}
			}
		}
		for (int o = 0; o < getNumberOfObjectives(); o++){
			f[o] = min_dis[o];
		}

		for (int i = 0; i < getNumberOfObjectives(); i++)
			solution.setObjective(i, f[i]);
  } // evaluate
} // OneZeroMax
