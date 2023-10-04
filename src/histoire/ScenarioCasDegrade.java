package histoire;
import villagegaulois.Etal;
import personnages.Gaulois;

public class ScenarioCasDegrade {
	public static void main(String[] args) {
		Etal etal = new Etal();
		Gaulois asterix = new Gaulois("Asterix", 5);
		Gaulois obelix = new Gaulois("Obelix", 20);
		etal.libererEtal();
		System.out.println("Fin du test libererEtal");
		etal.occuperEtal(null, "sangliers", 40);
		etal.acheterProduit(1, null);
		System.out.println("Fin du test acheterProduit acheteur null");
		etal.libererEtal();
		etal.occuperEtal(obelix, "menhirs", 15);
		try {
			etal.acheterProduit((-46), asterix);
		} catch (IllegalArgumentException iArg) {
			iArg.printStackTrace();
		}
		System.out.println("Fin du test acheterProduit quantité < 0");
		Etal etalNonOccupe = new Etal();
		try {
			etalNonOccupe.acheterProduit(3, obelix);
		} catch (IllegalStateException iState) {
			iState.printStackTrace();
		}
		System.out.println("Fin du test acheterProduit étal vide");
		
	}
}