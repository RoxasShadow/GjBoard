/**
	/GjBoard.java
	(C) Giovanni Capuano 2011
*/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/* Costruisce la GUI, dichiara i listener e contiene il main. */
public class GjBoard extends JFrame {
	private JTabbedPane jTabbedPane1;
    
/* <------ TAB 1 ------> */
	private JPanel jPanel1;
	private JButton jButton1;
	private JLabel jLabel0;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private SpinnerModel model1;
	private JSpinner jSpinner1;
	private JTextArea jTextArea1;
	private JTextField jTextField0;
	private JTextField jTextField1;
	private JScrollPane jScrollPane1;
    
/* <------ TAB 2 ------> */
	private JPanel jPanel2;
	private final DefaultListModel model2;
	private JList jList1;
	private JTextArea jTextArea2;
	private JSplitPane jSplitPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JButton jButton2;
	private JButton jButton3;
	private JButton jButton4;
	private JButton jButton5;
    
/* <------ GJBOARD ------>*/
	private int giorno, mese, anno, ora, minuto, importanza;
	private String appunto, categoria, titolo;
	private Text text, defaultText;
	private Core core;
	private Date date;

	public GjBoard() {
		super("GjBoard 0.99.1"); // Il nome dell'applicazione
        	core = new Core(); // Istanzio il Core
        	
/* <------ COMPONENTI GUI ------> */
		jTabbedPane1 = new JTabbedPane();
		jPanel1 = new JPanel(new BorderLayout());
		jPanel2 = new JPanel(new BorderLayout());
		jTabbedPane1.addTab("Crea", jPanel1);
		jTabbedPane1.addTab("Leggi", jPanel2);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
/* <------ TAB 1 ------> */
		jLabel0 = new JLabel("Titolo");
		jLabel1 = new JLabel("Categoria");
		jLabel2 = new JLabel("Importanza");
		jLabel3 = new JLabel("Appunto");
		jTextField0 = new JTextField();
		jTextField1 = new JTextField();
		model1 = new SpinnerNumberModel(1, 1, 10, 1);
		jSpinner1 = new JSpinner(model1);
		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();
		jButton1 = new JButton("Salva");
		
		jScrollPane1.setViewportView(jTextArea1);
		
		/* Al click del bottone prende i dati dai textfield e dagli spinner e li invia al database. Poi avverte con un alert. */
		jButton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					date = new Date();
					giorno = date.getGiorno();
					mese = date.getMese();
					anno = date.getAnno();						
					ora = date.getOra();	
					minuto = date.getMinuto();
					
					importanza = (Integer)jSpinner1.getValue();
					categoria = jTextField1.getText();
					appunto = jTextArea1.getText();
					titolo = jTextField0.getText();
					
					if((categoria.isEmpty()) || (appunto.isEmpty()) || (titolo.isEmpty()))
						JOptionPane.showMessageDialog(null, "Devi compilare tutti i campi per creare un nuovo appunto.");
					else {
						text = new Text(giorno, mese, anno, ora, minuto, importanza, appunto, categoria, titolo);
						if(core.newText(text) == 5)
							JOptionPane.showMessageDialog(null, "Esiste già un appunto con lo stesso titolo.");
						else if(core.newText(text) == 1)
							JOptionPane.showMessageDialog(null, "È accaduto un errore durante il controllo dell'esistenza di un appunto duplicato.");
						else
							if(core.addTexts())
								JOptionPane.showMessageDialog(null, "L'appunto è stato inserito nel database.");
							else
								JOptionPane.showMessageDialog(null, "Errore durante l'inserimento dell'appunto nel database.");
					}
				}
				catch(NumberFormatException n) {
					JOptionPane.showMessageDialog(null, "È accaduto un errore durante la lettura dell'input: assicurarsi che l'importanza dell'appunto sia indicata con un numero che va da 1 a 10.");
				}
			}
		});
		
