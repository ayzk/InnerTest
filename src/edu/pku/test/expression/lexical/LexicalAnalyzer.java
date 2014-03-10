package edu.pku.test.expression.lexical;

import static edu.pku.test.expression.lexical.LexicalConstants.ASSIGN_TOKEN;
import static edu.pku.test.expression.lexical.LexicalConstants.BLANK_PATTERN;
import static edu.pku.test.expression.lexical.LexicalConstants.DOUBLE_DELIMITERS;
import static edu.pku.test.expression.lexical.LexicalConstants.SINGLE_DELIMITERS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.pku.test.expression.lexical.dfa.DFADefinition;
import edu.pku.test.expression.lexical.dfa.DFAEndStateCode;
import edu.pku.test.expression.lexical.dfa.DFAMidState;
import edu.pku.test.expression.tokens.DataType;
import edu.pku.test.expression.tokens.TerminalToken;
import edu.pku.test.expression.tokens.TokenBuilder;
import edu.pku.test.expression.tokens.VariableToken;
import edu.pku.test.expression.utils.DataCache;
import edu.pku.test.expression.utils.ExpressionUtil;


/**
 * Lexical Analyzer
 * @author zhutao
 *
 */
public class LexicalAnalyzer {
	private DFADefinition DFA = DFADefinition.getDFA();
	
	private TerminalToken curToken;
	
	private StringBuilder curWord;
	
	private int curLine = 0;
	
	private int nextScanColumn = 0;
	
	private List<TerminalToken> tokens;
	
	private Scanner scanner;

	public LexicalAnalyzer() {}
	
	public List<TerminalToken> getTokens() {
		return tokens;
	}
	
	public List<TerminalToken> analysis(String expression) throws LexicalException {
		if(expression == null || expression.length() == 0)
			throw new LexicalException("Invalid empty expression.");
		this.scanner = new Scanner(expression);
		prepareLexicalAnalyzer();
		try {
			doAnalysis();
		} finally {
			scanner.close();
		}
		return tokens;
	}
	
	private void prepareLexicalAnalyzer() {
		curToken = null;
		curWord = new StringBuilder();
		curLine = 0;
		nextScanColumn = 0;
		tokens = new ArrayList<TerminalToken>();
	}
	
	/**
	 * Analysis the expression
	 * @return
	 * @throws LexicalException 
	 */
	private void doAnalysis() throws LexicalException {
		char[] curLineCharArray;	//the character array used to store the current line
		DFAMidState curMidState = null;	//the current mid state
		while(scanner.hasNextLine()) {
			curLineCharArray = nextLine().toCharArray();//read a new line
			curLine++;
			nextScanColumn = 0;
			while(escapeBlank(curLineCharArray) < curLineCharArray.length) {
				//set current state to the start state, prepare to recognize a new word
				curMidState = DFA.getDFAStartState();
				initAtStartState();
				//the loop will stop when one word is recognized, or a lexical error is found, or the line is end
				while(true) {
					if(nextScanColumn < curLineCharArray.length) {
						curMidState = goToNextMidState(curMidState, curLineCharArray);
						if(curMidState == null)
							break;
					} else {//come to the end of the current line
						analysisAtLineEnd(curMidState);
						break;
					}
				}
			}
		}
	}
	
	/**
	 * initialize before recognizing a new word
	 */
	private void initAtStartState() {
		curWord = new StringBuilder();
		curToken = null;
	}
	
	/**
	 * Go to the next middle state, if the next middle state is null, 
	 * it may come to an end state, and then we will act at this end state.
	 * if we can not find a next middle state or a next end state,
	 * there must be a lexical error in the current word
	 * @param curState
	 * @param curLineCharArray
	 * @return
	 * @throws LexicalException 
	 */
	private DFAMidState goToNextMidState(DFAMidState curMidState, char[] curLineCharArray) 
							throws LexicalException {
		char inputChar = curLineCharArray[nextScanColumn]; //store the coming char
		DFAMidState nextMidsState = null;	//the next mid state
		DFAEndStateCode nextEndStateCode = null;  //the next end state code
		//get the next mid state, it can be null
		nextMidsState = curMidState.getNextMidState(inputChar);
		nextEndStateCode = curMidState.getNextEndStateCode(inputChar); //get the next end state code, it can be null
		if(nextMidsState != null) {
			//the next state is a mid state
			actAtMiddleState(inputChar);
		} else if(nextEndStateCode != null) {
			//the next state is a end state
			actAtEndState(nextEndStateCode);
		} else {
			//if the next mid state and the next end state are all null, there must be a lexical error
			throw new LexicalException(curMidState, curLine, nextScanColumn);
		}
		return nextMidsState;
	}
	
