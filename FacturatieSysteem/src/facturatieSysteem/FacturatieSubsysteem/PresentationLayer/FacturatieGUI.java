package facturatieSysteem.FacturatieSubsysteem.PresentationLayer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import facturatieSysteem.FacturatieSubsysteem.BusinessLayer.FacturatieManager;
import facturatieSysteem.FacturatieSubsysteem.EntityLayer.Bon;
import facturatieSysteem.FacturatieSubsysteem.EntityLayer.Factuur;
import facturatieSysteem.KlantenSubsysteem.EntityLayer.Klant;
import facturatieSysteem.KlantenSubsysteem.EntityLayer.VerzekeringPolis;
import facturatieSysteem.VerzekeringSubsysteem.BusinessLayer.VerzekeringsmaatschappijManager;
import facturatieSysteem.VerzekeringSubsysteem.EntityLayer.Verzekeringsmaatschappij;
import facturatieSysteem.VerzekeringSubsysteem.EntityLayer.Verzekeringstype;

/**
 * The Class FacturatieGUI.
 */
@SuppressWarnings("serial")
public class FacturatieGUI extends JFrame {

	/** The facturatie manager impl. */
	private FacturatieManager facturatieManagerImpl;
	
	/** The row. */
	private Integer row;
	
	/** The button panel. */
	private JPanel buttonPanel;
	
	/** The zoekbalk. */
	private JTextField zoekbalk;
	
	/** The zoek knop. */
	private JButton zoekKnop;
	
	/** The terug knop. */
	private JButton terugKnop;
	
	/** The factureer knop. */
	private JButton factureerKnop;
	
	/** The open factuur knop. */
	private JButton openFactuurKnop;
	
	/** The print factuur knop. */
	private JButton printFactuurKnop;
	
	/** The overzicht. */
	private JTable overzicht;
	
	/** The factuur. */
	private JTextArea factuur;
	
	/** The factuur table panel. */
	private JScrollPane factuurTablePanel;
	
	/** The scroll pane. */
	private JScrollPane scrollPane = new JScrollPane();
	
	/** The main panel. */
	private JPanel mainPanel = new JPanel();
	
	/** The klant. */
	private Klant klant;
	
	/** The facturen. */
	private ArrayList<Factuur> facturen;
	
	/** The data table model. */
	private DataTableModelFactuur dataTableModel;
	
	/** The east panel. */
	private JPanel eastPanel;
	
	/** The factuur panel. */
	private JPanel factuurPanel;
	
	/** The scroll factuur. */
	private JScrollPane scrollFactuur;
	
	/** The m1. */
	private VerzekeringsmaatschappijManager m1;
	
	/** The maatschappij eind. */
	private Verzekeringsmaatschappij maatschappijEind;

	/**
	 * Fact gui.
	 *
	 * @param facturatieManagerImpl the facturatie manager impl
	 * @param klant the klant
	 * @param m1 the manager of the facturatie system
	 * @return the jpanel
	 */
	public JPanel FactGUI(FacturatieManager facturatieManagerImpl, Klant klant,
			VerzekeringsmaatschappijManager m1) {
		JPanel paneel = new JPanel();
		paneel.setName("FACTURATIE");
		paneel.add(scrollPane, BorderLayout.CENTER);
		this.klant = klant;
		this.m1 = m1;
		this.facturatieManagerImpl = facturatieManagerImpl;
		facturen = new ArrayList<>();
		dataTableModel = new DataTableModelFactuur();
		return initComponents(facturatieManagerImpl);
	}

