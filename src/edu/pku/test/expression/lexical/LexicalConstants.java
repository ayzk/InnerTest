package edu.pku.test.expression.lexical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.pku.test.expression.tokens.DelimiterToken;
import edu.pku.test.expression.tokens.TokenBuilder;



public class LexicalConstants {
	private LexicalConstants() {}
	
	/**
	 * digits
	 */
	public static final String DIGITS_PATTERN = "\\d";
	
	/**
	 * letter or underline
	 */
	public static final String LETTER_UNDERLINE_PATTERN = "[a-zA-Z_]";
	
	/**
	 * delimiters ; ( ) , + - * / = > < | & ! { } . %
	 */
	public static final String DELIMITER_PATTERN = "[;(),\\+\\-\\*/=><|&!{}\\.%]";
	
	/**
	 * not delimiter
	 */
	public static final String NOT_DELIMITER_PATTERN = "[^;(),\\+\\-\\*/=><|&!{}\\.]";
	
	/**
	 * left square bracket, act as the start of a date constants
	 */
	public static final String LEFT_SQUARE_BRACKET_PATTERN = "\\[";
	
	/**
	 * right square bracket, act as the end of a date constants
	 */
	public static final String RIGHT_SQUARE_BRACKET_PATTERN = "]";
	
	/**
	 * single-quote, act as the start and end of a character constants
	 */
	public static final String SINGLE_QUOTES_PATTERN = "'";
	
	/**
	 * double-quote, act as the start and end of a string constants
	 */
	public static final String DOUBLE_QUOTES_PATTERN = "\"";
	
	/**
	 * point, for the present it just is a decimal point
	 */
	public static final String DECIMAL_POINT_PATTERN = "\\.";
	
	/**
	 * exponent, 'E' or 'e'
	 */
	public static final String EXPONENT_PATTERN = "[Ee]";
	
	/**
	 * any character except letter, underline, point, digit
	 */
	public static final String NOT_LETTER_UNDERLINE_POINT_DIGIT_PATTERN = "[^a-zA-Z_\\.\\d]";
	
	/**
	 * any character except letter, underline, digit
	 */
	public static final String NOT_LETTER_UNDERLINE_DIGIT_PATTERN = "[^a-zA-Z_\\d]";
	
	/**
	 * positive sign or negative sign
	 */
	public static final String POSITIVE_NEGATIVE_PATTERN = "[\\+\\-]";
	
	/**
	 * any character
	 */
	public static final String ANY_CHAR_PATTERN = ".";
	
	/**
	 * back slash, sign the escape sequence
	 */
	public static final String BACKSLASH_PATTERN = "\\\\";
	
	/**
	 * not single-quote and back slash
	 */
	public static final String NOT_SINGLEQUOTES_BACKSLASH_PATTERN = "[^'\\\\]";
	
	/**
	 * valid escape sequence: \\ \b \t \n \f \r \' \"
	 */
	public static final String ESCAPE_PATTERN = "[\\\\btnfr\'\"]";
	
	/**
	 * not double-quote and back slash
	 */
	public static final String NOT_DOUBLEQUOTES_BACKSLASH_PATTERN = "[^\"\\\\]";
	
	/**
	 * blank
	 */
	public static final String BLANK_PATTERN = "\\s";
	
	/**
	 * not blank
	 */
	public static final String NOT_BLANK_PATTERN =  "\\S";
	
	/**
	 * integer value of true
	 */
	public static final int TRUE = 1;
	
	/**
	 * integer value of false
	 */
	public static final int FALSE = 0;
	
	/**
	 * Assign token
	 */
	public static final DelimiterToken ASSIGN_TOKEN = TokenBuilder.getBuilder().text("=").buildDelimiter();
	
	/**
	 * single character delimiters
	 */
	public static final List<String> SINGLE_DELIMITERS;
	
	/**
	 * double character delimiters
	 */
	public static final List<String> DOUBLE_DELIMITERS;
	
	public static final List<String> OPERATORS;
	
	static {
		
		List<String> doubleDelimiters = new ArrayList<String>();
		doubleDelimiters.add(">=");
		doubleDelimiters.add("<=");
		doubleDelimiters.add("==");
		doubleDelimiters.add("!=");
		doubleDelimiters.add("&&");
		doubleDelimiters.add("||");
		DOUBLE_DELIMITERS = Collections.unmodifiableList(doubleDelimiters);
		
		List<String> singleDelimiters = new ArrayList<String>();
		singleDelimiters.add("+");
		singleDelimiters.add("-");
		singleDelimiters.add("*");
		singleDelimiters.add("/");
		singleDelimiters.add("%");
		singleDelimiters.add("=");
		singleDelimiters.add(">");
		singleDelimiters.add("<");
		singleDelimiters.add("!");
		singleDelimiters.add(";");
		singleDelimiters.add(",");
		singleDelimiters.add("(");
		singleDelimiters.add(")");
		singleDelimiters.add("{");
		singleDelimiters.add("}");
		singleDelimiters.add(".");
		SINGLE_DELIMITERS = Collections.unmodifiableList(singleDelimiters);
		
		List<String> operators = new ArrayList<String>();
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		operators.add("%");
		operators.add(">");
		operators.add("<");
		operators.add(">=");
		operators.add("<=");
		operators.add("==");
		operators.add("!=");
		operators.add("&&");
		operators.add("||");
		operators.add("!");
		operators.add("=");
		OPERATORS = Collections.unmodifiableList(operators);
	}
}
