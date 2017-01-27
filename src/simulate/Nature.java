package simulate;

import ai.Player;
import game.SevenWonders;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {

    public static void main(String[] args) {

        int numGenerations = 1;
        int playersPerGame = 6;
        int populationSize = 6;
        List<Player> population = freshPopulation(populationSize);
        int playerIndex = 0;

        for (int i = 0; i < numGenerations; i++) {

            playerIndex = 0;
            Map<Player, Integer> roulette = new HashMap<Player, Integer>();
            int totalFitness = 0;

            // everyone play
            for (int j = 0; j < populationSize / playersPerGame; j++) {
                List<Player> curPlayers = new ArrayList<Player>();
                for (int k = 0; k < playersPerGame; k++) {
                    curPlayers.add(population.get(playerIndex++));
                }
                SevenWonders game = new SevenWonders(curPlayers);
                game.run();

                for (int k = 0; k < playersPerGame; k++) {
                    int fitness = curPlayers.get(k).calculateVP();
                    roulette.put(curPlayers.get(k), fitness);
                    totalFitness += fitness;
                }
            }

            System.out.println("gen " + i);
            printPopulationStats(population);

            population = breed(populationSize, roulette, totalFitness);
//            population.addAll(freshPopulation(42));
            mutate(population);
        }
    }

    private static List<Player> freshPopulation(int populationSize) {
        List<Player> population = new ArrayList<Player>();
        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[5];
            for (int j = 0; j < weights.length; j++) {
//                weights[j] = (Math.random() * 1000);
                weights[j] = 1;
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
                return p;
            }
        }
        return null;
    }

    private static double[] mate(Player man, Player woman) {
        double[] weights = new double[man.getWeights().length];
        double[] maleWeights = man.getWeights();
        double[] femaleWeights = woman.getWeights();
        double maleFraction = ((double) man.calculateVP()) / (man.calculateVP() + woman.calculateVP());
        double femaleFraction = ((double) woman.calculateVP()) / (man.calculateVP() + woman.calculateVP());
        for (int i = 0; i < weights.length; i++) {
            weights[i] = (maleWeights[i] + femaleWeights[i]) / 2;
//            weights[i] = (maleWeights[i] * maleFraction) + (femaleWeights[i] * femaleFraction);
        }
        return weights;
    }

    private static void mutate(List<Player> population) {
        double mutationProb = 0.001;
        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.get(i).getWeights().length; j++) {
                if (Math.random() < mutationProb) {
                    population.get(i).getWeights()[j] += (Math.random() * 10) - 5;
                }
            }
        }
    }

    private static double testAgainstRandoms(List<Player> evovled, int playersPerGame) {
        int totalVP = 0;
        for (int i = 0; i < evovled.size(); i++) {

            List<Player> randoms = new ArrayList<Player>();
            randoms.addAll(freshPopulation(playersPerGame-1));
            for (int j = 0; j < randoms.size(); j++) {
                randoms.get(j).stupid();
            }
            randoms.add(evovled.get(i));

            SevenWonders game = new SevenWonders(randoms);
            game.run();
            totalVP += randoms.get(randoms.size()-1).calculateVP();
        }
        return ((double) totalVP) / evovled.size();
    }

    private static void printPopulationStats(List<Player> population) {

        int minVP = 500;
        int maxVP = 0;
        int totalVP = 0;
        for (int i = 0; i < population.size(); i++) {
            int curVP = population.get(i).calculateVP();
            totalVP += curVP;
            minVP = (curVP < minVP) ? curVP : minVP;
            maxVP = (curVP > maxVP) ? curVP : maxVP;
        }

        System.out.printf("avg VP -> %.3f\n", ((double) totalVP) / population.size());
        System.out.printf("min VP -> %d\n", minVP);
        System.out.printf("max VP -> %d\n", maxVP);
    }
}
