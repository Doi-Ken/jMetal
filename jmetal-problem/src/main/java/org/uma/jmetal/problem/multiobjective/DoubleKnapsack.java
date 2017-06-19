package org.uma.jmetal.problem.multiobjective;

//Knapsack.java
//
//Author:
//   Antonio J. Nebro <antonio@lcc.uma.es>
//   Juan J. Durillo <durillo@lcc.uma.es>
//
//Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU Lesser General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public License
//along with this program.  If not, see <http://www.gnu.org/licenses/>.

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.management.JMException;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;

/**
* Class representing problem Knapsacak
*/
public class DoubleKnapsack extends AbstractDoubleProblem {
/**
 * Creates a default Knapsack problem (500 variables and 2 objectives)
 *
 * @param solutionType
 *            The solution type must "Real" or "BinaryReal".
 */
 		static double [] weight_limit;

		static double [][] weight;
		static double [][] original_weight;

		static double [][] profit;
		static double [][] original_profit;

		static double [][] profit_per_weight;
		static double [][] original_profit_per_weight;


		static double [] each_var_max_pro_per_wei; //(each max value of (profit[ob] / weight[ob]))
		static int constraint_objective_maximum_weight = 2; //(constraint with only 2 objecitves)

		static int [] variable_index;

		private double alpha;

		private int bits;

/**
 * Creates a Knapsack problem instance
 *
 * @param numberOfVariables
 *            Number of variables
 * @param numberOfObjectives
 *            Number of objective functions
 * @param solutionType
 *            The solution type must "Real" or "BinaryReal".
 */

public DoubleKnapsack() throws JMetalException {
    this(500, 2, 0);
 }

public DoubleKnapsack(Integer numberOfBits, Integer numberOfObjectives) throws JMetalException{
	this(numberOfBits, numberOfObjectives, 0);
}

public DoubleKnapsack(Integer numberOfBits,
		Integer numberOfObjectives, double alpha) {
	this.alpha = alpha;
	this.setNumberOfVariables(numberOfBits);
	this.bits = numberOfBits;
	this.setNumberOfObjectives(numberOfObjectives);
	this.setName("knapsack");

	weight_limit = new double [this.getNumberOfObjectives()];

	weight = new double [numberOfBits][this.getNumberOfObjectives()];
	original_weight = new double [numberOfBits][this.getNumberOfObjectives()];

	profit = new double [numberOfBits][this.getNumberOfObjectives()];
	original_profit = new double [numberOfBits][this.getNumberOfObjectives()];

	profit_per_weight = new double[numberOfBits][this.getNumberOfObjectives()];
	original_profit_per_weight = new double[numberOfBits][this.getNumberOfObjectives()];

	each_var_max_pro_per_wei = new double [numberOfBits];
	variable_index = new int [numberOfBits];


	  try {

        String filename = "knapsack";
        if(this.getNumberOfObjectives() == 2){
        	filename = getName() + "_2_" + String.valueOf(numberOfBits) + ".txt";
        }
        else{
        	filename = getName() + "_2_" + String.valueOf(numberOfBits )+ "to" + String.valueOf(getNumberOfObjectives()) + ".txt";
        	}

        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);


        String line;

        line = br.readLine();
        line = br.readLine();

        for(int o = 0;o < getNumberOfObjectives(); o++){
        	line = br.readLine();
        	weight_limit[o] = Double.valueOf(line).doubleValue();
        	for(int item = 0; item < numberOfBits;item++){
        		line = br.readLine();
        		original_weight[item][o] = Double.valueOf(line).doubleValue();
        		line = br.readLine();
        		original_profit[item][o] = Double.valueOf(line).doubleValue();
        		original_profit_per_weight[item][o] = original_profit[item][o] / original_weight[item][o];
        	}
        }

      	for(int var = 0;var < numberOfBits; var++){
        	variable_index[var] = var;
        }



		for (int var = 0; var < numberOfBits; var++){
			int max = 0;
			for (int ob = 0; ob < constraint_objective_maximum_weight; ob++){
				if (original_profit_per_weight[var][max] < original_profit_per_weight[var][ob]){
					max = ob;
				}
			}
			each_var_max_pro_per_wei[var] = original_profit_per_weight[var][max];
		}

		double temp_double = 0.0;
		int temp_int = 0;

		for (int i = 0; i < numberOfBits; i++){
			for (int j = numberOfBits - 1; j > i; j--){
				if (each_var_max_pro_per_wei[j - 1] > each_var_max_pro_per_wei[j]){
				temp_double = each_var_max_pro_per_wei[j];
				each_var_max_pro_per_wei[j] = each_var_max_pro_per_wei[j - 1];
				each_var_max_pro_per_wei[j - 1] = temp_double;


				temp_int = variable_index[j];
				variable_index[j] = variable_index[j - 1];
				variable_index[j - 1] = temp_int;
				}
			}
		}

		for(int ob = 0;ob < getNumberOfObjectives();ob++){
			for(int var = 0;var < numberOfBits;var++){
				weight[var][ob] = original_weight[variable_index[var]][ob];
			}
		}

		for(int ob = 0;ob < getNumberOfObjectives();ob++){
			for(int var = 0;var < numberOfBits;var++){
				profit[var][ob] = original_profit[variable_index[var]][ob];
			}
		}

		for(int ob = 0;ob < getNumberOfObjectives();ob++){
			for(int var = 0;var < numberOfBits;var++){
				profit_per_weight[var][ob] = original_profit_per_weight[variable_index[var]][ob];
			}
		}


        br.close();
        fr.close();

    } catch (IOException ex) {

        ex.printStackTrace();
    }

}


