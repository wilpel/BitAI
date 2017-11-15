package tests;


import com.BitAI.neural.NeuralNetwork;
import com.BitAI.neural.activation.ActivationTANH;
import com.BitAI.neural.layers.LayerFactory;

public class test001 {

	public static void main(String[] args) {

		LayerFactory lf = new LayerFactory();

		lf.addLayer(2, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);
		lf.addLayer(8, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);
		lf.addLayer(1, new ActivationTANH(), LayerFactory.TYPE_SIMPLELAYER);

		NeuralNetwork network = new NeuralNetwork(lf);

		
		for(int i = 0; i < network.getLayers().length; i++) {
			
			for(int x = 0; x < network.getLayers()[i].weights.length; x++) {
				for(int y = 0; y < network.getLayers()[i].weights[0].length; y++) {
					
					System.out.println(network.getLayers()[i].weights[x][y]);
					
				}
			}
			
		}
		
		for (int i = 0; i < 100000; i++) {
			
			network.compute(new float[] { 1, 0 });
			network.backPropagate(new float[] { 1 });
			
			network.compute(new float[] { 0, 0 });
			network.backPropagate(new float[] { 0 });
			
			network.compute(new float[] { 0, 1 });
			network.backPropagate(new float[] { 1 });
			
			network.compute(new float[] { 1, 1 });
			network.backPropagate(new float[] { 0 });

			
		}

		System.out.println("--------------");

		for(int i = 0; i < network.getLayers().length; i++) {
			
			for(int x = 0; x < network.getLayers()[i].weights.length; x++) {
				for(int y = 0; y < network.getLayers()[i].weights[0].length; y++) {
					
					System.out.println(network.getLayers()[i].weights[x][y]);
					
				}
			}
			
		}
		
		System.out.println("--------------");
		
		System.out.println(Math.round(network.compute(new float[] { 1, 0 })[0]));
		System.out.println(Math.round(network.compute(new float[] { 0, 0 })[0]));
		
		System.out.println(Math.round(network.compute(new float[] { 1, 1 })[0]));
		System.out.println(Math.round(network.compute(new float[] { 0, 1 })[0]));

	}

}
