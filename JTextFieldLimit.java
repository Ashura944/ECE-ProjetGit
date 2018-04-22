/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

/**
 * @author Hadi Persan
 * 
 * Classe récupérée sur Internet et qui permet de gérer la longueur
 * saisissable des champs.
 */
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextFieldLimit extends PlainDocument {

private int limit;

JTextFieldLimit(int limit) {
    super();
    this.limit = limit;
}

JTextFieldLimit(int limit, boolean upper) {
    super();
    this.limit = limit;
}

public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
    if (str == null)
    return;

    if ((getLength() + str.length()) <= limit) {
    super.insertString(offset, str, attr);
    }
}
}