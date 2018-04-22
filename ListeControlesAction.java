/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import vue.FenetreConnexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.TraitementLecture;
import vue.FenetrePrincipale;
import vue.FenetreMajEmploye;
import vue.FenetreCreEmploye;

/**
 * @author Quentin Huard
 * 
 * Cette classe ne contient pas de constructeur. Elle est instanciée dans
 * un objet de type FenetreConnexion puis sur cette instance appelle
 * les différentes méthodes.
 */
public class ListeControlesAction {
    
    private Connexion maconnexion;
    private TraitementLecture trtLecture;
    
   /**
     * Méthode Getter.
     * Cette méthode est utile pour lea classe Traitement (modele).
     * 
     * @return maconnexion Connexion (la connexion en cours)..
     */
    public Connexion getMaConnexion()
    {
        return maconnexion;
    }
        
    /**
     * Méthode Setter.
     * Cette méthode est utile pour lea classe Traitement (modele).
     * @param maconnexion Connexion (la connexion en cours).
     */
   public void setMaConnexion(Connexion maconnexion)
    {
        this.maconnexion=maconnexion;
    }

   /**
     * Méthode statique qui exécute la requête passée en paramètre.
     * Cette méthode est appelée lorsqu'il y a clic sur une ligne de requêtes
     * ou de table de la fenêtre principale (fenPrincipale, classe FenetrePrincipale (vue)). 
     * Elle est aussi appelée à l'ouverture de la fenêtre car on simule un
     * clic sur la 1ere requête et sur la 1ere table.
     * 
     * @param maconnexion Connexion (connexion en cours).
     * @param strReq String (requête à exécuter).
     * @return liste ArrayList String (pour stocker le résultat de la requête).
     */

