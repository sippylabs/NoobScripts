package oldschool.scripts.noobcrabs.gui;

import oldschool.scripts.common.utilities.Startup;
import oldschool.scripts.noobcrabs.NoobCrabs;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class StartupInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonCancel;
    private JSlider healthPercentSlider;
    private JLabel healthPercent;
    private JRadioButton doKillSteal;
    private JRadioButton dontKillSteal;
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
            System.out.println("Failed to set UI look and feel.");
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
        start.killSteal = doKillSteal.isSelected();
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel1, BorderLayout.WEST);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(0, 0));
        panel1.add(panel2, BorderLayout.NORTH);
        panel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JLabel label1 = new JLabel();
        label1.setText("Eat food at (% of total): ");
        panel2.add(label1, BorderLayout.WEST);
        healthPercent = new JLabel();
        healthPercent.setText("Label");
        panel2.add(healthPercent, BorderLayout.CENTER);
        healthPercentSlider = new JSlider();
        panel2.add(healthPercentSlider, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JLabel label2 = new JLabel();
        label2.setText("KS other people?");
        panel3.add(label2, BorderLayout.WEST);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        panel3.add(panel4, BorderLayout.SOUTH);
        doKillSteal = new JRadioButton();
        doKillSteal.setHorizontalAlignment(2);
        doKillSteal.setText("Yes, KS");
        panel4.add(doKillSteal);
        dontKillSteal = new JRadioButton();
        dontKillSteal.setHorizontalAlignment(0);
        dontKillSteal.setSelected(true);
        dontKillSteal.setText("No, don't KS");
        panel4.add(dontKillSteal);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel1.add(panel5, BorderLayout.SOUTH);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(5, 0));
        panel5.add(panel6, BorderLayout.EAST);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel6.add(buttonCancel, BorderLayout.EAST);
        buttonStart = new JButton();
        buttonStart.setText("Start");
        panel6.add(buttonStart, BorderLayout.WEST);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(dontKillSteal);
        buttonGroup.add(doKillSteal);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
