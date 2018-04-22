/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * @author Quentin Huard
 * 
 * Classe qui permet de gérer les propriétés des champs de saisie (JTextField)
 * et des boutons.
 * Cette classe n'a pas besoin d'être instanciée car les méthodes présentes
 * sont statiques.
 * Cette classe fait appel à la classe JTextFieldLimit.
 */
public class Attribut {
  
    /**
     * Méthode qui définit les propriétés d'un champ de saisie. 
     * La méthode est supposée définir la taille d'un champ, ce qu'elle fait,
     * mais lors du positionnement dans le layout, les champs de plus petites
     * tailles prennent la taille du plus grand champ.
     * 
     * @param JTextField champ (le champ sur lequel appliquer les propriétés).
     * @param int x (longeur du champ - toujours 30 en largeur).
     * @param int nb (nombre de caractères autorisé).

     */
    static void proprieteChamp (JTextField champ, int x, int nb)
    {
        Font police = new Font("Arial", Font.BOLD, 14);
        champ.setFont(police);
        // Si le composant n'a pas de parent, on utilise setSize(), sinon
        // setPreferredSize(). En effet, setSize() ne fait rien si le composant
        // parent utilise un layout manager (mais il semble que setPrerredSize
        // ne soit pas bon non plus).
        champ.setPreferredSize(new Dimension(x, 30));
        champ.setForeground(Color.BLUE);
        champ.setDocument(new JTextFieldLimit(nb));
    }
    
    /**
     * Méthode qui définit les propriétés d'un bouton.
     * Même remarque que pour proprieteChamp.
     * 
     * @param JButton bouton (le bouton sur lequel appliquer les propriétés).
     * @param int x (longeur du  bouton - toujours 30 en largeur).
     */
    static void proprieteBouton (JButton bouton,int x)
    {
        bouton.setPreferredSize(new Dimension(x, 30));
        bouton.setForeground(Color.BLUE);
    }

}
