/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import controleur.Connexion;
import controleur.ListeControlesAction;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.CardLayout;
import java.awt.*;
import static java.awt.Dialog.ModalityType.APPLICATION_MODAL;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author Quentin Huard
 * 
 * Classe permet de gérer la fenêtre principale de l'application.
 * et permet la connexion.
 * Dans cette classe, on génère une instance de FenetreConnexion (fenConnexion)
 * qui permettra de gérer la connexion à une base via une JDialog. Mais cette 
 * JDialog reste cachée tant que l'utilisateur n'a pas cliqué sur l'item de menu
 * Connexion.
 * Dans le constructeur, on génère aussi une JDialogie qui permet de faire des
 * choix pour le mofification ou suppression après sélection d'une ligne, mais
 * elle reste cachée jusqu'au moment ou on sélectionne une ligne 
 * (DgModifierOuSupprimer(dgMajSupprimer);
 * A noter aussi que maconnexion est mis à null en entrée de cette classe (maconnexion
 * contiendra une instance de Connexion (controleur) générée dans la
 * classe ListeControlesAction (controleur) ou null si échec). Ce null permet
 * de tester si c'est ok et si on continue ou non (voir actionPerformed) et cela
 * pour éviter une exception 
 * Exception in thread "AWT-EventQueue-0" java.lang.NullPointerException.
 * Une erreur se produit (mais c'est aléatoire) lorsqu'on fait 
 * appel à la fonction showMessageDialog. Les items du menu sont cachés par le
 * panneau principal (seul le 1er reste visible).
 * On remarquera que la méthode controleExeReq (classe ListeControlesAction du
 * controleur) qui elle même fait appel à la méthode afficherRes1 de la classe
 * TraitementLecture pour les accès à la base) est utilisée à la fois pour la
 * gestion des requêtes que pour celle des tables puisque dans cee dernier cas il
 * suffit de faire un select * from table.
 */
public class FenetrePrincipale extends JFrame implements ActionListener,ItemListener
{
    private JMenuBar menuBar=new JMenuBar();
    private JMenu menuFichier=new JMenu("Fichier");
    private JMenuItem menuItemConnecter=new JMenuItem("Connexion");
    private JMenuItem menuItemQuitter=new JMenuItem("Quitter");
    private JMenu menuVue=new JMenu("Vue");
    private JMenuItem menuItemTable=new JMenuItem("Table");
    private JMenuItem menuItemRequete=new JMenuItem("Requêtes");
    private JMenu menuMaj=new JMenu("Création");
    private JMenuItem menuItemMaj11=new JMenuItem("Employe");
    private JMenuItem menuItemMaj12=new JMenuItem("Les autres tables");
    private JMenu menuReporting=new JMenu("Reporting");
    private JMenuItem menuItemReporting11=new JMenuItem("Reporting 1.1");
    private JMenuItem menuItemReporting12=new JMenuItem("Reporting 1.2");
    private CardLayout cl;
    private JPanel panP,panTable,panTb1,panTb2,panRequete,panRq1,panRq2,panRq3;
    private JLabel lbTable,lbChamp,lbRequete,lbResultat;
    private JLabel lbReqSaisie;
    public JTextField tfReqSaisie;
    public JButton btExecuter;
    public ArrayList<String> liste;
    private java.awt.List listeDeTables, listeDeRequetes;   // Pour la partie gauche de la fenêtre.
    private java.awt.List listeDeChamps,listeDeLignes;      // Pour la partie droite de la fenêtre.
        
    private JDialog dialog;                                 // Dialog de connexion.              
    private static FenetreConnexion fenConnexion;
    private Connexion maconnexion=null;
    private String[] listContent= {"panTable", "panRequete"}; //Liste des noms des conteneurs pour la pile de cartes.
    
    private JDialog dgMajSupprimer=new JDialog();           // Dialog pour gérer choix maj ou suppression  
    private JPanel jpTop,jpLib,jpBt;                        // JPanel associés.
    private JLabel lbQuestion;                              // JLanel associé.
    private JButton btMaj,btSupprimer,btQuitter;            // Boutons associés
    private int option;                                     // pour le choix de la maj ou suppression.
    private Font police = new Font("Arial", Font.BOLD, 14);
    
   
    /**
     * Afficher les tables.
     * Permet d'afficher les tables alimentées dans la méthode remplirTables de
     * la classe TraitementLecture (modele). L'ArrayList (java.awt.List) est 
     * défini dans cette classe.
     * Avant d'alimenter, on supprime les existantes.
     */
    public void afficherTables() {
        listeDeTables.removeAll();
        for (String table : maconnexion.tables) {
            listeDeTables.add(table);
        }
    }
    
    /**
     * Afficher les requetes de selection.
     * Permet d'afficher les requêtes alimentées dans la méthode remplirRequetes de
     * la classe TraitementLecture (modele). L'ArrayList (java.awt.List) est 
     * défini dans cette classe.
     * Avant d'alimenter, on supprime les existantes.
     */
    public void afficherRequetes() {
        listeDeRequetes.removeAll();
        for (String requete : maconnexion.requetes) {
            listeDeRequetes.add(requete);
        }
    }
    
    /**
     * Afficher le résultat (les lignes d'une requête).
     * Avant d'alimenter, on supprime les existantes.
     * Cette méthode est utilisée pour les lignes d'une table et pour les lignes
     * d'une requête.
     * 
     * @param liste ArrayList String (le tableau contenant le résultat des requêtes).
     * @param type int (0 pour le 1er écran (les tables), 1 pour le 2eme (les requêtes).
     */
    public void afficherLignesSelected(ArrayList<String> liste,int type)
    {
        if(type==0)
        {
            listeDeChamps.removeAll();
            //On affiche les lignes de la requete selectionnee à partir de la liste.           
            for (String liste1 : liste)
            {
                listeDeChamps.add(liste1);
            }
        }
        else
        {
            listeDeLignes.removeAll();
            //On affiche les lignes de la requete selectionnee à partir de la liste.           
            for (String liste1 : liste)
            {
                listeDeLignes.add(liste1);
            }
        }     
    }
    
     /**
     * Afficher la JDialog qui permet de choisir entre une mise à jour ou une suppression.
     * En fait, comme on a pas réuissi à changer les JOptionPane (YES_NO_OPTION, etc.) des
     * javax.swing.JOptionPane.showConfirmDialog (c'est possible...), on a opté
     * pour une JDialog créée tout de suite mais cachée et qui est rendu visible
     * en cas de besoin. C'est l'objet de cette méthode.
     * @param dgMajSupprimer Jdialog.
     * @return int le choix effectué.
     */
    private int DgModifierOuSupprimer(JDialog dgMajSupprimer)
    {
        jpTop=new JPanel();
        jpTop.setLayout(new FlowLayout());
        jpLib = new JPanel();                           // Panel des Libellés et des champs.
        jpLib.setLayout(new FlowLayout());
        jpBt = new JPanel();                            // Panel des Libellés et des champs.
        jpBt.setLayout(new GridLayout(1,2));
        lbQuestion=new JLabel("Voules-vous modifier ou supprimer ?");
        btMaj=new JButton ("Modifier");
        Attribut.proprieteBouton(btMaj,100);
        btMaj.setToolTipText("Appeler l'écran de mise à jour de la table et la ligne sélectionnée.");
        btSupprimer=new JButton("Supprimer");
        Attribut.proprieteBouton(btSupprimer,100);
        btSupprimer.setToolTipText("Supprimer la ligne sélectionnée.");
        btQuitter=new JButton(" Quitter ");
        Attribut.proprieteBouton(btQuitter,100);
        btQuitter.setToolTipText("Ne rien faire.");
        // On emboite les panneaux.
        jpLib.add(lbQuestion);
        jpBt.add(btMaj);
        jpBt.add(btSupprimer);
        jpBt.add(btQuitter);
        jpTop.add(jpLib);
        jpTop.add(jpBt);
        dgMajSupprimer.setContentPane(jpTop);
        // Listener.
        btMaj.addActionListener(new ActionListener() {  
		public void actionPerformed(ActionEvent e) {  
                    option=1;
                    dgMajSupprimer.setVisible(false);
                    System.out.println("Je passe par là");
		}
        });
        btSupprimer.addActionListener(new ActionListener() {  
		public void actionPerformed(ActionEvent e) {  
                    option=2;
                    dgMajSupprimer.setVisible(false);
		}
        });
        btQuitter.addActionListener(new ActionListener() {  
		public void actionPerformed(ActionEvent e) {  
                    option=3;
                    dgMajSupprimer.setVisible(false);
		}
        });  
        // Définition des propriéts de la JCombox.
        dgMajSupprimer.setSize(350,140);
        dgMajSupprimer.setLocationRelativeTo(null);
        //setModal "is deprecated" et il faut utiliser setModalityType.
        // En plus il faut le faire avant de rendre visible.
        dgMajSupprimer.setModalityType(APPLICATION_MODAL);
        dgMajSupprimer.setVisible(false);             // Elle le deviendra en cas de besoin.
        dgMajSupprimer.setResizable(false);
        return option;
    }
           
    /**
     * Constructeur.
     * Il permet lors de son instanciation d'initialiser la fenêtre
     * princpale du projet et la JDialog de connexion. La fenêtre principale
     * utilise le layout CardLayout. La JDialog reste cachée jusqu'à ce que
     * l'utilisateur demande la connexion à une base.
     */
    public FenetrePrincipale() 
    {
        // Forcer EXIT_ON_CLOSE.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Ajout des menus et sous-menus.
        menuBar.add(menuFichier);                       // Ajout du menu Fichier.
        menuFichier.add(menuItemQuitter);               // Ajout du sous-menu Quitter.
        menuFichier.add(menuItemConnecter);             // Etc.
        menuBar.add(menuVue);
        menuVue.add(menuItemTable);
        menuVue.add(menuItemRequete);
        menuBar.add(menuMaj);
        menuMaj.add(menuItemMaj11);
        menuMaj.add(menuItemMaj12);
        menuBar.add(menuReporting);
        menuReporting.add(menuItemReporting11);
        menuReporting.add(menuItemReporting12);
        
        // Ajout du menuBar.
        setJMenuBar(menuBar);
        
        // Création d'un CardLayout.
        cl = new CardLayout();
        
        // Création de trois conteneurs : le principal, un pour la gestion des 
        // tables et un pour la gestion des requêtes.
        panP=new JPanel();
        panP.setLayout(cl);                             // On définit le layout.
        panTable=new JPanel();
        panTable.setLayout(new BorderLayout());
        panRequete=new JPanel();
        panRequete.setLayout(new BorderLayout());
                    
        //---------- Traitement du panneau panTable.
        // Initialisation des titres.
        lbTable=new JLabel("Liste des tables", JLabel.CENTER);
        lbChamp=new JLabel("Lignes de la table sélectionnée", JLabel.CENTER);
        
        // Création de la liste pour les tables et les champ.
        listeDeTables = new java.awt.List(10, false); 
        listeDeTables.setBackground(Color.CYAN);
        
        listeDeChamps = new java.awt.List(10, false);
        listeDeChamps.setBackground(Color.WHITE);
        listeDeChamps.setFont(police);                  // Arial, Font.BOLD 14).
        listeDeChamps.setForeground(Color.BLUE);


        
        
        // Création des panneaux et ajout dans le paneau de la 1ere carte.
        panTb1 = new JPanel();                          // Pour les entêtes.
        panTb1.setLayout(new GridLayout(1, 2));
        panTb1.add(lbTable);                            // Ajout des libellés.
        panTb1.add(lbChamp);
        panTb2 = new JPanel();                          // Pour le contenu.
        panTb2.setLayout(new GridLayout(1, 2));
        panTb2.add(listeDeTables);                      // Pour la liste et résultat.
        panTb2.add(listeDeChamps);
        panTable.add("North",panTb1);                   // Ajout dans panTable et disposition géographique.
        panTable.add("Center",panTb2);
        
        //---------- Traitement du panneau panRequete.
        // Initialisation des titres et du libellé .
        lbRequete=new JLabel("Liste des requêtes", JLabel.CENTER);
        lbResultat=new JLabel("Résultat de la requête", JLabel.CENTER);
        // Initialisation du JLabel de la saisie de requêtes, du JTextField et
        // du JButton d'exécution de la requête.
        lbReqSaisie=new JLabel("Saisissez votre requête",JLabel.CENTER);
        tfReqSaisie=new JTextField("");                 // Pas de valeurs par défaut.
        Attribut.proprieteChamp(tfReqSaisie,150,300);   // Définition des propriétés.
        btExecuter= new JButton("Exécuter");            // Bouton d'éxécution de la requête.
        Attribut.proprieteBouton(btExecuter,100);       // Définition des propriétés.
        // Création de la liste pour les requêtes et le résultat.
        listeDeRequetes = new java.awt.List(10, false); 
        listeDeRequetes.setBackground(Color.YELLOW);
        
        listeDeLignes = new java.awt.List(10, false); 
        listeDeLignes.setBackground(Color.WHITE);
        listeDeLignes.setFont(police);                  // Arial, Font.BOLD 14).
        listeDeLignes.setForeground(Color.BLUE);
        
        // Création des panneaux et ajout dans le paneau de la 2eme carte.
        panRq1 = new JPanel();                          // Pour les entêtes.
        panRq1.setLayout(new GridLayout(1, 2));
        panRq1.add(lbRequete);                          // Ajout des libellés.
        panRq1.add(lbResultat);
        panRq2 = new JPanel();                          // Pour le contenu.
        panRq2.setLayout(new GridLayout(1, 2));
        panRq2.add(listeDeRequetes);                    // Pour la liste et résultat.
        panRq2.add(listeDeLignes);
        panRq3 = new JPanel();                          // Pour la zone de saisie de requêtes.
        panRq3.setLayout(new GridLayout(1,3));
        panRq3.add(lbReqSaisie);
        panRq3.add(tfReqSaisie);
        panRq3.add(btExecuter);
        panRequete.add("North", panRq1);                // Ajout dans JPanel principal.
        panRequete.add("Center", panRq2);
        panRequete.add("South",panRq3);

        //Ajout des cartes à la pile.
        panP.add(panTable,listContent[0]);
        panP.add(panRequete,listContent[1]);
        
        // Ajout du JPanel principal à la fénêtre.
        this.getContentPane().add(panP,BorderLayout.CENTER);
        
        // On ajoute les listeners.
        menuItemConnecter.addActionListener(this);
        menuItemQuitter.addActionListener(this);
        menuItemTable.addActionListener(this);
        menuItemRequete.addActionListener(this);
        menuItemMaj11.addActionListener(this);
        menuItemMaj12.addActionListener(this);
        menuItemReporting11.addActionListener(this);
        menuItemReporting12.addActionListener(this);
        
        listeDeTables.addItemListener(this);
        listeDeRequetes.addItemListener(this);
        btExecuter.addActionListener(this);
        listeDeChamps.addItemListener(this);


        // On définit la taille de la fenêtre, on la centre, on la déclare
        // non redimensionnable puis on l'affiche.
        this.setSize(1400,800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setResizable(false);
        cl.show(panP,listContent[0]);
        menuBar.setVisible(true);
        
        // On crée une instance de la classse FenetreConnexion qui crée la
        // JDialog permettant la saisie des données de connexion.
        fenConnexion = new FenetreConnexion(this,true);
        
        // On appelle le Getter qui va permettre lors du clique sur l'item
        // de menu Connecter de rendre visible la JDialog.
        dialog=fenConnexion.getDialog();
        
        DgModifierOuSupprimer(dgMajSupprimer);
    }
   
     /**
     * Méthode qui gère les événements.
     * On gère le clic sur les items de menu et le clic sur le bouton btValider
     * (pour exécuter une requête saisie).
     * A noter qu on a essayé d'envoyé un événement itemStateChanged lors
     * du traitement menuItemConnecter afin de ne pas refaire le même code
     * mais je n'y suis pas arrivé.
     */
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==menuItemQuitter)
            System.exit(0); 
        if(e.getSource()==menuItemConnecter)
        {
            dialog.setVisible(true);
            maconnexion=fenConnexion.getMaConnexion();
            if(maconnexion!=null)                       // La connexion est OK.
            {
                afficherTables();                       // On affiche les tables.
                afficherRequetes();                     // On affiche les requêtes.
                listeDeTables.select(0);                // On se positionne sur la 1ere ligne.
                listeDeRequetes.select(0);
                
                // Récupérer le nom de la table sélectionnée.
                String nomTable = listeDeTables.getSelectedItem();
                // Construire la requête.
                String strReq = "select * from "+nomTable;
                liste=ListeControlesAction.controleExeReq(maconnexion,strReq);
                if(liste!=null)
                {
                    // On affiche les lignes de la requete selectionnee à partir de la liste.           
                    afficherLignesSelected(liste,0);
                }
                // On recupere la requete selectionnee puis on affiche le résultat.
                strReq = ""; strReq=listeDeRequetes.getSelectedItem();
                liste=ListeControlesAction.controleExeReq(maconnexion,strReq);
                if(liste!=null)
                {
                    // On affiche les lignes de la requête selectionnée à partir de la liste.           
                    afficherLignesSelected(liste,1);
                }
            }
        } 
        if(e.getSource()==menuItemTable)
        {
            cl.show(panP,listContent[0]);
        } 
        if(e.getSource()==menuItemRequete)
        {
            cl.show(panP,listContent[1]);
        }
        if(e.getSource()==menuItemMaj11)
        {
            FenetreCreEmploye fenCreEmploye=new FenetreCreEmploye(this,maconnexion);
            String donneesLgMod=fenCreEmploye.getDonneesLgMod();
            if(donneesLgMod!=null)                              // La céation s'est bien passée.
            {
                // On récupère le nom de la table sélectionnée car on ne réalimente la liste que si on est dessus.
                String nomTable = listeDeTables.getSelectedItem();
                if(nomTable=="employe")
                    listeDeChamps.add(donneesLgMod);
            }
        }
        
        if(e.getSource()==menuItemMaj12)
        {
            javax.swing.JOptionPane.showMessageDialog(null,"La création n'est implémentée que sur le table employé.", 
                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE);
        }
        if(e.getSource()==menuItemReporting11 || e.getSource()==menuItemReporting12)
        {
            javax.swing.JOptionPane.showMessageDialog(null,"Traitement non implémenté.", 
                    "INFORMATION", JOptionPane.INFORMATION_MESSAGE); 
        }
        if(e.getSource()==btExecuter)
        {
            liste=ListeControlesAction.controleBtExecuter(this,maconnexion);
            if(liste!=null)
            {
                // On affiche les lignes de la requete saisie.           
                afficherLignesSelected(liste,1);
            }
        }
    }

    /**
     * Pour gerer les actions sur items d'une liste on utilise la methode
     * itemStateChanged.
     */
    @Override
    @SuppressWarnings("CallToThreadDumpStack")
    public void itemStateChanged(ItemEvent evt) {
        // Sélection d'une requete et afficher ses résultats.
        if (evt.getSource() == listeDeRequetes) {
            // Récuperer la liste des lignes de la requête selectionnée.
            String strReq = listeDeRequetes.getSelectedItem();
            liste=ListeControlesAction.controleExeReq(maconnexion,strReq);
            if(liste!=null)
            {
                // On affiche les lignes de la requete sélectionnée à partir de la liste.           
                afficherLignesSelected(liste,1);
            }

        } else if (evt.getSource() == listeDeTables) {
            // Récupérer le nom de la table sélectionnée.
            String nomTable = listeDeTables.getSelectedItem();
            // Construire la requête.
            String strReq = "select * from "+nomTable;
            liste=ListeControlesAction.controleExeReq(maconnexion,strReq);
            if(liste!=null)
            {
                // On affiche les lignes de la table à partir de la liste.           
                afficherLignesSelected(liste,0);
            }

        } else if (evt.getSource() == listeDeChamps) {
            // Récupérer le nom de la table sélectionnée.
            dgMajSupprimer.setVisible(true);
            int option=DgModifierOuSupprimer(dgMajSupprimer);
            if(option!=0)
            {
                String nomTable = listeDeTables.getSelectedItem();
                int indLg=listeDeChamps.getSelectedIndex();
                String donneesLg = listeDeChamps.getSelectedItem();
                if (option==1)
                {  
                    boolean bOK;
                    bOK=ListeControlesAction.controleSelectionTableMaj(this,nomTable);
                    if(bOK==true)
                    {
                        FenetreMajEmploye fenMajEmploye=new FenetreMajEmploye(this,maconnexion,donneesLg);
                        String donneesLgMod=fenMajEmploye.getDonneesLgMod();
                        if(donneesLgMod!=null)
                            listeDeChamps.replaceItem(donneesLgMod, indLg);
                    }
                }
                if (option==2)
                {
                    // En suppression, on ne gère pas les contraintes.
                    String[] donneesTab = donneesLg.split(",");
                    boolean bRetour=ListeControlesAction.SupprimerEnreg(this,maconnexion,nomTable,donneesTab[0]);
                    if(bRetour==true)
                    {
                        listeDeChamps.remove(indLg);
                    }
                }
            }
        }
    }
}
