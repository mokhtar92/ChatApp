/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Hanaa
 */
public class Message implements Serializable {

    protected String from;
    protected String to;
    protected String body;
    protected int fontsSize;

    protected XMLGregorianCalendar date;
    protected String fontColor;
    protected String fontFamily;
    protected String fontStyle;

    protected String fontWeight;
    protected Boolean underline;
    public Message(){}
    public Message(int fontsSize, String from, String to, String fontColor, String fontFamily, String fontStyle, String body, String fontWeight, Boolean underline) {
        this.fontsSize = fontsSize;
        this.from = from;
        this.to = to;

        this.fontColor = fontColor;
        this.fontFamily = fontFamily;
        this.fontStyle = fontStyle;
        this.body = body;
        this.fontWeight = fontWeight;
        this.underline = underline;
    }

    public int getFontsSize() {
        return fontsSize;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public String getFontStyle() {
        return fontStyle;
    }

    public String getBody() {
        return body;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public Boolean getUnderline() {
        return underline;
    }

    public void setFontsSize(int fontsSize) {
        this.fontsSize = fontsSize;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public void setUnderline(Boolean underline) {
        this.underline = underline;
    }

}