protected int getBitsPerVariable(int index) {
  	if (index != 0) {
  		throw new JMetalException("Problem OneZeroMax has only a variable. Index = " + index) ;
  	}
  	return bits ;
  }



/**
 * Evaluates a solution
 *
 * @param solution
 *            The solution to evaluate
 * @throws JMException
 */
public void evaluate(DoubleSolution solution) {





	//System.out.println("");
	//System.out.println("bitset.length(): " + bitset.length());
	//System.out.println("");

	//Variable[] gen = new Variable[length_[0]];

	int[] x = new int[bits];
	double[] f = new double[getNumberOfObjectives()];

	for (int var = 0; var < bits; var++){
		//System.out.println(var);
		if(solution.getVariableValue(var) == 1.0)
		x[var] = 1;
		else{
			x[var] = 0;
		}
	}

	double [] sum_weight = new double [getNumberOfObjectives()];
	double [] sum_profit = new double [getNumberOfObjectives()];

	for(int ob = 0;ob < getNumberOfObjectives();ob++){
		sum_weight[ob] = 0.0;
		sum_profit[ob] = 0.0;
	}

	int var_count = bits - 1;
	for(; var_count >= 0;var_count--){
		int flag = 0;
		for(int ob = 0;ob < constraint_objective_maximum_weight;ob++){
			if(sum_weight[ob] + original_weight[variable_index[var_count]][ob]*x[variable_index[var_count]] > weight_limit[ob]){
				flag = 1;
				break;
			}
		}
		if(flag == 0){
		for(int ob = 0;ob < getNumberOfObjectives();ob++){
			sum_weight[ob] += original_weight[variable_index[var_count]][ob]*x[variable_index[var_count]];
			sum_profit[ob] += original_profit[variable_index[var_count]][ob]*x[variable_index[var_count]];
		}
		}
		else{
		break;
		}
	}
	for(;var_count >= 0;var_count--){
		//System.out.println("change: " + variable_index[var_count]);
		x[variable_index[var_count]] = 0;
	}

	//System.out.println("");
	//System.out.println(bitset);
	//System.out.println("");


	for(int ob = 0;ob < getNumberOfObjectives();ob++){
		if(ob == 0 || ob == 1){
			f[ob] = sum_profit[ob];
		}
		else{
			if(ob % 2 == 0){
				f[ob] = alpha * f[0] + (1.0 - alpha) * sum_profit[ob];
			}
			if(ob % 2 != 0){
				f[ob] = alpha * f[1] + (1.0 - alpha) * sum_profit[ob];
			}
		}
	}


	for (int i = 0; i < getNumberOfObjectives(); i++)
		solution.setObjective(i, -1.0 * f[i]);
} // evaluate

}
