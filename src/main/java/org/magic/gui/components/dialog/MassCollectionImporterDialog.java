package org.magic.gui.components.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import org.apache.log4j.Logger;
import org.magic.api.beans.MTGNotification;
import org.magic.api.beans.MTGNotification.MESSAGE_TYPE;
import org.magic.api.beans.MagicCard;
import org.magic.api.beans.MagicCollection;
import org.magic.api.beans.MagicDeck;
import org.magic.api.beans.MagicEdition;
import org.magic.api.interfaces.MTGCardsProvider;
import org.magic.api.interfaces.MTGDao;
import org.magic.gui.abstracts.AbstractBuzyIndicatorComponent;
import org.magic.services.MTGConstants;
import org.magic.services.MTGControler;
import org.magic.services.MTGLogger;
import org.magic.services.ThreadManager;
import org.magic.tools.UITools;

public class MassCollectionImporterDialog extends JDialog {

	private static final String NUMBER = "number";
	private static final long serialVersionUID = 1L;
	private String[] ids;
	private JTextPane txtNumbersInput;
	private MagicDeck deck;
	private transient Logger logger = MTGLogger.getLogger(this.getClass());
	private JComboBox<MagicEdition> cboEditions;
	
	public MassCollectionImporterDialog() {
		setSize(new Dimension(646, 290));
		setIconImage(MTGConstants.ICON_MASS_IMPORT.getImage());
		setTitle(MTGControler.getInstance().getLangService().getCapitalize("MASS_CARDS_IMPORT"));
		try {
			initGUI();
		} catch (Exception e) {
			logger.error("error init", e);
		}
	}

	private void initGUI() throws SQLException {
		getContentPane().setLayout(new BorderLayout(0, 0));
		deck = new MagicDeck();
		JPanel panelCollectionInput = new JPanel();
		getContentPane().add(panelCollectionInput, BorderLayout.NORTH);

		JLabel lblImport = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("IMPORT") + " ");
		panelCollectionInput.add(lblImport);
		DefaultComboBoxModel<MagicCollection> modCollections=new DefaultComboBoxModel<>();
		
		try {
			MTGControler.getInstance().getEnabled(MTGDao.class).listCollections().forEach(c->modCollections.addElement(c));
		} catch (Exception e2) {
			logger.error(e2);
		}
		
		cboEditions =UITools.createComboboxEditions();
		panelCollectionInput.add(cboEditions);
		
		JLabel lblNewLabel = new JLabel(MTGControler.getInstance().getLangService().getCapitalize("BY"));
		panelCollectionInput.add(lblNewLabel);

		JComboBox<String> cboByType = UITools.createCombobox(new String[] { NUMBER, "name" });
		panelCollectionInput.add(cboByType);

		JLabel lblIn = new JLabel("in");
		panelCollectionInput.add(lblIn);
		
		JComboBox<MagicCollection> cboCollections = UITools.createComboboxCollection();
		panelCollectionInput.add(cboCollections);

		JPanel panneauBas = new JPanel();
		getContentPane().add(panneauBas, BorderLayout.SOUTH);
		AbstractBuzyIndicatorComponent progressBar = AbstractBuzyIndicatorComponent.createProgressComponent();
		

		final JCheckBox checkNewOne = new JCheckBox(
				MTGControler.getInstance().getLangService().getCapitalize("IMPORT_OTHER_SERIE"));

		JButton btnInverse = new JButton("Inverse");
		btnInverse.addActionListener(e -> {
			MagicEdition ed = (MagicEdition) cboEditions.getSelectedItem();
			int max = ed.getCardCount();
			List<String> elements = Arrays
					.asList(txtNumbersInput.getText().replaceAll("\n", " ").replaceAll("  ", " ").trim().split(" "));
			List<String> edList = new ArrayList<>();
			for (int i = 1; i <= max; i++)
				edList.add(String.valueOf(i));

			edList.removeAll(elements);

			StringBuilder temp = new StringBuilder();
			for (String s : edList)
				temp.append(s).append(" ");

			txtNumbersInput.setText(temp.toString());
		});
		panneauBas.add(btnInverse);

		panneauBas.add(checkNewOne);

		JButton btnImport = new JButton(MTGControler.getInstance().getLangService().getCapitalize("IMPORT"),MTGConstants.ICON_MASS_IMPORT);
		btnImport.addActionListener(e -> {
			final MagicEdition ed = (MagicEdition) cboEditions.getSelectedItem();
			final MagicCollection col = (MagicCollection) cboCollections.getSelectedItem();

			if (cboByType.getSelectedItem().equals(NUMBER))
				ids = txtNumbersInput.getText().replaceAll("\n", " ").replaceAll("  ", " ").trim().split(" ");
			else
				ids = txtNumbersInput.getText().split("\n");
		
			progressBar.start(ids.length);
			ThreadManager.getInstance().execute(() -> {
				for (String id : ids) {
					try {
						MagicCard mc = null;

						if (cboByType.getSelectedItem().toString().equalsIgnoreCase(NUMBER))
							mc = MTGControler.getInstance().getEnabled(MTGCardsProvider.class).getCardByNumber(id, ed);
						else
							mc = MTGControler.getInstance().getEnabled(MTGCardsProvider.class)
									.searchCardByName( id.replaceAll("\n", " ").replaceAll("  ", " ").trim(),
											(MagicEdition) cboEditions.getSelectedItem(), true)
									.get(0);

						deck.add(mc);
						MTGControler.getInstance().saveCard(mc,col);
						progressBar.progress();
					} catch (Exception e1) {
						logger.error(e1);
					}
				}
				
				MTGControler.getInstance().notify(new MTGNotification(
						MTGControler.getInstance().getLangService().getCapitalize("FINISHED"),
						MTGControler.getInstance().getLangService().getCapitalize("X_ITEMS_IMPORTED", ids.length),
						MESSAGE_TYPE.INFO
						));
				if (!checkNewOne.isSelected()) {
					setVisible(false);
					progressBar.end();
				}
			}, "btnImport importCards");
		});
		panneauBas.add(btnImport);

		panneauBas.add(progressBar);

		txtNumbersInput = new JTextPane();

		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		scrollPane.setViewportView(txtNumbersInput);

		setModal(true);
		setLocationRelativeTo(null);

	}

	public MagicDeck getAsDeck() {
		return deck;
	}

	public void setDefaultEdition(MagicEdition magicEdition) {
		cboEditions.setSelectedItem(magicEdition);
		
	}

}
