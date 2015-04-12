package uk.ac.soton.ecs.experiments.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import uk.ac.soton.ecs.mobilesensors.Simulation;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityGraphImpl;
import uk.ac.soton.ecs.mobilesensors.layout.AccessibilityRelation;
import uk.ac.soton.ecs.mobilesensors.layout.Location;
import uk.ac.soton.ecs.mobilesensors.sensor.SensorID;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.picking.ShapePickSupport;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class EnvironmentPanel extends JPanel implements EPanel {

	private static final long serialVersionUID = 1L;

	private VisualizationViewer<Location, AccessibilityRelation> visualisationViewer;

	// private static final double MAX_RADIUS = 20.0;

	// setting for Figure in paper
	private static final double MAX_RADIUS = 10.0;

	private static final boolean DRAW_SENSOR_LABELS = true;

	private static final Paint VERTEX_COLOR = Color.BLACK;

	private Map<Point2D, Double> spatialField = new HashMap<Point2D, Double>();

	private Map<SensorID, Location> sensorLocations = new HashMap<SensorID, Location>();

	private double maxSpatialField;

	private Collection<Location> evaderLocations;

	private Collection<Location> riskLocations;
	
	private AccessibilityGraphImpl graph;

	private Collection<Set<Location>> clusters;

	private Map<Location, Paint> colorMap;
	
	private Map<Integer, Integer> riskStates = new HashMap<Integer, Integer>();
	
	private Map<Integer, Integer> informationStates = new HashMap<Integer, Integer>();

	private static Color[] colors = { Color.black, Color.blue, Color.red,
			Color.yellow, Color.cyan, Color.gray, Color.green, Color.orange,
			Color.pink, Color.white };

	static {
		for (int i = 0; i < colors.length; i++) {
			Color color = colors[i];

			colors[i] = new Color(color.getRed(), color.getGreen(), color
					.getBlue(), 100);
		}
	}

	public Component getComponent() {
		return this;
	}

	public void setAccessibilityGraph(AccessibilityGraphImpl graph) {
		this.graph = graph;
	}

	public EnvironmentPanel(Simulation simulation) {
		this(simulation.getEnvironment().getAccessibilityGraph());
	}

	public EnvironmentPanel(AccessibilityGraphImpl graph) {
		this.graph = graph;

		StaticLayout<Location, AccessibilityRelation> layout = createGraphLayout(graph);

		setLayout(new BorderLayout());

		visualisationViewer = new VisualizationViewer<Location, AccessibilityRelation>(
				layout);

		RenderContext<Location, AccessibilityRelation> renderContext = visualisationViewer
				.getRenderContext();
		setRendererContext(renderContext);
		
		visualisationViewer.repaint();
		if(graph.getRiskLoactions()!=null)
		{		
			setRiskLocations(graph.getRiskLoactions());
		}
		visualisationViewer.repaint();	
		
		DefaultModalGraphMouse<Location, AccessibilityRelation> mouse = new DefaultModalGraphMouse<Location, AccessibilityRelation>() {

		
			@Override
			protected void loadPlugins() {
				super.loadPlugins();
				((PickingGraphMousePlugin<Location, AccessibilityRelation>) pickingPlugin)
						.setLocked(true);
			}

			@Override
			protected void setPickingMode() {
				// hack to prevent vertices from being moved
				super.setPickingMode();
				// hack to enable zooming by scrolling the mouse wheel
				add(translatingPlugin);
			}
		};

		mouse.setMode(ModalGraphMouse.Mode.PICKING);

		visualisationViewer.setGraphMouse(mouse);

		visualisationViewer.setBackground(Color.white);

		visualisationViewer
				.setPickSupport(new ShapePickSupport<Location, AccessibilityRelation>(
						visualisationViewer));
		visualisationViewer.setDoubleBuffered(true);

		visualisationViewer
				.setVertexToolTipTransformer(getTooltipTransformer());

		// visualisationViewer
		// .addPostRenderPaintable(new CommunicationLinksPainter(
		// simulation, visualisationViewer));

		add(new GraphZoomScrollPane(visualisationViewer), BorderLayout.CENTER);
		add(createControlPanel(), BorderLayout.SOUTH);

		new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				Thread.sleep(1000);
				resetViewer();
				return null;
			}
		}.execute();
	}

	private Transformer<Location, String> getTooltipTransformer() {
		return new Transformer<Location, String>() {

			public String transform(Location input) {
				Point2D coordinates = input.getCoordinates();
				return coordinates.getX() + ", " + coordinates.getY()
						+ ". Value " + spatialField.get(input.getCoordinates());
			}
		};
	}

	public void addPickedStateListener(ItemListener listener) {
		visualisationViewer.getPickedVertexState().addItemListener(listener);
	}

	private StaticLayout<Location, AccessibilityRelation> createGraphLayout(
			AccessibilityGraphImpl graph) {
		Transformer<Location, Point2D> locationCoordinates = new Transformer<Location, Point2D>() {
			public Point2D transform(Location input) {
				return input.getCoordinates();
			}
		};
		StaticLayout<Location, AccessibilityRelation> layout = new StaticLayout<Location, AccessibilityRelation>(
				graph, locationCoordinates);

		return layout;
	}

	private JPanel createControlPanel() {
		final ScalingControl scaler = new CrossoverScalingControl();

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(visualisationViewer, 1.1f, visualisationViewer
						.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(visualisationViewer, 1 / 1.1f, visualisationViewer
						.getCenter());
			}
		});
		JButton reset = new JButton("reset");
		reset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				resetViewer();
			}
		});

		JPanel controls = new JPanel();
		controls.add(plus);
		controls.add(minus);
		controls.add(reset);
		return controls;
	}

	public void resetViewer() {
		visualisationViewer.getRenderContext().getMultiLayerTransformer()
				.getTransformer(Layer.LAYOUT).setToIdentity();
		visualisationViewer.getRenderContext().getMultiLayerTransformer()
				.getTransformer(Layer.VIEW).setToIdentity();

		MutableTransformer modelTransformer = visualisationViewer
				.getRenderContext().getMultiLayerTransformer().getTransformer(
						Layer.LAYOUT);

		Rectangle2D boundingBox = graph.getBoundingBox();
		//
		double centerX = boundingBox.getCenterX();
		double centerY = boundingBox.getCenterY();

		Dimension size = visualisationViewer.getSize();

		double heightRatio = size.getHeight() / boundingBox.getHeight();
		double widthRatio = size.getWidth() / boundingBox.getWidth();

		double scaleRatio = Math.min(heightRatio, widthRatio);

		scaleRatio /= 1.1;

		if (scaleRatio != 0.0)
			modelTransformer.scale(scaleRatio, scaleRatio, new Point2D.Double(
					0.0, 0.0));

		Point2D center = visualisationViewer.getCenter();

		modelTransformer.translate((center.getX() - scaleRatio * centerX)
				/ scaleRatio, (center.getY() - scaleRatio * centerY)
				/ scaleRatio);
	}

	private void setRendererContext( 
			RenderContext<Location, AccessibilityRelation> renderContext) {
		renderContext
				.setVertexStrokeTransformer(new Transformer<Location, Stroke>() {

					public Stroke transform(Location input) {
						if (isSensorLocation(input) || isEvaderLocation(input))
							return new BasicStroke(1.0f);
						else
							return new BasicStroke(1.0f);
					}
				});

		renderContext
				.setVertexShapeTransformer(new Transformer<Location, Shape>() {

					public Shape transform(Location input) {
						if (isSensorLocation(input)) {
							return new Ellipse2D.Double(-MAX_RADIUS,
									-MAX_RADIUS, MAX_RADIUS * 2, MAX_RADIUS * 2);
						}

						if (isEvaderLocation(input)) {
							return new Rectangle2D.Double(-MAX_RADIUS,
									-MAX_RADIUS, MAX_RADIUS * 2, MAX_RADIUS * 2);
						}

//						Double locationVariance = spatialField.get(input.getCoordinates())+1.0;

						Double locationVariance = spatialField.get(input.getCoordinates());
						locationVariance=Math.abs(locationVariance);

						double radius;

						if (locationVariance == null)
							radius = 0.0;
						else
							radius = locationVariance / maxSpatialField
									* MAX_RADIUS;

						return new Ellipse2D.Double(-radius / 2.0,
								-radius / 2.0, radius, radius);
					}
				});

		renderContext
				.setEdgeShapeTransformer(new EdgeShape.Line<Location, AccessibilityRelation>());

//		renderContext.setVertexDrawPaintTransformer(new ConstantTransformer(
//				Color.BLACK));
		renderContext.setVertexDrawPaintTransformer(new Transformer<Location, Paint>() {

			public Paint transform(Location input) {
				
				if (isSensorLocation(input))
					return Color.BLACK;

//				if (isEvaderLocation(input))
//					return Color.YELLOW;
				if (spatialField.get(input.getCoordinates())>=0)
//				if (riskStates.get(input.getID())==0)
					return Color.BLACK;
				else
					return Color.RED;
				
//				return Color.BLACK;
				
//				if (colorMap != null) {
//					return colorMap.get(input);
//				}

//				return VERTEX_COLOR;
			}
		});
		
		renderContext
				.setVertexFillPaintTransformer(new Transformer<Location, Paint>() {

					public Paint transform(Location input) {
						if (isSensorLocation(input))
							return Color.GREEN;

						if (isEvaderLocation(input))
							return Color.YELLOW;
						
						if (spatialField.get(input.getCoordinates()).doubleValue()>=0)
							return Color.BLACK;
						else
							return Color.RED;
						
//						return Color.BLACK;
						
//						if (colorMap != null) {
//							return colorMap.get(input);
//						}
//
//						return VERTEX_COLOR;
					}
				});

		renderContext
				.setVertexLabelTransformer(new Transformer<Location, String>() {

					public String transform(Location input) {
						
						
						if(riskStates.get(input.getID())==1)
						{
							return "r";
						}
						if(riskStates.get(input.getID())==2)
						{
							return "R";
						}


						return "";
						
//						if(riskStates.get(input.getID())==1)
//						{
//							return input.getID()+"r";
//						}
//						if(riskStates.get(input.getID())==2)
//						{
//							return input.getID()+"R";
//						}
//
//
//						return input.getID()+"";
						
//						DecimalFormat df = new DecimalFormat("0.000");
//						if (isSensorLocation(input) && DRAW_SENSOR_LABELS)
//						{
//							return "P"+input.getID()+", R="+riskStates.get(input.getID())+", I="+informationStates.get(input.getID())
//									+", S"+getSensorIDAt(input).toString();
//						}
//							
//						else {
//							return "P"+input.getID()+", R="+riskStates.get(input.getID())+", I="+informationStates.get(input.getID());
//							// Point2D coordinates = input.getCoordinates();
//							// return coordinates.getX() + ", " +
//							// coordinates.getY();
//						}
					}
				});
		
	}

	public void saveToSVG() {
		// Get a DOMImplementation
		DOMImplementation domImpl = GenericDOMImplementation
				.getDOMImplementation();
		String svgNamespaceURI = "http://www.w3.org/2000/svg";

		Document document = domImpl
				.createDocument(svgNamespaceURI, "svg", null);

		// Create an instance of the SVG Generator
		SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

		// Render into the SVG Graphics2D implementation
		paint(svgGenerator);

		// Finally, stream out SVG to the standard output using UTF-8
		// character to byte encoding
		boolean useCSS = true; // we want to use CSS style attribute

		OutputStream printWriter;
		try {
			printWriter = new FileOutputStream("test_"
					+ System.currentTimeMillis() + ".svg");

			Writer out = new OutputStreamWriter(printWriter, "UTF-8");
			svgGenerator.stream(out, useCSS);
			printWriter.flush();
			printWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isEvaderLocation(Location input) {
		if (evaderLocations == null) {
			return false;
		}

		return evaderLocations.contains(input);
	}
	private boolean isRiskLocation(Location input) {
		if (riskLocations == null) {
			return false;
		}

		return riskLocations.contains(input);
	}

	private SensorID getSensorIDAt(Location v) {
		for (SensorID id : sensorLocations.keySet()) {
			if (sensorLocations.get(id).equals(v)) {
				return id;
			}
		}

		return null;
	}

	private boolean isSensorLocation(Location v) {
		return sensorLocations.containsValue(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.soton.ecs.experiments.gui.EPanel#setSensorLocations(java.util.Map)
	 */
	public void setSensorLocations(Map<SensorID, Location> locations) {
		this.sensorLocations = locations;
		visualisationViewer.repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.soton.ecs.experiments.gui.EPanel#setSpatialField(java.util.Map)
	 */
	public void setSpatialField(Map<Point2D, Double> spatialField) {
		
		this.spatialField = spatialField;
//		double temp = Collections.max(spatialField.values());
		maxSpatialField =1.3;
		recomputeClusters();

		
		
	
	}

	private void recomputeClusters() {
		// System.out.println("recomputing");
		//
		// KMeansClusterer clusterer = new KMeansClusterer(5);
		// this.clusters = clusterer.transform(graph, spatialField);
		// colorMap = createColorMap(clusters);
		// System.out.println("done");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.ac.soton.ecs.experiments.gui.EPanel#setEvaderLocation(uk.ac.soton.
	 * ecs.mobilesensors.layout.Location)
	 */
	public void setEvaderLocation(Location evaderLocation) {
		if (evaderLocation == null)
			this.evaderLocations = new ArrayList<Location>();
		else
			this.evaderLocations = Collections.singleton(evaderLocation);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeuk.ac.soton.ecs.experiments.gui.EPanel#setAttackLocations(java.util.
	 * Collection)
	 */
	public void setAttackLocations(Collection<Location> locations) {
		if (locations == null)
			evaderLocations = new ArrayList<Location>();
		else
			this.evaderLocations = locations;
	}
	
	public void setRiskLocations(Collection<Location> locations) {
		if (locations == null)
			riskLocations = new ArrayList<Location>();
		else
			this.riskLocations = locations;
	}
	
	public void setRiskStates(Map<Integer, Integer> riskStates) {
		if (riskStates == null)
			this.riskStates =new HashMap<Integer, Integer>();
		else
			this.riskStates = riskStates;
	}
	
	
	public void setInformationStates(Map<Integer, Integer> informationStates) {
		if (informationStates == null)
			this.informationStates =new HashMap<Integer, Integer>();
		else
			this.informationStates = informationStates;
	}
}
