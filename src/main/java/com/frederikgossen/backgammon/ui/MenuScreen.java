package com.frederikgossen.backgammon.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import com.frederikgossen.backgammon.ai.AI;
import com.frederikgossen.backgammon.gamelogic.PlayerAndAI;
import com.frederikgossen.backgammon.model.Player;
import com.frederikgossen.backgammon.ui.exceptions.InvalidInputException;

public class MenuScreen extends JFrame {

	private static final long serialVersionUID = 1141072142930226119L;

	private MenuScreenAILoader aiLoader;

	private JPanel jPanel;
	private JButton btnLoadAIs;
	private JList<String> lstAis;
	private JTextField tfNumberOfGames;
	private JCheckBox cbShowMoves;
	private JTextField tfDurationPerMoveInMs;
	private JButton btnStart;
	private JProgressBar pbProgress;
	private JButton btnCancel;

	public MenuScreen(String title) {
		super(title);

		aiLoader = new MenuScreenAILoader();

		jPanel = new JPanel();
		setContentPane(jPanel);
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		jPanel.add(new JLabel("Select AIs"));
		lstAis = new JList<String>(aiLoader.getNames());
		lstAis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		jPanel.add(new JScrollPane(lstAis));
		btnLoadAIs = new JButton("Load more AIs");
		btnLoadAIs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadAIsFromFile();
			}
		});
		jPanel.add(btnLoadAIs);
		jPanel.add(new JLabel("Number of Games"));
		tfNumberOfGames = new JTextField();
		tfNumberOfGames.setText("100000");
		jPanel.add(tfNumberOfGames);
		cbShowMoves = new JCheckBox("Show moves");
		cbShowMoves.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfDurationPerMoveInMs.setEnabled(cbShowMoves.isSelected());
			}
		});
		cbShowMoves.setSelected(false);
		jPanel.add(cbShowMoves);
		jPanel.add(new JLabel("Duration per move in ms"));
		tfDurationPerMoveInMs = new JTextField();
		tfDurationPerMoveInMs.setEnabled(false);
		tfDurationPerMoveInMs.setText("500");
		jPanel.add(tfDurationPerMoveInMs);
		btnStart = new JButton("Start");
		jPanel.add(btnStart);
		pbProgress = new JProgressBar();
		jPanel.add(pbProgress);
		btnCancel = new JButton("Cancel");
		btnCancel.setEnabled(false);
		jPanel.add(btnCancel);

		pack();
		setResizable(false);
	}

	private void loadAIsFromFile() {
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(true);
		fc.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Backgammon AI (*.jar)";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".jar");
			}
		});
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File[] selectedFiles = fc.getSelectedFiles();
			aiLoader.loadFromJarFiles(selectedFiles);
			lstAis.setListData(aiLoader.getNames());
		}
	}

	public void addOnStartListener(ActionListener al) {
		btnStart.addActionListener(al);
	}

	public void addOnCancelListener(ActionListener al) {
		btnCancel.addActionListener(al);
	}

	public List<PlayerAndAI> getSelectedPlayerAndAIs() throws InvalidInputException {
		List<String> selectedAIs = lstAis.getSelectedValuesList();
		if (selectedAIs.size() < 2)
			throw new InvalidInputException("At least 2 AIs must be selected");
		List<PlayerAndAI> selectedPlayerAndAIs = new ArrayList<PlayerAndAI>();
		for (String name : selectedAIs) {
			AI ai = aiLoader.newInstance(name);
			Player p = new Player(name);
			selectedPlayerAndAIs.add(new PlayerAndAI(p, ai));
		}
		return selectedPlayerAndAIs;
	}

	public int getNumberOfGames() throws InvalidInputException {
		String numberOfGames = tfNumberOfGames.getText();
		try {
			return Integer.parseUnsignedInt(numberOfGames);
		} catch (NumberFormatException e) {
			throw new InvalidInputException("Number of games must be positive integral");
		}
	}

	public boolean isShowMovesSelected() {
		return cbShowMoves.isSelected();
	}

	public int getDurationPerMoveInMs() throws InvalidInputException {
		if (isShowMovesSelected()) {
			String durationInMs = tfDurationPerMoveInMs.getText();
			try {
				int iDurationInMs = Integer.parseUnsignedInt(durationInMs);
				return Math.max(10, iDurationInMs);
			} catch (NumberFormatException e) {
				throw new InvalidInputException("Duration per move in ms must be positive integral");
			}
		} else {
			return 0;
		}
	}

	public void setInProgress(final boolean inProgress) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				btnStart.setEnabled(!inProgress);
				btnCancel.setEnabled(inProgress);
				if (!inProgress) {
					pbProgress.setMinimum(0);
					pbProgress.setValue(0);
				}
			}
		});
	}

	public void setProgress(final double p) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				int n = pbProgress.getWidth();
				pbProgress.setMinimum(0);
				pbProgress.setMaximum(n);
				pbProgress.setValue((int) (p * n));
			}
		});
	}
}
