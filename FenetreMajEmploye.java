/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPanel;

import javax.swing.JButton;

import controleur.ListeControlesAction;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import javax.swing.JDialog;

/**
 * @author Quentin Huard
 * 
 * Classe qui permet de gérer la mise à jour de la table employe.
 */
public class FenetreMajEmploye extends JDialog implements ActionListener
{   
    private JPanel panTop,panLibChamp;                        // Les panneaux utilisés.
    private JLabel lbAdresse,lbNom,lbNumero,lbPrenom,lbTel;   // Libellés associés aux champs de saisie.
    public  JTextField tfAdresse,tfNom,tfNumero,              // Champs de saisie.
                       tfPrenom,tfTel;
    private JButton btValider;                                 // Bouton de validation.
    private JDialog dialog;
    
    // maConnex ci-dessous a été créé car si on passait bien maconnexion
    // en paramètre (constructeur ci-dessous) on ne pouvait pas l'utiliser
    // dans la méthode public actionPerformed. Il a donc fallu créer une
    // variable au niveau de la classe.
    public Connexion maConnex;
    public String donneesLgMod;
    
    /**
     * Méthode Getter.
     * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     * FenetrePrincipale (vue).
     * 
     * @return donneesLgMod String (les champs mis à jour ou null).
     */
    public String getDonneesLgMod()
    {
        return donneesLgMod;
    }
        
    /**
     * Méthode Setter.
     * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     * FenetrePrincipale (vue).
     * 
     * @param donneesLgMod String (ls champs mis à jour ou null).
     */
    public void setDonneesLgMod(String donneesLgMod)
    {
        this.donneesLgMod=donneesLgMod;
    }
    
    /**
     * Constructeur.
     * Permet lors de son instanciation d'initialiser la JDialog permettant
     * la gestion d'un employé.
     * 
     * @param fenPrincipale FenetrePrincipale (la fenêtre appelante).
     * @param maconnexion Connexion (la connexion en cours).
     * @param donneesLigne String (la ligne récupérée qui va être "splitée").
     */
    public FenetreMajEmploye(FenetrePrincipale fenPrincipale,Connexion maconnexion,String donneesLigne) 
    {
        // Ne pas mettre true pour la rendre modale tout de suite
        // car ça génère des java.awt.IllegalComponentStateException: 
        // component must be showing on the screen to determine its location/
        // Donc on la passe modale à la fin, avant de l'afficher.
        dialog=new JDialog(fenPrincipale,false);              // Instanciation d'une JDialog.
     
        // On récupère au niveau d'une variable de classe les variables passées en paramètre
        // et visible seulement dans la méthode (ici le constructeur).
        maConnex=maconnexion; 
       
        // On découpe la ligne avec comme séparateur ",".
        String[] donneesTab = donneesLigne.split(",");
        
        // Instanciation des JPanels.
        panTop = new JPanel();                               // Panel de plus haut niveau.
        panTop.setLayout(new FlowLayout());
        panLibChamp = new JPanel();                          // Panel des Libellés et des champs.
        panLibChamp.setLayout(new GridLayout(5,2));
            
        // Libelles et champs de saisie.
        lbNumero = new JLabel("Numero :");
        lbNom = new JLabel("Nom :");
        lbPrenom = new JLabel("Prénom :");
        lbAdresse = new JLabel("Adresse :");
        lbTel = new JLabel("Téléphone :");
        tfNumero=new JTextField();
        Attribut.proprieteChamp(tfNumero,200,4);             // Définition des propriétés.                       
        tfNom=new JTextField();
        Attribut.proprieteChamp(tfNom,200,12);               // 200 pour la taille et 12 
        tfPrenom=new JTextField();                           // pour le nombre de caractères saisissable.
        Attribut.proprieteChamp(tfPrenom,200,12);
        tfAdresse=new JTextField();    
        Attribut.proprieteChamp(tfAdresse,300,40);
        tfTel=new JTextField();
        Attribut.proprieteChamp(tfTel,200,14);
        
        // On remplit les champs. On fait ça après l'attribution des propriétés
        // car il semble qu'elle influe sur le setText.
        tfNumero.setText(donneesTab[0]);
        tfNom.setText(donneesTab[1]);
        tfPrenom.setText(donneesTab[2]);
        tfAdresse.setText(donneesTab[3]);
        tfTel.setText(donneesTab[4]);
        
        // Le champ numéro étant la clef on le rend inaccessible.
        tfNumero.setEnabled(false);
        
        // Boutons.
        btValider = new JButton("Valider");                  // Bouton de validation.
        
        // Ajout des libellés et champs de saisie dans le JPanel panLibChamp.
        panLibChamp.add(lbNumero);
        panLibChamp.add(tfNumero);
        panLibChamp.add(lbNom);
        panLibChamp.add(tfNom);
        panLibChamp.add(lbPrenom);
        panLibChamp.add(tfPrenom);
        panLibChamp.add(lbAdresse);
        panLibChamp.add(tfAdresse);
        panLibChamp.add(lbTel);
        panLibChamp.add(tfTel);
            
        // On emboite les panneaux secondaires dans le panneau principal.
        panTop.add(panLibChamp);
        panTop.add(btValider);

        // On demande ici à ce que le containeur de la fenêtre soit panTop.
        dialog.setContentPane(panTop);
        
        // On ajoute les listeners.
        btValider.addActionListener(this);
               
        // On définit la taille de la fenêtre, on la centre, on la déclare
        // non redimensionnable puis on l'affiche.
        dialog.setSize(650,250);
        dialog.setLocationRelativeTo(fenPrincipale);
        // Attention, setModal "is deprecated" et il faut utiliser setModalityType.
        // En plus il faut le faire avant de rendre visible.
        dialog.setModalityType(APPLICATION_MODAL);
        dialog.setVisible(true);
        dialog.setResizable(true);
        
    }
    
    /**
     * Méthode qui gère les événements.
     * On gère le clic sur le bouton btValider.
     */
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==this.btValider){
            // On crée une instance de la classse ListeControlesAction qui contient
            //les méthodes de contrôles et de mise à jour.
            ListeControlesAction listControlAction=new ListeControlesAction();
            boolean bRetour=false;
            bRetour=listControlAction.controleChampsMajEmploye(this,maConnex);
            if(bRetour==true)
            {
                donneesLgMod=this.tfNumero.getText()+","+
                             this.tfNom.getText()+","+
                             this.tfPrenom.getText()+","+
                             this.tfAdresse.getText()+","+
                             this.tfTel.getText();
            }
            else donneesLgMod=null;
        }
    }
}
    