/* <----- TAB 2 -----> */
		model2 = new DefaultListModel();
		jList1 = new JList(model2);
		jTextArea2 = new JTextArea();
		jSplitPane1 = new JSplitPane();
		jScrollPane2 = new JScrollPane();
		jScrollPane3 = new JScrollPane();
		jButton2 = new JButton("Esporta");
		jButton3 = new JButton("Elimina");
		jButton4 = new JButton("Elimina tutto");
		jButton5 = new JButton("About");
		
		jScrollPane2.setViewportView(jList1);
		jScrollPane3.setViewportView(jTextArea2);
		
		jSplitPane1.setLeftComponent(jScrollPane2);
		jSplitPane1.setRightComponent(jScrollPane3);
		
		/* Al click del bottone richiede il nome del file da creare e ci copia gli appunti */
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomefile = JOptionPane.showInputDialog(null, "Nome del file da creare");
				if(core.esportaAppunti(nomefile) == 0)
					JOptionPane.showMessageDialog(null, "Il file è stato creato.");
				else if(core.esportaAppunti(nomefile) == 6)
					JOptionPane.showMessageDialog(null, "È accaduto un errore nella creazione del file.");
				else
					JOptionPane.showMessageDialog(null, "È accaduto un errore prelevando gli appunti dal database.");
			}
		});
		
		/* Al click del bottone elimina l'appunto selezionato e aggiorna il tab */
		jButton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String titolo = (String)jList1.getSelectedValue();
				if(core.eliminaAppunto(titolo))
					JOptionPane.showMessageDialog(null, "L'appunto è stato cancellato.");
				else
					JOptionPane.showMessageDialog(null, "È accaduto un errore durante la cancellazione dell'appunto dal database.");
				jTextArea2.setText("");
				model2.clear();
				for(String titoli:core.buildMenu())
					model2.addElement(titoli.toString());
			}
		});
		
		/* Al click del bottone elimina tutto il contenuto del database e aggiorna il tab */
		jButton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String titolo = (String)jList1.getSelectedValue();
				if(core.eliminaAppunti() == 0)
					JOptionPane.showMessageDialog(null, "Tutti gli appunti sono stati cancellati");
				else if((core.eliminaAppunti() == 6) || (core.eliminaAppunti() == 2))
					JOptionPane.showMessageDialog(null, "È accaduto un errore durante la cancellazione degli appunti dal database.");
				jTextArea2.setText("");
				model2.clear();
				for(String titoli:core.buildMenu())
					model2.addElement(titoli.toString());
			}
		});
		
		/* Al click del bottone apre la finestra con l'about */
		jButton5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "GjBoard v0.98 (C) 2011\nAutore: Giovanni 'Roxas Shadow' Capuano\nRingraziamenti: Marco RootkitNeo\nLicenza: GNU/GPLv3\nSito web: http://www.giovannicapuano.net\nChangelog: Gestione delle eccezioni");
			}
		});		
		
		/* Ad ogni scambio di tab rimuove gli elementi dal menù e li rimette prendendoli dall'arraylist di appunti */
		jTabbedPane1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				jTextArea2.setText("");
				model2.clear();
				for(String titoli:core.buildMenu())
					model2.addElement(titoli.toString());
			}
		});
		
		/* Al click di un elemento, lo visualizza nella textarea. Ogni click corrisponde a selezione e rilascio, quindi a due valori true e false. */
		jList1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String titolo = (String)jList1.getSelectedValue();
				if(jList1.getValueIsAdjusting() == true) {
					jTextArea2.setText("");
					jTextArea2.setText(core.readText(titolo));
				}
			}
		});
		
/* <------ LAYOUT ------> */
	/* <------ TAB 1 ------> */
		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
		    jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(jPanel1Layout.createSequentialGroup()
		        .addContainerGap()
		        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
		            .addGroup(jPanel1Layout.createSequentialGroup()
		                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                    .addGroup(jPanel1Layout.createSequentialGroup()
		                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                            .addComponent(jLabel1)
		                            .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
		                        .addGap(24)
		                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                            .addComponent(jLabel0)
		                            .addComponent(jTextField0, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
		                        .addGap(24)
		                        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
		                            .addComponent(jSpinner1)
		                            .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
		                    .addComponent(jLabel3))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 110, Short.MAX_VALUE)
		                .addComponent(jButton1)))
		        .addContainerGap())
		);
		jPanel1Layout.setVerticalGroup(
		    jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(jPanel1Layout.createSequentialGroup()
		        .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
		            .addGroup(jPanel1Layout.createSequentialGroup()
		                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                    .addComponent(jLabel1)
		                    .addComponent(jLabel0)
		                    .addComponent(jLabel2))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                    .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		                    .addComponent(jTextField0, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		                    .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                .addComponent(jLabel3))
		            .addComponent(jButton1))
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
		        .addContainerGap())
		);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
		    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(layout.createSequentialGroup()
		        .addContainerGap()
		        .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
		        .addContainerGap())
		);
		layout.setVerticalGroup(
		    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(layout.createSequentialGroup()
		        .addContainerGap()
		        .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
		        .addContainerGap())
		);
		
	/* <------ TAB 2 ------> */
		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
		    jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(jPanel2Layout.createSequentialGroup()
		        .addContainerGap()
		        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		            .addComponent(jSplitPane1, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
		            .addGroup(jPanel2Layout.createSequentialGroup()
		                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
		                    .addComponent(jButton3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                    .addComponent(jButton2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		                .addGap(156, 156, 156)
		                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
		                    .addComponent(jButton5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		                    .addComponent(jButton4, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))))
		        .addContainerGap())
		);
		jPanel2Layout.setVerticalGroup(
		    jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(jPanel2Layout.createSequentialGroup()
		        .addContainerGap()
		        .addComponent(jSplitPane1, GroupLayout.PREFERRED_SIZE, 281, GroupLayout.PREFERRED_SIZE)
		        .addGap(12, 12, 12)
		        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(jButton3)
		            .addComponent(jButton4))
		        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		        .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		            .addComponent(jButton2)
		            .addComponent(jButton5))
		        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);

		GroupLayout layout2 = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout2);
		layout2.setHorizontalGroup(
		    layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(layout2.createSequentialGroup()
		        .addContainerGap()
		        .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		        .addContainerGap())
		);
		layout2.setVerticalGroup(
		    layout2.createParallelGroup(GroupLayout.Alignment.LEADING)
		    .addGroup(layout2.createSequentialGroup()
		        .addContainerGap()
		        .addComponent(jTabbedPane1, GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
		        .addContainerGap())
		);
        
	}
    
	public static void makeGUI() {
		GjBoard GjBoard = new GjBoard();
		GjBoard.setSize(450,500);
		GjBoard.setVisible(true);
	}
    
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					makeGUI();
				}
			});
		}
		catch(Exception e) {
			System.out.println("Inizializzazione fallita.");
		}
	}
}
