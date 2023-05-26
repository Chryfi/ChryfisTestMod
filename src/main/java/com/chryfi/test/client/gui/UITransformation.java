package com.chryfi.test.client.gui;

import org.checkerframework.checker.guieffect.qual.UI;

public class UITransformation {
    private Unit x = new Unit(0);
    private Unit y = new Unit(0);
    private POSITION position = POSITION.RELATIVE;
    private Unit anchorX = new Unit(0);
    private Unit anchorY = new Unit(0);
    private Unit width = new Unit(0);
    private Unit height = new Unit(0);
    /**
     * padding: top, right, bottom, left
     */
    private Unit[] padding = new Unit[]{new Unit(0), new Unit(0), new Unit(0), new Unit(0)};
    /**
     * margin: top, right, bottom, left
     */
    private Unit[] margin = new Unit[]{new Unit(0), new Unit(0), new Unit(0), new Unit(0)};

    private UIElement target;

    public UITransformation(UIElement target) {
        if (target == null) throw new IllegalArgumentException("WTF ARE YOU DOING!?");

        this.target = target;
    }

    public Unit getX() {
        return this.x;
    }

    public Unit getY() {
        return this.y;
    }

    public POSITION getPositionType() {
        return this.position;
    }

    public Unit getAnchorX() {
        return this.anchorX;
    }

    public Unit getAnchorY() {
        return this.anchorY;
    }

    public Unit getWidth() {
        return this.width;
    }

    public Unit getHeight() {
        return this.height;
    }


    /*
     * PADDING
     */

    public Unit getPaddingTop() {
        return this.padding[0];
    }

    public Unit getPaddingRight() {
        return this.padding[1];
    }

    public Unit getPaddingBottom() {
        return this.padding[2];
    }

    public Unit getPaddingLeft() {
        return this.padding[3];
    }


    /*
     * MARGIN
     */

    public Unit getMarginTop() {
        return this.margin[0];
    }

    public Unit getMarginRight() {
        return this.margin[1];
    }

    public Unit getMarginBottom() {
        return this.margin[2];
    }

    public Unit getMarginLeft() {
        return this.margin[3];
    }

    /**
     * Entry point for resizing the children
     */
    public void resize() {
        this.resize(null);
    }

    /**
     * Recursive method for resizing
     * @param last
     */
    private void resize(UIElement last) {
        this.apply(last);

        last = null;
        for (UIElement element : this.target.getChildren()) {
            element.transformation.resize(last);

            /*
             * Position absolute removes an element from the document flow
             */
            if (element.transformation.getPositionType() != UITransformation.POSITION.ABSOLUTE) {
                last = element;
            }
        }
    }

    /**
     * Calculates stuff, very important. Traverse the UI tree and call this method.
     * @param last the element that was processed before this. This is needed to create document flow.
     *             Can be null
     */
    public void apply(UIElement last) {
        /**
         * TODO in DOM elements in one row align themselves to the lowest
         * element in that row - e.g. margin of the second element can influence where the first element is
         */

        final Area parentContentBox, parentFlowArea;
        final Area lastContentBox, lastFlowArea;

        if (this.target.getParent().isPresent()) {
            parentFlowArea = this.target.getParent().get().flowArea;
            parentContentBox = this.target.getParent().get().contentBox;
        } else {
            parentFlowArea = new Area(0,0,0,0);
            parentContentBox = new Area(0,0,0,0);
        }

        if (last != null) {
            lastFlowArea = last.flowArea;
            lastContentBox = last.contentBox;
        } else {
            lastFlowArea = new Area(0,0,0,0);
            lastContentBox = new Area(0,0,0,0);
        }

        this.target.contentBox.setWidth(this.calculatePixels(parentContentBox.getWidth(), this.width));
        this.target.contentBox.setHeight(this.calculatePixels(parentContentBox.getHeight(), this.height));

        int marginTop = this.calculatePixels(this.target.contentBox.getHeight(), this.getMarginTop());
        int marginBottom = this.calculatePixels(this.target.contentBox.getHeight(), this.getMarginBottom());
        int marginLeft = this.calculatePixels(this.target.contentBox.getWidth(), this.getMarginLeft());
        int marginRight = this.calculatePixels(this.target.contentBox.getWidth(), this.getMarginRight());
        /* debugging purpose */
        this.target.margin = new int[]{marginTop, marginRight, marginBottom, marginLeft};

        this.target.flowArea.setHeight(marginTop + marginBottom + this.target.contentBox.getHeight());
        this.target.flowArea.setWidth(marginLeft + marginRight + this.target.contentBox.getWidth());

        /* target contentBox has now relative coordinates to target flowArea */
        this.target.contentBox.setX(marginLeft);
        this.target.contentBox.setY(marginTop);

        /* target flowArea has now global coordinates - is now relative to the parent's contentBox */
        this.target.flowArea.setX(parentContentBox.getX());
        this.target.flowArea.setY(parentContentBox.getY());

        int anchorX = this.calculatePixels(this.target.contentBox.getWidth(), this.anchorX);
        int anchorY = this.calculatePixels(this.target.contentBox.getHeight(), this.anchorY);
        int x = this.calculatePixels(parentContentBox.getWidth(), this.x) - anchorX;
        int y = this.calculatePixels(parentContentBox.getHeight(), this.y) - anchorY;

        if (this.position == POSITION.RELATIVE) {
            /*
             * This code is responsible for document flow. Check if this element fit's
             * in the gap between last element and the parent's border.
             */
            if (last != null) {
                int occupiedWidth = lastFlowArea.getX() - parentContentBox.getX() + lastFlowArea.getWidth();

                /*
                 * target flowArea has now global coordinates
                 */
                if (parentContentBox.getWidth() - occupiedWidth >= this.target.flowArea.getWidth()) {
                    this.target.flowArea.addX(occupiedWidth);
                    //TODO we need the biggest height of a row -> build some sort of chain that will be reset when going into the next row or so
                    this.target.flowArea.setY(lastFlowArea.getY());
                } else {
                    //TODO we need the biggest height of a row -> build some sort of chain that will be reset when going into the next row or so
                    this.target.flowArea.setY(lastFlowArea.getY() + lastFlowArea.getHeight());
                }
            }

            /* target contentBox has now global coordinates */
            this.target.contentBox.addX(this.target.flowArea.getX());
            this.target.contentBox.addY(this.target.flowArea.getY());

            /* offset the position */
            this.target.contentBox.addX(x);
            this.target.contentBox.addY(y);
        } else if (this.position == POSITION.ABSOLUTE){
            /*
             * TODO In CSS absolute positions it without document flow relative to the closest ancestor.
             * What about an easy way of positioning with screen coordinates?
             */
            this.target.contentBox.addX(x);
            this.target.contentBox.addY(y);
        }
    }

    /**
     *
     * @param relative the value to use when the unit is in percentage.
     * @param unit the unit to calculate the pixels
     * @return the pixel value of the given unit.
     */
    private int calculatePixels(int relative, Unit unit) {
        if (unit.getType() == Unit.TYPE.PERCENTAGE) {
            return Math.round(relative * unit.getValue());
        } else {
            return (int) unit.getValue();
        }
    }


    public enum POSITION {
        /**
         * Relative to the parent
         */
        RELATIVE,
        /**
         * Global position
         */
        ABSOLUTE
    }

    public enum DISPLAYSTYLE {
        BLOCK,
        INLINEBLOCK
    }
}
