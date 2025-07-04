package it.unibo.monopoly.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.monopoly.controller.api.GameController;
import it.unibo.monopoly.controller.api.GameboardLogic;
import it.unibo.monopoly.controller.impl.GameboardLogicImpl;
import it.unibo.monopoly.model.turnation.api.Position;
import it.unibo.monopoly.model.turnation.impl.PositionImpl;
import it.unibo.monopoly.utils.impl.GuiUtils;
import it.unibo.monopoly.view.api.GameboardView;

/**
    * board view implementation.
*/
public final class GameboardViewImpl extends JPanel implements GameboardView {
    private static final String FONT_NAME = "Dialog";
    private static final int FONT_SIZE = 12;
    private static final double HEIGHT_PERC = 1.0;
    private static final double WIDTH_PERC = 0.8;
    private static final long serialVersionUID = 1L; /**serial version UID.*/
    private static final String HOUSE = "house";
    private static final String HOTEL = "hotel";
    private static final int PAWN_SIZE = 5; /**size of the pawns.*/
    private static final int STRIPE_WIDTH = 150; /**width of the stripes of the tiles.*/
    private static final int STRIPE_HEIGHT = 10; /**height of the stripes of the tiles.*/
    private final transient GameboardLogic logic; /**controller only for this view.*/
    private final transient GameController controller; /**main controller.*/
    private final List<JPanel> tilesView = new ArrayList<>(); /**list with all the jpanels which represent the tiles.*/
    private final int size; /**size of the board.*/
    /**map with the number of the player and their positions.*/
    private final Map<Integer, Position> pawnPositions = new HashMap<>();
    /**map with the jpanels which represent the tiles and their positions.*/
    private final Map<JPanel, Position> tilePositions = new HashMap<>();

