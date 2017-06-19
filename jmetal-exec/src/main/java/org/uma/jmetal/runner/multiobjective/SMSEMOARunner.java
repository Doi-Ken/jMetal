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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.smsemoa.SMSEMOABuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.RandomSelection;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.multiobjective.DMP;
import org.uma.jmetal.problem.multiobjective.PDMP;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ4;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ5;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ6;
import org.uma.jmetal.problem.multiobjective.dtlz.DTLZ7;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ1;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ2;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ3;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ4;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ5;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ6;
import org.uma.jmetal.problem.multiobjective.dtlz.maxDTLZ7;
import org.uma.jmetal.problem.multiobjective.wfg.WFG1;
import org.uma.jmetal.problem.multiobjective.wfg.WFG2;
import org.uma.jmetal.problem.multiobjective.wfg.WFG3;
import org.uma.jmetal.problem.multiobjective.wfg.WFG4;
import org.uma.jmetal.problem.multiobjective.wfg.WFG5;
import org.uma.jmetal.problem.multiobjective.wfg.WFG6;
import org.uma.jmetal.problem.multiobjective.wfg.WFG7;
import org.uma.jmetal.problem.multiobjective.wfg.WFG8;
import org.uma.jmetal.problem.multiobjective.wfg.WFG9;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG1;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG2;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG3;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG4;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG5;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG6;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG7;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG8;
import org.uma.jmetal.problem.multiobjective.wfg.maxWFG9;
import org.uma.jmetal.qualityindicator.impl.Hypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.runner.AbstractAlgorithmRunner;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;

/**
 * Class to configure and run the SMSEMOA algorithm
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class SMSEMOARunner extends AbstractAlgorithmRunner {
  /**
   * @param args Command line arguments.
   * @throws SecurityException
   * Invoking command:
  java org.uma.jmetal.runner.multiobjective.SMSEMOARunner problemName [referenceFront]
   */
  public static void main(String[] args) throws JMetalException, FileNotFoundException {
    DoubleProblem problem;
    Algorithm<List<DoubleSolution>> algorithm;
    CrossoverOperator<DoubleSolution> crossover;
    MutationOperator<DoubleSolution> mutation;
    SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;


    int experimental_number = 10;
    String problemName_ = "dtlz2";
    int NumberOfObjectives_ = 3;
    int population_size = 100;
    int generation_size = 50;
    double crossoverDistributionIndex = 0.5 ; // is translated into f
    double mutationDistributionIndex = 20.0 ;

    double mutation_probability = 1.0;
    double crossoverProbability = 0.9 ; // is translated into cr

    int k_fac = 4;
    int l_fac = 20;
    int k_dtlz = 5;
    int H1_ = 99;
    int H2_ = 0;


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
    	}
    }

    int var_num = k_dtlz + NumberOfObjectives_ - 1;
    problem = new DTLZ1(6, 2);

    problem = null;
	 if(problemName_.equals("dtlz1")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ1(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz2")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ2(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz3")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ3(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz4")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ4(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz5")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ5(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz6")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ6(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("dtlz7")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ7(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz1")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ1(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz2")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ2(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz3")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ3(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz4")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ4(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz5")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ5(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz6")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ6(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxdtlz7")){
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new maxDTLZ7(var_num, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg1")){
		 var_num = k_fac + l_fac;
		 problem = new WFG1(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg2")){
		 var_num = k_fac + l_fac;
		 problem = new WFG2(k_fac, l_fac, NumberOfObjectives_);

	 }
	 else if(problemName_.equals("wfg3")){
		 var_num = k_fac + l_fac;
		 problem = new WFG3(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg4")){
		 var_num = k_fac + l_fac;
		 problem = new WFG4(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg5")){
		 var_num = k_fac + l_fac;
		 problem = new WFG5(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg6")){
		 var_num = k_fac + l_fac;
		 problem = new WFG6(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg7")){
		 var_num = k_fac + l_fac;
		 problem = new WFG7(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg8")){
		 var_num = k_fac + l_fac;
		 problem = new WFG8(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("wfg9")){
		 var_num = k_fac + l_fac;
		problem = new WFG9(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg1")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG1(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg2")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG2(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg3")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG3(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg4")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG4(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg5")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG5(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg6")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG6(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg7")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG7(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg8")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG8(k_fac, l_fac, NumberOfObjectives_);
	 }
	 else if(problemName_.equals("maxwfg9")){
		 var_num = k_fac + l_fac;
		problem = new maxWFG9(k_fac, l_fac, NumberOfObjectives_);
	 }else if(problemName_.equals("dmp")){
		 var_num = k_dtlz;
		 problem = new DMP(var_num, NumberOfObjectives_);

	 } else if(problemName_.equals("pdmp")){
		 var_num = k_dtlz;
		 problem = new PDMP(var_num, NumberOfObjectives_);

	 }
	 else{
		 var_num = k_dtlz + NumberOfObjectives_ - 1;
		 problem = new DTLZ1(var_num, NumberOfObjectives_);
	 }


    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

    double mutationProbability = mutation_probability  / problem.getNumberOfVariables() ;
    mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex) ;

    selection = new RandomSelection<DoubleSolution>();

    Hypervolume<DoubleSolution> hypervolume ;
    hypervolume = new PISAHypervolume<>() ;
    hypervolume.setOffset(100.0);

    algorithm = new SMSEMOABuilder<DoubleSolution>(problem, crossover, mutation)
        .setSelectionOperator(selection)
        .setMaxEvaluations(generation_size * population_size)
        .setPopulationSize(population_size)
        .setHypervolumeImplementation(hypervolume)
        .build() ;

    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
        .execute() ;

    List<DoubleSolution> population = algorithm.getResult() ;
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

	if(problemName_.equals("dmp") || problemName_.equals("pdmp")){
		String varfilename = "var" + experimental_number + ".txt";
		FileOutputStream fos2;
		OutputStreamWriter osw2;
		BufferedWriter bw2;
		try {
			fos2 = new FileOutputStream(varfilename);
			osw2 = new OutputStreamWriter(fos2);
			bw2 = new BufferedWriter(osw2);
			for (int i = 0; i < population.size(); i++) {

				double x_v1 = 0.0;
				double v1norm = 0.0;
				double x_v2 = 0.0;
				double v2norm = 0.0;
				for (int it = 0; it < var_num; it++){
					if ((it % 2) == 0){
						x_v1 += population.get(i).getVariableValue(it);
						v1norm += 1.0;
					}
					else{
						x_v2 +=  population.get(i).getVariableValue(it);
						v2norm += 1.0;
					}
				}
				try {
		        	 bw2.write(x_v1 / v1norm + "\t" + x_v2 / v2norm);
		        } catch (IOException e) {
						// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}

				try {
		    	   bw2.newLine();
		       } catch (IOException e) {
				// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
		    }
			try {
				bw2.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
	 }

  }
}