   public static ArrayList<String> controleExeReq(Connexion maconnexion,String strReq)
   {
        String strMsg = ""; 
        ArrayList<String> liste = null;
                 
        if(maconnexion==null)
        {
           strMsg="Vous n'êtes pas connecté à une base. Exécution impossible.";
        }
        else
        {
            try {
                liste=TraitementLecture.afficherRes1(strReq,maconnexion);
            } catch (SQLException ex) {
                // Ici comme je ne sais pas ce qu'est ce Logger je préfère sortir
                // directement après message d'erreur, d'où le return null (donc
                // la liste sera null).
                javax.swing.JOptionPane.showMessageDialog(null,"Echec requete SQL", 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);  
                Logger.getLogger(ListeControlesAction.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
            if(liste==null) strMsg="Echec requête SQL";
        }
          
        if(strMsg.length()>0)
        {
            javax.swing.JOptionPane.showMessageDialog(null,strMsg, 
                        "ERREUR", JOptionPane.ERROR_MESSAGE);  
        }
        return liste;
    }

   /**
     * Méthode statique qui effectue les contrôles sur la saisie du champ
     * de saisie (champ requête) de la fenêtre principale puis exécute la
     * requête.
     * Cette méthode est appelée lorsqu'il y a clic sur le bouton btExecuter
     * de la fenêtre principale (fenPrincipale, classe FenetrePrincipale (vue)). 
     * Si erreur on repositionne le curseur sur le champ en erreur et on 
     * affiche un message d'erreur.
     * On applique la méthode trim sur les champs et on réaffecte au champ la
     * valeur résultante.
     * Si pas d'erreur, on appelle la méthode controleExeReq (controleur).
     * 
     * @param fenPrincipale FenetrePrincipale (instance de la classe FenetrePrincipale).
     * @param maconnexion Connexion (connexion en cours).
     * @return liste ArrayList String (pour stocker le résultat de la requête).
     */
    public static ArrayList<String> controleBtExecuter(FenetrePrincipale fenPrincipale,Connexion maconnexion)
    {
        String strMsg = ""; 
        ArrayList<String> liste = null;
        
        String strReqSaisie=fenPrincipale.tfReqSaisie.getText();
        strReqSaisie=strReqSaisie.trim();
        fenPrincipale.tfReqSaisie.setText(strReqSaisie);
        if(strReqSaisie.length()==0)
        {
            strMsg="Pour cette action, vos devez saisir une requête.";
        }
        else
        {
            if(maconnexion==null)
            {
                strMsg="Vous n'êtes pas connecté à une base. Exécution impossible.";
            }
            else
            {
                try {
                    liste=TraitementLecture.afficherRes1(strReqSaisie,maconnexion);
                } catch (SQLException ex) {
                    // Ici comme on ne sait pas ce qu'est ce Logger on préfère sortir
                    // directement après message d'erreur, d'où le return null (donc
                    // la liste sera null).
                    javax.swing.JOptionPane.showMessageDialog(null,"Echec requete SQL", 
                        "ERREUR", JOptionPane.ERROR_MESSAGE);  
                    Logger.getLogger(ListeControlesAction.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
                if(liste==null) strMsg="Echec requête SQL";
            }
          }
        if(strMsg.length()>0)
        {
            javax.swing.JOptionPane.showMessageDialog(fenPrincipale,strMsg, 
                        "ERREUR", JOptionPane.ERROR_MESSAGE);  
            fenPrincipale.tfReqSaisie.requestFocus();
        }
        return liste;
    }
        
    /**
     * Méthode qui effectue les contrôles sur la saisie des champs de la fenêtre
     * de connexion.
     * Cette méthode est appelée par la méthode controleConnexion. 
     * On teste les champs en fonction du bouton radio, sauf pour le champ
     * qui contient le nom de la base.
     * Si erreur on repositionne le curseur sur le champ en erreur et on 
     * affiche un message d'erreur.
     * On applique la méthode trim sur les champs et on réaffecte au champ la
     * valeur résultante.
     * 
     * @param fenConnexion FenetreConnexion (instance de la classe FenetreConnexion).
     * @return boolean (true ok, false ko);
     */
    public boolean controleChampsConnexion(FenetreConnexion fenConnexion)
    {
        String strMsg; strMsg = "";
        String strChamp;
        JTextField tfChamp;
        tfChamp = new JTextField();
        boolean retourOK = true;
        
        if(!fenConnexion.rbtOption1.isSelected() && 
           !fenConnexion.rbtOption2.isSelected())
        {
            strMsg="Choix base distante ou locale obligatoire !";
            fenConnexion.rbtOption1.requestFocus();
            retourOK = false;
        }
        
        // Pour la base distante on teste tous les champs.
        if(fenConnexion.rbtOption1.isSelected())
        {
            // Contrôle sur le champ Login ECE.
            if(retourOK==true)
            {
                strChamp=fenConnexion.tfLoginECE.getText();
                strChamp=strChamp.trim();
                fenConnexion.tfLoginECE.setText(strChamp);
                if(strChamp.length()==0)
                {
                    strMsg="La saisie du champ Login ECE est obligatoire.";
                    fenConnexion.tfLoginECE.requestFocus();
                    retourOK = false;
                }
            }
            // Contrôle sur le champ Password ECE si pas d'erreur sur le précédent.
            if(retourOK==true)                                 
            {
                strChamp=fenConnexion.tfPasswordECE.getText();
                strChamp=strChamp.trim();
                fenConnexion.tfPasswordECE.setText(strChamp);
                if(strChamp.length()==0)
                {
                    strMsg="La saisie du champ Password ECE est obligatoire.";
                    fenConnexion.tfPasswordECE.requestFocus();
                    retourOK = false;
                }
            }
            // Contrôle sur le champ Login base si pas d'erreur sur le précédent.
            if(retourOK==true)                                 
            {
                strChamp=fenConnexion.tfLoginBase.getText();
                strChamp=strChamp.trim();
                fenConnexion.tfLoginBase.setText(strChamp);
                if(strChamp.length()==0)
                {
                    strMsg="La saisie du champ Login Base est obligatoire.";
                    fenConnexion.tfLoginBase.requestFocus();
                    retourOK = false;
                }
            }
            // Contrôle sur le champ Password base si pas d'erreur sur le précédent.
            if(retourOK==true)                                 
            {
                strChamp=fenConnexion.tfPasswordBase.getText();
                strChamp=strChamp.trim();
                fenConnexion.tfPasswordBase.setText(strChamp);
                if(strChamp.length()==0)
                {
                    strMsg="La saisie du champ Password Base est obligatoire.";
                    fenConnexion.tfPasswordBase.requestFocus();
                    retourOK = false;
                }
            }
        }
        // Contrôle sur le champ base (distante ou locale).
        if(retourOK==true)
        {
            strChamp=fenConnexion.tfNomBase.getText();
            strChamp=strChamp.trim();
            fenConnexion.tfNomBase.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Nom base est obligatoire.";
                fenConnexion.tfNomBase.requestFocus();
                retourOK = false;
            }
        }
        // Si KO, alors il y a erreur.
        if(retourOK==false) 
        {
            javax.swing.JOptionPane.showMessageDialog(fenConnexion, strMsg, 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
        return retourOK;
    }
    
    /**
     * Méthode qui permet d'effectuer la connection à la base.
     * Cette méthode effectue au préalable les tests sur les champs
     * (méthode controleChampsConnexion) et est appelée depuis la JDialog 
     * (classe FenetreConnexion).
     * 
     * @param fenConnexion FenetreConnexion (instance de la classe FenetreConnexion).
     * @return boolean (true ok, false ko);
     */
    public boolean controleConnexion(FenetreConnexion fenConnexion)
    {
        boolean bRetour;
        bRetour=controleChampsConnexion(fenConnexion);
        if(bRetour==true)
        {
            if(fenConnexion.rbtOption1.isSelected())                     // Cas de la base distante.
            {
                ArrayList<String> liste;
                String passwdECEString = new String(fenConnexion.tfPasswordECE.getPassword());
                String passwdBDDString = new String(fenConnexion.tfPasswordBase.getPassword());
                try {
                    try {
                        // Tentative de connexion sur la base distante avec les 4 attributs.
                        maconnexion = new Connexion(fenConnexion.tfLoginECE.getText(), passwdECEString,
                                        fenConnexion.tfLoginBase.getText(), passwdBDDString);
//if(maconnexion==null) System.out.println("LA CONNEXION EST NULLE");
                        // Traitement de lecture des données.
                        trtLecture=new TraitementLecture(this);
                        
                    } catch (ClassNotFoundException cnfe) {
                        javax.swing.JOptionPane.showMessageDialog(fenConnexion, "Connexion echouee : probleme de classe", 
                            "ERREUR", JOptionPane.ERROR_MESSAGE);
                        bRetour=false;
                        //cnfe.printStackTrace();                        // On met ça en commentaire car ça pollue la console.
                    }
                } catch (SQLException e) {
                    javax.swing.JOptionPane.showMessageDialog(fenConnexion, "Connexion echouee : probleme SQL");
                    bRetour=false;
                    //e.printStackTrace();                               // On met ça en commentaire car ça pollue la console.
                }
            } else                                                       // Cas de la base locale.
            {
                ArrayList<String> liste;
                try {
                    try {
                        // Tentative de connexion sur la base locale avec un attribut (le nom de la base).
                        maconnexion = new Connexion(fenConnexion.tfNomBase.getText(), "root", "");

                        // Traitement de lecture des données.
                        trtLecture=new TraitementLecture(this);
                        
                    } catch (ClassNotFoundException cnfe) {
                        javax.swing.JOptionPane.showMessageDialog(fenConnexion, "Connexion echouee : probleme de classe");
                        bRetour=false;
                        //cnfe.printStackTrace();                        // On met ça en commentaire car ça pollue la console.
                        // Cette commande ci-dessous est supposée ramener le message mais ça n'a rien donné, donc en commentaire.
                        //System.out.println("NO 1 "+cnfe.getCause().getMessage());
                    }
                } catch (SQLException e) {
                    javax.swing.JOptionPane.showMessageDialog(fenConnexion, "Connexion echouee : probleme SQL");
                    bRetour=false;
                    //e.printStackTrace();                               // On met ça en commentaire car ça pollue la console.
                    //System.out.println("NO 2 "+e.getCause().getMessage());
                }
            }
            if(bRetour==true)
            {
                javax.swing.JOptionPane.showMessageDialog(fenConnexion, "Connexion réussie.");
            }
        }
        return bRetour;
    }
    
    /**
     * Méthode statique qui effectue un contrôle sur demande de maj d'une table.
     * Cette méthode est déclenchée lorsque l'utilisateur clique sur une
     * ligne listeDeChamp (java.awt.List) de la fenêtre principale.
     * Seule la mise à jour sur employé est implémentée.
     * @param fenPrincipale FenetrePrincipale (la fenêtre appelante).
     * @param table String (la table correspondant à la ligne sélectionnée).
     * @return boolean (true ok, false ko);
     */
    public static boolean controleSelectionTableMaj(FenetrePrincipale fenPrincipale,String nomTable)
    {
        boolean bRetourOK=true;
        if(!nomTable.equals("employe")) 
        {
            bRetourOK=false;
            javax.swing.JOptionPane.showMessageDialog(fenPrincipale,"La mise à jour est uniquement implémentée sur employe.", 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
        return bRetourOK;
    }
           
    /**
     * Méthode qui effectue les contrôles et l'appel de la maj sur JDialog employe.
     * Cette méthode est appelée par un instance de FenetreMajEmploye (vue). 
     * Si erreur on repositionne le curseur sur le champ en erreur et on 
     * affiche un message d'erreur.
     * On applique la méthode trim sur les champs et on réaffecte au champ la
     * valeur résultante.
     * On suppose que le nom, le prénom et l'adresse sont obligatoire, mais pas
     * le téléphone (dans la base ces champs sont tous non obligatoires). Pour
     * le téléphone, on fait un trim.
     * Si les tests sont OK, on fait appel au modele.
     * 
     * @param fenMajEmploye FenetreMajEmploye (instance de la classe FenetreMajEmploye).
     * @param maconnexion Connexion (la connexion en cours).
     * @return boolean (true ok, false ko);

     */
    public boolean controleChampsMajEmploye(FenetreMajEmploye fenMajEmploye,Connexion maconnexion)
    {
        String strMsg; strMsg = "";
        String strChamp;
        JTextField tfChamp;
        tfChamp = new JTextField();
        boolean retourOK = true;
        
        // Controle sur la connexion.
        if(maconnexion==null)
        {
            strMsg="Vous n'êtes plus connecté :";
            retourOK=false;
        }
        
        // Conrôle sur le champ de saisie nom.
        if(retourOK==true)                                 
        {
            strChamp=fenMajEmploye.tfNom.getText();
            strChamp=strChamp.trim();
            fenMajEmploye.tfNom.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Nom est obligatoire.";
                fenMajEmploye.tfNom.requestFocus();
                retourOK = false;
            }
        }
        
        if(retourOK==true)                                 
        {
            // Contrôle sur le champ de saisie prenom.
            strChamp=fenMajEmploye.tfPrenom.getText();
            strChamp=strChamp.trim();
            fenMajEmploye.tfPrenom.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Prenom est obligatoire.";
                fenMajEmploye.tfPrenom.requestFocus();
                retourOK = false;
            }
        }
        
        if(retourOK==true)                                 
        {
            // Contrôle sur le champ de saisie prenom.
            strChamp=fenMajEmploye.tfAdresse.getText();
            strChamp=strChamp.trim();
            fenMajEmploye.tfAdresse.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Adresse est obligatoire.";
                fenMajEmploye.tfAdresse.requestFocus();
                retourOK = false;
            }
        }
        
        // On reformate le champ téléphone.
        strChamp=fenMajEmploye.tfTel.getText();
        strChamp=strChamp.trim();
        fenMajEmploye.tfTel.setText(strChamp);
        
        // Si OK on fait la mise à jour.
        if(retourOK==true) 
        {
            String strReqMaj="update employe set nom=\""+fenMajEmploye.tfNom.getText()+"\", "
                                              + "prenom=\""+fenMajEmploye.tfPrenom.getText()+"\", "
                                              + "adresse=\""+fenMajEmploye.tfAdresse.getText()+"\", "
                                              + "tel=\""+fenMajEmploye.tfTel.getText()+"\" "                   
                            + "where numero="+fenMajEmploye.tfNumero.getText()+";";
                            //+ "where numero=\""+fenMajEmploye.tfNumero.getText()+"\";";
            // Mise à jour.
            try {
              maconnexion.executeUpdate(strReqMaj);
            } catch (SQLException ex) {
                //Logger.getLogger(FenetreMajEmploye.class.getName()).log(Level.SEVERE, null, ex);
                retourOK=false;
            }
            if (retourOK==false) strMsg="Oup's ! Il y a eu une erreur de update.";
        }
        
        if(retourOK==false) 
        {
            javax.swing.JOptionPane.showMessageDialog(fenMajEmploye, strMsg, 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(fenMajEmploye,"Mise à jour effectuée", 
                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        return retourOK;
    }
    
    /**
     * Méthode qui effectue un controle sur un champ numérique.
     * On aurait pu aussi utiliser un format avec JFormattedTextField()
     * ou mettre un listener sur le JTextField avec appel à une fonction
     * qui gère le caractère saisi.
     * 
     * @param champ String (contenu du JTextField à contrôler).
     * @return boolean (true ok, false ko);
     */
    
    public boolean champEstEntier(String champ) {
        try {
            Integer.parseInt(champ);
	} catch (NumberFormatException e){
            return false;
	}
 	return true;
    } 
 
    /**
     * Méthode qui effectue les contrôles et l'appel de la création sur JDilog employe.
     * Idem controleChampsMajEmploye sauf qu'on contrôle le champ numero
     * en plus.
     * @param fenCreEmploye FenetreCreEmploye (instance de la classe FenetreCreEmploye).
     * @param maconnexion Connexion (la connexion en cours).
     * @return boolean (true ok, false ko);
     */
    public boolean controleChampsCreEmploye(FenetreCreEmploye fenCreEmploye,Connexion maconnexion)
    {
        String strMsg; strMsg = "";
        String strChamp;
        JTextField tfChamp;
        tfChamp = new JTextField();
        boolean retourOK = true;
        
        // Controle sur la connexion.
        if(maconnexion==null)
        {
            strMsg="Vous n'êtes pas connecté :";
            retourOK=false;
        }
        
        // Conrôle sur le champ de saisie numero.
        if(retourOK==true)                                 
        {
            strChamp=fenCreEmploye.tfNumero.getText();
            strChamp=strChamp.trim();
            fenCreEmploye.tfNumero.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Numero est obligatoire.";
                fenCreEmploye.tfNumero.requestFocus();
                retourOK = false;
            }
            if(champEstEntier(strChamp)==false)
            {
                strMsg="Le Numero doit être numérique.";
                fenCreEmploye.tfNumero.requestFocus();
                retourOK = false;
            }
        }
        

        
        // Conrôle sur le champ de saisie nom.
        if(retourOK==true)                                 
        {
            strChamp=fenCreEmploye.tfNom.getText();
            strChamp=strChamp.trim();
            fenCreEmploye.tfNom.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Nom est obligatoire.";
                fenCreEmploye.tfNom.requestFocus();
                retourOK = false;
            }
        }
        
        if(retourOK==true)                                 
        {
            // Contrôle sur le champ de saisie prenom.
            strChamp=fenCreEmploye.tfPrenom.getText();
            strChamp=strChamp.trim();
            fenCreEmploye.tfPrenom.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Prenom est obligatoire.";
                fenCreEmploye.tfPrenom.requestFocus();
                retourOK = false;
            }
        }
        
        if(retourOK==true)                                 
        {
            // Contrôle sur le champ de saisie prenom.
            strChamp=fenCreEmploye.tfAdresse.getText();
            strChamp=strChamp.trim();
            fenCreEmploye.tfAdresse.setText(strChamp);
            if(strChamp.length()==0)
            {
                strMsg="La saisie du champ Adresse est obligatoire.";
                fenCreEmploye.tfAdresse.requestFocus();
                retourOK = false;
            }
        }
        
        // On reformate le champ téléphone.
        strChamp=fenCreEmploye.tfTel.getText();
        strChamp=strChamp.trim();
        fenCreEmploye.tfTel.setText(strChamp);
        
        // Si OK on fait la création.
        if(retourOK==true) 
        {
            String strReqMaj="insert into employe (adresse,nom,numero,prenom,tel) values "
                                             + "(\""+fenCreEmploye.tfAdresse.getText()+"\", "
                                              + "\""+fenCreEmploye.tfNom.getText()+"\", "
                                              +fenCreEmploye.tfNumero.getText()+","
                                              + "\""+fenCreEmploye.tfPrenom.getText()+"\", "  
                                              + "\""+fenCreEmploye.tfTel.getText()+"\"); " ; 

            // Création.
            try {
              maconnexion.executeUpdate(strReqMaj);
            } catch (SQLException ex) {
                //Logger.getLogger(FenetreMajEmploye.class.getName()).log(Level.SEVERE, null, ex);
                retourOK=false;
            }
            if (retourOK==false) strMsg="Oup's ! Il y a eu une erreur de création. Le numéro saisi doit déjà exister.";
        }
        
        if(retourOK==false) 
        {
            javax.swing.JOptionPane.showMessageDialog(fenCreEmploye, strMsg, 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(fenCreEmploye,"Création effectuée", 
                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        return retourOK;
    }
    
    public static boolean SupprimerEnreg(FenetrePrincipale fenPrincipale,Connexion maconnexion,String table,String clef)
    {
        String strMsg; strMsg = "";
        boolean retourOK = true;
        
        // Controle sur la connexion.
        if(maconnexion==null)
        {
            strMsg="Vous n'êtes plus connecté :";
            retourOK=false;
        }
                
       
        // Si OK on fait la suppression.
        if(retourOK==true) 
        {
            String strReqMaj="delete from "+table+ " where numero="+clef+";";
            // Delete.
            try {
              maconnexion.executeUpdate(strReqMaj);
            } catch (SQLException ex) {
                //Logger.getLogger(FenetreMajEmploye.class.getName()).log(Level.SEVERE, null, ex);
                retourOK=false;
            }
            if (retourOK==false) strMsg="Oup's ! Il y a eu une erreur de delete. Sans doute une erreur d'intégrité !";
        }
        
        if(retourOK==false) 
        {
            javax.swing.JOptionPane.showMessageDialog(fenPrincipale, strMsg, 
                    "ERREUR", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            javax.swing.JOptionPane.showMessageDialog(fenPrincipale,"Suppression effectuée", 
                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        return retourOK;
    }
}
