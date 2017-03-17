/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

/**
 *
 * @author sanat
 */
public class Utilities {
    public static boolean isValidDouble(String string) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!Character.isDigit(c) && c != '.' && c != '-') {
                return false;
            }
        }
        return true;
    }
}
