package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.buttons.FunctionButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleButton;
import hr.fer.zemris.java.gui.calc.buttons.InvertibleOperatorButton;
import hr.fer.zemris.java.gui.calc.buttons.NumberButton;
import hr.fer.zemris.java.gui.calc.buttons.OperatorButton;
import hr.fer.zemris.java.gui.calc.buttons.OperatorButton.BinaryOperator;
import hr.fer.zemris.java.gui.calc.buttons.SimetricFunctionButton;
import hr.fer.zemris.java.gui.layouts.CalcLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This is a simple calculator program which supports trigonometric and
 * logarithmic functions.
 * 
 * @author Luka Skugor
 *
 */
public class Calculator extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Math error display message.
	 */
	private static final String MATH_ERROR = "Math error";
	/**
	 * Input error display message.
	 */
	private static final String INPUT_ERROR = "Input error";

	/**
	 * Calculator's screen.
	 */
	private JLabel screen;

	/**
	 * If there's an operation waiting for a second operand this holds reference
	 * to the first one.
	 */
	private Double operationStackValue;
	/**
	 * Operation which will be performed when second operand is inputed. By
	 * default it's value is <code>null</code>. It will hold an
	 * {@link BinaryOperator} when the operation button is pressed.
	 */
	private BinaryOperator operation;

	/**
	 * A flag indicating if user is inputing something. It will be false if last
	 * pressed button is not a number, otherwise true.
	 */
	private boolean input;

	/**
	 * Calculator's stack.
	 */
	private Stack<Double> stack;

	/**
	 * Creates a new calculator window.
	 */
	public Calculator() {
		setLocation(200, 100);
		setSize(500, 300);
		setTitle("Calculator");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		initFields();
		initGUI();
	}

	/**
	 * Initializes fields.
	 */
	private void initFields() {
		stack = new Stack<Double>();
	}

	/**
	 * Initializes GUI.
	 */
	private void initGUI() {
		JPanel p = new JPanel(new CalcLayout(3));
		p.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		screen = new JLabel();
		screen.setHorizontalAlignment(SwingConstants.RIGHT);
		screen.setBackground(Color.WHITE);
		screen.setOpaque(true);
		p.add(screen, "1,1");

		addNumbers(p);

		JCheckBox invert = new JCheckBox();
		invert.setText("Inv");
		invert.setHorizontalAlignment(SwingConstants.CENTER);
		invert.setOpaque(false);
		List<InvertibleButton> invertibles = new LinkedList<InvertibleButton>();

		invertibles.addAll(addFunctions(p));
		invertibles.addAll(addOperatorts(p));

		invert.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
				for (InvertibleButton iButton : invertibles) {
					iButton.invert(invert.isSelected());
					pack();
					revalidate();
				}
			});
		});

		addResultControls(p);

		// TODO reset input on button press

		p.add(invert, "5,7");

		setMinimumSize(p.getMinimumSize());
		p.setBackground(Color.ORANGE);
		add(p, BorderLayout.CENTER);
	}

	/**
	 * Adds buttons to the calculator panel, which control what to do with
	 * current configuration of the calculator.
	 * 
	 * @param p
	 *            calculator's central panel
	 */
	private void addResultControls(JPanel p) {
		JButton equals = new JButton("=");
		equals.addActionListener((e) -> {
			screen.setText(applyCurrentOperation().toString());
			resetInput();
			removeStackedOperation();
		});

		p.add(equals, "1,6");

		JButton push = new JButton("PUSH");
		push.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
				Double screenValue = getScreenValue();
				if (screenValue != null) {
					stack.push(screenValue);
				}
			});
		});
		JButton pop = new JButton("POP");
		pop.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
				try {
					resetInput();
					screen.setText(stack.pop().toString());
					revalidate();
				} catch (EmptyStackException emptyStack) {
					JOptionPane.showMessageDialog(this, "No elements on stack",
							"EmptyStack", JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		p.add(push, "3,7");
		p.add(pop, "4,7");

		JButton clear = new JButton("CLR");
		clear.addActionListener((e) -> {
			SwingUtilities.invokeLater(() -> {
				resetScreen();
				resetInput();
				revalidate();
			});
		});
		p.add(clear, "1,7");

		JButton reset = new JButton("RES");
		reset.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				resetScreen();
				resetInput();
				stack = new Stack<Double>();
				removeStackedOperation();
				revalidate();
			});
		});
		p.add(reset, "2,7");
	}

	/**
	 * Adds binary operators to the calculator panel.
	 * 
	 * @param p
	 *            calculator's central panel
	 * @return button which have inverse functionality
	 */
	private Collection<? extends InvertibleButton> addOperatorts(JPanel p) {
		List<OperatorButton> operatorButtons = new LinkedList<OperatorButton>();

		OperatorButton plus = new OperatorButton("+", (d1, d2) -> d1 + d2);
		p.add(plus, "5,6");
		operatorButtons.add(plus);

		OperatorButton minus = new OperatorButton("-", (d1, d2) -> d1 - d2);
		p.add(minus, "4,6");
		operatorButtons.add(minus);

		OperatorButton multiplication = new OperatorButton("*", (d1, d2) -> d1
				* d2);
		p.add(multiplication, "3,6");
		operatorButtons.add(multiplication);

		OperatorButton division = new OperatorButton("/", (d1, d2) -> d1 / d2);
		p.add(division, "2,6");
		operatorButtons.add(division);

		OperatorButton pow = new InvertibleOperatorButton("x^n", "nRootx",
				Math::pow, (a, b) -> Math.pow(a, 1 / b));
		p.add(pow, "5,1");
		operatorButtons.add(pow);

		for (OperatorButton operator : operatorButtons) {
			operator.addActionListener((e) -> {
				SwingUtilities.invokeLater(() -> {
					operationStackValue = applyCurrentOperation();
					operation = operator.getOperator();
					resetInput();
				});
			});
		}

		return filterInvertibleButtons(operatorButtons);
	}

	/**
	 * Applies currently saved binary operation. If none is saved returns screen
	 * value.
	 * 
	 * @return result of the operation if there's an operation to perform or
	 *         else screen value.
	 */
	private Double applyCurrentOperation() {
		if (operation != null) {
			return operation.operate(operationStackValue, getScreenValue());
		} else {
			return getScreenValue();
		}
	}

	/**
	 * Adds functions to the calculator's panel.
	 * 
	 * @param p
	 *            calculator's central panel
	 * @return invertible functions
	 */
	private Collection<? extends InvertibleButton> addFunctions(JPanel p) {
		List<FunctionButton> functionButtons = new LinkedList<FunctionButton>();

		FunctionButton sin = new FunctionButton("sin", "asin", Math::sin,
				Math::asin);
		p.add(sin, "2,2");
		functionButtons.add(sin);

		FunctionButton cos = new FunctionButton("cos", "acos", Math::cos,
				Math::acos);
		p.add(cos, "3,2");
		functionButtons.add(cos);

		FunctionButton tan = new FunctionButton("tan", "atan", Math::tan,
				Math::atan);
		p.add(tan, "4,2");
		functionButtons.add(tan);

		FunctionButton ctg = new FunctionButton("ctg", "actg",
				d -> 1 / Math.tan(d), d -> Math.PI / 2 - Math.atan(d));
		p.add(ctg, "5,2");
		functionButtons.add(ctg);

		FunctionButton reciprocal = new SimetricFunctionButton("1/x",
				d -> 1 / d);
		p.add(reciprocal, "2,1");
		functionButtons.add(reciprocal);

		FunctionButton log = new FunctionButton("log", "10^x", Math::log10,
				d -> Math.pow(10, d));
		p.add(log, "3,1");
		functionButtons.add(log);

		FunctionButton ln = new FunctionButton("ln", "e^n", Math::log,
				d -> Math.pow(Math.E, d));
		p.add(ln, "4,1");
		functionButtons.add(ln);

		FunctionButton algSign = new SimetricFunctionButton("+/-", d -> -d);
		p.add(algSign, "5,4");
		functionButtons.add(algSign);

		for (FunctionButton function : functionButtons) {
			function.addActionListener((e) -> {
				SwingUtilities.invokeLater(() -> {
					try {
						screen.setText(function.apply(getScreenValue())
								.toString());
					} catch (Exception mathError) {
						screen.setText(MATH_ERROR);
					} finally {
						resetInput();
					}
					revalidate();
				});
			});
		}

		return filterInvertibleButtons(functionButtons);
	}

	/**
	 * Filters {@link InvertibleButton}s from a {@link java.util.Collection}.
	 * 
	 * @param buttons
	 *            collection of buttons to filter
	 * @return collection of filtered invertible buttons
	 */
	private Collection<? extends InvertibleButton> filterInvertibleButtons(
			Collection<? extends JButton> buttons) {
		List<InvertibleButton> invertibles = new LinkedList<InvertibleButton>();
		buttons.forEach((f) -> {
			if (f instanceof InvertibleButton) {
				invertibles.add((InvertibleButton) f);
			}
		});
		return invertibles;
	}

	/**
	 * Adds number buttons and decimal point button to the calculator's panel.
	 * 
	 * @param p
	 *            calculator's central panel
	 */
	private void addNumbers(JPanel p) {
		NumberButton[] numbers = new NumberButton[11];
		for (int i = 0; i < numbers.length - 1; i++) {
			numbers[i] = new NumberButton(Integer.toString(i));
		}
		// decimal point button
		numbers[10] = new NumberButton(".");

		p.add(numbers[1], "4,3");
		p.add(numbers[2], "4,4");
		p.add(numbers[3], "4,5");
		p.add(numbers[4], "3,3");
		p.add(numbers[5], "3,4");
		p.add(numbers[6], "3,5");
		p.add(numbers[7], "2,3");
		p.add(numbers[8], "2,4");
		p.add(numbers[9], "2,5");
		p.add(numbers[0], "5,3");
		p.add(numbers[10], "5,5");

		for (NumberButton numberButton : numbers) {
			numberButton
					.addActionListener((e) -> {
						SwingUtilities.invokeLater(() -> {
							if (input == false) {
								resetScreen();
							}

							input = true;
							screen.setText(screen.getText()
									+ numberButton.getNumber());
							if (getScreenValue() == null) {
								resetScreen();
							}

							revalidate();
						});
					});
		}

	}

	/**
	 * Called on program start. Creates program windows.
	 * 
	 * @param args
	 *            command line arguments
	 */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new Calculator();
			frame.pack();
			frame.setVisible(true);
		});

	}

	/**
	 * Parses screen value.
	 * 
	 * @return parsed screen value or <code>null</code> if screen can't be
	 *         parsed
	 */
	private Double getScreenValue() {
		try {
			return Double.parseDouble(screen.getText());
		} catch (Exception e) {
			screen.setText(INPUT_ERROR);
			return null;
		}
	}

	/**
	 * Sets input flag to false.
	 */
	private void resetInput() {
		input = false;
	}

	/**
	 * Resets screen.
	 */
	private void resetScreen() {
		screen.setText("");
		resetInput();
	}

	/**
	 * Clears operation waiting for a second operand.
	 */
	private void removeStackedOperation() {
		operation = null;
		operationStackValue = null;
	}

}
