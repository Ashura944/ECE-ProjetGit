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
import javax.swing.Box;
import javax.swing.JPanel;

import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import controleur.ListeControlesAction;
import javax.swing.JDialog;
import javax.swing.JPasswordField;

/**
 * @author Quentin Huard
 * 
 * Classe qui permet de gérer la saisie des identifiants de connexion à une base
 * et qui procède à la connexion.
 * En entrée de cette classe, on instancie la classe ListeControlesAction dont
 * le rôle sera après clic sur le bouton btValider de procéder au contrôle des 
 * champs puis à la connexion.
 */
public class FenetreConnexion extends JDialog implements ActionListener
{   
    private JLabel lbLoginECE,lbPasswordECE,lbLoginBase,      // Libellés associés aux champs de saisie.
                   lbPasswordBase;
    public JTextField tfNomBase,tfLoginECE,tfLoginBase;       // Champs de saisie.
    public JPasswordField tfPasswordECE,tfPasswordBase;
    public JRadioButton rbtOption1 = new JRadioButton("Base distante");
    public JRadioButton rbtOption2 = new JRadioButton("Base locale");
    public JButton btValider = new JButton("Valider");        // Bouton de validation.
    
    private JDialog dialog;                                   // Dialog pour la connexion.
     
    private Connexion maconnexion,maconnexionSave;
    
    // On crée une instance de la classse ListeControlesAction qui contient
    // les méthodes de contrôles de saisie et de connexion.
    ListeControlesAction listControlAction=new ListeControlesAction();
    
    /**
     * Méthode Getter.
     * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     * FenetrePrincipale (vue).
     * 
     * @return dialog JDialog (la JDialog permettant la connexion).
     */
    public JDialog getDialog()
    {
        return dialog;
    }
        
    ///**
    // * Méthode Setter.
    // * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     //* FenetrePrincipale (vue).
    // * 
    // * @param dialog Jdialog (la JDialog permettant la connexion).
    // */
    //public void setDialog(JDialog dialog)
    //{
        //this.dialog=dialog;
    //} 
    /**
     * Méthode Getter.
     * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     * FenetrePrincipale (vue).
     * 
     * @return maconnexion Connexion (la connexion effectuée sur la JDialog).
     */
    public Connexion getMaConnexion()
    {
        return maconnexion;
    }
        
    /**
     * Méthode Setter.
     * Cette méthode est utile pour l'objet fenPrincipale de la classe 
     * FenetrePrincipale (vue).
     * 
     * @param maconnexion Connexion (la connexion effectuée sur la JDialog).
     */
    public void setMaConnexion(Connexion maconnexion)
    {
        this.maconnexion=maconnexion;
    }

