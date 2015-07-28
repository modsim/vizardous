package vizardous.delegate;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vizardous.delegate.graphics.AbstractChart2D;

/**
 * TODO Documentation
 * 
 * @author Stefan Helfrich <s.helfrich@fz-juelich.de>
 */
public class MultipleChartsTab extends JPanel implements ActionListener {

	/** TODO Documentation */
	private static final long serialVersionUID = -3303761964941221665L;
	
	/** A list of possible charts that can be view through this tab. */
	List<AbstractChart2D> charts;

	/** Parent {@link JTabbedPane} of this tab. */
	JTabbedPane tabPane;
	
	/** The {@link Logger} for this class. */
	final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** TODO Documentation */
	JPanel pnlTab;
	
	/**
	 * TODO Documentation
	 * 
	 * @param charts
	 */
	public MultipleChartsTab(JTabbedPane tabPane, List<AbstractChart2D> charts) {
		super();
		
		this.tabPane = tabPane;
		this.charts = charts;
		
		init();
	}
	
	/**
	 * TODO Documentation
	 */
	public MultipleChartsTab() {
		// TODO Auto-generated constructor stub
	}

	private void init() {
		/* TODO Fix the size when the default tab is added */
		this.add(charts.get(0));
	}

	public void setActiveChart(int index) {
		/* Remove all previouslay added tabs */
		this.removeAll();
		
		/* Add new chart */
		AbstractChart2D chart = charts.get(index);
		chart.setPreferredSize(this.getSize());
		chart.repaint();		
		this.add(chart);
		
		/* Set tab name of new chart */
		int indexOfMultipleChartsTab = this.tabPane.indexOfComponent(this);
		this.tabPane.setTitleAt(indexOfMultipleChartsTab, chart.getTabName());

		Component c = this.pnlTab.getComponent(0);
		if (c instanceof JLabel) {
			((JLabel) c).setText(chart.getTabName());
		}
		
		this.repaint();
	}

	public List<AbstractChart2D> getCharts() {
		return this.charts;
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
        final int index = tabPane.indexOfComponent(this);
        final TabPopUpMenu popupMenu = new TabPopUpMenu(this);
        
        /* Show the menu at the left border of the tab */
        final Rectangle tabBounds = tabPane.getBoundsAt(index);
        popupMenu.show(tabPane, tabBounds.x, tabBounds.y + tabBounds.height);
        
        Component c = this.pnlTab.getComponent(1);
		if (c instanceof JButton) {
			((JButton) c).setSelected(false);
		}
    }

	/** TODO Documentation */
	public JPanel getTabPanel() {
		int index = tabPane.indexOfComponent(this);
		String tabName = tabPane.getTitleAt(index);
		pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel(tabName);
		
		Icon arrowIcon = new ImageIcon(getClass().getResource("/icons/arrow24x16.png"));
		JButton btnOpen = new JButton(arrowIcon);
		
		Border emptyBorder = BorderFactory.createEmptyBorder();
		btnOpen.setBorder(emptyBorder);
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
	
		pnlTab.add(lblTitle, gbc);
	
		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnOpen, gbc);
	
		btnOpen.addActionListener(this);
		
		return this.pnlTab;
	}

}