	/**
	 * Come to a new middle state, append the input character to the current word
	 * @param inputChar
	 */
	private void actAtMiddleState(char inputChar) {
		curWord.append(inputChar);
		nextScanColumn++;
	}
	
	/**
	 * When the current line is end, we will check if it has a next end state,
	 * if it has, we will act at this end state, and one word is recognized,
	 * otherwise there is a lexical error
	 * @param midState
	 * @throws LexicalException 
	 */
	private void analysisAtLineEnd(DFAMidState midState) throws LexicalException {
		DFAEndStateCode nextEndStateCode = midState.getNextEndStateCode();
		if(nextEndStateCode != null) // the next state can be an end state, go to act at this end state
			actAtEndState(nextEndStateCode);
		else	//the next state isn't an end state, a lexical error occurring
			throw new LexicalException(midState, curLine, nextScanColumn);
	}
	
	/**
	 * Act at an end state, recognize the word, if a lexical error occurring, catch it
	 * @param endStateCode
	 * @throws LexicalException 
	 */
	private void actAtEndState(DFAEndStateCode endStateCode) throws LexicalException {
		String curWordText = curWord.toString();
		int wordStartColumn = nextScanColumn - curWordText.length();
		switch(endStateCode) {
		case NUMBER_END:
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.NUMBER)
							.index(DataCache.getBigDecimalIndex(new BigDecimal(curWordText)))
							.buildConst();
			break;
		case ID_END:
			if("true".equals(curWordText) || "TRUE".equals(curWordText)
					|| "false".equals(curWordText) || "FALSE".equals(curWordText)) {
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).dataType(DataType.BOOLEAN)
								.index(DataCache.getBooleanIndex(Boolean.valueOf(curWordText)))
								.buildConst();
			} else
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildVariable();
			break;
		case SINGLE_DELIMITER_END:
			if(SINGLE_DELIMITERS.contains(curWordText))
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(curWordText).buildDelimiter();
			else
				throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			break;
		case DOUBLE_DELIMITER_END:
			if(DOUBLE_DELIMITERS.contains(curWordText)) {
				curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
								.text(curWordText).buildDelimiter();
			} else {
				String firstDelimiter = curWordText.substring(0, 1);
				if(SINGLE_DELIMITERS.contains(firstDelimiter)) {
					curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
									.text(firstDelimiter).buildDelimiter();
					nextScanColumn--;
				} else
					throw new LexicalException("Invalid delimiter.", curLine, wordStartColumn);
			}
			break;
		case CHAR_END:
			char ch;
			if(curWordText.length() == 3)
				ch = curWordText.toCharArray()[1];
			else
				ch = ExpressionUtil.getEscapedChar(curWordText.toCharArray()[2]);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.CHARACTER)
							.index(DataCache.getCharIndex(ch)).buildConst();
			break;
		case STRING_END:
			String str = curWordText.substring(1, curWordText.length()-1);
			str = ExpressionUtil.transformEscapesInString(str);
			curToken = TokenBuilder.getBuilder().line(curLine).column(wordStartColumn)
							.text(curWordText).dataType(DataType.STRING)
							.index(DataCache.getStringIndex(str)).buildConst();
			break;
		}
		if(curToken != null) {
			tokens.add((TerminalToken)curToken);
			findVariableToBeAssigned();
		}
	}
	
	/**
	 * Get the last two tokens, if the first is a VariableToken,
	 * and the second is an assignment mark '=',
	 * then set the 'toBeAssigned' flag of the VariableToken to true.
	 */
	private void findVariableToBeAssigned() {
		int size = tokens.size();
		if(size < 2)
			return;
		TerminalToken first = tokens.get(size-2);
		TerminalToken second = tokens.get(size - 1);
		if(!second.equalsInGrammar(ASSIGN_TOKEN))
			return;
		if(first instanceof VariableToken)
			((VariableToken)first).setToBeAssigned(true);
	}
	
	private int escapeBlank(char[] curLineCharArray) {
		while(nextScanColumn < curLineCharArray.length 
				&& ((Character)curLineCharArray[nextScanColumn]).toString()
				.matches(BLANK_PATTERN) ) 
			nextScanColumn++;
			
		return nextScanColumn;
	}
	
	private String nextLine(){
		//discard comment before return a new line
		return discardComment(scanner.nextLine());
	}
	
	private String discardComment(String target) {
		Pattern commentPattern = Pattern.compile("##.*");
		Matcher matcher = commentPattern.matcher(target);
		return matcher.replaceFirst("");
	}
	
}
