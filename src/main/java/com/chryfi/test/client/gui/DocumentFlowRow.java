package com.chryfi.test.client.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This helper class chains the elements of one document flow "row" together.
 *
 * Everytime a new element gets added to the document flow,
 * the maximum height can be calculated. This is needed so the next "row" can start at that maximum height, without overlapping any
 * of the previous elements.
 * It is also needed to influence the other elements when an element with a greater flow height gets added.
 *
 * In plain DOM it nodes all position at the baseline of one consecutive chain of blocks.
 */
public class DocumentFlowRow {
    private final List<AreaNode> areas = new ArrayList<>();
    private final List<UIElement> elements = new ArrayList<>();
    /**
     * The biggest height of the elements in this row.
     */
    private int maxHeight;
    /**
     * The width of this row.
     */
    private int width;
    private int y;

    public Optional<UIElement> getLast() {
        return this.elements.isEmpty() ? Optional.empty() : Optional.of(this.elements.get(this.elements.size() - 1));
    }

    public int getMaxHeight() {
        return this.maxHeight;
    }

    public int getWidth() {
        return this.width;
    }

    /**
     * Global y coordinates of this row.
     * @return
     */
    public int getY() {
        return this.y;
    }

    public void reset() {
        this.maxHeight = 0;
        this.width = 0;
        this.y = 0;
        this.areas.clear();
        this.elements.clear();
    }

    /**
     * Adds the element to the chain and, if necessary, adjust it based on the current chain.
     * @param element
     */
    public void addElement(UIElement element) {
        AreaNode node = new AreaNode(element.flowArea);
        node.appendChild(element.contentBox);

        if (!this.areas.isEmpty()) {
            /*
             * Nothing needs to be done when the height is equal, as then all the previous elements
             * are already in the correct positions.
             */
            if (element.flowArea.getHeight() < this.maxHeight) {
                node.addY(this.maxHeight - element.getFlowArea().getHeight());
            } else if (element.getFlowArea().getHeight() > this.maxHeight) {
                this.recalculateRow(node);
                this.maxHeight = element.getFlowArea().getHeight();
                this.y = element.getFlowArea().getY();
            }
        } else {
            this.maxHeight = element.getFlowArea().getHeight();
            this.y = element.getFlowArea().getY();
        }

        this.areas.add(node);
        this.elements.add(element);
        this.width += element.getFlowArea().getWidth();
    }

    private void recalculateRow(AreaNode initiator) {
        for (AreaNode areaNode : this.areas) {
            if (areaNode == initiator) continue;

            int areaNodeMax = areaNode.area.getY() + areaNode.area.getHeight();
            int initiatorMax = initiator.area.getY() + initiator.area.getHeight();

            areaNode.addY(initiatorMax - areaNodeMax);
        }
    }

    /**
     * A linked chain of areas so changes to the root area will be propagated towards the end.
     *
     * The root element should always be the area used for the document flow.
     */
    public static class AreaNode {
        private AreaNode child;
        private Area area;

        public AreaNode(Area area) {
            this.area = area;
        }

        public void addX(int x) {
            this.area.addX(x);
            if (this.child != null) this.child.addX(x);
        }

        public void addY(int y) {
            this.area.addY(y);
            if (this.child != null) this.child.addY(y);
        }

        public void setX(int x) {
            this.area.setX(x);
            if (this.child != null) this.child.setX(x);
        }

        public void setY(int y) {
            this.area.setY(y);
            if (this.child != null) this.child.setY(y);
        }

        /**
         * Adds the given child to the very end of this linked chain.
         */
        public AreaNode appendChild(Area area) {
            if (this.child != null) {
                return this.child.appendChild(area);
            } else {
                return this.child = new AreaNode(area);
            }
        }
    }
}
