package server;

public class Elo {
	
	private static int k = 50;

	public Elo() {
		// TODO Auto-generated constructor stub
	}

	static float Probability(float rating1, float rating2) {
		return 1.0f * 1.0f / (1 + 1.0f * (float) (Math.pow(10, 1.0f * (rating1 - rating2) / 400)));
	}
	
	static void EloRating(Account accountA, Account accountB, int seat) {
		float Ra = (float) accountA.getMmr();
		float Rb = (float) accountB.getMmr();
		
		float Pb = Probability(Ra, Rb);
		float Pa = Probability(Rb, Ra);
		
		if (seat == 0) {
			Ra = Ra + k * (1 - Pa);
			Rb = Rb + k * (0 - Pb);
		}

		else if (seat == 1){
			Ra = Ra + k * (0 - Pa);
			Rb = Rb + k * (1 - Pb);
		}
		
		if(seat != -1) {
			accountA.setMmr(Math.round(Ra));
			accountB.setMmr(Math.round(Rb));
		}	
	}

}
