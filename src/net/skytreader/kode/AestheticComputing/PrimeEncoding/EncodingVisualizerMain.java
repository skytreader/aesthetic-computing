package net.skytreader.kode.AestheticComputing.PrimeEncoding;

import net.skytreader.kode.utils.MyFrame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class EncodingVisualizerMain{
	private static EncodingVisualizerComponent EncoderVisualizer;
	private static JFrame EncodingFrame;
	private static JTextField InputField;
	private static int limit;
	private static int x_origin, y_origin;
	
	private static class DrawActionListener implements ActionListener{
		private JButton clicker;
		private boolean cumulative;
		
		public DrawActionListener(JButton clicker, boolean cumulative){
			this.clicker = clicker;
			this.cumulative = cumulative;
		}
		
		public void actionPerformed(ActionEvent ae){
			if(InputField.getText().matches("\\d+")){			
				int input = Integer.parseInt(InputField.getText());
				if(input >= limit){
					JOptionPane.showMessageDialog(EncodingFrame,
								      "Please enter a number less than " + limit,
								      "Too large",
								      JOptionPane.ERROR_MESSAGE);
				} else{
					EncoderVisualizer.setDisplay(Integer.parseInt(InputField.getText()));
				}
			} else{
				JOptionPane.showMessageDialog(EncodingFrame,
							      "Please restrict your input to integers.",
							      "Non-numeric input",
							      JOptionPane.ERROR_MESSAGE);
			}
			
			EncodingFrame.getRootPane().setDefaultButton(clicker);
		}
	}
	
	private static class GUIRunnable implements Runnable{
		
		public GUIRunnable(){
		}
		
		private void limitCheck(String l){
			if(l == null){
				System.exit(0);
			}
		}
		
		public void run(){
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch(Exception e){
				e.printStackTrace();
			}
			
			String l = JOptionPane.showInputDialog(null, "What upper bound will we use?");
			limitCheck(l);
			
			while(!l.matches("\\d+")){
				l = JOptionPane.showInputDialog(null, "What upper bound will we use?");
				
				limitCheck(l);
			}
			
			limit = Integer.parseInt(l);
			
			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension screenSize = tk.getScreenSize();
			
			EncodingFrame = new MyFrame(screenSize.width / 2, screenSize.height / 2);
			EncodingFrame.setTitle("Number Encoder");
			Container EncodingFramePane = EncodingFrame.getContentPane();
			EncodingFramePane.setLayout(new BoxLayout(EncodingFramePane, BoxLayout.Y_AXIS));
			
			EncoderVisualizer = new LinearVisualizer(limit);
			
			JPanel ControlPanel = new JPanel();
			ControlPanel.setLayout(new BoxLayout(ControlPanel, BoxLayout.X_AXIS));
			ControlPanel.setMaximumSize(new Dimension(screenSize.width, 100));
			JLabel InputLabel = new JLabel("Enter number: ");
			InputField = new JTextField(EncoderVisualizer.getEncoderLimit());
			JButton DrawCumulativeButton = new JButton("Cumulative Draw");
			DrawCumulativeButton.addActionListener(new DrawActionListener(DrawCumulativeButton, true));
			JButton OverdrawButton = new JButton("Overdraw");
			OverdrawButton.addActionListener(new DrawActionListener(OverdrawButton, false));
			ControlPanel.add(InputLabel);
			ControlPanel.add(InputField);
			ControlPanel.add(DrawCumulativeButton);
			ControlPanel.add(OverdrawButton);
			
			EncodingFramePane.add(ControlPanel);
			EncodingFramePane.add(EncoderVisualizer);
			EncodingFramePane.doLayout();
		}
	
	}
	
	public static void main(String[] args){
		EventQueue.invokeLater(new GUIRunnable());
	}
}
