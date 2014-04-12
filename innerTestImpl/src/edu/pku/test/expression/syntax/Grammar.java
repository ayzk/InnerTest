package edu.pku.test.expression.syntax;

import edu.pku.test.expression.syntax.operator.OperatorFactory;
import edu.pku.test.expression.tokens.ConstToken;
import edu.pku.test.expression.tokens.DelimiterToken;
import edu.pku.test.expression.tokens.ExecutionToken;
import edu.pku.test.expression.tokens.NonterminalToken;
import edu.pku.test.expression.tokens.TerminalToken;
import edu.pku.test.expression.tokens.Token;
import edu.pku.test.expression.tokens.TokenBuilder;
import edu.pku.test.expression.tokens.VariableToken;


public class Grammar {
	private static Grammar grammar = new Grammar();
	
	private Grammar() {
		start.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, notMark}, 
								new Token[]{sentence});
		
		//block.addProduction(new TerminalToken[]{variableToBeAssigned, variable, constant, minusMark, leftBracket, notMark}, 
		//					new Token[]{sentence, block});
		
		sentence.addProduction(new TerminalToken[]{variableToBeAssigned},
								new Token[]{variableToBeAssigned, assignMark, bolExpression, assignExe, semicolon});
		sentence.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, notMark}, 
								new Token[]{bolExpression, semicolon});
		
		bolExpression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, notMark},
								new Token[]{bolTerm, _bolExpression});
		
		_bolExpression.addProduction(new TerminalToken[]{orMark},
								new Token[]{orMark, bolTerm, orExe, _bolExpression});
		_bolExpression.addProduction(new TerminalToken[]{rightBracket, comma, semicolon},
								new Token[]{});
		
		bolTerm.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket, notMark},
								new Token[]{bolFactor, _bolTerm});
		
		_bolTerm.addProduction(new TerminalToken[]{andMark},
								new Token[]{andMark, bolFactor, andExe, _bolTerm});
		_bolTerm.addProduction(new TerminalToken[]{orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		bolFactor.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket},
								new Token[]{compare});
		bolFactor.addProduction(new TerminalToken[]{notMark},
								new Token[]{notMark, bolFactor, notExe});
		
		compare.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket},
								new Token[]{expression, _compare});
		
		_compare.addProduction(new TerminalToken[]{equalMark},
								new Token[]{equalMark, expression, equalExe});
		_compare.addProduction(new TerminalToken[]{notEMark},
								new Token[]{notEMark, expression, notEqualExe});
		_compare.addProduction(new TerminalToken[]{greatMark},
								new Token[]{greatMark, expression, greatExe});
		_compare.addProduction(new TerminalToken[]{greatEMark},
								new Token[]{greatEMark, expression, greatEExe});
		_compare.addProduction(new TerminalToken[]{lessMark},
								new Token[]{lessMark, expression, lessExe});
		_compare.addProduction(new TerminalToken[]{lessEMark},
								new Token[]{lessEMark, expression, lessEExe});
		_compare.addProduction(new TerminalToken[]{andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		expression.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket},
								new Token[]{term, _expression});
		
		_expression.addProduction(new TerminalToken[]{addMark},
								new Token[]{addMark, term, addExe, _expression});
		_expression.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, term, minusExe, _expression});
		_expression.addProduction(new TerminalToken[]{equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		term.addProduction(new TerminalToken[]{variable, constant, minusMark, leftBracket},
								new Token[]{factor, _term});
		
		_term.addProduction(new TerminalToken[]{multiplyMark},
								new Token[]{multiplyMark, factor, multiplyExe, _term});
		_term.addProduction(new TerminalToken[]{divideMark},
								new Token[]{divideMark, factor, divideExe, _term});
		_term.addProduction(new TerminalToken[]{modMark},
								new Token[]{modMark, factor, modExe, _term});
		_term.addProduction(new TerminalToken[]{addMark, minusMark, equalMark, notEMark, greatMark, greatEMark, lessMark, lessEMark, andMark, orMark, rightBracket, comma, semicolon},
								new Token[]{});
		
		factor.addProduction(new TerminalToken[]{variable},
								new Token[]{variable});
		factor.addProduction(new TerminalToken[]{constant},
								new Token[]{constant});
		factor.addProduction(new TerminalToken[]{minusMark},
								new Token[]{minusMark, factor, negativeExe});
		factor.addProduction(new TerminalToken[]{leftBracket},
								new Token[]{leftBracket, bolExpression, rightBracket});

	}
	
	public static Grammar getGrammar() {
		return grammar;
	}
	
	public NonterminalToken getStart() {
		return start;
	}
	
	public TerminalToken getGrammarEnd() {
		return semicolon;
	}
	
	public TerminalToken getAssignMark() {
		return assignMark;
	}
	
	//constant
	private ConstToken constant = TokenBuilder.getBuilder().buildConst();
	
	//variable used in expression
	private VariableToken variable = TokenBuilder.getBuilder().buildVariable();
	
	//variable to be assigned
	private VariableToken variableToBeAssigned = TokenBuilder.getBuilder().toBeAssigned(true).buildVariable();
	
	
	//delimiters
	private DelimiterToken addMark =  TokenBuilder.getBuilder().text("+").buildDelimiter();
	private DelimiterToken minusMark = TokenBuilder.getBuilder().text("-").buildDelimiter();
	private DelimiterToken multiplyMark = TokenBuilder.getBuilder().text("*").buildDelimiter();
	private DelimiterToken divideMark = TokenBuilder.getBuilder().text("/").buildDelimiter();
	private DelimiterToken modMark = TokenBuilder.getBuilder().text("%").buildDelimiter();
	private DelimiterToken greatMark = TokenBuilder.getBuilder().text(">").buildDelimiter();
	private DelimiterToken greatEMark = TokenBuilder.getBuilder().text(">=").buildDelimiter();
	private DelimiterToken lessMark = TokenBuilder.getBuilder().text("<").buildDelimiter();
	private DelimiterToken lessEMark = TokenBuilder.getBuilder().text("<=").buildDelimiter();
	private DelimiterToken equalMark = TokenBuilder.getBuilder().text("==").buildDelimiter();
	private DelimiterToken notEMark = TokenBuilder.getBuilder().text("!=").buildDelimiter();
	private DelimiterToken andMark = TokenBuilder.getBuilder().text("&&").buildDelimiter();
	private DelimiterToken orMark = TokenBuilder.getBuilder().text("||").buildDelimiter();
	private DelimiterToken notMark = TokenBuilder.getBuilder().text("!").buildDelimiter();
	private DelimiterToken comma = TokenBuilder.getBuilder().text(",").buildDelimiter();
	private DelimiterToken semicolon = TokenBuilder.getBuilder().text(";").buildDelimiter();
	private DelimiterToken leftBracket = TokenBuilder.getBuilder().text("(").buildDelimiter();
	private DelimiterToken rightBracket = TokenBuilder.getBuilder().text(")").buildDelimiter();
	private DelimiterToken assignMark = TokenBuilder.getBuilder().text("=").buildDelimiter();
	
	
	//nonterminal tokens
	private NonterminalToken start = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken sentence = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolExpression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _bolTerm = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken bolFactor = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _compare = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _expression = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken _term = TokenBuilder.getBuilder().buildNT();
	private NonterminalToken factor = TokenBuilder.getBuilder().buildNT();
	//action tokens
	private ExecutionToken addExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ADD")).buildExecution();
	private ExecutionToken minusExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MINUS")).buildExecution();
	private ExecutionToken multiplyExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MULTIPLY")).buildExecution();
	private ExecutionToken divideExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("DIVIDE")).buildExecution();
	private ExecutionToken modExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("MOD")).buildExecution();
	private ExecutionToken negativeExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NEGATIVE")).buildExecution();
	private ExecutionToken andExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("AND")).buildExecution();
	private ExecutionToken orExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("OR")).buildExecution();
	private ExecutionToken notExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOT")).buildExecution();
	private ExecutionToken greatExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREAT")).buildExecution();
	private ExecutionToken greatEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("GREATE")).buildExecution();
	private ExecutionToken lessExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESS")).buildExecution();
	private ExecutionToken lessEExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("LESSE")).buildExecution();
	private ExecutionToken equalExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("EQUAL")).buildExecution();
	private ExecutionToken notEqualExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("NOTEQUAL")).buildExecution();
	private ExecutionToken assignExe = TokenBuilder.getBuilder().executable(OperatorFactory.getOperator("ASSIGN")).buildExecution();
	
}