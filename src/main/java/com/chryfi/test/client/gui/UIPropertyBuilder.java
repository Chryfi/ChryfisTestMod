package com.chryfi.test.client.gui;

/**
 * For easy method chaining without unnecessary method clutter in the actual {@link UIElement} class.
 */
public class UIPropertyBuilder {
    private UIElement target;

    public UIPropertyBuilder(UIElement element) {
        if (element == null) throw new IllegalArgumentException("The argument element cannot be null.");

        this.target = element;
    }

    public static UIPropertyBuilder setup(UIElement element) {
        return new UIPropertyBuilder(element);
    }

    public UIPropertyBuilder relative() {
        this.target.getTransformation().setPositionType(UITransformation.POSITION.RELATIVE);
        return this;
    }

    public UIPropertyBuilder absolute() {
        this.target.getTransformation().setPositionType(UITransformation.POSITION.ABSOLUTE);
        return this;
    }

    public UIPropertyBuilder x(float x) {
        this.target.getTransformation().getX().setValue(x);
        return this;
    }

    public UIPropertyBuilder x(int x) {
        this.target.getTransformation().getX().setValue(x);
        return this;
    }

    public UIPropertyBuilder y(float y) {
        this.target.getTransformation().getY().setValue(y);
        return this;
    }

    public UIPropertyBuilder y(int y) {
        this.target.getTransformation().getY().setValue(y);
        return this;
    }

    public UIPropertyBuilder anchorX(float x) {
        this.target.getTransformation().getAnchorX().setValue(x);
        return this;
    }

    public UIPropertyBuilder anchorX(int x) {
        this.target.getTransformation().getAnchorX().setValue(x);
        return this;
    }

    public UIPropertyBuilder anchorY(float y) {
        this.target.getTransformation().getAnchorY().setValue(y);
        return this;
    }

    public UIPropertyBuilder anchorY(int y) {
        this.target.getTransformation().getAnchorY().setValue(y);
        return this;
    }

    public UIPropertyBuilder width(int value) {
        this.target.getTransformation().getWidth().setValue(value);
        return this;
    }

    public UIPropertyBuilder width(float value) {
        this.target.getTransformation().getWidth().setValue(value);
        return this;
    }

    public UIPropertyBuilder widthAuto() {
        this.target.getTransformation().getWidth().setAuto();
        return this;
    }

    public UIPropertyBuilder height(int value) {
        this.target.getTransformation().getHeight().setValue(value);
        return this;
    }

    public UIPropertyBuilder height(float value) {
        this.target.getTransformation().getHeight().setValue(value);
        return this;
    }

    public UIPropertyBuilder heightAuto() {
        this.target.getTransformation().getHeight().setAuto();
        return this;
    }


    /*
     * PADDING
     */


    public UIPropertyBuilder paddingTop(float p) {
        this.target.getTransformation().getPaddingTop().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingTop(int p) {
        this.target.getTransformation().getPaddingTop().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingBottom(float p) {
        this.target.getTransformation().getPaddingBottom().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingBottom(int p) {
        this.target.getTransformation().getPaddingBottom().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingLeft(float p) {
        this.target.getTransformation().getPaddingLeft().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingLeft(int p) {
        this.target.getTransformation().getPaddingLeft().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingRight(float p) {
        this.target.getTransformation().getPaddingRight().setValue(p);

        return this;
    }

    public UIPropertyBuilder paddingRight(int p) {
        this.target.getTransformation().getPaddingRight().setValue(p);

        return this;
    }


    /*
     * MARGIN
     */

    public UIPropertyBuilder marginTop(float value) {
        this.target.getTransformation().getMarginTop().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginTop(int value) {
        this.target.getTransformation().getMarginTop().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginRight(float value) {
        this.target.getTransformation().getMarginRight().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginRight(int value) {
        this.target.getTransformation().getMarginRight().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginBottom(float value) {
        this.target.getTransformation().getMarginBottom().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginBottom(int value) {
        this.target.getTransformation().getMarginBottom().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginLeft(float value) {
        this.target.getTransformation().getMarginLeft().setValue(value);
        return this;
    }

    public UIPropertyBuilder marginLeft(int value) {
        this.target.getTransformation().getMarginLeft().setValue(value);
        return this;
    }

    public UIPropertyBuilder backgroundColor(float r, float g, float b) {
        this.target.backgroundColor(r, g, b);
        return this;
    }

    public UIPropertyBuilder backgroundColor(float r, float g, float b, float a) {
        this.target.backgroundColor(r, g, b, a);
        return this;
    }
}
