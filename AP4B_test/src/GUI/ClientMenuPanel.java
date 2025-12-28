package GUI;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import network.*;

public class ClientMenuPanel extends JPanel {
    public ClientMenuPanel(ClientFrame frame) {
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel box = new JPanel(new GridLayout(4, 1, 10, 10));

        JLabel title = new JLabel("TRIO - LE JEU", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        box.add(title);

        // Host
        JButton btnHost = new JButton("Héberger une partie");
        btnHost.addActionListener(e -> {
            String nbStr = JOptionPane.showInputDialog("Nombre de joueurs (3-6) :", "3");
            if (nbStr != null) {
                try {
                    int nb = Integer.parseInt(nbStr);
                    if (nb < 3 || nb > 6) throw new Exception();
                    new Thread(() -> new Server(nb).start()).start();
                    frame.startGame("localhost");
                } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalide (3-6)."); }
            }
        });
        box.add(btnHost);

        // Join
        JButton btnJoin = new JButton("Rejoindre une partie");
        btnJoin.addActionListener(e -> {
            String ip = JOptionPane.showInputDialog("Adresse IP du serveur :", "localhost");
            if (ip != null) frame.startGame(ip);
        });
        box.add(btnJoin);

        add(box);
    }
}
