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
        UIElement wrapper = new UIElement();
        UIPropertyBuilder.setup(wrapper)
                .width(1F)
                .height(1F);

        UIElement test1 = new UIElement();
        UIPropertyBuilder.setup(test1)
                .width(0.35F)
                .height(0.15F)
                .marginBottom(100)
                .marginTop(50)
                .marginLeft(0.25F);

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
                .height(0.2F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test7 = new UIElement();
        UIPropertyBuilder.setup(test7)
                .width(700)
                .height(0.35F)
                .marginLeft(50)
                .marginTop(40)
                .paddingLeft(100)
                .paddingRight(100);

        UIElement test8 = new UIElement();
        UIPropertyBuilder.setup(test8)
                .width(700)
                .height(0.1F);

        UIElement test9 = new UIElement();
        UIPropertyBuilder.setup(test9)
                .width(600)
                .height(0.1F);

        test7.addChildren(test9);

        wrapper.addChildren(test1, test2, test3, test4, test5, test6, test7, test8);

        return wrapper;
    }
}