    /**
     * Constructeur.
     * Permet lors de son instanciation d'initialiser la JDialog permettant
     * la saisie des indentifiants de connexion. On ne rend pas visible la 
     * JDialog que sera rendue visible lors du choix de l'item de menu Connexion
     * sur la fenêtre principale (fenPrincipale).
     * En entrée on génère une instance (listControlAction) de la
     * classe ListeControlesAction (controleur).
     * 
     * @param fenPrincipale FenetrePrincipale (la fenêtre appelante).
     * @param modal boolean (modale ou non ; il faut toujours appeler cette JDialog
     * avec true).
     */
    public FenetreConnexion(FenetrePrincipale fenPrincipale, boolean modal) 
    {
        dialog=new JDialog(fenPrincipale,modal);              // Instanciation d'une JDialog.

        // Instanciation des JPanels.
        JPanel panTop = new JPanel();                         // Panel de plus haut niveau.
        panTop.setLayout(new FlowLayout());
        //panTop.setBackground(Color.LIGHT_GRAY);             // Pas de couleur, c'est moche.
            
        // Boutons radio.
        ButtonGroup group = new ButtonGroup();                // Instanciation du groupe de boutons (interdépendants).
        group.add(rbtOption1);
        group.add(rbtOption2);
        Box bRadioButton = Box.createVerticalBox();           // On crée un conteneur avec gestion verticale.
        bRadioButton.add(rbtOption1);
        bRadioButton.add(rbtOption2);
        
        // Libelles et champs de saisie.
        lbLoginECE = new JLabel("Login ECE");
        lbPasswordECE = new JLabel("Password ECE");
        lbLoginBase = new JLabel("Login base");
        lbPasswordBase = new JLabel("Password base");
        JLabel lbNomBase = new JLabel("nom base");
        tfLoginECE=new JTextField("");                        // Pas de valeurs par défaut.
        Attribut.proprieteChamp(tfLoginECE,200,100);          // Définition des propriétés.
        tfPasswordECE=new JPasswordField("");
        Attribut.proprieteChamp(tfPasswordECE,200,100);       // 200 pour la taille et 100
        tfLoginBase=new JTextField("");                       // pour le nombre de caractères saisissables.
        Attribut.proprieteChamp(tfLoginBase,200,100);
        tfPasswordBase=new JPasswordField("");
        Attribut.proprieteChamp(tfPasswordBase,200,100);
        tfNomBase=new JTextField("");
        Attribut.proprieteChamp(tfNomBase,200,100);
        
        // Panel pour les champs de saisie.
        JPanel panChamp = new JPanel();                       
        //panChamp.setBackground(Color.LIGHT_GRAY);           // Pas de couleur, c'est moche.
        panChamp.setLayout(new GridLayout(5,2));
        panChamp.add(lbLoginECE);
        panChamp.add(tfLoginECE);
        panChamp.add(lbPasswordECE);
        panChamp.add(tfPasswordECE);
        panChamp.add(lbLoginBase);
        panChamp.add(tfLoginBase);
        panChamp.add(lbPasswordBase);
        panChamp.add(tfPasswordBase);
        panChamp.add(lbNomBase);
        panChamp.add(tfNomBase);
        
        // Bouton Valider.
        Attribut.proprieteBouton(btValider,100);
        
        // On emboite les panneaux secondaires dans le panneau principal.
        panTop.add(bRadioButton);
        panTop.add(panChamp);
        panTop.add(btValider);

        // On demande ici à ce que le containeur de la fenêtre soit panTop.
        dialog.setContentPane(panTop);
        
        // On ajoute les listeners.
        rbtOption1.addActionListener(this);
        rbtOption2.addActionListener(this);
        btValider.addActionListener(this);
        
        // On définit la taille de la fenêtre, on la centre, on la déclare
        // non redimensionnable puis on l'affiche.
        dialog.setSize(450,300);
        dialog.setLocationRelativeTo(fenPrincipale);
        dialog.setVisible(false);
        dialog.setResizable(false);
    }
    
    /**
     * Méthode qui gère les événements.
     * On gère le clic sur les boutons radio et le clic sur valider.
     * Dans le traitement du bouton btValider, on sauvegarde l'ancienne
     * connexion (qui a été initialisée à null en lançant l'application)
     * afin de réinitialiser maconnexion valide en cours si une nouvelle
     * tentative de connexion sur une autre base a été faite et a échoué.
     * Sinon on se retouve sur l'écran principal avec des listes renseignées
     * et une connexion mauvaise.
     */
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==this.rbtOption1){
            // On cache les libellés.
            lbLoginECE.setVisible(true);
            lbPasswordECE.setVisible(true);
            lbLoginBase.setVisible(true);
            lbPasswordBase.setVisible(true);
            // On cache les champs de saisie.
            tfLoginECE.setVisible(true);
            tfPasswordECE.setVisible(true);
            tfLoginBase.setVisible(true);
            tfPasswordBase.setVisible(true);
        }
        if(e.getSource()==this.rbtOption2){
            // On cache les libellés.
            lbLoginECE.setVisible(false);
            lbPasswordECE.setVisible(false);
            lbLoginBase.setVisible(false);
            lbPasswordBase.setVisible(false);
            // On cache les champs de saisie.
            tfLoginECE.setVisible(false);
            tfPasswordECE.setVisible(false);
            tfLoginBase.setVisible(false);
            tfPasswordBase.setVisible(false);
        }
        if(e.getSource()==this.btValider){
            // Contrôles de saisie, de connexion, et lecture des données.
            maconnexionSave=maconnexion;
            boolean bRetour=false;
            bRetour=listControlAction.controleConnexion(this);
            if(bRetour==true)
            {
                maconnexion=listControlAction.getMaConnexion();
                dialog.setVisible(false);
            }
            else
                maconnexion=maconnexionSave;
        }
    }

}