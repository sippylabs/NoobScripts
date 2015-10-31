package oldschool.scripts.noobcrabs.gui;

import oldschool.scripts.common.Task;
import oldschool.scripts.noobcrabs.NoobCrabs;
import oldschool.scripts.noobcrabs.Startup;
import oldschool.scripts.noobcrabs.enums.Location;
import oldschool.scripts.noobcrabs.tasks.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class StartupInterface extends JDialog {
    private JPanel contentPane;
    private JButton buttonStart;
    private JButton buttonCancel;
    private JSlider healthPercentSlider;
    private JLabel healthPercent;
    private JRadioButton doKillSteal;
    private JRadioButton dontKillSteal;
    private JSpinner maxPlayersInArea;
    private JSpinner worldsToHop;
    private JRadioButton avoidHobgoblins;
    private JRadioButton dontAvoidHobgoblins;
    private JCheckBox enableWorldhopping;
    private JRadioButton deadmanEnabled;
    private JRadioButton deadmanDisabled;
    private ClientContext ctx;
    private Startup start;
    private ArrayList<Task> tasks;

    public StartupInterface(ClientContext ctx, Startup start, ArrayList<Task> tasks) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonStart);
        this.ctx = ctx;
        this.start = start;
        healthPercent.setText(String.valueOf(healthPercentSlider.getValue()) + "%");
        this.setLocationRelativeTo(Frame.getFrames()[0]);
        worldsToHop.setModel(new SpinnerListModel(new Object[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15}));
        worldsToHop.setValue(10);
        maxPlayersInArea.setModel(new SpinnerListModel(new Object[]{4, 5, 6, 7, 8, 9}));
        maxPlayersInArea.setValue(8);
        this.tasks = tasks;

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

        enableWorldhopping.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                worldsToHop.setEnabled(enableWorldhopping.isSelected());
                maxPlayersInArea.setEnabled(enableWorldhopping.isSelected());
                deadmanEnabled.setEnabled(enableWorldhopping.isSelected());
                deadmanDisabled.setEnabled(enableWorldhopping.isSelected());

                if (enableWorldhopping.isSelected()) {
                    ((JSpinner.DefaultEditor) maxPlayersInArea.getEditor()).getTextField().setEditable(false);
                    ((JSpinner.DefaultEditor) worldsToHop.getEditor()).getTextField().setEditable(false);
                }
            }
        });
    }

    private void onOK() {
        start.killSteal = doKillSteal.isSelected();
        start.startTime = System.currentTimeMillis();
        start.eatAtPercentage = healthPercentSlider.getValue() / 100.0f;
        start.pure = avoidHobgoblins.isSelected();

        start.hoppingEnabled = enableWorldhopping.isSelected();
        start.maxWorldsToHop = (Integer) worldsToHop.getValue();
        start.maxPlayersInArea = (Integer) maxPlayersInArea.getValue();
        start.deadmanEnabled = deadmanEnabled.isSelected();

        start.atkxp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        start.strxp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        start.defxp = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        start.hpxp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);

        if (Location.LEFT.area().contains(ctx.players.local()))
            NoobCrabs.location = Location.LEFT;
        else if (Location.RIGHT.area().contains(ctx.players.local()))
            NoobCrabs.location = Location.RIGHT;
        else {
            Tile player = ctx.players.local().tile();
            NoobCrabs.location = (player.distanceTo(Location.LEFT.area().getCentralTile())
                    > player.distanceTo(Location.RIGHT.area().getCentralTile()))
                    ? Location.RIGHT : Location.LEFT;
        }

        if (start.pure && NoobCrabs.location.equals(Location.LEFT))
            NoobCrabs.location = Location.LEFTPURE;

        tasks.addAll(Arrays.asList(
                        new Find(ctx)
                        , new Reset(ctx)
                        , new Attack(ctx, start.killSteal)
                        , new Eat(ctx, start.eatAtPercentage)
                        , new Hop(ctx, start.hoppingEnabled, start.maxWorldsToHop, start.maxPlayersInArea, start.deadmanEnabled)
                )
        );

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
        contentPane.setMinimumSize(new Dimension(420, 250));
        contentPane.setOpaque(false);
        contentPane.setPreferredSize(new Dimension(420, 250));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel1, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Basic Options"));
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
        healthPercentSlider.setValue(0);
        panel2.add(healthPercentSlider, BorderLayout.SOUTH);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new BorderLayout(0, 0));
        panel1.add(panel3, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel3.add(panel4, BorderLayout.CENTER);
        panel4.setBorder(BorderFactory.createTitledBorder("KS other players?"));
        doKillSteal = new JRadioButton();
        doKillSteal.setHorizontalAlignment(2);
        doKillSteal.setText("Yes, KS");
        panel4.add(doKillSteal, BorderLayout.WEST);
        dontKillSteal = new JRadioButton();
        dontKillSteal.setHorizontalAlignment(0);
        dontKillSteal.setSelected(true);
        dontKillSteal.setText("No, don't KS");
        panel4.add(dontKillSteal, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        panel1.add(panel5, BorderLayout.SOUTH);
        panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(0, 0));
        panel5.add(panel6, BorderLayout.NORTH);
        panel6.setBorder(BorderFactory.createTitledBorder("Avoid hobgoblins?"));
        avoidHobgoblins = new JRadioButton();
        avoidHobgoblins.setText("Yes");
        panel6.add(avoidHobgoblins, BorderLayout.WEST);
        dontAvoidHobgoblins = new JRadioButton();
        dontAvoidHobgoblins.setSelected(true);
        dontAvoidHobgoblins.setText("No");
        panel6.add(dontAvoidHobgoblins, BorderLayout.CENTER);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel7, BorderLayout.CENTER);
        panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Advanced Options"));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new BorderLayout(0, 0));
        panel7.add(panel8, BorderLayout.NORTH);
        panel8.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, new Color(-4473925)));
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new BorderLayout(0, 0));
        panel8.add(panel9, BorderLayout.NORTH);
        panel9.setBorder(BorderFactory.createTitledBorder("World Hopping"));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new BorderLayout(0, 0));
        panel9.add(panel10, BorderLayout.NORTH);
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new BorderLayout(0, 0));
        panel11.setEnabled(true);
        panel10.add(panel11, BorderLayout.CENTER);
        final JLabel label2 = new JLabel();
        label2.setText("Max players in area:");
        panel11.add(label2, BorderLayout.WEST);
        maxPlayersInArea = new JSpinner();
        maxPlayersInArea.setAutoscrolls(true);
        maxPlayersInArea.setEnabled(false);
        maxPlayersInArea.setMinimumSize(new Dimension(40, 26));
        maxPlayersInArea.setPreferredSize(new Dimension(40, 26));
        panel11.add(maxPlayersInArea, BorderLayout.EAST);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new BorderLayout(0, 0));
        panel12.setEnabled(true);
        panel10.add(panel12, BorderLayout.SOUTH);
        final JLabel label3 = new JLabel();
        label3.setText("Num worlds to hop:");
        panel12.add(label3, BorderLayout.WEST);
        worldsToHop = new JSpinner();
        worldsToHop.setEnabled(false);
        worldsToHop.setMinimumSize(new Dimension(40, 26));
        worldsToHop.setPreferredSize(new Dimension(40, 26));
        panel12.add(worldsToHop, BorderLayout.EAST);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new BorderLayout(0, 0));
        panel10.add(panel13, BorderLayout.NORTH);
        enableWorldhopping = new JCheckBox();
        enableWorldhopping.setText("Enable");
        panel13.add(enableWorldhopping, BorderLayout.CENTER);
        final JPanel panel14 = new JPanel();
        panel14.setLayout(new BorderLayout(0, 0));
        panel9.add(panel14, BorderLayout.CENTER);
        panel14.setBorder(BorderFactory.createTitledBorder("Deadman Mode"));
        deadmanEnabled = new JRadioButton();
        deadmanEnabled.setEnabled(false);
        deadmanEnabled.setText("Yes");
        panel14.add(deadmanEnabled, BorderLayout.WEST);
        deadmanDisabled = new JRadioButton();
        deadmanDisabled.setEnabled(false);
        deadmanDisabled.setSelected(true);
        deadmanDisabled.setText("No");
        panel14.add(deadmanDisabled, BorderLayout.CENTER);
        final JPanel panel15 = new JPanel();
        panel15.setLayout(new BorderLayout(0, 0));
        contentPane.add(panel15, BorderLayout.SOUTH);
        panel15.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        final JPanel panel16 = new JPanel();
        panel16.setLayout(new BorderLayout(5, 0));
        panel15.add(panel16, BorderLayout.EAST);
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel16.add(buttonCancel, BorderLayout.EAST);
        buttonStart = new JButton();
        buttonStart.setText("Start");
        panel16.add(buttonStart, BorderLayout.WEST);
        ButtonGroup buttonGroup;
        buttonGroup = new ButtonGroup();
        buttonGroup.add(dontKillSteal);
        buttonGroup.add(doKillSteal);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(avoidHobgoblins);
        buttonGroup.add(dontAvoidHobgoblins);
        buttonGroup = new ButtonGroup();
        buttonGroup.add(deadmanDisabled);
        buttonGroup.add(deadmanEnabled);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }
}
