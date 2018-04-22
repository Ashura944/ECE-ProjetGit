/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import vue.FenetrePrincipale; 

/**
 * @author Quentin Huard
 * 
 * Cette classe contient le main.
 */
public class Projet 
{
    private static FenetrePrincipale fenPrincipale;
      
    /**
     * @param args the command line arguments
     * 
     * Cette méthode ne fait rien d'autre qu'instancier la classe FenetrePrincipale (vue)
     * et créer l'objet fenPrincipale.
     */
    public static void main(String[] args)
    {
        fenPrincipale = new FenetrePrincipale();
    }
}
