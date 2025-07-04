package it.unibo.monopoly.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import it.unibo.monopoly.utils.impl.GuiUtils;


/**
 * RulesWindow view.
 */
public final class RulesWindowView extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String TITLE_WINDOW = "Monopoly - Rules";
    private static final String TITLE_TEXT = "Rules";
    private static final String DONE_TEXT = "Done";

    private static final int TOP_BORDER = 10;
    private static final int BOTTOM_BORDER = 10;
    private static final int SIDE_BORDER = 20;


    /**
     * Creates a view that displays the game rules, importing them from a file.
     * @param parent the parent frame that owns this dialog and will be blocked while the dialog is visible
     * @param rules a {@link String} with the rules of the game, to show
     */
    public RulesWindowView(final Frame parent, final String rules) {
        final var size = GuiUtils.getDimensionWindow();
        GuiUtils.configureWindow(this,
                                 (int) size.getWidth(),
                                 (int) size.getHeight(),
                                 TITLE_WINDOW,
                                 new BorderLayout(),
                                 parent);
        final JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(TOP_BORDER, SIDE_BORDER, BOTTOM_BORDER, SIDE_BORDER));
        add(mainPanel);

        final JLabel titleLabel = new JLabel(TITLE_TEXT, SwingConstants.CENTER);
        titleLabel.setForeground(Color.RED);

        // Create a text area for display all the rules
        final JTextArea rulesTextArea = new JTextArea();
        rulesTextArea.setEditable(false);
        rulesTextArea.setLineWrap(true);
        rulesTextArea.setWrapStyleWord(true);
        rulesTextArea.setText(rules);
        rulesTextArea.setCaretPosition(0);

        // Create a scrollable view for the rulesTextArea
        final JScrollPane scrollPane = new JScrollPane(rulesTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Create an exit button for the window
        final JButton doneButton = new JButton(DONE_TEXT);
        doneButton.addActionListener(e -> dispose());

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(doneButton, BorderLayout.SOUTH);

        GuiUtils.refresh(this);
    }
}
