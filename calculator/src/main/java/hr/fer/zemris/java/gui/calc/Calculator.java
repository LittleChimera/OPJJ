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
import java.util.function.DoubleBinaryOperator;

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

public class Calculator extends JFrame {

	private static final String MATH_ERROR = "Math error";
	private static final String INPUT_ERROR = "Input error";

	private JLabel screen;

	private Double operationStackValue;
	private BinaryOperator operation;

	private boolean input;

	private Stack<Double> stack;

	public Calculator() {
		setLocation(200, 100);
		setSize(500, 300);
		setTitle("Calculator");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		initFields();
		initGUI();
	}

	private void initFields() {
		stack = new Stack<Double>();
	}

	private void initGUI() {
		JPanel p = new JPanel(new CalcLayout(3));
		p.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

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
		
		//TODO reset input on button press

		p.add(invert, "5,7");

		setMinimumSize(p.getMinimumSize());
		p.setBackground(Color.ORANGE);
		add(p, BorderLayout.CENTER);
	}

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

	private Double applyCurrentOperation() {
		if (operation != null) {
			return operation.operate(operationStackValue, getScreenValue());
		} else {
			return getScreenValue();
		}
	}

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

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new Calculator();
			frame.pack();
			frame.setVisible(true);
		});

	}

	public Double getScreenValue() {
		try {
			return Double.parseDouble(screen.getText());
		} catch (Exception e) {
			screen.setText(INPUT_ERROR);
			return null;
		}
	}

	public void resetInput() {
		input = false;
	}

	public void resetScreen() {
		screen.setText("");
		resetInput();
	}

	private void removeStackedOperation() {
		operation = null;
		operationStackValue = null;
	}

}