    /**
    * start view.
    * @param controller controller
    */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
                justification = "must return reference to the object instead of a copy")
    public GameboardViewImpl(final GameController controller) {
        this.controller = controller;
        this.logic = new GameboardLogicImpl();
        this.size = logic.getSize(this.controller.getTiles().size());
        renderDefaultUI();
    }

    @Override
    public void addHouse(final String propName, final Color color, final int numHouses) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(propName)) {
                final JPanel panel = entry.getKey();
                for (final Component c : panel.getComponents()) {
                    if (HOUSE.equals(c.getName())) {
                        panel.remove(c);
                        panel.revalidate();
                        panel.repaint();
                    }
                }

                final JLabel label = new JLabel("HOUSES: " + numHouses);
                label.setName(HOUSE);
                label.setForeground(color);
                panel.add(label);
                panel.revalidate();
                panel.repaint();
                break;
            }
        }
    }

    @Override
    public void addHotel(final String propName, final Color color) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(propName)) {
                final JPanel panel = entry.getKey();
                final JLabel label = new JLabel("HOTEL: ✔");
                label.setName(HOTEL);
                label.setForeground(color);
                panel.add(label);
                panel.revalidate();
                panel.repaint();
                break;
            }
        }
    }

    @Override
    public void changePos(final int currPlayer, final Position newPos) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getValue().equals(pawnPositions.get(currPlayer - 1))) {
                final JPanel p = entry.getKey();
                for (final Component c : p.getComponents()) {
                    if (c instanceof PawnCircle pawnCircle 
                    && pawnCircle.getColor().equals(controller.getCurrPlayer().getColor())) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                        break;
                    }
                }
            }
        }

        pawnPositions.replace(currPlayer - 1, pawnPositions.get(currPlayer - 1), newPos);

        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getValue().equals(pawnPositions.get(currPlayer - 1))) {
                final JPanel p = entry.getKey();
                final PawnCircle pawnGUI = new PawnCircle(controller.getCurrPlayer().getColor());
                p.add(pawnGUI);
                p.setLayout(new FlowLayout(FlowLayout.CENTER, PAWN_SIZE, PAWN_SIZE));
                p.revalidate();
                p.repaint();
                break;
            }
        }
    }

    @Override
    public void buyProperty(final String propName, final Color playerColor) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(propName)) {
                final JPanel p = entry.getKey();
                final PawnSquare propertyGUI = new PawnSquare(playerColor);
                p.add(propertyGUI);
                p.revalidate();
                p.repaint();
                break;
            }
        }
    }

    @Override
    public void renderDefaultUI() {
        GuiUtils.applyGlobalFont(GuiUtils.createFont(FONT_NAME, FONT_SIZE));
        this.setLayout(new BorderLayout());
        this.setSize(GuiUtils.getDimensionWindow(WIDTH_PERC, HEIGHT_PERC));
        final JPanel board = new JPanel(new GridLayout(this.size, this.size));
        this.add(board);
        final JPanel[][] grid = new JPanel[this.size][this.size];

        // create the bottom row from right
        for (int col = this.size - 1; col >= 0; col--) {
            grid[this.size - 1][col] = createTile();
        }

        // create in the left column from the bottom
        for (int row = this.size - 2; row >= 0; row--) {
            grid[row][0] = createTile();
        }

        // create in the top row from left
        for (int col = 1; col < this.size; col++) {
            grid[0][col] = createTile();
        }

        // create on the right column from the top
        for (int row = 1; row < this.size - 1; row++) {
            grid[row][this.size - 1] = createTile();
        }

        // fill the tiles which are not in the board with gray layout, and create che community chest and chance tiles
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (grid[i][j] == null) {
                    final JPanel panel = new JPanel();
                    if (logic.tileCard(i, j, this.size) > -1) {
                        panel.setBorder(BorderFactory.createLineBorder(Color.black));

                        if (logic.tileCard(i, j, this.size) == 0) {
                            panel.setBackground(Color.RED);
                            final JTextArea text = new JTextArea("COMMUNITY CHEST");
                            text.setLineWrap(true);
                            text.setWrapStyleWord(true);
                            text.setEditable(false);
                            text.setBackground(Color.RED);
                            panel.add(text, BorderLayout.CENTER);
                        } else {
                            panel.setBackground(Color.YELLOW);
                            final JTextArea text = new JTextArea("CHANCE");
                            text.setLineWrap(true);
                            text.setWrapStyleWord(true);
                            text.setEditable(false);
                            text.setBackground(Color.YELLOW);
                            panel.add(text, BorderLayout.CENTER);
                        }
                    } else {
                        panel.setBackground(Color.LIGHT_GRAY);
                    }
                    grid[i][j] = panel;
                }
            }
        }

        // add the panels in the correct order
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                board.add(grid[i][j]);
            }
        }

        for (int i = 0; i < controller.getTiles().size(); i++) {
            final JPanel panel = this.tilesView.get(i);
            tilePositions.put(panel, controller.getTiles().get(i).getPosition());
            final JPanel stripe = new JPanel();
            stripe.setPreferredSize(new Dimension(STRIPE_WIDTH, STRIPE_HEIGHT));
            stripe.setBackground(controller.getTiles().get(i).getGroup().getColor());
            panel.add(stripe, BorderLayout.NORTH);
            final JTextArea text = new JTextArea(controller.getTiles().get(i).getName());
            text.setLineWrap(true);
            text.setWrapStyleWord(true);
            text.setEditable(false);
            panel.add(text, BorderLayout.CENTER);
            panel.setName(controller.getTiles().get(i).getName());
        }

        for (int i = 0; i < controller.getPawns().size(); i++) {
            pawnPositions.put(i, new PositionImpl(0));
        }

        for (int i = 0; i < controller.getPawns().size(); i++) {
            final JPanel panel = this.tilesView.get(pawnPositions.get(i).getPos());
            final PawnCircle pawnGUI = new PawnCircle(this.controller.getPawns().get(i).getColor());
            pawnGUI.setName("pawn" + i);
            panel.add(pawnGUI);
            panel.setLayout(new FlowLayout(FlowLayout.CENTER, PAWN_SIZE, PAWN_SIZE));
        }

        this.setVisible(true);
        GuiUtils.applyGlobalFont(GuiUtils.getFontFromConfiguration(controller.getConfiguration()));
    }

    @Override
    public Component getPanel() {
        return this;
    }
    /**
     * create the view for the tile of the board.
     * @return JPanel
     */
    private JPanel createTile() {
        final JPanel tile = new JPanel(new BorderLayout());
        tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        tile.setBackground(Color.WHITE);
        this.tilesView.add(tile);
        return tile;
    }

    @Override
    public void clearPanel(final String name) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(name)) { 
                final JPanel p = entry.getKey();
                for (final Component c : p.getComponents()) {
                    if (c instanceof PawnSquare) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                    }
                    if ((HOUSE.equals(c.getName()) || HOTEL.equals(c.getName())) && c instanceof JLabel) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                    }
                }
            }
        }
    }

    @Override
    public void removeHouse(final String propName, final int numHouses, final Color color) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(propName)) {
                final JPanel p = entry.getKey();
                for (final Component c : p.getComponents()) {
                    if (HOUSE.equals(c.getName()) && c instanceof JLabel) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();

                        if (numHouses > 0) {
                            final JLabel label = new JLabel("HOUSES: " + numHouses);
                            label.setName(HOUSE);
                            label.setForeground(color);
                            p.add(label);
                        }

                        break;
                    }
                }

            }
        }
    }

    @Override
    public void removeHotel(final String propName, final Color color) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            if (entry.getKey().getName().equals(propName)) {
                final JPanel p = entry.getKey();
                for (final Component c : p.getComponents()) {
                    if (HOTEL.equals(c.getName()) && c instanceof JLabel) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                        break;
                    }
                }
            }
        }
    }
    @Override
    public void deletePlayer(final Color color, final int id) {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            final JPanel p = entry.getKey();
            for (final Component c : p.getComponents()) {
                    if (c instanceof PawnCircle pawnCircle && pawnCircle.getColor().equals(color)) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                    } else if (c instanceof PawnSquare pawnSquare && pawnSquare.getColor().equals(color)) {
                        p.remove(c);
                        p.revalidate();
                        p.repaint();
                    }
                }
        }
        this.pawnPositions.remove(id - 1);
    }

    @Override
    public void clearAll() {
        for (final Map.Entry<JPanel, Position> entry : this.tilePositions.entrySet()) {
            final JPanel p = entry.getKey();
            for (final Component c : p.getComponents()) {
                if (c instanceof PawnSquare) {
                    p.remove(c);
                    p.revalidate();
                    p.repaint();
                }
            }
        }
    }
}
