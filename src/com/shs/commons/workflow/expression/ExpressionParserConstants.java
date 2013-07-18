/* Generated By:JavaCC: Do not edit this line. ExpressionParserConstants.java */
package com.shs.commons.workflow.expression;

public interface ExpressionParserConstants {

  int EOF = 0;
  int SINGLE_LINE_COMMENT = 8;
  int MULTI_LINE_COMMENT = 9;
  int DECLARATION = 11;
  int BEGIN = 12;
  int END = 13;
  int STATEMENT = 14;
  int IF = 15;
  int THEN = 16;
  int ELSE = 17;
  int CASE = 18;
  int WHEN = 19;
  int SET = 20;
  int WHILE = 21;
  int DO = 22;
  int SKIP_STMT = 23;
  int ACQUIESCENT_PASS = 24;
  int PRINT = 25;
  int PRINTLN = 26;
  int RETURN = 27;
  int STRING = 28;
  int INT = 29;
  int DOUBLE = 30;
  int BOOLEAN = 31;
  int RECORD = 32;
  int LIST = 33;
  int MAP = 34;
  int PARTICIPANT_SET = 35;
  int OBJECT = 36;
  int LIKE = 37;
  int IS = 38;
  int EMPTY = 39;
  int IN = 40;
  int UNION = 41;
  int MINUS = 42;
  int INTERSECT = 43;
  int INCLUDE = 44;
  int EXCLUDE = 45;
  int ASSIGN_BY_UNIT = 46;
  int ASSIGN_BY_POSITION = 47;
  int ASSIGN_BY_GROUP = 48;
  int ASSIGN_BY_STAFF = 49;
  int PROCESS_INITIATOR = 50;
  int EXECUTOR_OF = 51;
  int NULL = 52;
  int TRUE = 53;
  int FALSE = 54;
  int INTEGER_LITERAL = 55;
  int DECIMAL_LITERAL = 56;
  int HEX_LITERAL = 57;
  int OCTAL_LITERAL = 58;
  int FLOATING_POINT_LITERAL = 59;
  int EXPONENT = 60;
  int CHARACTER_LITERAL = 61;
  int STRING_LITERAL = 62;
  int IDENTIFIER = 63;
  int LETTER = 64;
  int DIGIT = 65;
  int LPAREN = 66;
  int RPAREN = 67;
  int LBRACE = 68;
  int RBRACE = 69;
  int LBRACKET = 70;
  int RBRACKET = 71;
  int SEMICOLON = 72;
  int COMMA = 73;
  int DOT = 74;
  int DOLLAR = 75;

  int DEFAULT = 0;
  int IN_SINGLE_LINE_COMMENT = 1;
  int IN_MULTI_LINE_COMMENT = 2;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"//\"",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "<token of type 10>",
    "\"DECLARATION\"",
    "\"BEGIN\"",
    "\"END\"",
    "\"STATEMENT\"",
    "\"IF\"",
    "\"THEN\"",
    "\"ELSE\"",
    "\"CASE\"",
    "\"WHEN\"",
    "\"SET\"",
    "\"WHILE\"",
    "\"DO\"",
    "\"SKIP\"",
    "\"ACQUIESCENT_PASS\"",
    "\"PRINT\"",
    "\"PRINTLN\"",
    "\"RETURN\"",
    "\"STRING\"",
    "\"INT\"",
    "\"DOUBLE\"",
    "\"BOOLEAN\"",
    "\"RECORD\"",
    "\"LIST\"",
    "\"MAP\"",
    "\"PARTICIPANT_SET\"",
    "\"OBJECT\"",
    "\"LIKE\"",
    "\"IS\"",
    "\"EMPTY\"",
    "\"IN\"",
    "\"UNION\"",
    "\"MINUS\"",
    "\"INTERSECT\"",
    "\"INCLUDE\"",
    "\"EXCLUDE\"",
    "\"AssignByUnit\"",
    "\"AssignByPosition\"",
    "\"AssignByGroup\"",
    "\"AssignByStaff\"",
    "\"PROCESS_INITIATOR\"",
    "\"ExecutorOf\"",
    "\"NULL\"",
    "\"TRUE\"",
    "\"FALSE\"",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEX_LITERAL>",
    "<OCTAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<CHARACTER_LITERAL>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\",\"",
    "\".\"",
    "\"$\"",
    "\"=\"",
    "\"||\"",
    "\"&&\"",
    "\"|\"",
    "\"^\"",
    "\"&\"",
    "\"==\"",
    "\"!=\"",
    "\"<\"",
    "\">\"",
    "\"<=\"",
    "\">=\"",
    "\"<<\"",
    "\">>\"",
    "\">>>\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "\"++\"",
    "\"--\"",
    "\"~\"",
    "\"!\"",
  };

}
