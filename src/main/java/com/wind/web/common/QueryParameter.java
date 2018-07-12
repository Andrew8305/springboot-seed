package com.wind.web.common;

import lombok.Data;

@Data
public class QueryParameter {
    private String type;
    private QueryParameterMethod method;
    private String value;
    private QueryParameterType valueType;

    public QueryParameter(String type, QueryParameterMethod method, String value, QueryParameterType valueType) {
        this.type = type;
        this.method = method;
        this.valueType = valueType;
        this.value = value;
    }

    public QueryParameter()
    {
        this.type = "";
        this.method = QueryParameterMethod.EQUAL;
        this.valueType = QueryParameterType.LONG;
        this.value = "";
    }
}
