import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MonteCarlo extends JPanel implements Runnable, MouseListener,
		KeyListener, ComponentListener {
	Thread th;
	private int xkasten = 10;
	private int ykasten = 20;
	private int kastenlength = 500;
	private int radius = 12;
	private int g = 100;
	private double x, y;
	private double v = 0;
	JTextField anzahltropfen, berechnetespi, exaktespi, tropfenimkreis,
			tropfenaussen, intervall, circleradius, rahmenl, rahmenx, rahmeny,consumedtime,tropfenberechnet;
	JLabel header1, header2, header3, header4, header5, header6, header7,
			header8, header9, header10, header11, header12,header13,header14;
	Button button, stop, change;
	JCheckBox check1;
	JRadioButton radio1, radio2, radio3;
	private ArrayList<Point> points = new ArrayList<Point>();
	Point edgepoint = new Point();
	private int h;
	private int w;
	private int time = 100;
	private double a;
	private int verhaeltnis = 0;
	private boolean hypo;
	Dimension screenSize;
	private long starttime;
	private long stoptime;
	private boolean isalive;

	public MonteCarlo(int w, int h) {
		this.w = w;
		this.h = h;
		createWindow();

	}
	// Erstellt Fenster inkl. aller Komponenten, wie Buttons, CheckBoxen etc...
	private void createWindow() {
		// TODO Auto-generated constructor stub
		JFrame frame = new JFrame(
				"Monte Carlo Methode Simulator - By mklueh");
		frame.setPreferredSize(new Dimension(w, h));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frame.addComponentListener(this);

		JPanel panel = new JPanel();
		JPanel sidebar = new JPanel();
		JPanel bottombar = new JPanel();
		sidebar.setBackground(Color.gray);
		panel.setPreferredSize(new Dimension(w - 100, h));

		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.add(header1 = new JLabel("Anzahl der Tropfen"));
		sidebar.add(anzahltropfen = new JTextField());
		anzahltropfen.setText(Integer.toString(g));
		sidebar.add(header8 = new JLabel("Methode"));
		sidebar.add(radio1 = new JRadioButton("Verh. Innen/Außen "));
		sidebar.add(radio2 = new JRadioButton("Verh. Innen/Gesamt"));
		sidebar.add(radio3 = new JRadioButton("Verh. Innen/Platz. Punkte"));
		radio1.setSelected(true);
		radio2.setSelected(false);
		radio3.setSelected(false);
		radio1.addMouseListener(this);
		radio2.addMouseListener(this);
		radio3.addMouseListener(this);
		radio1.setToolTipText("PI = Punkte Innen / Punkte Außen");
		radio2.setToolTipText("PI = Punkte Innen / Punkte gesamt (eingegebener Wert)");
		radio3.setToolTipText("PI = Punkte Innen / Gesamte platzierte Punkte");
		sidebar.add(header6 = new JLabel("Zeitintervall"));
		sidebar.add(intervall = new JTextField(Integer.toString(time)));
		sidebar.add(header7 = new JLabel("Tropfenradius"));
		sidebar.add(circleradius = new JTextField(Integer.toString(radius)));
		sidebar.add(header2 = new JLabel("Berechnetes PI"));
		sidebar.add(berechnetespi = new JTextField());
		sidebar.add(header3 = new JLabel("Exaktes PI"));
		sidebar.add(exaktespi = new JTextField("3.141592654"));
		sidebar.add(header4 = new JLabel("Tropfen im Kreis"));
		sidebar.add(tropfenimkreis = new JTextField());
		sidebar.add(header5 = new JLabel("Tropfen außerhalb des Kreises"));
		sidebar.add(tropfenaussen = new JTextField());
		sidebar.add(header14=new JLabel("Berechnete Tropfen"));
		sidebar.add(tropfenberechnet=new JTextField());
		sidebar.add(header13=new JLabel("Benötigte Zeit"));
		sidebar.add(consumedtime=new JTextField());
		sidebar.add(check1 = new JCheckBox("Linien"));
		check1.addMouseListener(this);
		check1.setSelected(false);
		edgepoint.x = 0;
		edgepoint.y = h;
		button = new Button("Start");
		button.setPreferredSize(new Dimension(sidebar.getWidth(), 100));
		button.setBackground(Color.cyan);
		button.addMouseListener(this);
		sidebar.add(button);
		stop = new Button("Stop");
		stop.setBackground(Color.cyan);
		stop.setPreferredSize(new Dimension(sidebar.getWidth(), 100));
		stop.addMouseListener(this);
		sidebar.add(stop);

		anzahltropfen.setFocusable(true);
		anzahltropfen.addKeyListener(this);
		bottombar.add(header10 = new JLabel("Rahmengröße & Kreisdurchmesser: "));
		bottombar.add(rahmenl = new JTextField());
		rahmenl.setPreferredSize(new Dimension(50, 30));
		rahmenl.setText(Integer.toString(this.kastenlength));
		bottombar.add(header11 = new JLabel("XPOS Rahmen: "));
		bottombar.add(rahmenx = new JTextField());
		rahmenx.setPreferredSize(new Dimension(50, 30));
		rahmenx.setText(Integer.toString(xkasten));
		bottombar.add(header12 = new JLabel("YPOS Rahmen: "));
		bottombar.add(rahmeny = new JTextField());
		rahmeny.setPreferredSize(new Dimension(50, 30));
		rahmeny.setText(Integer.toString(ykasten));
		bottombar.add(change = new Button("Ändern"));
		change.addMouseListener(this);
		frame.add("Center", panel);
		frame.add("East", sidebar);
		frame.add("South", bottombar);
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();

	}

	public void calculatePi(int g, int time) {
		radius = Integer.parseInt(circleradius.getText().toString());
		points.clear();
		v = 0;
		a = 0;
		for (int i = 1; i <= g; i++) {
			x = xkasten + kastenlength / 2 + Math.random() * (kastenlength / 2);
			y = ykasten + Math.random() * (kastenlength / 2);
			Point point = new Point();
			point.x = (int) x;
			point.y = (int) y;
			points.add(point);
			if (Math.hypot(-xkasten - kastenlength / 2 + x, -kastenlength / 2
					- ykasten + y) <= kastenlength / 2) {
				v++;

			} else {
				a++;
			}

			// 3 verschiedene Methoden zum berechnen von PI
			if (verhaeltnis == 0) {

				double pi = v / a;

				if (Double.isInfinite(pi)) {
					pi = 0;
				}
				berechnetespi.setText(Double.toString(pi));

			} else if (verhaeltnis == 1) {
				double pi = 4 * v / g;
				if (Double.isInfinite(pi)) {
					pi = 0;
				}

				berechnetespi.setText(Double.toString(pi));
			} else if (verhaeltnis == 2) {
				double pi = 4 * v / (v + a);
				if (Double.isInfinite(pi)) {
					pi = 0;
				}

				berechnetespi.setText(Double.toString(pi));
			}

			tropfenimkreis.setText(Integer.toString((int) v));
			tropfenaussen.setText(Integer.toString((int) a));
			tropfenberechnet.setText(Integer.toString((int)a+(int)v)+" von "+g);
			repaint();
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Berechnet benötigte Zeit
			stoptime=(System.currentTimeMillis()-starttime)/1000;
			consumedtime.setText(Double.toString(stoptime)+" Sekunden");

		}
		
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Achsenbeschriftung
		g.drawString("Y", xkasten + kastenlength / 2 - 3, ykasten - 2);
		g.drawString("X", xkasten + kastenlength + 10, ykasten + kastenlength
				/ 2 + 3);
		// Kasten horizontal
		g.drawLine(xkasten, ykasten, xkasten + kastenlength, ykasten);
		g.drawLine(xkasten, ykasten + kastenlength, xkasten + kastenlength,
				ykasten + kastenlength);
		// Kasten vertikal
		g.drawLine(xkasten, ykasten, xkasten, ykasten + kastenlength);
		g.drawLine(xkasten + kastenlength, ykasten, xkasten + kastenlength,
				ykasten + kastenlength);
		//Linien, die den Kasten unterteilen
		g.setColor(Color.black);
		g.drawLine(xkasten + kastenlength / 2, ykasten, xkasten + kastenlength
				/ 2, ykasten + kastenlength);
		g.drawLine(xkasten, ykasten + kastenlength / 2, xkasten + kastenlength,
				ykasten + kastenlength / 2);
		//Zeichnet Einheitskreist
		g.drawOval(xkasten, ykasten, kastenlength, kastenlength);
		//Zeichnet Punkte aus Array
		for (int i = 0; i < points.size(); i++) {
			Point aktuellerpunkt = points.get(i);
			if (Math.hypot(-xkasten - kastenlength / 2 + aktuellerpunkt.x,
					-kastenlength / 2 - ykasten + aktuellerpunkt.y) <= kastenlength / 2) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.green);
			}
			//Punkte
			g.fillOval(aktuellerpunkt.x-radius/4, aktuellerpunkt.y-radius/4, radius / 2,
					radius / 2);
			
			if (hypo) {
				//Linien Hypo, Ankath.
				g.setColor(Color.blue);
				g.drawLine(xkasten + kastenlength / 2, ykasten + kastenlength
						/ 2, aktuellerpunkt.x, aktuellerpunkt.y);
				g.setColor(Color.orange);
				g.drawLine(aktuellerpunkt.x, aktuellerpunkt.y,
						aktuellerpunkt.x, ykasten + kastenlength / 2);
			}
		}
		if(hypo){
			//Legende
			g.setFont(new Font("Serif", Font.PLAIN, 24));
			g.setColor(Color.blue);
			g.fillRect(xkasten+kastenlength/2+20, ykasten+kastenlength/2+10, kastenlength/10,kastenlength/25);
			g.drawString("Hypotenuse", xkasten+kastenlength/2+kastenlength/6,  ykasten+kastenlength/2+kastenlength/18);
			
			
			
			g.setColor(Color.orange);
			g.fillRect(xkasten+kastenlength/2+20, ykasten+kastenlength/2+kastenlength/10, kastenlength/10,kastenlength/25);
			g.drawString("Ankathete", xkasten+kastenlength/2+kastenlength/6,  ykasten+kastenlength/2+kastenlength/7-2);
			
			
			
		}

	}

	public static void main(String[] args) {

		new MonteCarlo(800, 600);

	}

	@Override
	public void run() {
		getNumberFromField();

	}

	public void getNumberFromField() {
		starttime=0;
		starttime=System.currentTimeMillis();
		int number = Integer.parseInt(anzahltropfen.getText().toString());
		if (number > 15000) {
			number = 15000;
			anzahltropfen.setText(Integer.toString(number));

		}
		this.calculatePi(number,
				Integer.parseInt(intervall.getText().toString()));

	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == button) {
			if(!isalive){
				isalive=true;
				th = new Thread(this);
				th.start();	
				System.out.println("Started");
				
				
			}
			else if(isalive){
				System.out.println("Stopped");
				th.stop();
				isalive=false;
			}
		}
		if (e.getSource() == stop) {
			th.stop();
			
			
		}
		
		if (e.getSource() == radio1) {
			if (radio1.isSelected()) {
				radio1.setSelected(true);
				radio2.setSelected(false);
				radio3.setSelected(false);
				verhaeltnis = 0;

			}

		}
		if (e.getSource() == radio2) {

			if (radio2.isSelected()) {
				radio1.setSelected(false);
				radio2.setSelected(true);
				radio3.setSelected(false);
				verhaeltnis = 1;

			}
		}
		if (e.getSource() == radio3) {
			if (radio3.isSelected()) {
				radio1.setSelected(false);
				radio2.setSelected(false);
				radio3.setSelected(true);
				verhaeltnis = 2;
			}
		}
		if (e.getSource() == check1) {
			if (check1.isSelected()) {
				hypo = true;
				repaint();
			} else {
				hypo = false;
				repaint();
			}
		}
		if (e.getSource() == change) {
			try {
				kastenlength = Integer.parseInt(rahmenl.getText());
				repaint();
			} catch (NumberFormatException ex) {
				kastenlength = 500;
				
			}
			try {
				xkasten = Integer.parseInt(rahmenx.getText());
				repaint();
			} catch (NumberFormatException ex) {
				xkasten = 10;
				
			}
			try {
				ykasten = Integer.parseInt(rahmeny.getText());
				repaint();
			} catch (NumberFormatException ex) {
				ykasten = 20;
				
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!isalive){
				System.out.println("Started");
				isalive=true;
				Thread th = new Thread(this);
				th.start();	
				
				
			}
			else if(isalive){
				th.stop();
				System.out.println("Stopped");
				isalive=false;
			}
			
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
		if (e.getKeyCode() == KeyEvent.VK_P) {

			th.stop();
			isalive=false;

		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* Passt den Kasten der Fenstergröße an
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		if(isalive){
			th.stop();
			isalive=false;
		}
		kastenlength = Integer.parseInt(this.rahmenl.getText())
				* this.getSize().width / 571;
		System.out.println("W:" + w + "Size " + this.getSize());
		kastenlength = Math.min(
				500 * this.getSize().width
						/ 571,
				500
						* this.getSize().height / 522);
		rahmenl.setText(Integer.toString(kastenlength));
		points.clear();
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent
	 * )
	 */
	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub

	}

}
