package guiForms;

import managers.FilterManager;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.ListModel;
import javax.swing.RepaintManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


import javax.swing.AbstractListModel;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import controller.DBController;

import mainUI.ApplicationGUIMain;
import model.CheckListManager;
import model.ResponseModel;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;

public class FilterPopup extends JFrame {
	
	private JPanel contentPane;
	DefaultListModel tableNameLM=new DefaultListModel();
	public ResponseModel getAllResponse() {
		return allResponse;
	}

	public void setAllResponse(ResponseModel allResponse) {
		this.allResponse = allResponse;
	}

	ResponseModel allResponse=null;
	
	CheckListManager checkListManager=null;
	
	private JList tableNameLBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					FilterPopup frame = new FilterPopup(null);
//		            frame.setVisible(true);
		            
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public FilterPopup(ResponseModel allres) throws SQLException {
		
		super();
		setTitle("Filter Popup");
		allResponse=allres;
		
		try {
			UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName()) ;
			SwingUtilities.updateComponentTreeUI ( this ) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 268, 347);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		tableNameLBox = new JList();
		String xmlString =allResponse.getXmlString();
		
		Document xmlDoc = null;
		try {
			xmlDoc = Global.Common.convertStringtoXMLDoc(xmlString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList allColandValAL=Global.Common.splitValuesfromXML(xmlDoc);
		Hashtable uniqueHT=new Hashtable<>();
		int index=0;
		for(int i=0;i<allColandValAL.size();i++)
		{
			TreeMap rowTM=(TreeMap)allColandValAL.get(i);
			 Set<String> keys = rowTM.keySet();
		        for(String key: keys){
		        	if(!uniqueHT.contains(key))
		        	{
		        		uniqueHT.put(index, key);
		        		index++;
		        	}
		        	
		        }
				
		}
		final String[] uniqueCols=new String[uniqueHT.size()];
		for(int i=0;i<uniqueHT.size();i++)
		{
			uniqueCols[i]=(String)uniqueHT.get(i);
		}
		tableNameLBox.setModel(new AbstractListModel() {
			String[] values = uniqueCols;
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
		
		 checkListManager = new CheckListManager(tableNameLBox); 
        
		tableNameLBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JLabel lblTable = new JLabel("Filter");
		
		tableNameLBox.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                if (!arg0.getValueIsAdjusting()) {
                  
                }
            }
        });
      
		
		JButton btnApplyFilter = new JButton("Apply Filter");
		btnApplyFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Hashtable filterHT=new Hashtable<>();
				
				for(int i=0;i<tableNameLBox.getModel().getSize();i++)
				{
					if(checkListManager.getSelectionModel().isSelectedIndex(i))
					{
						filterHT.put(tableNameLBox.getModel().getElementAt(i), tableNameLBox.getModel().getElementAt(i));
					}
				}
				
				FilterManager fm=new FilterManager();
				fm.applyFilter(allResponse.getXmlString(),filterHT);
				
				repaint();
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(11)
					.addComponent(lblTable))
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addContainerGap(10, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnApplyFilter)
						.addComponent(tableNameLBox, GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE))
					.addGap(237))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(11)
					.addComponent(lblTable)
					.addGap(5)
					.addComponent(tableNameLBox, GroupLayout.PREFERRED_SIZE, 222, GroupLayout.PREFERRED_SIZE)
					.addGap(13)
					.addComponent(btnApplyFilter)
					.addContainerGap())
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	
	 
}
