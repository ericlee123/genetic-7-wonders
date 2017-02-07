package simulate;

import ai.Greedy;
import game.Player;
import game.SevenWonders;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {

    public static void main(String[] args) {

        int numGenerations = 1000;
        int playersPerGame = 6;
        int populationSize = 1000;
        List<Player> population = freshPopulation(populationSize);

        List<Player> babies = new ArrayList<Player>();
        babies.addAll(population);

        for (int i = 0; i < numGenerations; i++) {

            if (i % 50 == 0) {
                double[] totalWeights = new double[6];
                for (int j = 0; j < population.size(); j++) {
                    for (int k = 0; k < population.get(j).getWeights().length; k++) {
//                        totalWeights[k] += population.get(j).getWeights()[k];
                    }
                }
                System.out.print("gen " + i + " |");
                for (int j = 0; j < totalWeights.length; j++) {
                    System.out.printf(" %.2f", totalWeights[j] / population.size());
                }
                System.out.println();
            }

            // play everyone against randoms
            Map<Player, Integer> roulette = new HashMap<Player, Integer>();
            int totalFitness = 0;
            for (int j = 0; j < populationSize; j++) {
                playAgainstGreedy(population.get(j), playersPerGame);

                int fitness = population.get(j).calculateVP();

                roulette.put(population.get(j), (int) Math.pow(fitness, 2));
                totalFitness += fitness;
            }

            population = breed(populationSize, roulette, totalFitness);
            mutate(population);
        }

        // make new players with only weights copied over
        List<Player> evolved = new ArrayList<Player>();
        for (int i = 0; i < population.size(); i++) {
            Player p = new Player();
            p.setWeights(population.get(i).getWeights());
            evolved.add(p);
        }

        compare(babies, evolved, 1000);
    }

    private static List<Player> freshPopulation(int populationSize) {
        List<Player> population = new ArrayList<Player>();
        for (int i = 0; i < populationSize; i++) {
            double[][] weights = new double[3][6];
            for (int j = 0; j < weights.length; j++) {
                for (int k = 0; k < weights[j].length; k++) {
                    weights[j][k] = (Math.random()*20) - 10;
                }
            }
            Player p = new Player();
            p.setWeights(weights);
            population.add(p);
        }
        return population;
    }

    private static void playAgainstGreedy(Player smart, int playersPerGame) {
        Greedy g = new Greedy();
        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < playersPerGame-1; i++) {
            Player p = new Player();
            p.setStrategy(g);
            players.add(p);
        }
        players.add(smart);
        SevenWonders game = new SevenWonders(players);
        game.run();
    }

    private static void playAgainstRandoms(Player smart, int playersPerGame) {

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

    private static double[][] mate(Player man, Player woman) {
        double[][] weights = new double[man.getWeights().length][man.getWeights()[0].length];
        double[][] maleWeights = man.getWeights();
        double[][] femaleWeights = woman.getWeights();
        double maleFraction = ((double) man.calculateVP()) / (man.calculateVP() + woman.calculateVP());
        double femaleFraction = ((double) woman.calculateVP()) / (man.calculateVP() + woman.calculateVP());
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                weights[i][j] = (maleWeights[i][j] * maleFraction) + (femaleWeights[i][j] * femaleFraction);
            }
        }
        return weights;
    }

    private static void mutate(List<Player> population) {
        double mutationProb = 0.01;
        for (int i = 0; i < population.size(); i++) {
            for (int j = 0; j < population.get(i).getWeights().length; j++) {
                if (Math.random() < mutationProb) {
                    population.get(i).getWeights()[j] += (Math.random() * 5) - 2.5;
                }
            }
        }
    }

    private static void compare(List<Player> babies, List<Player> evolved, int numTests) {
        int evolvedWins = 0;
        for (int i = 0; i < numTests; i++) {
            if (i % 100 == 0) {
                System.out.println("test " + i);
            }
            int babyVP = 0;
            int evolvedVP = 0;
            for (int j = 0; j < babies.size(); j++) {
                Player b = new Player();
                Player e = new Player();
                b.setWeights(babies.get(j).getWeights());
                e.setWeights(evolved.get(j).getWeights());
                playAgainstGreedy(b, 6);
                playAgainstGreedy(e, 6);
                babyVP += b.calculateVP();
                evolvedVP += e.calculateVP();
            }
            if (evolvedVP > babyVP) {
                evolvedWins++;
            }
        }
        System.out.println("baby wins    -> " + (numTests - evolvedWins));
        System.out.println("evolved wins -> " + evolvedWins);
    }
}
