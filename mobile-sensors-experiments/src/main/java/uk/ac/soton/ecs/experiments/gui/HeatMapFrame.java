package uk.ac.soton.ecs.experiments.gui;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.GraphFactory;
import uk.ac.soton.ecs.mobilesensors.layout.GraphGridAdaptor;
import uk.ac.soton.ecs.mobilesensors.layout.Grid;

/**
 * <p>
 * This class is a very simple example of how to use the HeatMap class.
 * </p>
 * 
 * <hr />
 * <p>
 * <strong>Copyright:</strong> Copyright (c) 2007, 2008
 * </p>
 * 
 * <p>
 * HeatMap is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * </p>
 * 
 * <p>
 * HeatMap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * </p>
 * 
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * HeatMap; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 * </p>
 * 
 * @author Matthew Beckler (matthew@mbeckler.org)
 * @author Josh Hayes-Sheen (grey@grevian.org), Converted to use BufferedImage.
 * @author J. Keller (jpaulkeller@gmail.com), Added transparency (alpha)
 *         support, data ordering bug fix.
 * @version 1.6
 */

public class HeatMapFrame extends JFrame {
	private HeatMap panel;
	private Grid grid;

	public HeatMapFrame(Grid grid) {
		super("Heat Map Frame");

		this.grid = grid;
		// double[][] data = HeatMap.generateRampTestData();
		boolean useGraphicsYAxis = true;

		// you can use a pre-defined gradient:
		panel = new HeatMap(createZeroMap(grid), useGraphicsYAxis,
				Gradient.GRADIENT_BLUE_TO_RED);

		panel.setDrawLegend(true);

		panel.setTitle("Height (m)");
		panel.setDrawTitle(true);

		panel.setXAxisTitle("X-Distance (m)");
		panel.setDrawXAxisTitle(true);

		panel.setYAxisTitle("Y-Distance (m)");
		panel.setDrawYAxisTitle(true);

		Rectangle2D bounds = grid.getBoundingRectangle();

		panel.setCoordinateBounds(bounds.getMinX() - 5, bounds.getMaxX() + 5,
				bounds.getMaxY() + 5, bounds.getMinY() - 5);
		panel.setDrawXTicks(true);
		panel.setDrawYTicks(true);

		panel.setColorForeground(Color.black);
		panel.setColorBackground(Color.white);

		this.getContentPane().add(panel);
	}

	private double[][] createZeroMap(Grid grid) {
		List<Point2D> gridPoints = grid.getGridPoints();

		Point2D current = gridPoints.get(0);
		int rowLength = 0;

		for (int i = 0; i < gridPoints.size(); i++) {
			Point2D point2d = gridPoints.get(i);

			if (point2d.getX() != current.getX()) {
				rowLength = i;
				break;
			}

			current = point2d;
		}

		int columnLength = gridPoints.size() / rowLength;

		double[][] data = new double[columnLength][rowLength];

		return data;
	}

	public void updateData(Map<Point2D, Double> values) {
		List<Point2D> gridPoints = grid.getGridPoints();

		double[][] data = createZeroMap(grid);
		int rowLength = data[0].length;
		int columnLength = data.length;

		for (int i = 0; i < columnLength; i++) {
			for (int j = 0; j < rowLength; j++) {
				Double value = values.get(gridPoints.get(i * columnLength + j));

				value = value == null ? 0.0 : value;

				data[i][j] = value;
			}
		}

		panel.updateData(data, true);
	}

	// this function will be run from the EDT
	private static void createAndShowGUI(GraphGridAdaptor grid)
			throws Exception {
		HeatMapFrame hmf = new HeatMapFrame(grid);
		hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hmf.setSize(500, 500);
		hmf.setVisible(true);

		Map<Point2D, Double> values = new HashMap<Point2D, Double>();

		for (Point2D point2d : grid) {
			values.put(point2d, Math.random());
		}

		hmf.updateData(values);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					AccessibilityGraphImpl graph = GraphFactory
							.createRectangularGridGraph(100, 100, 11, 11, 0, 0);
					GraphGridAdaptor grid = new GraphGridAdaptor();
					grid.setGraph(graph);
					grid.afterPropertiesSet();
					createAndShowGUI(grid);
				} catch (Exception e) {
					System.err.println(e);
					e.printStackTrace();
				}
			}
		});
	}
}
