package cakelab.backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Customization {
    private Long baseCakeId;
    private String baseName;
    private String baseBildUrl;

    private String size;
    private String fontFamily;
    private String fontColor;
    private String text;

    public Long getBaseCakeId() {
        return baseCakeId;
    }

    public void setBaseCakeId(Long baseCakeId) {
        this.baseCakeId = baseCakeId;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseBildUrl() {
        return baseBildUrl;
    }

    public void setBaseBildUrl(String baseBildUrl) {
        this.baseBildUrl = baseBildUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getFontColor() {
        return fontColor;
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
