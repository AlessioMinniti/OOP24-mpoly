package it.unibo.monopoly.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.monopoly.controller.api.GameController;
import it.unibo.monopoly.controller.impl.GUIVenditaLogicImpl;
import it.unibo.monopoly.model.gameboard.api.Board;
import it.unibo.monopoly.model.gameboard.api.Special;
import it.unibo.monopoly.model.transactions.api.Bank;
import it.unibo.monopoly.model.transactions.api.BankAccount;
import it.unibo.monopoly.model.transactions.api.PropertyActionsEnum;
import it.unibo.monopoly.model.transactions.api.TitleDeed;
import it.unibo.monopoly.model.turnation.api.Player;
import it.unibo.monopoly.utils.impl.GuiUtils;
import it.unibo.monopoly.view.api.AccountPanel;
import it.unibo.monopoly.view.api.ContractPanel;
import it.unibo.monopoly.view.api.GameActionsPanel;
import it.unibo.monopoly.view.api.GamePanelsFactory;
import it.unibo.monopoly.view.api.GameboardView;
import it.unibo.monopoly.view.api.MainGameView;
import it.unibo.monopoly.view.api.PlayerPanel;
import it.unibo.monopoly.view.api.StandardControlsPanel;
import it.unibo.monopoly.view.impl.gamepanels.SwingPanelsFactory;

/**
 * Implementation of the {@link MainGameView} interface
 * using the Swing components library. The class builds 
 * its graphical UI by using a combination of {@link JFrame} {@code objects}.
 */
public final class MainViewImpl implements MainGameView {

    private static final double INITIAL_WIDTH = 1.0;
    private static final double INITIAL_HEIGHT = 1.0;
    private static final double GAMEBOARD_ACTION_WIDTH_RATIO = 0.60;

    private final JFrame mainGameFrame = new JFrame();

    private final PlayerPanel playerInfoPanel;
    private final AccountPanel accountInfoPanel;
    private final ContractPanel contractPanel;
    private final GameActionsPanel gameActionsPanel;
    private final StandardControlsPanel mainActionsPanel;
    private final GameboardView gameBoardPanel;

