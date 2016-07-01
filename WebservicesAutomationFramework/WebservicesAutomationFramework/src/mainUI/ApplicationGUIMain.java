package mainUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import java.awt.List;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Button;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.CardLayout;
import javax.swing.SpringLayout;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.DropMode;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.ServiceController;

import java.awt.TextArea;
import java.util.Hashtable;


public class ApplicationGUIMain extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField urlField;
	public static JLabel statusLabel=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationGUIMain frame = new ApplicationGUIMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ApplicationGUIMain() {
		setTitle("WebServices Simulator");
		try {
			UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName()) ;
			SwingUtilities.updateComponentTreeUI ( this ) ;
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 500, 1060,711);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		final JComboBox method_cBox = new JComboBox();
		method_cBox.setBounds(20, 44, 70, 23);
		method_cBox.setModel(new DefaultComboBoxModel(new String[] {"GET", "POST", "PUT", "DELETE"}));
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setBounds(20, 107, 474, 534);
		
	
		statusLabel = new JLabel("");
		statusLabel.setBounds(20, 652, 497, 21);
		contentPane.add(statusLabel);
				
		
		textField = new JTextField();
		textField.setColumns(10);
		
//		TextArea txtArea_HTMLreq = new TextArea();
//		tabbedPane.addTab("TEXT/XML", null, txtArea_HTMLreq, null);
		
//		TextArea txtArea_PLAINTEXTreq = new TextArea();
//		tabbedPane.addTab("multipart", null, txtArea_PLAINTEXTreq, null);
		
		final TextArea txtArea_XMLreq = new TextArea();
		tabbedPane.addTab("Application/XML", null, txtArea_XMLreq, null);
		
		final TextArea txtArea_JSONreq = new TextArea();
		tabbedPane.addTab("Application/JSON", null, txtArea_JSONreq, null);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane_1.setBounds(505, 106, 514, 535);
		
		
		final TextArea txtArea_HTMLres = new TextArea();
		tabbedPane_1.addTab("HTML", null, txtArea_HTMLres, null);
		
		final TextArea txtArea_PLAINTEXTres = new TextArea();
		tabbedPane_1.addTab("PLAIN TEXT", null, txtArea_PLAINTEXTres, null);
		
		final TextArea txtArea_XMLres = new TextArea();
		tabbedPane_1.addTab("XML", null, txtArea_XMLres, null);
		
		final TextArea txtArea_JSONres = new TextArea();
		tabbedPane_1.addTab("JSON", null, txtArea_JSONres, null);
		contentPane.add(tabbedPane_1);
		
		JLabel lblRequest = new JLabel("REQUEST");
		lblRequest.setBounds(20, 82, 46, 14);
		contentPane.add(lblRequest);
		
	
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setBounds(292, 397, 89, 23);
		
		JButton sendButton = new JButton("SEND");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				txtArea_JSONres.setText("Please Wait...");
				txtArea_HTMLres.setText("Please Wait...");
				txtArea_XMLres.setText("Please Wait...");
				txtArea_PLAINTEXTres.setText("Please Wait...");
				
				int selectedMTypeIndex=tabbedPane.getSelectedIndex();
				String selectedMediaType="";
				String reqText="";
				System.out.println("Selected Index : "+selectedMTypeIndex);
				if(selectedMTypeIndex==0)
				{
					selectedMediaType="application/xml";
					reqText=txtArea_XMLreq.getText();
					
				}else{
					selectedMediaType="application/json";
					reqText=txtArea_JSONreq.getText();
				}
				
				
				
				Hashtable allResponseHT=new Hashtable<>();
				ServiceController svcController=new ServiceController(urlField.getText(), method_cBox.getSelectedItem().toString(),selectedMediaType,reqText);
				allResponseHT=svcController.sendRequest();
				repaint();
				txtArea_JSONres.setText((String)allResponseHT.get("JSON"));
				txtArea_HTMLres.setText((String)allResponseHT.get("HTML"));
				txtArea_XMLres.setText((String)allResponseHT.get("XML"));
				txtArea_PLAINTEXTres.setText((String)allResponseHT.get("PLAIN"));
				
			}
		});
		sendButton.setBounds(930, 78, 89, 23);
		contentPane.add(sendButton);
		contentPane.add(tabbedPane_1);
		contentPane.add(lblRequest);
		contentPane.add(sendButton);
		contentPane.add(method_cBox);
		contentPane.add(tabbedPane);
		
		JLabel lblResponse = new JLabel("RESPONSE");
		lblResponse.setBounds(505, 82, 52, 14);
		contentPane.add(lblResponse);
		
		urlField = new JTextField("http://localhost:8085/WebservicesAutomationFramework/json/product/EmployeeDetails");
		urlField.setBounds(120, 44, 895, 23);
		contentPane.add(urlField);
		urlField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("End Point");
		lblNewLabel.setBounds(120, 22, 46, 29);
		contentPane.add(lblNewLabel);
		
		
		
		
	
	
		
	}
}
