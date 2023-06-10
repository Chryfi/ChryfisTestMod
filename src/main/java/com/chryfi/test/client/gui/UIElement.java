package com.chryfi.test.client.gui;

import com.chryfi.test.utils.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * When overriding internal children storage behaviour, you need to override following methods:
 * {@link #getChildren()}, {@link #addChildren(UIElement...)} and {@link #removeChild(UIElement)}.
 */
@OnlyIn(Dist.CLIENT)
public class UIElement extends GuiComponent implements GuiEventListener {
    /**
     * For debugging purpose
     */
    public int[] margin = new int[4];
    public int[] padding = new int[4];



    /**
     * The entire area occupied by this UIElement. This includes the content area and margins.
     * This is used for document flow.
     */
    protected final Area flowArea = new Area();
    /**
     * The area that the background and border render in.
     * With relative positioning, this area can be outside the document flow area {@link #flowArea}
     */
    protected final Area contentArea = new Area();
    /**
     * This is the area that is inside the contentArea and affected by padding.
     * This is where the content/children render in.
     */
    protected final Area innerArea = new Area();
    private UITransformation transformation = new UITransformation(this);
    protected Color background;
    private final List<UIElement> children = new ArrayList<>();
    protected UIElement parent;

    public UIElement() {
        this.background = new Color((float)Math.random(),
                (float)Math.random(),
                (float)Math.random());

        UIPropertyBuilder.setup(this).width(1F);
    }

    /*
     * STYLE ATTRIBUTES
     * Methods should return this to allow easy method chaining.
     */
    public UIElement backgroundColor(float r, float g, float b, float a) {
        this.background = new Color(r, g, b, a);
        return this;
    }

    public UIElement backgroundColor(float r, float g, float b) {
        return this.backgroundColor(r, g, b, 1F);
    }

    /*
     * STYLES END
     */


    public void addChildren(UIElement... elements) {
        for (UIElement element : elements) {
            element.remove();
            element.parent = this;
        }

        this.children.addAll(Arrays.asList(elements));
    }

    /**
     * @return a list of the children of this element. Changes to this list will NOT be reflected in the internal list.
     *          See methods that modify the child/parent relationship
     *          like {@link #remove()} or {@link #removeChild(UIElement)} for alternatives
     */
    public List<UIElement> getChildren() {
        return new ArrayList<>(this.children);
    }

    public Optional<UIElement> getParent() {
        return Optional.ofNullable(this.parent);
    }

    /**
     * @return The transformation element responsible for calculating positions and dimensions
     */
    public UITransformation getTransformation() {
        return this.transformation;
    }

    public void resetAreas() {
        this.flowArea.reset();
        this.contentArea.reset();
        this.innerArea.reset();
    }

    public Area getFlowArea() {
        return this.flowArea;
    }

    public Area getContentArea() {
        return this.contentArea;
    }

    public Area getInnerArea() {
        return this.innerArea;
    }

    /**
     * Useful for performing operations that should propagate through the areas.
     * @return
     */
    public DocumentFlowRow.AreaNode getAreaChain() {
        final DocumentFlowRow.AreaNode root = new DocumentFlowRow.AreaNode(this.flowArea);
        final DocumentFlowRow.AreaNode contentNode = root.appendChild(this.contentArea);
        final DocumentFlowRow.AreaNode innerNode = contentNode.appendChild(this.innerArea);

        return root;
    }

    /**
     * Removes this element from the parent.
     */
    public void remove() {
        if (this.parent != null) {
            this.parent.removeChild(this);
            this.parent = null;
        }
    }

    public void removeChild(UIElement child) {
        this.children.remove(child);
        child.parent = null;
    }

    /**
     * Calculates and caches the positions, widths and heights.
     * This should be called before the first render and when resizing.
     */
    public void resize() {
        this.getTransformation().resize();
    }

    /**
     * This is called after resizing this element
     * and finishing the calculation of the areas.
     */
    public void onAreasCalculated() { }

    /**
     * This gets called when the UIElement is closed.
     */
    public final void onClose() {
        this._onClose();

        for (UIElement element : this.getChildren()) {
            element.onClose();
        }
    }

    /**
     * This method is supposed to be overridden to define logic to happen when the UI is closed.
     */
    protected void _onClose() { }

    public void render(GuiContext context) {
        if (UIScreen.debug) {
            GuiComponent.fill(new PoseStack(), this.flowArea.getX(), this.flowArea.getY(),
                    this.flowArea.getX() + this.flowArea.getWidth(), this.flowArea.getY() + this.flowArea.getHeight(),
                    new Color(0F, 0.25F, 1F, 0.25F).getRGBAColor());
        }

        if (this.background != null) {
            /*GuiComponent.fill(new PoseStack(), this.contentArea.getX(), this.contentArea.getY(),
                    this.contentArea.getX() + this.contentArea.getWidth(), this.contentArea.getY() + this.contentArea.getHeight(),
                    this.background.getRGBAColor());*/

            this.vLine(new PoseStack(), this.contentArea.getX(),
                    this.contentArea.getY() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight(),
                    new Color(0,0,0, 1).getRGBAColor());
            this.vLine(new PoseStack(), this.contentArea.getX() + 1,
                    this.contentArea.getY() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight(),
                    new Color(0,0,0, 1).getRGBAColor());

            this.vLine(new PoseStack(), this.contentArea.getX() + this.contentArea.getWidth() - 1,
                    this.contentArea.getY() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight(),
                    new Color(0,0,0, 1).getRGBAColor());
            this.vLine(new PoseStack(), this.contentArea.getX() + this.contentArea.getWidth() - 2,
                    this.contentArea.getY() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight(),
                    new Color(0,0,0, 1).getRGBAColor());

            this.hLine(new PoseStack(), this.contentArea.getX(),
                    this.contentArea.getX() + this.contentArea.getWidth() - 1,
                    this.contentArea.getY(),
                    new Color(0,0,0, 1).getRGBAColor());
            this.hLine(new PoseStack(), this.contentArea.getX(),
                    this.contentArea.getX() + this.contentArea.getWidth() - 1,
                    this.contentArea.getY() + 1,
                    new Color(0,0,0, 1).getRGBAColor());

            this.hLine(new PoseStack(), this.contentArea.getX(),
                    this.contentArea.getX() + this.contentArea.getWidth() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight() - 1,
                    new Color(0,0,0, 1).getRGBAColor());
            this.hLine(new PoseStack(), this.contentArea.getX(),
                    this.contentArea.getX() + this.contentArea.getWidth() - 1,
                    this.contentArea.getY() + this.contentArea.getHeight() - 2,
                    new Color(0,0,0, 1).getRGBAColor());
        }

        if (UIScreen.debug) {
            this.drawMargins();
            this.drawPaddings();
        }

        for (UIElement child : this.getChildren()) {
            child.render(context);
        }
    }

    protected void drawMargins() {
        GuiComponent.fill(new PoseStack(), this.contentArea.getX(), this.contentArea.getY() - this.margin[0],
                this.contentArea.getX() + this.contentArea.getWidth(), this.contentArea.getY(),
                new Color(1F, 0.5F, 0F, 0.5F).getRGBAColor());

        GuiComponent.fill(new PoseStack(), this.contentArea.getX() - this.margin[3], this.contentArea.getY(),
                this.contentArea.getX(), this.contentArea.getY() + this.contentArea.getHeight(),
                new Color(1F, 0.5F, 0F, 0.5F).getRGBAColor());

        GuiComponent.fill(new PoseStack(), this.contentArea.getX() + this.contentArea.getWidth(), this.contentArea.getY(),
                this.contentArea.getX() + this.contentArea.getWidth() + this.margin[1], this.contentArea.getY() + this.contentArea.getHeight(),
                new Color(1F, 0.5F, 0F, 0.5F).getRGBAColor());

        GuiComponent.fill(new PoseStack(), this.contentArea.getX(), this.contentArea.getY() + this.contentArea.getHeight(),
                this.contentArea.getX() + this.contentArea.getWidth(), this.contentArea.getY() + this.contentArea.getHeight() + margin[2],
                new Color(1F, 0.5F, 0F, 0.5F).getRGBAColor());
    }

    protected void drawPaddings() {
        GuiComponent.fill(new PoseStack(), this.innerArea.getX(), this.contentArea.getY(),
                this.innerArea.getX() + this.innerArea.getWidth(), this.contentArea.getY() + this.padding[0],
                new Color(0F, 1F, 0F, 0.5F).getRGBAColor());
        /* left */
        GuiComponent.fill(new PoseStack(), this.contentArea.getX(), this.innerArea.getY(),
                this.innerArea.getX(), this.innerArea.getY() + this.innerArea.getHeight(),
                new Color(0F, 1F, 0F, 0.5F).getRGBAColor());
        /* right */
        GuiComponent.fill(new PoseStack(), this.innerArea.getX() + this.innerArea.getWidth(), this.innerArea.getY(),
                this.innerArea.getX() + this.innerArea.getWidth() + this.padding[1],
                this.innerArea.getY() + this.innerArea.getHeight(),
                new Color(0F, 1F, 0F, 0.5F).getRGBAColor());

        GuiComponent.fill(new PoseStack(), this.innerArea.getX(), this.innerArea.getY() + this.innerArea.getHeight(),
                this.innerArea.getX() + this.innerArea.getWidth(),
                this.innerArea.getY() + this.innerArea.getHeight() + this.padding[2],
                new Color(0F, 1F, 0F, 0.5F).getRGBAColor());
    }


    /*
     * Mouse event capturing template pattern
     */

    @Override
    public final boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        List<UIElement> children = this.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement element = children.get(i);
            if (element.mouseClicked(mouseX, mouseY, mouseKey)) return true;
        }

        return this.mouseClick(mouseX, mouseY, mouseKey);
    }

    public boolean mouseClick(double mouseX, double mouseY, int mouseKey) {
        return false;
    }

    @Override
    public final boolean mouseReleased(double mouseX, double mouseY, int mouseKey) {
        List<UIElement> children = this.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement element = children.get(i);
            if (element.mouseReleased(mouseX, mouseY, mouseKey)) return true;
        }

        return this.mouseRelease(mouseX, mouseY, mouseKey);
    }

    public boolean mouseRelease(double mouseX, double mouseY, int mouseKey) {
        return false;
    }

    @Override
    public final boolean mouseDragged(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        List<UIElement> children = this.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement element = children.get(i);
            if (element.mouseDragged(mouseX, mouseY, mouseKey, dragX, dragY)) return true;
        }

        return this.mouseDrag(mouseX, mouseY, mouseKey, dragX, dragY);
    }

    public boolean mouseDrag(double mouseX, double mouseY, int mouseKey, double dragX, double dragY) {
        return false;
    }

    @Override
    public final boolean isMouseOver(double mouseX, double mouseY) {
        List<UIElement> children = this.getChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement element = children.get(i);
            if (element.isMouseOver(mouseX, mouseY)) return true;
        }

        return this.isMouseOverCurrent(mouseX, mouseY);
    }

    public boolean isMouseOverCurrent(double mouseX, double mouseY) {
        return this.contentArea.isInside(mouseX, mouseY);
    }
}
