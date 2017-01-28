package simulate;

import ai.Player;
import game.SevenWonders;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {

    public static void main(String[] args) {

        int numGenerations = 100;
        int playersPerGame = 6;
        int populationSize = 100;
        List<Player> population = freshPopulation(populationSize);

        for (int i = 0; i < numGenerations; i++) {

            Map<Player, Integer> roulette = new HashMap<Player, Integer>();
            int totalFitness = 0;

            double weight = 0;
            for (int j = 0; j < population.size(); j++) {
                weight += population.get(j).getWeights()[0];
            }
            if (i == 0 || i == numGenerations-1) {
                System.out.printf("avg weight -> %.3f\n", weight / population.size());
            }

            // play everyone against randoms
            for (int j = 0; j < populationSize; j++) {
                playAgainstRandoms(population.get(j), playersPerGame);

                int fitness = population.get(j).calculateVP() - 50;
                if (fitness < 0) {
                    continue;
                }
                roulette.put(population.get(j), (int) Math.pow(fitness, 2));
                totalFitness += fitness;
            }

            int totalVP = 0;
            for (int j = 0; j < population.size(); j++) {
                totalVP += population.get(j).calculateVP();
            }
            if (i == 0 || i == numGenerations-1) {
                System.out.printf("avg vp -> %.3f\n", ((double) totalVP) / population.size());
            }

            population = breed(populationSize-50, roulette, totalFitness);
            population.addAll(freshPopulation(50));
//            mutate(population);
        }
    }

    private static List<Player> freshPopulation(int populationSize) {
        List<Player> population = new ArrayList<Player>();
        for (int i = 0; i < populationSize; i++) {
            double[] weights = new double[5];
            for (int j = 0; j < weights.length; j++) {
                weights[j] = (Math.random()*60) - 20;
            }
            Player p = new Player();
            p.setWeights(weights);
            population.add(p);
        }
        return population;
    }

    private static void playAgainstRandoms(Player smart, int playersPerGame) {

        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < playersPerGame-1; i++) {
            Player p = new Player();
//            p.stupid();
            double[] weights = new double[5];
            for (int j = 0; j < weights.length; j++) {
                weights[j] = 1;
            }
            p.setWeights(weights);
            players.add(p);
        }
        players.add(smart);

        SevenWonders game = new SevenWonders(players);
        game.run();
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
        for (int i = 0; i < 1; i++) {
            weights[i] = (maleWeights[i] * maleFraction) + (femaleWeights[i] * femaleFraction);
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

    private static double totalAvgVP = 0;

    private static void printPopulationStats(List<Player> population) {

        int minVP = 500;
        int maxVP = 0;
        int totalVP = 0;
        double weight0 = 0;
        for (int i = 0; i < population.size(); i++) {
            int curVP = population.get(i).calculateVP();
            totalVP += curVP;
            minVP = (curVP < minVP) ? curVP : minVP;
            maxVP = (curVP > maxVP) ? curVP : maxVP;

            weight0 += population.get(i).getWeights()[0];
        }

        System.out.printf("avg VP -> %.3f\n", ((double) totalVP) / population.size());
        totalAvgVP += ((double) totalVP) / population.size();
        System.out.printf("avg weight -> %.3f\n", weight0 / population.size());
//        System.out.printf("fuck %.3f\n\n", weight0 / population.size());
//        System.out.printf("min VP -> %d\n", minVP);
//        System.out.printf("max VP -> %d\n", maxVP);
    }
}
