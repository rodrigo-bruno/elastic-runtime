package ch.ethz.systems.ert;

public class Environment {
	
	public static void main(String[] args) {
		System.out.println(String.format("Processors %d cores", Runtime.getRuntime().availableProcessors()));
		System.out.println(String.format("Max memory %d MBs", Runtime.getRuntime().maxMemory() / (1024 * 1024)));
	}

}