	/**
	 * Inits the components.
	 *
	 * @param factManagerImpl the fact manager impl
	 * @return the j panel
	 */
	public JPanel initComponents(FacturatieManager factManagerImpl) {

		// panels aanmaken
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 0));
		mainPanel.setBorder(new MatteBorder(0, 1, 0, 0, (Color) new Color(30,
				144, 255)));
		mainPanel.setBackground(new Color(255, 255, 255));

		eastPanel = new JPanel();
		eastPanel.setLayout(new BorderLayout(0, 0));
		eastPanel.setPreferredSize(new Dimension(500, 10));
		eastPanel.setBackground(new Color(255, 255, 255));

		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(255, 255, 255));

		factuurPanel = new JPanel();
		factuurPanel.setLayout(new BorderLayout(0, 0));

		// Tekst initialiseren van de knoppen, tekstvelden en textarea's.
		factuur = new JTextArea();
		factuur.setEditable(false);
		scrollFactuur = new JScrollPane(factuur);
		factuur.setBorder(new TitledBorder(new LineBorder(new Color(30, 144,
				255)), "Factuur", TitledBorder.LEADING, TitledBorder.TOP, null,
				null));
		scrollFactuur.setBorder(null);

		zoekKnop = new JButton();
		zoekKnop.setText("Zoek");

		terugKnop = new JButton();
		terugKnop.setText("Terug");

		factureerKnop = new JButton();
		factureerKnop.setText("Factureren");

		factureerKnop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (facturatieManagerImpl.factureer(klant, m1) == null) {
					showConfirmationWindow("Deze klant heeft geen behandelingen om te factureren");
				} else {
					facturen.clear();
					fillTable(klant);
				}
			}
		});

		openFactuurKnop = new JButton();
		openFactuurKnop.setText("Open factuur");

		printFactuurKnop = new JButton();
		printFactuurKnop.setText("Print factuur");

		zoekbalk = new JTextField();
		zoekbalk.setText("Vul factuurcode in");

		// overzicht tabel aanmaken en vullen.
		overzicht = new JTable(dataTableModel) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};

		String[] headers = new String[] { "Factuurnummer", "Factuurdatum",
				"Vervaldatum", "Status" };
		dataTableModel.setTableHeader(headers);

		TableColumn column = overzicht.getColumnModel().getColumn(0);
		column.setPreferredWidth(6);

		// Handle row selection, only one row can be selected
		overzicht.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		fillTable(klant);

		factuurTablePanel = new JScrollPane(overzicht);
		factuurTablePanel.setPreferredSize(new Dimension(450, 400));
		factuurTablePanel.setBackground(new Color(255, 255, 255));
		overzicht.setFillsViewportHeight(true);
		overzicht.getTableHeader().setReorderingAllowed(false);
		overzicht.getTableHeader().setResizingAllowed(false);

		factuurTablePanel.setBorder(new TitledBorder(new LineBorder(new Color(
				30, 144, 255)), "Facturenlijst", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		mainPanel.add(factuurTablePanel, BorderLayout.CENTER);

		overzicht.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				row = overzicht.getSelectedRow();
				fillField(row);
			}

		});

		openFactuurKnop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int row2 = overzicht.getSelectedRow();
				Factuur factuur = vindFactuur(row2);
				maatschappijEind = null;
				for (Verzekeringsmaatschappij maatschappij : m1
						.getVerzekeringsmaatschappijen()) {

					// Loopen door de typeArray om het te op te halen
					// van de maatschappij
					for (Verzekeringstype type : maatschappij.getTypes()) {

						// loopen voor het type
						String polisNaam = "";
						for (VerzekeringPolis polis : klant
								.getVerzekeringPolissen()) {
							polisNaam = polis.getVerzekeringsType();
						}
						// de maatschappij ophalen aan de hand van de
						// klant zijn type
						if (polisNaam.equals(type.getNaam())) {
							maatschappijEind = maatschappij;
							break;
						}
					}
				}
				new Bon(facturatieManagerImpl, factuur, maatschappijEind,
						klant, m1);
				if (Desktop.isDesktopSupported()) {
					File file = new File("Facturen/"
							+ factuur.getFactuurDatum() + "-"
							+ factuur.getFactuurNummer() + ".pdf");
					try {
						Desktop.getDesktop().open(file);
						file.deleteOnExit();
					} catch (IOException e1) {
						showConfirmationWindow("Desktop is not supported!");
					}

				}
			}

		});
		printFactuurKnop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row2 = overzicht.getSelectedRow();
				Factuur factuur = vindFactuur(row2);
				maatschappijEind = null;
				for (Verzekeringsmaatschappij maatschappij : m1
						.getVerzekeringsmaatschappijen()) {

					// Loopen door de typeArray om het te op te halen
					// van de maatschappij
					for (Verzekeringstype type : maatschappij.getTypes()) {

						// loopen voor het type
						String polisNaam = "";
						for (VerzekeringPolis polis : klant
								.getVerzekeringPolissen()) {
							polisNaam = polis.getVerzekeringsType();
						}
						// de maatschappij ophalen aan de hand van de
						// klant zijn type
						if (polisNaam.equals(type.getNaam())) {
							maatschappijEind = maatschappij;
							break;
						}

					}

				}

				new Bon(facturatieManagerImpl, factuur, maatschappijEind,
						klant, m1);

				String value = overzicht.getModel()
						.getValueAt(overzicht.getSelectedRow(), 0).toString();
				File file = null;
				if (value.equals(factuur.getFactuurNummer())) {
					file = new File("Facturen/" + factuur.getFactuurDatum()
							+ "-" + factuur.getFactuurNummer() + ".pdf");
					System.out.println(file);
					if (file.exists()) {
						System.out.println("print");
						if (Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().print(file);
							} catch (IOException e1) {
								showConfirmationWindow("There was no printer found.");
							}
							file.deleteOnExit();
						}
					} else {
						showConfirmationWindow("File doesn't exist, the invoice couldn't be created");
					}
				}
			}
		});

		// panels vullen
		buttonPanel.add(factureerKnop);
		buttonPanel.add(openFactuurKnop);
		buttonPanel.add(printFactuurKnop);

		factuurPanel.add(scrollFactuur);

		eastPanel.add(factuurPanel, BorderLayout.CENTER);
		eastPanel.add(buttonPanel, BorderLayout.SOUTH);

		mainPanel.add(eastPanel, BorderLayout.EAST);

		return mainPanel;

	}

	/**
	 * Fill table.
	 *
	 * @param klant the klant
	 */
	public void fillTable(Klant klant) {
		facturen = facturatieManagerImpl.haalFacturen(klant.getBSN());
		dataTableModel.setValues(facturen);
		facturen.clear();
	}

	/**
	 * method to fill the field with the linked information.
	 *
	 * @param row the row
	 */
	public void fillField(int row) {
		String factuur_nummer = overzicht.getModel().getValueAt(row, 0).toString();
		factuur.setText(facturatieManagerImpl.toonFactuur(factuur_nummer, klant));
		facturen.clear();
	}

	/**
	 * finds the factuur.
	 *
	 * @param row2 the row2
	 * @return Factuur
	 */
	public Factuur vindFactuur(int row2) {
		String factuur_nummer2 = overzicht.getModel().getValueAt(row2, 0).toString();
		return facturatieManagerImpl.getFactuur(factuur_nummer2, klant);
	}

	/**
	 * shows a confirmation window with a costum message.
	 *
	 * @param message the message
	 */
	public void showConfirmationWindow(String message) {
		Component frame = null;
		JOptionPane.showMessageDialog(frame, message);

	}
}
