package simulate;

import ai.Player;
import game.SevenWonders;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {
    public static void main(String[] args) {

        int numGenerations = 1000;
        int playersPerGame = 6;
        int populationSize = 600;
        List<Player> population = freshPopulation(populationSize);
        int playerIndex = 0;

        for (int i = 0; i < numGenerations; i++) {

            playerIndex = 0;
            Map<Player, Integer> roulette = new HashMap<Player, Integer>();
            int totalFitness = 0;

            for (int j = 0; j < populationSize / playersPerGame; j++) {
                List<Player> curPlayers = new ArrayList<Player>();
                for (int k = 0; k < playersPerGame; k++) {
                    curPlayers.add(population.get(playerIndex++));
                }
                SevenWonders game = new SevenWonders(curPlayers);
                int[] results = game.run();
                for (int k = 0; k < playersPerGame; k++) {
                    roulette.put(curPlayers.get(k), (int) Math.pow(results[k], 2));
                    totalFitness += results[k];
                }
            }
            System.out.printf("gen " + i + " average vp -> %.3f\n", ((double) totalFitness) / populationSize);
            population = breed(populationSize, roulette, totalFitness);
            mutate(population);
        }
    }

    private static List<Player> freshPopulation(int populationSize) {
        List<Player> population = new ArrayList<Player>();
        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[5];
            for (int j = 0; j < weights.length; j++) {
                weights[j] = Math.random() * 1000;
            }
            Player p = new Player();
            p.setWeights(weights);
            population.add(p);
        }
        return population;
    }

    private static List<Player> breed(int populationSize, Map<Player, Integer> roulette, int totalFitness) {
        List<Player> newPopulation = new ArrayList<Player>();
        for (int i = 0; i < populationSize; i++) {
            Player man = spinTheWheel(roulette, totalFitness);
            Player woman = spinTheWheel(roulette, totalFitness);
            Player baby = new Player();
            baby.setWeights(mate(man, woman));
            newPopulation.add(baby);
        }
        return newPopulation;
    }

    private static Player spinTheWheel(Map<Player, Integer> roulette, int totalFitness) {
        int num = (int) (Math.random() * totalFitness);
        Set<Player> population = roulette.keySet();
        Player chosen = null;
        for (Player p : roulette.keySet()) {
            num -= roulette.get(p);
            if (num <= 0) {
                chosen = p;
            }
        }
        return chosen;
    }

    private static double[] mate(Player man, Player woman) {
        double[] weights = new double[man.getWeights().length];
        double[] maleWeights = man.getWeights();
        double[] femaleWeights = woman.getWeights();
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (maleWeights[i] + femaleWeights[i]) / 2;
        }
        return weights;
    }

    private static void mutate(List<Player> population) {
        double mutationProb = 0.0001;
        for (int i = 0; i < population.size(); i++) {
            if (Math.random() < mutationProb) {
                int weightIndex = (int) (Math.random() * population.get(i).getWeights().length);
                population.get(i).getWeights()[weightIndex] += ((Math.random() * 20) - 10);
            }
        }
    }
}
