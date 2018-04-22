/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import controleur.Connexion;
import controleur.ListeControlesAction;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Quentin Huard (inspiré du source dejdbcv2018 de M. Segado).
 * 
 * Cette classe contient toutes les méthodes qui font appel à la base de données
 * ou font comme (par exemple TraitementLecture ne fait pas appel mais il
 * suffirait de lire le schéma).
 */
public class TraitementLecture {
    
    private static Connexion maconnexion;
    
    /**
    * Méthode privée et statique qui initialise la liste des tables.
    */
    private static void remplirTables() {
        maconnexion.ajouterTable("chambre");
        maconnexion.ajouterTable("docteur");
        maconnexion.ajouterTable("employe");
        maconnexion.ajouterTable("hospitalisation");
        maconnexion.ajouterTable("infirmier");
        maconnexion.ajouterTable("malade");
        maconnexion.ajouterTable("service");
        maconnexion.ajouterTable("soigne");
    }

    /**
     * Méthode privée et statique qui initialise la liste des requetes de selection;
     */
    private static void remplirRequetes() {
        // Requête 1.
        maconnexion.ajouterRequete("SELECT m.prenom, m.nom FROM malade m where m.mutuelle= \"maaf\" ;");     
        // Requête 2.
        maconnexion.ajouterRequete("SELECT e.prenom, e.nom FROM infirmier i,employe e "
                                 + "WHERE e.numero = i.numero AND i.rotation=\"nuit\" ORDER BY e.nom ;");
        // Requête 3.
        maconnexion.ajouterRequete("SELECT s.nom, s.batiment, e.prenom, e.nom, d.specialite "
                                 + "FROM service s, employe e, docteur d "
                                 + "WHERE e.numero = s.directeur AND d.numero = e.numero ORDER BY s.nom ;");
        // Requête 4.
        maconnexion.ajouterRequete("SELECT h.no_chambre, h.lit, s.nom AS 'service', m.prenom,m.nom, m.mutuelle "
                                 + "FROM hospitalisation h, service s,malade m "
                                 + "WHERE s.batiment='b' AND m.mutuelle LIKE 'mn%' "
                                 + "AND m.numero=h.no_malade AND h.code_service=s.code "
                                 + "ORDER BY h.no_chambre,h.lit ;");
        // Requête 5.
        maconnexion.ajouterRequete("SELECT s.nom AS 'Code service',AVG(i.salaire) AS 'Moyenne des salaires' "
                                 + "FROM infirmier i, service s "
                                 + "WHERE i.code_service = s.code GROUP BY (i.code_service) ORDER BY s.code ;");
        // Requête 6.
        maconnexion.ajouterRequete("SELECT code, AVG(nb_lits) "
                                 + "FROM chambre c , service s "
                                 + "WHERE s.batiment LIKE '%A%' "
                                 + "AND s.code = c.code_service GROUP BY s.code ORDER BY s.code ;");
        // Requête 7.
        maconnexion.ajouterRequete("SELECT m.nom, m.prenom, count(*) as nb_soignants,count(distinct d.specialite) as nb_specialites "
                                 + "FROM docteur d , soigne s, malade m "
                                 + "WHERE d.numero = s.no_docteur and s.no_malade = m.numero "
                                 + "GROUP BY m.nom , m.prenom "
                                 + "HAVING COUNT(*) > 3 ;");
        // Requête 8.
        maconnexion.ajouterRequete("SELECT  s.nom,x.nombre AS malades, y.nombre AS infirmiers,y.nombre/x.nombre AS rapport " +
                                     "FROM service s,(SELECT h.code_service,COUNT(*) AS nombre FROM hospitalisation h " +
                                                     "GROUP BY h.code_service) x, " +
                                                    "(SELECT i.code_service,COUNT(*) AS nombre FROM infirmier i " +
                                                     "GROUP BY i.code_service) y " +
                                    "WHERE x.code_service=s.code " +
                                      "AND y.code_service=s.code " +
                                    "ORDER BY s.nom");
        // On crée 2 tables temporaises : ce qui est encadré par ( et ) auxquelles
        // on donne un alias (x et y). Dans le select de chacune de ces tables
        // temporaires on ramène le code service car il faudra faire la jointure
        // avec le service (alias s). On fait aussi un GROUP BY car on veut le COUNT(*)
        // par service. Quant au COUNT(*), on leur donne un alias
        // pour calculer le rapport (y.nombre/x.nombre).
        
        // Requête 9.
        //Là, c'est une simple jointure qui donc par défaut n'est ni gauche, ni
        //droite, ni externe, donc renvoie les tables si il y a vraiment jointure.
        maconnexion.ajouterRequete("SELECT DISTINCT e.prenom,e.nom "
                                 + "FROM docteur d,employe e,soigne s,hospitalisation h "
                                 + "WHERE s.no_docteur=d.numero "
                                 + "AND h.no_malade=s.no_malade "
                                 + "AND e.numero= d.numero "
                                 + "ORDER BY e.nom ;");
        
        // Requête 10.
        maconnexion.ajouterRequete("SELECT e.prenom, e.nom FROM employe e,docteur d "
                                  + "WHERE e.numero=d.numero "
                                  + "AND d.numero NOT IN (SELECT s.no_docteur FROM soigne s,hospitalisation h "
                                                        + "WHERE h.no_malade=s.no_malade "
                                                        + "AND s.no_docteur=d.numero) "
                                  + "ORDER BY e.nom ;");
        // On peut faire ausi :
        // SELECT e.prenom, e.nom FROM employe e,docteur d
        // WHERE e.numero=d.numero 
        //   AND NOT EXISTS (SELECT 1 FROM soigne s,hospitalisation h
        //                    WHERE h.no_malade=s.no_malade
        //                      AND s.no_docteur=d.numero)
        // ORDER BY e.nom
    }
    
     /**
     * Constructeur.
     * Permet lors de son instanciation depuis FentreConnexion (modele) de
     * lancer la lecture des données.
     * 
     * @param listCtrlAct ListeControlesAction (l'instance de ListeControlesAction en cours).
     */
    public TraitementLecture(ListeControlesAction listCtrlAct) 
    {
        maconnexion=listCtrlAct.getMaConnexion();
        
        // Initialisation de la liste des tables.
        remplirTables();
        remplirRequetes();
    }
    
    /**
     * Récupérer les lgnes de la requete exécutée.
     * Cette méthode permet d'exécuter une requête et est appelée depuis la
     * classe ListeControlesAction (controleur).
     *
     * @param requeteSelectionnee String (la requête sélectionnée).
     * @param maconnexion Connexion (la connexion en cours)..
     * @return ArrayList String (le tableau qui retourne le résultat).
     * @throws java.sql.SQLException (les exceptions gérées).
     */
    public static ArrayList<String> afficherRes1(String requeteSelectionnee,Connexion maconnexion) throws SQLException {
        ArrayList<String> liste = null;
        try {
            // Recupérér les résultats de la requete selectionnée.
            liste = maconnexion.remplirChampsRequete(requeteSelectionnee);

        } catch (SQLException e) {
            liste=null;
        }
        return liste;
    }
}
