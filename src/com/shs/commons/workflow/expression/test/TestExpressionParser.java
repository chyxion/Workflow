package com.shs.commons.workflow.expression.test;
import com.shs.commons.workflow.expression.ExpressionParser;
import com.shs.commons.workflow.models.WfVar;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: </p>
 * <p>Description: 测试表达式解析器.
 *  支持各种常用的表达式,表达式中可以含有流程变量:
 *   括号：  ( )
 *   算术：  + ,-, *, /, % ,移位 <<  >> , ++, --
 *   逻辑:   && , || , !
 *   关系:   > , >=  , < ,<= ,== , !=
 </p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: ShengHangSoft </p>
 * <p>Date: 2011-5-17</p>
 * <p>Time: 17:21:31</p>
 * <p>@version 1.0</p>
 */
public class TestExpressionParser {

    public static void main(String [] args) {
        ExpressionParser parser = new ExpressionParser();

        try {
            //1. 定义数值型流程变量
            WfVar v1 = new WfVar();
            v1.setType(WfVar.TYPE_NUMBER);
            BigDecimal sumBigDecimal = new BigDecimal("10000.50");
            v1.setName("sum");
            v1.setValue("6000");
            //2. 定义字符串型流程变量
            WfVar v2 = new WfVar();
            String toolString="flyer";
            v2.setName("tool");
            v2.setValue(toolString);
            //3. 把变量加入变量集
            Map<String, WfVar> vst = new HashMap<String, WfVar>();
            vst.put(v1.getName(), v1);
            vst.put(v2.getName(), v2);
            //4. 解析表达式
            String exp = " sum >=5000 ";
            Object result = parser.parseExpr(exp, vst);
            //5. 日志
            System.out.println("表达式: "+exp+" 值： "+result.toString() );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            //throw new WorkflowException(WorkflowException.EXCEPTION_CODE_PARSE_EXPRESSION_ERROR);
        }

    }
}
