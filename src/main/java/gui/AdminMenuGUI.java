package gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import businessLogic.BlFacade;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JComboBox;

public class AdminMenuGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private BlFacade businessLogic;
	
	private JPanel contentPane;
	private JButton createEventBtn;
	private JButton createQuestionBtn;
	private JButton addForecastBtn;
	private JButton browseQuestionsBtn;
	private JComboBox<String> localeCB;

	/**
	 * Create the frame.
	 */
	public AdminMenuGUI(BlFacade bl) {
		businessLogic = (BlFacade) bl;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 398, 261);
		setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/final_logo.png"));
		setTitle(ResourceBundle.getBundle("Etiquetas").getString("AdminMenu"));
	
		initializeMenuPane(); 
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addComponent(createQuestionBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(addForecastBtn, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(browseQuestionsBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addComponent(createEventBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(localeCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(createEventBtn, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(createQuestionBtn, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(addForecastBtn, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(browseQuestionsBtn, GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * This method initializes the menu pane.
	 */
	private void initializeMenuPane() {
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		initializeLocaleCB();
		initializeCreateEventBtn();
		initializeCreateQuestionBtn();
		initializeAddForecastBtn();
		initializeBrowseQuestionsBtn();
	}
	
	/**
	 * This method initializes the combo box with the languages.
	 */
	private void initializeLocaleCB() {
		localeCB = new JComboBox<String>();
		
		localeCB.addItem("EN");
		localeCB.addItem("EUS");
		localeCB.addItem("ES");
		
		localeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String language = localeCB.getSelectedItem().toString();
				Locale.setDefault(new Locale(language));
				System.out.println("Locale: " + Locale.getDefault());
				redraw();
			}
		});	
	}
	
	/**
	 * This methods initializes the button for creating events.
	 */
	private void initializeCreateEventBtn() {
		createEventBtn = new JButton();	
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		
		createEventBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateEventGUI createEventWindow = new CreateEventGUI(businessLogic);
				createEventWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initialized the button for creating questions.
	 */
	private void initializeCreateQuestionBtn() {
		createQuestionBtn = new JButton();
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		
		createQuestionBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateQuestionGUI createQuestionWindow = new CreateQuestionGUI(businessLogic);
				createQuestionWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initializes the button for browsing questions.
	 */
	private void initializeAddForecastBtn() {
		addForecastBtn = new JButton();
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		
		addForecastBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CreateForecastGUI createForecastWindow = new CreateForecastGUI(businessLogic);
				createForecastWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method initializes the method for browsing questions.
	 */
	private void initializeBrowseQuestionsBtn() {
		browseQuestionsBtn = new JButton();
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		
		browseQuestionsBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BrowseQuestionsGUI browseQuestionsWindow = new BrowseQuestionsGUI(businessLogic);
				browseQuestionsWindow.setVisible(true);
				dispose();
			}
		});
	}
	
	/**
	 * This method refreshes all the text fields in the GUI. It is used when the
	 * language of the application is switched.
	 */
	private void redraw() {
		createEventBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateEvent"));
		createQuestionBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("CreateQuestion"));
		addForecastBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("AddForecast"));
		browseQuestionsBtn.setText(ResourceBundle.getBundle("Etiquetas").getString("BrowseQuestions"));
		
	}
}
