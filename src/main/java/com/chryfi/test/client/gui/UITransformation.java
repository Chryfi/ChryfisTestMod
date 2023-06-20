package com.chryfi.test.client.gui;

import com.chryfi.test.client.gui.unit.LengthUnit;
import com.chryfi.test.client.gui.unit.Unit;
import com.chryfi.test.client.gui.unit.UnitType;
import org.checkerframework.checker.guieffect.qual.UI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class UITransformation {
    private LengthUnit x = new LengthUnit(0);
    private LengthUnit y = new LengthUnit(0);
    private POSITION position = POSITION.RELATIVE;
    private LengthUnit anchorX = new LengthUnit(0);
    private LengthUnit anchorY = new LengthUnit(0);
    private Unit width = new Unit(0);
    private Unit height = new Unit(0);
    /**
     * Whether to break the elements into a new row when they don't fit anymore.
     */
    private boolean wrap = true;
    /**
     * padding: top, right, bottom, left
     */
    private LengthUnit[] padding = new LengthUnit[]{new LengthUnit(0), new LengthUnit(0), new LengthUnit(0), new LengthUnit(0)};
    /**
     * margin: top, right, bottom, left
     */
    private LengthUnit[] margin = new LengthUnit[]{new LengthUnit(0), new LengthUnit(0), new LengthUnit(0), new LengthUnit(0)};

    private UIElement target;

    public UITransformation(UIElement target) {
        if (target == null) throw new IllegalArgumentException("WTF ARE YOU DOING!?");

        this.target = target;
    }

    public LengthUnit getX() {
        return this.x;
    }

    public LengthUnit getY() {
        return this.y;
    }

    public POSITION getPositionType() {
        return this.position;
    }

    public void setPositionType(POSITION type) {
        this.position = type;
    }

    public LengthUnit getAnchorX() {
        return this.anchorX;
    }

    public LengthUnit getAnchorY() {
        return this.anchorY;
    }

    public Unit getWidth() {
        return this.width;
    }

    public Unit getHeight() {
        return this.height;
    }

    public boolean isWrap() {
        return this.wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }


    /*
     * PADDING
     */

    public LengthUnit getPaddingTop() {
        return this.padding[0];
    }

    public LengthUnit getPaddingRight() {
        return this.padding[1];
    }

    public LengthUnit getPaddingBottom() {
        return this.padding[2];
    }

    public LengthUnit getPaddingLeft() {
        return this.padding[3];
    }


    /*
     * MARGIN
     */

    public LengthUnit getMarginTop() {
        return this.margin[0];
    }

    public LengthUnit getMarginRight() {
        return this.margin[1];
    }

    public LengthUnit getMarginBottom() {
        return this.margin[2];
    }

    public LengthUnit getMarginLeft() {
        return this.margin[3];
    }

    /**
     * Recursive method for resizing traversing the tree.
     * @param parentRow
     */
    public void resize(DocumentFlowRow parentRow) {
        /*
         * Reset the areas for a clean start.
         */
        this.target.resetAreas();

        boolean widthAuto = this.width.getType() == UnitType.AUTO;
        boolean heightAuto = this.height.getType() == UnitType.AUTO;
        boolean autoDimensions = widthAuto || heightAuto;

        if (autoDimensions) {
            /*
             * To calculate the dimensions we don't need document flow for this target.
             * When you pass it and if this target does not fit in the row, the row will end.
             * This would mess up the document flow, because the dimension might turn out to be smaller
             * and this target might fit into the row after all.
             */
            this.apply(null);
        } else {
            this.apply(parentRow);
        }

        DocumentFlowRow childrenRow = new DocumentFlowRow();
        ChildrenResult result = this.traverseChildren(childrenRow);

        int[] paddings = this.calculatePaddings();

        if (widthAuto && !result.rowBreak) {
            this.width.setValue(childrenRow.getWidth() + paddings[1] + paddings[3]);
        }

        if (heightAuto) {
            this.height.setValue(result.totalHeight + paddings[0] + paddings[2]);
        }

        if (autoDimensions) {
            this.target.resetAreas();
            this.apply(parentRow);
            /*
             * Traverse the children again as things like paddings and margins, when defined as percentages,
             * refer to the parent's dimensions. The parent's dimensions are only finished after this code block here.
             */
            this.traverseChildren(new DocumentFlowRow());
        }

        if (widthAuto) this.width.setAuto();
        if (heightAuto) this.height.setAuto();

        this.target.onAreasCalculated();
    }

    /**
     * Offset this element and the children element, only if their position type
     * implies a positional relationship between the child and this element.
     * Position type global does not fall under this category, as it ignores the parent's position.
     *
     * The offset will always be applied to this target element,
     * but when the child does not have a positional relationship with this target, it will be ignored.
     * @param x
     * @param y
     */
    public void offsetTree(int x, int y) {
        this.target.getAreaChain().addX(x);
        this.target.getAreaChain().addY(y);

        for (UIElement child : this.target.getChildren()) {
            child.getTransformation().offsetTree(x, y);
        }
    }

    /**
     * @return true when the children didn't fit into one continuous document flow row.
     */
    protected ChildrenResult traverseChildren(DocumentFlowRow flowRow) {
        boolean rowBreak = false;
        int totalHeight = 0;
        List<UIElement> children = this.target.getChildren();

        for (int i = 0; i < children.size(); i++) {
            UIElement element = children.get(i);
            element.getTransformation().resize(flowRow);

            if (flowRow.isEnd()) {
                totalHeight += flowRow.getMaxHeight();
                flowRow.reset();
                rowBreak = true;
            }

            if (element.getTransformation().getPositionType() != UITransformation.POSITION.ABSOLUTE) {
                flowRow.addElement(element);
            }

            if (i == children.size() - 1) {
                totalHeight += flowRow.getMaxHeight();
            }
        }

        return new ChildrenResult(rowBreak, totalHeight);
    }

    /**
     * Calculates the areas for this target element. Traverse the UI tree and call this method.
     * @param row
     */
    private void apply(@Nullable final DocumentFlowRow row) {
        final Area parentInnerArea = this.getParentInnerArea();

        final DocumentFlowRow.AreaNode root = new DocumentFlowRow.AreaNode(this.target.getFlowArea());
        final DocumentFlowRow.AreaNode contentNode = root.appendChild(this.target.getContentArea());
        final DocumentFlowRow.AreaNode innerNode = root.appendChild(this.target.getInnerArea());

        this.setContentAreaDimensions();

        final int[] margin = this.calculateMargins();
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
            if (row != null) this.calculateDocumentFlow(row, this.target, root);

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

        final int[] padding = this.calculatePaddings();
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
        if (row.getLast().isEmpty()) return;

        final boolean parentWrap = this.target.getParent().isEmpty() || this.target.getParent().get().getTransformation().isWrap();
        final Area parentInnerArea = this.getParentInnerArea();

        int occupiedWidth = row.getWidth();

        root.addY(row.getY() - parentInnerArea.getY());

        if (parentInnerArea.getWidth() - occupiedWidth >= target.getFlowArea().getWidth() || !parentWrap) {
            root.addX(occupiedWidth);
        } else {
            /* element doesn't fit -> breaks into new row */
            root.addY(row.getMaxHeight());
            row.end();
        }
    }

    /**
     * Calculate the margins of the given target
     * @return {marginTop, marginRight, marginBottom, marginLeft}
     */
    protected int[] calculateMargins() {
        final Area parentInnerArea = this.getParentInnerArea();
        int marginTop = this.calculatePixels(parentInnerArea.getHeight(), this.getMarginTop());
        int marginBottom = this.calculatePixels(parentInnerArea.getHeight(), this.getMarginBottom());
        int marginLeft = this.calculatePixels(parentInnerArea.getWidth(), this.getMarginLeft());
        int marginRight = this.calculatePixels(parentInnerArea.getWidth(), this.getMarginRight());

        return new int[]{marginTop, marginRight, marginBottom, marginLeft};
    }

    protected int[] calculatePaddings() {
        final Area parentInnerArea = this.getParentInnerArea();
        int paddingTop = this.calculatePixels(parentInnerArea.getHeight(), this.getPaddingTop());
        int paddingBottom = this.calculatePixels(parentInnerArea.getHeight(), this.getPaddingBottom());
        int paddingLeft = this.calculatePixels(parentInnerArea.getWidth(), this.getPaddingLeft());
        int paddingRight = this.calculatePixels(parentInnerArea.getWidth(), this.getPaddingRight());

        return new int[]{paddingTop, paddingRight, paddingBottom, paddingLeft};
    }

    protected void setContentAreaDimensions() {
        final Optional<UIElement> parent = this.target.getParent();
        final Area parentInnerArea = this.getParentInnerArea();

        Unit effectiveWidth = this.width.getType() == UnitType.AUTO ? new Unit(1F) : this.width;
        Unit effectiveHeight = this.height.getType() == UnitType.AUTO ? new Unit(Integer.MAX_VALUE) : this.height;

        if (parent.isPresent() && parent.get().getTransformation().getWidth().getType() == UnitType.AUTO) {
            this.target.getContentArea().setWidth(this.calculatePixels(0, effectiveWidth));
        } else {
            this.target.getContentArea().setWidth(this.calculatePixels(parentInnerArea.getWidth(), effectiveWidth));
        }

        if (parent.isPresent() && parent.get().getTransformation().getHeight().getType() == UnitType.AUTO) {
            this.target.getContentArea().setHeight(this.calculatePixels(0, effectiveHeight));
        } else {
            this.target.getContentArea().setHeight(this.calculatePixels(parentInnerArea.getHeight(), effectiveHeight));
        }
    }

    /**
     *
     * @param relative the value to use when the unit is in percentage.
     * @param unit the unit to calculate the pixels
     * @return the pixel value of the given unit.
     */
    protected int calculatePixels(int relative, LengthUnit unit) {
        if (unit.getType() == UnitType.PERCENTAGE) {
            /*
             * Math.round can cause issues sometimes where with "perfect" percentages,
             * e.g. 4 * 25% width elements might not always fit in one row
             * TODO floor fixes this but could in theory lead to 1 pixel inconsistencies
             */
            return (int) Math.floor(relative * unit.getValue());
        } else {
            return (int) unit.getValue();
        }
    }

    protected Area getParentInnerArea() {
        final Area parentInnerArea;

        if (this.target.getParent().isPresent()) {
            parentInnerArea = this.target.getParent().get().getInnerArea();
        } else {
            parentInnerArea = new Area(0,0,0,0);
        }

        return parentInnerArea;
    }

    public enum POSITION {
        RELATIVE,
        /**
         * Relative to its own position, but does not contribute to the document flow.
         */
        ABSOLUTE
    }

    /**
     * Helper class to return the results of traversing the children elements.
     */
    private class ChildrenResult {
        private boolean rowBreak;
        private int totalHeight;

        public ChildrenResult(boolean rowBreak, int totalHeight) {
            this.totalHeight = totalHeight;
            this.rowBreak = rowBreak;
        }

        public boolean isRowBreak() {
            return this.rowBreak;
        }

        public int getTotalHeight() {
            return this.totalHeight;
        }
    }
}
