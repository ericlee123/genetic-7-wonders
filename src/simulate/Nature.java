package simulate;

import ai.*;
import game.Player;
import game.SevenWonders;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {

    public static void main(String[] args) {

        int numGenerations = 100;
        int playersPerGame = 6;
        int populationSize = 1000;
        List<Player> population = freshPopulation(populationSize);

        // save the first generation players
        List<Player> babies = new ArrayList<Player>();
        for (int i = 0; i < population.size(); i++) {
            Player baby = new Player(population.get(i));
            baby.setStrategy(population.get(i).getStrategy());
            babies.add(baby);
        }

        // current strategy is to train against greedy
        System.out.println("training for " + numGenerations + " generations");
        for (int i = 0; i < numGenerations; i++) {

            // progress
            if (i % (numGenerations / 10) == 0) {
                double min = Double.MAX_VALUE;
                double max = Double.MIN_VALUE;
                double avg = 0;
                for (int j = 0; j < population.size(); j++) {
                    double weight = population.get(j).getWeights()[1][0];
                    avg += weight;
                    min = (weight < min) ? weight : min;
                    max = (weight > max) ? weight : max;
                }
                System.out.println(i + " | " + min + " " + (avg/population.size()) + " " + max);
            }

            Map<Player, Integer> roulette = new HashMap<Player, Integer>();
            int totalFitness = 0;
            for (int j = 0; j < populationSize; j++) {
                playAgainstGreedy(population.get(j), playersPerGame);
                int fitness = population.get(j).calculateVP();
                int rouletteFitness = (int) Math.pow(fitness, 3); // bigger bias towards higher scoring ppl
                roulette.put(population.get(j), rouletteFitness);
                totalFitness += rouletteFitness;
            }
            population = breed(populationSize, roulette, totalFitness);
            mutate(population);
        }
        System.out.println();

        // make new players with only weights copied over
        List<Player> evolved = new ArrayList<Player>();
        for (int i = 0; i < population.size(); i++) {
            Player p = new Player();
            p.setWeights(population.get(i).getWeights());
            p.setStrategy(population.get(i).getStrategy());
            evolved.add(p);
        }

        // make population of greedy and random
        List<Player> greedies = new ArrayList<Player>();
        List<Player> randalls = new ArrayList<Player>();
        Greedy g = new Greedy();
        ai.Random r = new ai.Random();
        for (int i = 0; i < population.size(); i++) {
            Player greedy = new Player();
            Player randall = new Player();
            greedy.setStrategy(g);
            randall.setStrategy(r);
            greedies.add(greedy);
            randalls.add(randall);
        }

        // compare evolved against first generation, greedy, and random
        int numTests = 100;
        System.out.println(numTests + " tests");
        System.out.print("comparing evolved against first generation | ");
        compare(evolved, babies, numTests);
        System.out.print("comparing evolved against greedies | ");
        compare(evolved, greedies, numTests);
        System.out.print("comparing evolved against randoms | ");
        compare(evolved, randalls, numTests);
        System.out.print("comparing greedies against first generation | ");
        compare(greedies, babies, numTests);
    }

    private static List<Player> freshPopulation(int populationSize) {
        List<Player> population = new ArrayList<Player>();
        Weighted w = new Weighted();
        for (int i = 0; i < populationSize; i++) {
            double[][] weights = new double[SevenWonders.NUM_AGES][Player.NUM_WEIGHTS];
            for (int j = 0; j < weights.length; j++) {
                for (int k = 0; k < weights[j].length; k++) {
                    weights[j][k] = Math.random(); // [0, 1) is a sufficient range, right? yes
                }
            }
            Player p = new Player();
            p.setWeights(weights);
            p.setStrategy(w);
            population.add(p);
        }
        return population;
    }

    private static void playAgainstGreedy(Player smart, int playersPerGame) {
        List<Player> players = new ArrayList<Player>();
        Greedy g = new Greedy();
        for (int i = 0; i < playersPerGame-1; i++) {
            Player p = new Player();
            p.setStrategy(g);
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
            baby.setStrategy(man.getStrategy());
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

    private static double[][] mate(Player man, Player woman) {
        double[][] weights = new double[man.getWeights().length][man.getWeights()[0].length];
        double[][] maleWeights = man.getWeights();
        double[][] femaleWeights = woman.getWeights();
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (maleWeights[i][j] + femaleWeights[i][j]) / 2;
            }
        }
        return weights;
    }

    private static void mutate(List<Player> population) {
        double mutationProb = 0.001;
        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.get(i).getWeights().length; j++) {
                for (int k = 0; k < population.get(i).getWeights()[j].length; k++) {
                    if (Math.random() < mutationProb) {
                        population.get(i).getWeights()[j][k] += (Math.random() * 0.4) - 0.2;
                    }
                }
            }
        }
    }

    private static void compare(List<Player> evolved, List<Player> test, int numTests) {
        int evolvedWins = 0;
        for (int i = 0; i < numTests; i++) {

            // show progress
            if (i % 10 == 0) {
                System.out.print(i + " ");
            }

            int testVP = 0;
            int evolvedVP = 0;
            for (int j = 0; j < evolved.size(); j++) {
                Player e = new Player();
                e.setWeights(evolved.get(j).getWeights());
                e.setStrategy(evolved.get(j).getStrategy());
                Player t = new Player();
                t.setWeights(test.get(j).getWeights());
                t.setStrategy(test.get(j).getStrategy());
                playAgainstGreedy(t, 6);
                playAgainstGreedy(e, 6);
                testVP += t.calculateVP();
                evolvedVP += e.calculateVP();
            }
            if (evolvedVP > testVP) {
                evolvedWins++;
            }
        }
        System.out.println();
        System.out.println("evolved wins -> " + evolvedWins);
        System.out.println("test wins    -> " + (numTests - evolvedWins));
        System.out.println("--------------------------------------------");
    }
}
