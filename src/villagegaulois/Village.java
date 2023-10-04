package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;
import villagegaulois.VillageSansChefException;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private int nbEtalsMax;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtalsMax) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtalsMax);
	}

	/* Classe interne Marche*/
	private static class Marche {
		private Etal[] etals;
		
		/*Constructeur*/
		private Marche(int nbEtals) {
			this.etals = new Etal[nbEtals];
			for (int i = 0; i < nbEtals; i++) {
				etals[i] = new Etal();
			}
		}
		
		/*Méthodes*/
		private void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			assert (indiceEtal > 0 && indiceEtal <= etals.length);
			etals[indiceEtal-1].occuperEtal(vendeur, produit, nbProduit);
		}
		
		private int trouverEtalLibre() {
			int indiceEtal = 0;
			boolean trouve = false;
			while (!trouve && indiceEtal < etals.length) {
				if (!etals[indiceEtal].isEtalOccupe())
					trouve = true;
				else
					indiceEtal++;
			}
			return (trouve)? (++indiceEtal) : -1; //car les étals sont numérotés de 1 à ...
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbEtalsAvecProduit = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
					nbEtalsAvecProduit++;
				}
			}
			Etal[] etalsAvecProduit = new Etal[nbEtalsAvecProduit];
			int indDernierAjoute = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
					etalsAvecProduit[indDernierAjoute] = etal;
					indDernierAjoute++;
				}
			}
			return etalsAvecProduit;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			int indiceEtal = 0;
			while (indiceEtal < etals.length) {
				Etal etalVu = etals[indiceEtal];
				if (etalVu.getVendeur() == gaulois)
					return etalVu;
			}
			return null;
		}
		
		private String afficherMarche() {
			StringBuilder chaine = new StringBuilder();
			int nbEtalVide = 0;
			for (Etal etal : etals) {
				if (etal.isEtalOccupe())
					chaine.append(etal.afficherEtal());
				else
					nbEtalVide++;
			}
			if (nbEtalVide != 0)
				chaine.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
			return chaine.toString();
		}
	}
	
	/*Méthodes d'intéraction avec Marché*/
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + ' ' + produit + ".\n");
		int indEtalLibre = marche.trouverEtalLibre();
		assert (indEtalLibre != -1);
		marche.utiliserEtal(indEtalLibre, vendeur, produit, nbProduit);
		chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n°" + indEtalLibre + ".\n");
		return chaine.toString();
	}
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Les vendeurs qui proposent des " + produit + " sont :\n");
		Etal[] etalsOccupes = marche.trouverEtals(produit);
		for (Etal etal : etalsOccupes) {
			//System.out.println(etal.contientProduit(produit) + "    " + etal.afficherEtal() + "       " + produit);
			chaine.append(" - " + etal.getVendeur().getNom() + "\n");
		}
		return chaine.toString();
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		return marche.trouverVendeur(vendeur);
	}
	
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = rechercherEtal(vendeur);
		return etal.libererEtal();
	}
	
	
	public String afficherMarche() {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Le marché du village \"" + nom + "\" possède plusieurs étals : \n");
		chaine.append(marche.afficherMarche());
		return chaine.toString();
	}
	
	/*Méthodes de base*/
	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois () throws VillageSansChefException {
		StringBuilder chaine = new StringBuilder();
		try {
			if (nbVillageois < 1) {
				chaine.append("Il n'y a encore aucun habitant au village du chef "
						+ chef.getNom() + ".\n");
			} else {
				chaine.append("Au village du chef " + chef.getNom()
				+ " vivent les légendaires gaulois :\n");
				for (int i = 0; i < nbVillageois; i++) {
					chaine.append("- " + villageois[i].getNom() + "\n");
				}
			}
		} catch (NullPointerException npe) {
			throw new VillageSansChefException("Il n'existe pas de village sans chef.");
		}
		return chaine.toString();
	}
}