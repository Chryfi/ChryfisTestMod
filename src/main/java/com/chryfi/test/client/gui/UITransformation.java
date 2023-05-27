package com.chryfi.test.client.gui;

import org.checkerframework.checker.units.qual.A;

import javax.print.Doc;

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
        this.resize(new DocumentFlowRow());
    }

    /**
     * Recursive method for resizing traversing the tree.
     * @param row
     */
    private void resize(DocumentFlowRow row) {
        /*
         * Reset the areas so in case of auto dimensions the children don't use these
         * as reference for example for percentages, as the auto dimensions need to be calculated after the children.
         */
        this.target.resetAreas();

        this.apply(row);

        DocumentFlowRow flowRow = new DocumentFlowRow();
        for (UIElement element : this.target.getChildren()) {
            element.getTransformation().resize(flowRow);

            /*
             * Position absolute removes an element from the document flow
             */
            if (element.getTransformation().getPositionType() != UITransformation.POSITION.ABSOLUTE) {
                flowRow.addElement(element);
            }
        }
    }

    /**
     * Calculates stuff, very important. Traverse the UI tree and call this method.
     * @param row
     */
    private void apply(DocumentFlowRow row) {
        final Area parentInnerArea;

        if (this.target.getParent().isPresent()) {
            parentInnerArea = this.target.getParent().get().getInnerArea();
        } else {
            parentInnerArea = new Area(0,0,0,0);
        }

        DocumentFlowRow.AreaNode root = new DocumentFlowRow.AreaNode(this.target.getFlowArea());
        DocumentFlowRow.AreaNode contentNode = root.appendChild(this.target.getContentArea());
        DocumentFlowRow.AreaNode innerNode = root.appendChild(this.target.getInnerArea());


        this.target.getContentArea().setWidth(this.calculatePixels(parentInnerArea.getWidth(), this.width));
        this.target.getContentArea().setHeight(this.calculatePixels(parentInnerArea.getHeight(), this.height));

        int[] margin = this.calculateMargins(this.target);
        this.target.margin = margin;

        this.target.getFlowArea().setHeight(margin[0] + margin[2] + this.target.getContentArea().getHeight());
        this.target.getFlowArea().setWidth(margin[3] + margin[1] + this.target.getContentArea().getWidth());

        root.setX(parentInnerArea.getX());
        root.setY(parentInnerArea.getY());

        int anchorX = this.calculatePixels(this.target.getContentArea().getWidth(), this.anchorX);
        int anchorY = this.calculatePixels(this.target.getContentArea().getHeight(), this.anchorY);
        int x = this.calculatePixels(parentInnerArea.getWidth(), this.x) - anchorX;
        int y = this.calculatePixels(parentInnerArea.getHeight(), this.y) - anchorY;

        if (this.position == POSITION.RELATIVE) {
            this.calculateDocumentFlow(row, this.target, root);

            /* offset the position */
            contentNode.addX(x);
            contentNode.addY(y);
        } else if (this.position == POSITION.ABSOLUTE){
            /*
             * TODO In CSS absolute positions it without document flow relative to the closest ancestor.
             * What about an easy way of positioning with screen coordinates?
             */
            contentNode.addX(x);
            contentNode.addY(y);
        }

        contentNode.addX(margin[3]);
        contentNode.addY(margin[0]);

        int[] padding = this.calculatePaddings(this.target);
        this.target.padding = padding;
        this.target.getInnerArea().setWidth(this.target.getContentArea().getWidth() - padding[1] - padding[3]);
        this.target.getInnerArea().setHeight(this.target.getContentArea().getHeight() - padding[0] - padding[2]);
        innerNode.addX(padding[3]);
        innerNode.addY(padding[0]);
    }

    /**
     * Calculates the document flow of the given root area.
     * This method modifies the positions of the areas in the given chain.
     *
     * If it fits besides the previous elements in the given row, it will be placed in the row, if not, the row will end,
     * and it will flow into a new row.
     * @param row
     * @param root the area flow node of the element to place in the document.
     */
    protected void calculateDocumentFlow(DocumentFlowRow row, UIElement target, DocumentFlowRow.AreaNode root) {
        final Area parentInnerArea;

        if (target.getParent().isPresent()) {
            parentInnerArea = target.getParent().get().getInnerArea();
        } else {
            parentInnerArea = new Area(0,0,0,0);
        }

        if (row.getLast().isPresent()) {
            int occupiedWidth = row.getWidth();//lastFlowArea.getX() - parentContentArea.getX() + lastFlowArea.getWidth();

            if (parentInnerArea.getWidth() - occupiedWidth >= target.getFlowArea().getWidth()) {
                root.addX(occupiedWidth);
                root.setY(row.getY());
            } else {
                /* element doesn't fit -> breaks into new row */
                root.setY(row.getY() + row.getMaxHeight());
                row.reset();
            }
        }
    }

    /**
     * Calculate the margins of the given target
     * @param target
     * @return {marginTop, marginRight, marginBottom, marginLeft}
     */
    protected int[] calculateMargins(UIElement target) {
        int marginTop = this.calculatePixels(target.getContentArea().getHeight(), target.getTransformation().getMarginTop());
        int marginBottom = this.calculatePixels(target.getContentArea().getHeight(), target.getTransformation().getMarginBottom());
        int marginLeft = this.calculatePixels(target.getContentArea().getWidth(), target.getTransformation().getMarginLeft());
        int marginRight = this.calculatePixels(target.getContentArea().getWidth(), target.getTransformation().getMarginRight());

        return new int[]{marginTop, marginRight, marginBottom, marginLeft};
    }

    protected int[] calculatePaddings(UIElement target) {
        int paddingTop = this.calculatePixels(target.getContentArea().getHeight(), target.getTransformation().getPaddingTop());
        int paddingBottom = this.calculatePixels(target.getContentArea().getHeight(), target.getTransformation().getPaddingBottom());
        int paddingLeft = this.calculatePixels(target.getContentArea().getWidth(), target.getTransformation().getPaddingLeft());
        int paddingRight = this.calculatePixels(target.getContentArea().getWidth(), target.getTransformation().getPaddingRight());

        return new int[]{paddingTop, paddingRight, paddingBottom, paddingLeft};
    }

    /**
     *
     * @param relative the value to use when the unit is in percentage.
     * @param unit the unit to calculate the pixels
     * @return the pixel value of the given unit.
     */
    protected int calculatePixels(int relative, Unit unit) {
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
