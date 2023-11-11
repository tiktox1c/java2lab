package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Calculator {

    private static Map<String, Double> variables = new HashMap<>();

    public static void setVariable(String variableName, double value) {
        variables.put(variableName, value);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите выражение:");
        String expression = scanner.nextLine();

        try {
            double result = evaluateExpression(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    static double evaluateExpression(String expression) {
        expression = expression.replaceAll("\\s+", "");

        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (Character.isDigit(ch)) {
                i = extractNumber(expression, i, numbers);
            } else if (Character.isLetter(ch)) {
                i = extractVariable(expression, i, numbers);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                processClosingParenthesis(operators, numbers);
            } else if (isOperator(ch)) {
                processOperator(ch, operators, numbers);
            }
        }

        processRemainingOperators(operators, numbers);

        if (numbers.size() != 1 || !operators.isEmpty()) {
            throw new IllegalArgumentException("Некорректное выражение");
        }

        return numbers.pop();
    }

    private static int extractNumber(String expression, int index, Stack<Double> numbers) {
        StringBuilder num = new StringBuilder();
        while (index < expression.length() && (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {
            num.append(expression.charAt(index));
            index++;
        }
        numbers.push(Double.parseDouble(num.toString()));
        return index - 1;
    }

    private static int extractVariable(String expression, int index, Stack<Double> numbers) {
        StringBuilder variable = new StringBuilder();
        while (index < expression.length() && Character.isLetterOrDigit(expression.charAt(index))) {
            variable.append(expression.charAt(index));
            index++;
        }

        if (!variables.containsKey(variable.toString())) {
            System.out.print("Введите значение переменной " + variable + ": ");
            double value = new Scanner(System.in).nextDouble();
            variables.put(variable.toString(), value);
        }

        numbers.push(variables.get(variable.toString()));
        return index - 1;
    }

    private static void processClosingParenthesis(Stack<Character> operators, Stack<Double> numbers) {
        while (!operators.isEmpty() && operators.peek() != '(') {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }
        operators.pop(); // убрать '(' из стека
    }

    private static void processOperator(char ch, Stack<Character> operators, Stack<Double> numbers) {
        while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(ch)) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }
        operators.push(ch);
    }

    private static void processRemainingOperators(Stack<Character> operators, Stack<Double> numbers) {
        while (!operators.isEmpty()) {
            char operator = operators.pop();
            if (operator == '(') {
                throw new IllegalArgumentException("Некорректное выражение: непарная открывающая скобка");
            }

            if (numbers.size() < 2) {
                throw new IllegalArgumentException("Некорректное выражение");
            }

            double b = numbers.pop();
            double a = numbers.pop();

            numbers.push(applyOperator(operator, b, a));
        }
    }

    private static double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Деление на ноль");
                }
                return a / b;
            default:
                throw new IllegalArgumentException("Некорректный оператор: " + operator);
        }
    }

    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}
