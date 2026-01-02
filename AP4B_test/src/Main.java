import modele.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import GUI.*;

public class Main {
    public static void main(String[] args) {
        // Lancement de l'interface dans le Thread graphique
        SwingUtilities.invokeLater(() -> new ClientFrame());
    }
}