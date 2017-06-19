//  NSGAIIRunner.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//
//  Copyright (c) 2014 Antonio J. Nebro
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
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.runner.multiobjective;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.UniformedCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.multiobjective.Knapsack;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalLogger;

/**
 * Class for configuring and running the NSGA-II algorithm (binary encoding)
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class NSGAIIBinaryRunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws org.uma.jmetal.util.JMetalException
   * @throws java.io.IOException
   * @throws SecurityException
   * @throws ClassNotFoundException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.NSGAIIBinaryRunner problemName [referenceFront]
   */
  public static void main(String[] args) throws
          Exception {

    BinaryProblem problem;
    Algorithm<List<BinarySolution>> algorithm;
    CrossoverOperator<BinarySolution> crossover;
    MutationOperator<BinarySolution> mutation;
    SelectionOperator<List<BinarySolution>, BinarySolution> selection;

    int experimental_number = 10;
    String problemName_ = "dtlz2";
    int NumberOfObjectives_ = 2;
    int population_size = 100;
    int generation_size = 400;
    double crossoverDistributionIndex = 20.0 ;
    double mutationDistributionIndex = 20.0 ;

    double mutation_probability = 1.0;
    double crossoverProbability = 0.9 ;

    int k_fac = 4;
    int l_fac = 20;
    int k_dtlz = 5;
    int H1_ = 99;
    int H2_ = 0;
    double knapalpha_ = 0.0;

    if(args.length % 2 != 0){
    	System.out.println("wrong number(args)");
    	System.exit(-1);
    }
    else{
    	for(int i = 0;i < args.length;i += 2){
    		if(args[i].equals("-num")){
    			experimental_number = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-pro")){
    			problemName_ = args[i + 1];
    		}
    		else if(args[i].equals("-obj")){
    			NumberOfObjectives_ = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-pop")){
    			population_size = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-gen")){
    			generation_size = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-sbxeta")){
    			crossoverDistributionIndex = Double.parseDouble(args[i + 1]);
    		}
    		else if(args[i].equals("-muteta")){
    			mutationDistributionIndex = Double.parseDouble(args[i + 1]);
    		}
    		else if(args[i].equals("-co")){
    			crossoverProbability = Double.parseDouble(args[i + 1]);
    		}
    		else if(args[i].equals("-mut")){
    			mutation_probability = Double.parseDouble(args[i + 1]);
    		}
    		else if(args[i].equals("-k_fac")){
    			k_fac = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-l_fac")){
    			l_fac = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-k_dtlz")){
    			k_dtlz = Integer.parseInt(args[i + 1]);
    		}
    		else if(args[i].equals("-dev1")){
    			H1_ = Integer.parseInt(args[i+1]);
    		}
    		else if(args[i].equals("-dev2")){
    			H2_ = Integer.parseInt(args[i+1]);
    		}
    		else if(args[i].equals("-knapalpha")){
    			knapalpha_ = Double.parseDouble(args[i+1]);
    		}
    	}
    }


    if(population_size % 2 != 0){
		 population_size++;
	 }

    int var_num = 500;
    problem = new Knapsack(500, 2, 0);

    problem = null;
    if(problemName_.equals("dtlz1")){
		 var_num = 500;
		 problem = new Knapsack(var_num, NumberOfObjectives_, knapalpha_);
	 }
   else{
   	problem = new Knapsack(var_num, NumberOfObjectives_, knapalpha_);
   }


    //problem = (BinaryProblem) ProblemUtils.<BinarySolution> loadProblem(problemName);


    //crossover = new SinglePointCrossover(crossoverProbability) ;
    crossover = new UniformedCrossover(crossoverProbability);

    double mutationProbability = mutation_probability / problem.getNumberOfBits(0) ;
    mutation = new BitFlipMutation(mutationProbability) ;

    selection = new BinaryTournamentSelection<BinarySolution>() ;

    algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation)
            .setSelectionOperator(selection)
            .setMaxEvaluations(generation_size * population_size)
            .setPopulationSize(population_size)
            .setNormalization("normal")//it is also used as normalize flag (if "normalization" is specified, you can use normalization)
            .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
            .execute() ;

    List<BinarySolution> population = algorithm.getResult() ;
    long computingTime = algorithmRunner.getComputingTime() ;

    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");

    printFinalSolutionSet(population);

    String filename = "graph" + experimental_number + ".txt";
    FileOutputStream fos = new FileOutputStream(filename);
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
  }
}
