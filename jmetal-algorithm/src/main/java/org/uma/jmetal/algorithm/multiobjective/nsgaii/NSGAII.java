package org.uma.jmetal.algorithm.multiobjective.nsgaii;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.impl.AbstractGeneticAlgorithm;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.selection.RankingAndCrowdingSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

import Jama.Matrix;

/**
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
@SuppressWarnings("serial")
public class NSGAII<S extends Solution<?>> extends AbstractGeneticAlgorithm<S, List<S>> {
  protected final int maxEvaluations;

  protected final SolutionListEvaluator<S> evaluator;

  protected int evaluations;
	double[] zmax;

	double[][] extremePoints;

	double[] zideal;
	double[] intercepts;

	String normalization;
  /**
   * Constructor
   */
  public NSGAII(Problem<S> problem, int maxEvaluations, int populationSize,
      CrossoverOperator<S> crossoverOperator, MutationOperator<S> mutationOperator,
      SelectionOperator<List<S>, S> selectionOperator, SolutionListEvaluator<S> evaluator) {
    super(problem);
    this.maxEvaluations = maxEvaluations;
    setMaxPopulationSize(populationSize); ;

    this.crossoverOperator = crossoverOperator;
    this.mutationOperator = mutationOperator;
    this.selectionOperator = selectionOperator;
    this.evaluator = evaluator;
  }

  public void setNormalization(String normalization){
	  this.normalization = normalization;

  }

  public String getNormalization(){
	  return this.normalization;
  }

  @Override protected void initProgress() {
    evaluations = getMaxPopulationSize();
  }

  @Override protected void updateProgress() {
    evaluations += getMaxPopulationSize() ;
  }

  @Override protected boolean isStoppingConditionReached() {
    return evaluations >= maxEvaluations;
  }

  @Override protected List<S> evaluatePopulation(List<S> population) {
    population = evaluator.evaluate(population, getProblem());

    return population;
  }

  @Override protected List<S> replacement(List<S> population, List<S> offspringPopulation) {
    List<S> jointPopulation = new ArrayList<>();
    jointPopulation.addAll(population);
    jointPopulation.addAll(offspringPopulation);

    if (normalization == "normalization"){
    	computeIdealPoint(jointPopulation);
		computeMaxPoint(jointPopulation);
		computeExtremePoints(jointPopulation);
		computeIntercepts();
		normalizePopulation(jointPopulation);
	}

    RankingAndCrowdingSelection<S> rankingAndCrowdingSelection ;
    rankingAndCrowdingSelection = new RankingAndCrowdingSelection<S>(getMaxPopulationSize()) ;

    rankingAndCrowdingSelection.setNormalization(normalization);
    return rankingAndCrowdingSelection.execute(jointPopulation) ;
  }

  @Override public List<S> getResult() {
    return getNonDominatedSolutions(getPopulation());
  }

  protected List<S> getNonDominatedSolutions(List<S> solutionList) {
    return SolutionListUtils.getNondominatedSolutions(solutionList);
  }

  @Override public String getName() {
    return "NSGAII" ;
  }

  @Override public String getDescription() {
    return "Nondominated Sorting Genetic Algorithm version II" ;
  }


	void computeIdealPoint(List<S> jointPopulation) {
		zideal = new double[problem.getNumberOfObjectives()];

		for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
			zideal[j] = Double.MAX_VALUE;

			for (int i = 0; i < jointPopulation.size(); i++) {
				if (jointPopulation.get(i).getObjective(j) < zideal[j])
					zideal[j] = jointPopulation.get(i).getObjective(j);
			}
		}

	}
	void computeMaxPoint(List<S> jointPopulation) {
		zmax = new double[problem.getNumberOfObjectives()];

		for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
			zmax[j] = Double.MIN_VALUE;

			for (int i = 0; i < jointPopulation.size(); i++) {
				if (jointPopulation.get(i).getObjective(j) > zmax[j])
					zmax[j] = jointPopulation.get(i).getObjective(j);
			}
		}
	}

	void computeExtremePoints(List<S> jointPopulation) {
		extremePoints = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];

		for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
			int index = -1;
			double min = Double.MAX_VALUE;

			for (int i = 0; i < jointPopulation.size(); i++) {
				double asfValue = asfFunction(jointPopulation.get(i), j);
				if (asfValue < min) {
					min = asfValue;
					index = i;
				}
			}

			for (int k = 0; k < problem.getNumberOfObjectives(); k++)
				extremePoints[j][k] = jointPopulation.get(index).getObjective(k);
		}
	}

	void computeIntercepts() {

		intercepts = new double[problem.getNumberOfObjectives()];

		double[][] temp = new double[problem.getNumberOfObjectives()][problem.getNumberOfObjectives()];

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {
			for (int j = 0; j < problem.getNumberOfObjectives(); j++) {
				double val = extremePoints[i][j] - zideal[j];
				temp[i][j] = val;
			}
		}

		Matrix EX = new Matrix(temp);

		if (EX.rank() == EX.getRowDimension()) {
			double[] u = new double[problem.getNumberOfObjectives()];
			for (int j = 0; j < problem.getNumberOfObjectives(); j++)
				u[j] = 1;

			Matrix UM = new Matrix(u, problem.getNumberOfObjectives());

			Matrix AL = EX.inverse().times(UM);

			int j = 0;
			for (j = 0; j < problem.getNumberOfObjectives(); j++) {

				double aj = 1.0 / AL.get(j, 0) + zideal[j];

				if ((aj > zideal[j]) && (!Double.isInfinite(aj)) && (!Double.isNaN(aj)))
					intercepts[j] = aj;
				else
					break;
			}
			if (j != problem.getNumberOfObjectives()) {
				for (int k = 0; k < problem.getNumberOfObjectives(); k++)
					intercepts[k] = zmax[k];
			}

		} else {
			for (int k = 0; k < problem.getNumberOfObjectives(); k++)
				intercepts[k] = zmax[k];
		}

	}


	void normalizePopulation(List<S> jointPopulation) {
		for (int i = 0; i < jointPopulation.size(); i++) {
			S sol = jointPopulation.get(i);

			for (int j = 0; j < problem.getNumberOfObjectives(); j++) {

				double val = (sol.getObjective(j) - zideal[j])
						/ (intercepts[j] - zideal[j]);

				sol.setNormalizedObjective(j, val);
			}
		}
	}

	double asfFunction(S sol, int j) {
		double max = Double.MIN_VALUE;
		double epsilon = 1.0E-6;

		for (int i = 0; i < problem.getNumberOfObjectives(); i++) {

			double val = Math.abs(sol.getObjective(i) - zideal[i]);

			if (j != i)
				val = val / epsilon;

			if (val > max)
				max = val;
		}

		return max;
	}

}
