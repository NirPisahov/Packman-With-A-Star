package il.nirpis.astar.graphics.packman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Float;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import il.nirpis.astar.graphics.AstarMap;

public class GameInfoPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private ButtonsPanel buttonsPanel;
	
	public GameInfoPanel(Consumer<String> action) {
		setLayout(new BorderLayout());
		setOpaque(false);
		
		title = new JLabel();
		title.setForeground(Color.WHITE);
		title.setFont(new Font("Tahoma", Font.BOLD, 120));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setHorizontalTextPosition(JLabel.CENTER);
		title.setVerticalAlignment(JLabel.CENTER);
		title.setVerticalTextPosition(JLabel.CENTER);
		title.setBackground(Color.BLUE);
		
		buttonsPanel = new ButtonsPanel(action);
		
		add(title, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(128, 128, 128, 150));
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public GameInfoPanel newPanel(String title, String... buttons) {
		this.title.setText(title);
		this.buttonsPanel.chooseButtons(buttons);
		return this;
	}
	
	class ButtonsPanel extends JPanel implements ActionListener {
		private static final long serialVersionUID = 1L;
		private Map<String, MyButton> buttonMap;
		private Consumer<String> action;
		
		public ButtonsPanel(Consumer<String> action) {
			setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
			setOpaque(false);
			setBorder(new LineBorder(Color.YELLOW));
			this.action = action;
			
			buildButtons();
		}

		private void buildButtons() {
			buttonMap = new HashMap<>();
			
			MyButton exitButton = new MyButton("Exit");
			MyButton newGameButton = new MyButton("New Game");
			MyButton resumeButton = new MyButton("Resume");
			
			exitButton.setActionCommand("exit");
			newGameButton.setActionCommand("new_game");
			resumeButton.setActionCommand("resume");
			
			exitButton.addActionListener(this);
			newGameButton.addActionListener(this);
			resumeButton.addActionListener(this);
			
			buttonMap.put("exit", exitButton);
			buttonMap.put("new_game", newGameButton);
			buttonMap.put("resume", resumeButton);
		}
		
		public void chooseButtons(String... buttons) {
			removeAll();
			for (String button : buttons) {
				add(buttonMap.get(button));
			}
			revalidate();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			action.accept(e.getActionCommand());
		}
	}
	
	class MyButton extends JButton {

		private static final long serialVersionUID = 1L;
		private static final int R = 18; // radius of the rounded button (8)
		private Float bounds; // the bounds of the button
		
		private final Font font = new Font("Tahoma", Font.BOLD, 32);
		
		// default constructor, sets the title, font, no bounds set
		public MyButton(String title) {
			super(title);
			setFont(font);
		}

		@Override
		public void updateUI() {
			super.updateUI();
			
			// center the text in the button
			setVerticalAlignment(SwingConstants.CENTER);
			setVerticalTextPosition(SwingConstants.CENTER);
			setHorizontalAlignment(SwingConstants.CENTER);
			setHorizontalTextPosition(SwingConstants.CENTER);
			
			// border in the button
			setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
			setMargin(new Insets(2, 8, 2, 8));
			
			setContentAreaFilled(false);
			setFocusPainted(false);
			setOpaque(false);
			setForeground(Color.WHITE);
			BackGroundIcon bgi = new BackGroundIcon();
			setIcon(bgi);
		}

		class BackGroundIcon implements Icon {
			private int width, height;

			@Override
			public void paintIcon(Component c, Graphics g, int x, int y) {
				AbstractButton b = (AbstractButton) c;
				Insets i = b.getMargin();
				int w = c.getWidth();
				int h = c.getHeight();
				width = w - i.left - i.right;
				height = h - i.top - i.bottom;
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHints(AstarMap.ANTIALIASING);
				bounds = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, R, R);
				Shape area = bounds;
				Color color = new Color(0f, 0f, 0f, .3f);
				ButtonModel m = b.getModel();

				if (m.isPressed()) {
					color = new Color(0f, 0f, 0f, .3f);
				} else if (m.isRollover()) {
					color = new Color(1f, 1f, 1f, .3f);
				}
				g2.setPaint(color);
				g2.fill(area);
				g2.setPaint(Color.WHITE);
				g2.draw(area);
				g2.dispose();
			}

			@Override
			public int getIconWidth() {
				return Math.max(width, 100);
			}

			@Override
			public int getIconHeight() {
				return Math.max(height, 24);
			}
		}
	}
}
