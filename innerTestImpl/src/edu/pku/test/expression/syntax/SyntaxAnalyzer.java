package edu.pku.test.expression.syntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.pku.test.expression.lexical.LexicalConstants;
import edu.pku.test.expression.syntax.operator.AssignOperator;
import edu.pku.test.expression.syntax.operator.Operator;
import edu.pku.test.expression.tokens.ConstToken;
import edu.pku.test.expression.tokens.DelimiterToken;
import edu.pku.test.expression.tokens.ExecutionToken;
import edu.pku.test.expression.tokens.NonterminalToken;
import edu.pku.test.expression.tokens.TerminalToken;
import edu.pku.test.expression.tokens.Token;
import edu.pku.test.expression.tokens.TokenType;
import edu.pku.test.expression.tokens.Valuable;
import edu.pku.test.expression.tokens.VariableToken;
import edu.pku.test.expression.utils.Stack;


/**
 * Syntax Analyzer
 * @author zhutao
 *
 */
public class SyntaxAnalyzer {
	private Grammar grammar = Grammar.getGrammar();
	
	private Valuable finalResult;
	
	private Stack<Token> syntaxStack = new Stack<Token>();
	
	private Stack<Valuable> semanticStack = new Stack<Valuable>();
	
	private Stack<DelimiterToken> operatorTokenStack = new Stack<DelimiterToken>();
		
	private Stack<Integer> argumentStartIndexStack = new Stack<Integer>();
	
	private Stack<Context> contextStack = new Stack<Context>();
		
	public SyntaxAnalyzer() {}
	
	public Valuable getFinalResult() {
		return finalResult;
	}
	
	public Map<String, Valuable> getVariableTable() {
		if(contextStack.isEmpty())
			return null;
		return contextStack.top().getVariableTable();
	}
	
	public Valuable analysis(List<TerminalToken> tokens) throws SyntaxException {
		return analysis(tokens, null);
	}
	
	public Valuable analysis(List<TerminalToken> tokens, Map<String, Valuable> variableTable)
				throws SyntaxException {
		this.finalResult = null;
		Map<String, Valuable> initVariableTable = variableTable == null ? 
									new HashMap<String, Valuable>() : variableTable;
		contextStack.push(new Context(true, initVariableTable, 0));
		
		int index = 0;
		while(index < tokens.size()) {
			index = analysisSentence(tokens, index);
		}
		
		return finalResult;
	}
	
	private int analysisSentence(List<TerminalToken> tokens, int index)
				throws SyntaxException {
		prepareStacks();
		syntaxStack.push(grammar.getStart());
		TerminalToken currentToken = tokens.get(index++);
		Token syntaxStackTop = null;
		while(!syntaxStack.isEmpty()) {//analyze a sentence in loop
			syntaxStackTop = syntaxStack.pop();
			switch(syntaxStackTop.getTokenType()) {
			case NT:
				Token[] production = ((NonterminalToken)syntaxStackTop).getProduction(currentToken);
				if(production != null)
					reverseProductionIntoSyntaxStack(production);
				else 
					throw new SyntaxException(currentToken);
				break;
			case EXECUTION:
				ExecutionToken executionToken = (ExecutionToken)syntaxStackTop;
				Executable executable = executionToken.getExecutable();
				if(executable instanceof Operator) {
					//execute operator
					executeOperator((Operator)executable);
				} else {
					//execute function
				}
				break;
			default:
				if(matchTerminalToken((TerminalToken)syntaxStackTop, currentToken)
						&& !syntaxStack.isEmpty()) {
					if(index < tokens.size())
						currentToken = tokens.get(index++);
					else 
						throw new SyntaxException("Sentence is not properly over at line:"
								+ currentToken.getLine() + ".");
				}
				break;
			}
		}
		
		if(!semanticStack.isEmpty())
			finalResult = semanticStack.pop();
		
		return index;
	}
	
	private boolean matchTerminalToken(TerminalToken syntaxStackTop, TerminalToken currentToken)
							throws SyntaxException {
		boolean currentTokenMatched = syntaxStackTop.equalsInGrammar(currentToken);
		if(currentTokenMatched) {
			switch(syntaxStackTop.getTokenType()) {
			case CONST:
				semanticStack.push((ConstToken)currentToken);
				break;
			case VARIABLE:
				VariableToken variable = (VariableToken)currentToken;
				Valuable valueOfVariable = getVariableValue(variable.getText());
				if(valueOfVariable != null)
					variable.assignWith(valueOfVariable);
				semanticStack.push(variable);
				break;
			case DELIMITER:
				if(LexicalConstants.OPERATORS.contains(currentToken.getText()))
					operatorTokenStack.push((DelimiterToken)currentToken);
				break;
			}
		} else {
			throw new SyntaxException(currentToken);
		}
		return currentTokenMatched;
	}
	
	private void reverseProductionIntoSyntaxStack(Token[] production) {
		if(production.length > 0)
			for(int i=production.length-1; i>=0; i--)
				syntaxStack.push(production[i]);
	}
	
	private void executeOperator(Operator operator) 
				throws VariableNotInitializedException, ArgumentsMismatchException {
		Valuable[] arguments = getArgumentsForOperator(operator);
		DelimiterToken operatorToken = operatorTokenStack.pop();
		try {
			Valuable result = operator.execute(arguments);
			if(operator instanceof AssignOperator){
				VariableToken variable = (VariableToken)arguments[0];
				setVariableValue(variable.getText(), result);
			}
			semanticStack.push(result);
		} catch(ArgumentsMismatchException e) {
			throw new ArgumentsMismatchException(e.getMessage(), operatorToken, e);
		} catch(ArithmeticException e) {
			ArithmeticException arithmeticException = new ArithmeticException(e.getMessage()
					+ " At line:" + operatorToken.getLine() 
					+ ", column:" + operatorToken.getColumn() + ".");
			arithmeticException.initCause(e);
			throw arithmeticException;
		}
	}
	
	private Valuable[] getArgumentsForOperator(Operator operator) 
					throws VariableNotInitializedException {
		return getArguments(operator.getArgumentNum(), operator instanceof AssignOperator);
	}
	
	private Valuable[] getArgumentsForFunction(int argumentNum) 
					throws VariableNotInitializedException {
		return getArguments(argumentNum, false);
	}
	
	private Valuable[] getArguments(int argumentNum, boolean isForAssignment)
					throws VariableNotInitializedException {
		Valuable[] arguments = new Valuable[argumentNum];
		for(int i=argumentNum-1; i>=0; i--) {
			arguments[i] = semanticStack.pop();
			if(arguments[i].getTokenType() == TokenType.VARIABLE) {
				if(isForAssignment && i == 0)
					break;
				else if(arguments[i].getIndex() < 0) 
					throw new VariableNotInitializedException((VariableToken)arguments[i]);
			}
		}
		return arguments;
	}
	
	private Valuable getVariableValue(String variableName) {
		Context currentContext = contextStack.top();
		return currentContext.getVariableValue(variableName);
	}
	
	private void setVariableValue(String text, Valuable value) {
		Context currentContext = contextStack.top();
		currentContext.setVariableValue(text, value);
	}
	
	private void recoverSemanticStack(int startIndex) {
		while(semanticStack.size() > startIndex)
			semanticStack.pop();
	}
	
	private void prepareStacks() {
		syntaxStack.clear();
		semanticStack.clear();
		operatorTokenStack.clear();
		argumentStartIndexStack.clear();
	}
}
