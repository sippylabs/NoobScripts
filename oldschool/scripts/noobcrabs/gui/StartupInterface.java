package oldschool.scripts.noobcrabs.gui;

import oldschool.scripts.common.utilities.Startup;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;

public class StartupInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonCancel;
    private JSlider healthPercentSlider;
    private JLabel healthPercent;
    private ClientContext ctx;
    private Startup start;

    public StartupInterface(ClientContext ctx, Startup start) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonStart);
        this.ctx = ctx;
        this.start = start;
        healthPercent.setText(String.valueOf(healthPercentSlider.getValue()) + "%");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Failed to set UI look and feel. Rare and pointless exception.");
        }

        buttonStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        healthPercentSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                healthPercent.setText(String.valueOf(healthPercentSlider.getValue()) + "%");
            }
        });
    }

    private void onOK() {
        start.startTime = System.currentTimeMillis();
        start.eatAtPercentage = healthPercentSlider.getValue() / 100;
        NoobCrabs.initialising = false;
        dispose();
    }

    private void onCancel() {
        ctx.controller.stop();
        dispose();
    }

    public static void main(String[] args) {
    }
}