    private final GameController controller;
    /**
     * Assembles the UI of the game interface and adds all components to {@code mainFrame} object.
     * The {@code mainFrame} is a {@link JFrame}. 
     * @param controller the {@link GameController} that captures the events 
     * happening on this view, implementing the observer pattern. User events
     * will be captured and handled by the {@code controller} provided to this constructor. 
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP",
                justification = "must return reference to the object instead of a copy")
    public MainViewImpl(final GameController controller) {
        this.controller = controller;
        this.gameBoardPanel = new GameboardViewImpl(controller);
        final GamePanelsFactory fact = new SwingPanelsFactory();
        contractPanel = fact.contractPanel();
        contractPanel.renderDefaultUI();
        playerInfoPanel = fact.userInfoPanel();
        playerInfoPanel.renderDefaultUI();
        accountInfoPanel = fact.bankAccountInfoPanel();
        accountInfoPanel.renderDefaultUI();
        gameActionsPanel = fact.gameActionsPanel();
        gameActionsPanel.renderDefaultUI();
        mainActionsPanel = fact.standardControlsPanel(controller);
        mainActionsPanel.renderDefaultUI();
        final JPanel actionPanel = buildActionPanelUI(controller);
        this.gameBoardPanel.getPanel().setPreferredSize(
            GuiUtils.getDimensionWindow(INITIAL_WIDTH * GAMEBOARD_ACTION_WIDTH_RATIO, INITIAL_HEIGHT)
        );
        final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gameBoardPanel.getPanel(), actionPanel);
        splitPane.setResizeWeight(GAMEBOARD_ACTION_WIDTH_RATIO); 
        splitPane.setDividerSize(2);    // Spessore del divisore
        mainGameFrame.getContentPane().add(splitPane);
        mainGameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainGameFrame.setSize(GuiUtils.getDimensionWindow(INITIAL_WIDTH, INITIAL_HEIGHT));
        mainGameFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                new GUIRanking(mainGameFrame, controller.getRanking(), controller.getWinner());
                mainGameFrame.dispose();
            }
        });
        mainGameFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                splitPane.setDividerLocation(GAMEBOARD_ACTION_WIDTH_RATIO);
            }
        });
        mainGameFrame.setVisible(true);
    }

    private JPanel buildActionPanelUI(final GameController controller) {
        final JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());
        final JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout());
        userInfoPanel.add(playerInfoPanel.getPanel(), BorderLayout.NORTH);
        userInfoPanel.add(accountInfoPanel.getPanel(), BorderLayout.SOUTH);

        actionPanel.add(userInfoPanel, BorderLayout.NORTH);
        actionPanel.add(contractPanel.getPanel(), BorderLayout.CENTER);
        actionPanel.add(gameActionsPanel.getPanel(), BorderLayout.WEST);

        final JButton handlePropertiesButton = new JButton("Handle properties");
        handlePropertiesButton.addActionListener(e -> controller.loadCurrentPlayerInformation());

        final JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(handlePropertiesButton, BorderLayout.SOUTH);
        southPanel.add(mainActionsPanel.getPanel(), BorderLayout.CENTER);

        actionPanel.add(southPanel, BorderLayout.SOUTH);
        return actionPanel;
    }

    @Override
    public void displayPlayerInfo(final Player player, final BankAccount account) {
        playerInfoPanel.displayPlayer(player);
        accountInfoPanel.displayBankAccount(account);
        mainGameFrame.repaint();
    }

    @Override
    public void refreshUIForNewTurn(final boolean canThrowDices) {
        playerInfoPanel.renderDefaultUI();
        accountInfoPanel.renderDefaultUI();
        contractPanel.renderDefaultUI();
        gameActionsPanel.renderDefaultUI();
        mainActionsPanel.renderDefaultUI();
        mainActionsPanel.setDiceButtonEnabled(canThrowDices);
        mainGameFrame.repaint();
    }

    @Override
    public void displayPropertyContractInfo(final TitleDeed propertyContract) {
        contractPanel.displayPropertyContract(propertyContract);
        mainGameFrame.repaint();
    }

    @Override
    public void displaySpecialInfo(final Special tile) {
        contractPanel.displaySpecialInfo(tile);
        mainGameFrame.repaint();
    }

    @Override
    public void displayPlayerActions(final Set<PropertyActionsEnum> actions) {
        gameActionsPanel.buildActionButtons(actions, controller);
        mainGameFrame.repaint();
    }


    @Override
    public void displayDiceResult(final List<Integer> results) {
        mainActionsPanel.displayDicesResults(results);
    }

    @Override
    public void displayRules(final String rules) {
        new RulesWindowView(this.mainGameFrame, rules);
    }

    @Override
    public void displayPlayerStats(final Player player, final Bank bank, final Board board) {
        new GUIVendita(player,
            new GUIVenditaLogicImpl(), 
            bank,
            board,
            this,
            this.mainGameFrame
        );
        //this.displayPlayerInfo(player, bank.getBankAccount(player.getID()));
    }

    @Override
    public void displayMessage(final String message) {
        GuiUtils.showInfoMessage(mainGameFrame, message, message);
    }

    @Override
    public void displayError(final Exception e) {
        GuiUtils.showInfoMessage(mainGameFrame, "ERROR", e.getMessage());
    }

    @Override
    public void callChangePositions() {
        this.gameBoardPanel.changePos(this.controller.getCurrPlayer().getID(), this.controller.getCurrPawn().getPosition());
    }

    @Override
    public void callClearPanel(final String name) {
        this.gameBoardPanel.clearPanel(name);
    }

    @Override
    public void callBuyProperty(final String name, final Color color) {
        this.gameBoardPanel.buyProperty(name, color);
    }

    @Override
    public void displayOptionMessage(final String message) {
        final int result = JOptionPane.showConfirmDialog(null, message, "Continue?", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            this.controller.endTurnPlayerDies();
        }
    }

    @Override
    public void callBuyHouse(final String propName, final Color color, final int nHouses) {
        this.gameBoardPanel.addHouse(propName, color, nHouses);
    }

    @Override
    public void callBuyHotel(final String propName, final Color color) {
        this.gameBoardPanel.addHotel(propName, color);
    }

    @Override
    public void callSellHouse(final String propName, final int nHouses, final Color color) {
        this.gameBoardPanel.removeHouse(propName, nHouses, color);
    }

    @Override
    public void callSellHotel(final String propName, final Color color) {
       this.gameBoardPanel.removeHotel(propName, color);
    }
    @Override
    public void callDeletePlayer(final Color color, final int id) {
        this.gameBoardPanel.deletePlayer(color, id);
    }

    @Override
    public void showRanking() {
        new GUIRanking(mainGameFrame, controller.getRanking(), controller.getWinner());
        mainGameFrame.dispose();
    }

    @Override
    public void callClearAll() {
        this.gameBoardPanel.clearAll();
    }
}
