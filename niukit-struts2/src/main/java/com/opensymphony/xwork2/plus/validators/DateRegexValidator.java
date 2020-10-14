package com.opensymphony.xwork2.plus.validators;

import java.util.HashMap;

import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;
import com.woshidaniu.basicutils.DateUtils;

public class DateRegexValidator  extends FieldValidatorSupport {

    private static HashMap<String, Integer> map = new HashMap<String, Integer>();

    /* 默认最大年限 */
    private static final Integer MAX_YEAR = 2008;

    /* 默认最小年限 */
    private static final Integer MIN_YEAR = 1900;

    /* 最大年限 */
    private Integer maxYear;

    /* 最小年限 */
    private Integer minYear;

    /* 验证日期的正则表达式 */
    private String expression;

    /* 是否使用正则表达式验证 */
    private Boolean isRegex;
    
    public DateRegexValidator() {
        maxYear = null;
        minYear = null;
        expression = null;
        isRegex = false;
    }

    public void validate(Object object) throws ValidationException {

        // 获得字段的名字
        String fieldName = getFieldName();
        // 获得输入界面输入的值
        String value = getFieldValue(fieldName, object).toString();

        if (value == null || value.length() <= 0)
            return;

        if (fieldName.equals("regexYearField"))
            validateYear(value, object);
        else if (fieldName.equals("regexMonthField"))
            validateMonth(value, object);
        else if (fieldName.equals("regexDayField"))
            validateDay(value, object);
    }

    /**
     * 验证年份
     * 
     * @param object
     * @throws ValidationException
     */
    private void validateYear(String value, Object object) throws ValidationException {
        /* 设置默认值 */
        if (maxYear == null) {
            maxYear = MAX_YEAR;
        }
        if (minYear == null) {
            minYear = MIN_YEAR;
        }

        Integer temp = null;
        try {
            temp = Integer.valueOf(value);
        } catch (NumberFormatException ex) {
            addFieldErrorEx(getFieldName(), object);
            return;
        }

        if (temp != null) {
            if (temp >= minYear && temp <= maxYear){
                map.put("year", temp);
            }else{
                addFieldErrorEx(getFieldName(), object);
            }
        }
    }

    /**
     * 验证月份
     * 
     * @param object
     * @throws ValidationException
     */
    private void validateMonth(String value, Object object)
            throws ValidationException {
        try {
            Integer temp = Integer.valueOf(value);
            if (temp != null) {
                if (temp >= 1 && temp <= 12){
                    map.put("month", temp);
                }else{
                    addFieldErrorEx(getFieldName(), object);
                }
            }
        } catch (NumberFormatException ex) {
            addFieldErrorEx(getFieldName(), object);
            return;
        }
    }

    /**
     * 验证日期
     * 
     * @param object
     * @throws ValidationException
     */
    private void validateDay(String value, Object object)
            throws ValidationException {
        try {
            Integer temp = Integer.valueOf(value);
            if (temp != null) {
                Integer year = map.get("year");
                Integer month = map.get("month");
                
                //直接使用天数
                if(year==null || month==null) {
                    return;
                }
                
                // 是否使用正则表达式验证
                if (!isRegex) {
                    if(!DateUtils.isDate(year + "-" + month + "-" + temp)){
                        addFieldErrorEx(getFieldName(), object);
                    }
                } else {
                    String StrDay = temp < 10 ? "0" + temp.toString() : temp.toString();
                    String StrMonth = month < 10 ? "0" + month.toString() : month.toString();

                    //前面已经验证过了
                    //String StrYear = year < 100 ? year < 10 ? "200" + year: "19" + year : "1" + year;

                    String str = year + "-" + StrMonth + "-" + StrDay;

                    System.out.println("Date:" + str);

                    if (expression == null) {
                        if(!DateUtils.isDate(str, null)){
                            addFieldErrorEx(getFieldName(), object);
                        }
                    } else {
                        if (expression.trim().length() > 0) {
                            if(DateUtils.isDate(str, expression)){
                                addFieldErrorEx(getFieldName(), object);
                            }
                        }
                    }
                }
                map.clear();
            }
        } catch (NumberFormatException ex) {
            addFieldErrorEx(getFieldName(), object);
            return;
        }
    }

    /**
     * 控制是否只显示一条报错信息
     * 
     * @param arg0
     * @param arg1
     */
    private void addFieldErrorEx(String arg0, Object arg1) {
        if (this.getValidatorContext().getFieldErrors().size() == 0){
            addFieldError(arg0, arg1);
        }
    }

    public Integer getMaxYear() {
        return maxYear;
    }

    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }

    public Integer getMinYear() {
        return minYear;
    }

    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Boolean getIsRegex() {
        return isRegex;
    }

    public void setIsRegex(Boolean isRegex) {
        this.isRegex = isRegex;
    }
}
