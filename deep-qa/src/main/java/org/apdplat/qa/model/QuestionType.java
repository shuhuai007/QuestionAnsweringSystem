package org.apdplat.qa.model;

/**
 * 问题类型
 * @author 杨尚川
 */
public enum QuestionType {

    NULL("未知"),
    PERSON_NAME("人名"),
    LOCATION_NAME("地名"),
    ORGANIZATION_NAME("团体机构名"),
    NUMBER("数字"),
    TIME("时间"),
    DEFINITIION("定义"),
    OBJECT("对象");

    public String getPos() {
        String pos = "unknown";
        //nr 人名
        if (QuestionType.PERSON_NAME == this) {
            pos = "nr";
        }
        //ns 地名
        if (QuestionType.LOCATION_NAME == this) {
            pos = "ns";
        }
        //nt 团体机构名
        if (QuestionType.ORGANIZATION_NAME == this) {
            pos = "nt";
        }
        //m=数词
        //mh=中文数词
        //mb=百分数词
        //mf=分数词
        //mx=小数词
        //mq=数量词
        if (QuestionType.NUMBER == this) {
            pos = "m";
        }
        //t=时间词
        //tq=时间量词
        //tdq=日期量词
        if (QuestionType.TIME == this) {
            pos = "t";
        }

        return pos;
    }

    private QuestionType(String des) {
        this.des = des;
    }

    private final String des;

    public String getDes() {
        return des;
    }
}