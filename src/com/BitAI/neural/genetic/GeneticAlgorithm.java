package com.BitAI.neural.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.BitAI.neural.NeuralNetwork;

public class GeneticAlgorithm {

	private Mutator mutator;
	int population, iretations;
	NeuralNetwork network;

	public GeneticAlgorithm(NeuralNetwork network, int population, int iretations) {

		this.network = network;
		this.population = population;
		this.iretations = iretations;

		mutator = new Mutator();

	}

	public NeuralNetwork train(NetworkScore netScore) {

		float highestScore = 0;
		int id = 0;
		
		List<NetworkScoreResult> scoredNetworks = new ArrayList<NetworkScoreResult>();

		List<NeuralNetwork> generatedNetworks = generatePopulation(network, population);

		for (int i = 0; i < iretations; i++) {

			for (int j = 0; j < generatedNetworks.size(); j++) {

				float score = netScore.calculateNetworkScore(generatedNetworks.get(j));
				scoredNetworks.add(new NetworkScoreResult(score, generatedNetworks.get(j)));

			}



			
			for(int j = 0;  j < scoredNetworks.size(); j++) {
				//System.out.println(scoredNetworks.get(j).getScore()+">"+highestScore+" : "+(scoredNetworks.get(j).getScore()>highestScore));
				if(scoredNetworks.get(j).getScore()>highestScore) {
					
					System.out.println("score: "+scoredNetworks.get(j).getScore()+" because "+scoredNetworks.get(j).getScore()+" is > than "+highestScore);	
					id = j;
					highestScore = scoredNetworks.get(j).getScore();
				}
			}
			
			generatedNetworks = generatePopulation(scoredNetworks.get(id).getNeuralNetwork(), population);

		}

		return scoredNetworks.get(id).getNeuralNetwork();

	}

	private List<NeuralNetwork> generatePopulation(NeuralNetwork network, int length) {

		List<NeuralNetwork> generated = new ArrayList<NeuralNetwork>();

		generated.add(network);
		generated.add(mutator.mutate(network, 0, 10));
		for (int i = 0; i < length-2; i++) {
			generated.add(mutator.mutate(network, 0.99f, 1.01f));
		}

		return generated;
	}

}
