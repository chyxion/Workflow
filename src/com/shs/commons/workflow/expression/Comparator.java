package com.shs.commons.workflow.expression;
import org.apache.oro.text.regex.*;

import java.util.Set;

/**
 * User: wangtao
 * Date: 2004-12-16
 * Time: 15:16:59
 */
public class Comparator {
	static org.apache.oro.text.regex.PatternMatcher patternMatcher  = new Perl5Matcher();
	static PatternCompiler patternCompiler = new Perl5Compiler();
	static Boolean T = new Boolean(true);
	static Boolean F = new Boolean(false);

	public static Boolean compare(String opr, Object obj1, Object obj2, int line, int column) throws ParseException
	{
		if(opr.equals("IN")){
			if(obj1 == null)
				throw new ParseException("集合运算符 IN 的左边表达式的值为空, 行号:"+ line + " 列号: " + column);
			if(obj2 == null)
				throw new ParseException("集合运算符 IN 的右边表达式的值为空, 行号:"+ line + " 列号: " + column);
			if(! (obj1 instanceof Set) )
				throw new ParseException("集合运算符 IN 的左边表达式的值不是集合类型, 行号:"+ line + " 列号: " + column);
			if(! (obj2 instanceof Set ))
				throw new ParseException("集合运算符 IN 的右边表达式的值不是集合类型, 行号:"+ line + " 列号: " + column);
			Set set1 = (Set) obj1;
			Set set2 = (Set) obj2;
			return new Boolean(set1.containsAll(set2));
		}
		else {
			if(obj1 == null || obj2 == null) {
				if(opr.equals("==")) {
					if(obj1 == null && obj2 == null)  return T;
					else return F;
				}
				else if(opr.equals("!=")){
					if(obj1 == null && obj2 == null)  return F;
					else return T;
				}
				else throw new ParseException("比较运算符\"" + opr + "\"的比较值为NULL, 行号:"+ line + " 列号: " + column);
			}
			else if(obj1 instanceof String){
				if(obj2 instanceof String){
					String s1 = (String)obj1;
					String s2 = (String)obj2;
					if(opr.equals("<")){
						if(s1.compareTo(s2) < 0) return T;
						else return F;
					}
					else if(opr.equals(">")){
						if(s1.compareTo(s2) > 0) return T;
						else return F;
					}
					else if(opr.equals("<=")){
						if(s1.compareTo(s2) <= 0) return T;
						else return F;
					}
					else if(opr.equals(">=")){
						if(s1.compareTo(s2) >= 0) return T;
						else return F;
					}
					else if(opr.equals("==")){
						if(s1.compareTo(s2) == 0) return T;
						else return F;
					}
					else if(opr.equals("!=")){
						if(s1.compareTo(s2) != 0) return T;
						else return F;
					}
					else if(opr.equals("LIKE")) {
						Pattern pattern = null;
						try {
							pattern = patternCompiler.compile(s2);
						} catch (MalformedPatternException e) {
							throw new ParseException("字符串模式匹配LIKE运算的模式字符串不符合Perl 5规范, 模式字符串: " + s2 + ", 行号: " + line + " 列号: " + column);
						}
						if(patternMatcher.matches(s1, pattern)) return T;
						else return F;
					}
				}
				else throw new ParseException("字符串类型不能和其它类型作比较，行号: " + line + " 列号: " + column);
			}
			else if(obj1 instanceof Number){
				double n1 = ((Number)obj1).doubleValue();
				double n2;
				if(obj2 instanceof Number){
					n2 = ((Number)obj2).doubleValue();
				}
				else if(obj2 instanceof Boolean){
					if(((Boolean)obj2).booleanValue() == true) n2 = 1.0;
					else n2 = 0.0;
				}
				else if(obj2 instanceof Character){
					n2 = (double)((Character)obj2).charValue();
				}
				else throw new ParseException("数值类型不能和其它类型作比较，行号: " + line + " 列号: " + column);
				return compareNumber(opr, n1, n2);
			}
			else if(obj1 instanceof java.util.Date){
				double n1 = (double)((java.util.Date)obj1).getTime();
				double n2;
				if(obj2 instanceof java.util.Date){
					n2 = (double)((java.util.Date)obj2).getTime();
				}
				else throw new ParseException("日期类型不能和其它类型作比较，行号: " + line + " 列号: " + column);
				return compareNumber(opr, n1, n2);
			}
			else if(obj1 instanceof Boolean){
				double n1, n2;
				if(((Boolean)obj1).booleanValue() == true) n1 = 1.0;
				else n1 = 0.0;
				if(obj2 instanceof Number){
					n2 = ((Number)obj2).doubleValue();
				}
				else if(obj2 instanceof Boolean){
					if(((Boolean)obj2).booleanValue() == true) n2 = 1.0;
					else n2 = 0.0;
				}
				else if(obj2 instanceof Character){
					n2 = (double)((Character)obj2).charValue();
				}
				else throw new ParseException("布尔类型不能和其它类型作比较，行号: " + line + " 列号: " + column);
				return compareNumber(opr, n1, n2);
			}
			else if(obj1 instanceof Character){
				double n1, n2;
				n1 = (double)((Character)obj1).charValue();
				if(obj2 instanceof Number){
					n2 = ((Number)obj2).doubleValue();
				}
				else if(obj2 instanceof Boolean){
					if(((Boolean)obj2).booleanValue() == true) n2 = 1.0;
					else n2 = 0.0;
				}
				else if(obj2 instanceof Character){
					n2 = (double)((Character)obj2).charValue();
				}
				else throw new ParseException("字符类型不能和其它类型作比较，行号: " + line + " 列号: " + column);
				return compareNumber(opr, n1, n2);
			}
			else throw new ParseException("数据类型不支持比较操作，行号: " + line + " 列号: " + column);
			return F;
		}
	}

	private static Boolean compareNumber(String opr, double n1, double n2)
	{
		if(opr.equals("<")){
			if(n1 < n2) return T;
			else return F;
		}
		else if(opr.equals(">")){
			if(n1 > n2) return T;
			else return F;
		}
		else if(opr.equals("<=")){
			if(n1 <= n2) return T;
			else return F;
		}
		else if(opr.equals(">=")){
			if(n1 >= n2) return T;
			else return F;
		}
		else if(opr.equals("==")){
			if(n1 == n2) return T;
			else return F;
		}
		else if(opr.equals("!=")){
			if(n1 != n2) return T;
			else return F;
		}
		else return F;
	}
}
