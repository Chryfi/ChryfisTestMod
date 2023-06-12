package com.chryfi.test.client.gui;

public class TestStuff {

    public static UIElement createTestContainerColumn(float width) {
        UIElement column = new UIElement();
        UIPropertyBuilder.setup(column)
                .width(width)
                .height(1F)
                .paddingLeft(16)
                .paddingRight(16)
                .backgroundColor(0,0,0,0);

        return column;
    }

    public static UIElement createTestRow(UIElement... elements) {
        UIElement row = new UIElement();
        UIPropertyBuilder.setup(row)
                .width(1F)
                .height(128)
                .marginTop(10)
                .backgroundColor(0,0,0,0);

        float w = 1F / elements.length;
        for (UIElement element : elements) {
            UIPropertyBuilder.setup(element).height(1F);

            UIElement colContainer = TestStuff.createTestContainerColumn(w);
            colContainer.addChildren(element);
            row.addChildren(colContainer);
        }

        return row;
    }

    public static UIElement bigChungusTest() {
        UIElement wrapper = new UIViewport();
        UIPropertyBuilder.setup(wrapper)
                .width(1F)
                .height(1F)
                .paddingLeft(64);

        UIElement test1 = new UIElement();
        UIPropertyBuilder.setup(test1)
                .width(0.35F)
                .height(0.15F)
                .marginBottom(100)
                .marginTop(50)
                .marginLeft(0.1F);

        UIElement test2 = new UIElement();
        UIPropertyBuilder.setup(test2)
                .width(200)
                .height(0.15F)
                .marginLeft(20)
                .marginTop(20);

        UIElement test3 = new UIElement();
        UIPropertyBuilder.setup(test3)
                .width(300)
                .height(0.35F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test4 = new UIElement();
        UIPropertyBuilder.setup(test4)
                .width(250)
                .height(0.05F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test5 = new UIElement();
        UIPropertyBuilder.setup(test5)
                .width(300)
                .height(0.1F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test6 = new UIElement();
        UIPropertyBuilder.setup(test6)
                .width(300)
                .height(150)
                .marginLeft(50)
                .marginTop(40);

        UIElement test7 = new UIElement();
        UIPropertyBuilder.setup(test7)
                .widthAuto()
                .heightAuto()
                .marginTop(40)
                .paddingLeft(100)
                .paddingRight(100);

        test7.getTransformation().setWrap(false);

        UIElement test8 = new UIElement();
        UIPropertyBuilder.setup(test8)
                .width(900)
                .height(50);

        UIElement test9 = new UIElement();
        UIPropertyBuilder.setup(test9)
                .width(500)
                .height(125).heightAuto();

        UIElement test91 = new UIElement();
        UIPropertyBuilder.setup(test91)
                .width(200)
                .height(25)
                .marginTop(150)
                .marginLeft(25);
        test9.addChildren(test91);

        UIElement test10 = new UIElement();
        UIPropertyBuilder.setup(test10)
                .width(550)
                .height(150)
                .marginTop(200);

        test7.addChildren(test9, test10, test8, test6);

        wrapper.addChildren(test7);

        return wrapper;
    }
}
