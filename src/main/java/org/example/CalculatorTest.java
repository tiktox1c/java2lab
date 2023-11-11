package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    @Test
    void testSimpleExpression() {
        String expression = "2 + 3 * 4";
        double result = Calculator.evaluateExpression(expression);
        assertEquals(14.0, result, 0.0001);
    }

    @Test
    void testExpressionWithVariables() {
        String expression = "a + b * 2";
        Calculator.setVariable("a", 5);
        Calculator.setVariable("b", 3);
        double result = Calculator.evaluateExpression(expression);
        assertEquals(11.0, result, 0.0001);
    }

    @Test
    void testExpressionWithParentheses() {
        String expression = "(2 + 3) * 4";
        double result = Calculator.evaluateExpression(expression);
        assertEquals(20.0, result, 0.0001);
    }

    @Test
    void testExpressionWithDivideByZero() {
        String expression = "5 / 0";
        assertThrows(ArithmeticException.class, () -> Calculator.evaluateExpression(expression));
    }

    @Test
    void testInvalidExpression() {
        String expression = "2 + * 3";
        assertThrows(IllegalArgumentException.class, () -> Calculator.evaluateExpression(expression));
    }

}
