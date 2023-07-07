package com.chryfi.test.client.gui;

public class TestStuff {

    public static UIElement createTestContainerColumn(float width) {
        return new UIElement()
                .width(width)
                .height(1F)
                .paddingLeft(16)
                .paddingRight(16)
                .backgroundColor(0,0,0,0);
    }

    public static UIElement createTestRow(UIElement... elements) {
        UIElement row = new UIElement()
                .width(1F)
                .height(128)
                .marginTop(10)
                .backgroundColor(0,0,0,0);

        float w = 1F / elements.length;
        for (UIElement element : elements) {
            element.height(1F);

            UIElement colContainer = TestStuff.createTestContainerColumn(w);
            colContainer.addChildren(element);
            row.addChildren(colContainer);
        }

        return row;
    }

    public static UIElement bigChungusTest() {
        UIElement wrapper = new UIViewport()
                .width(1F)
                .height(1F)
                .paddingLeft(84).paddingTop(10).paddingBottom(0.2F).paddingRight(50);

        UIElement test1 = new UIElement()
                .width(0.35F)
                .height(0.15F)
                .marginBottom(100)
                .marginTop(50)
                .marginLeft(0.1F);

        UIElement test2 = new UIElement()
                .width(200)
                .height(0.15F)
                .marginLeft(20)
                .marginTop(20);

        UIElement test3 = new UIElement()
                .width(300)
                .height(0.35F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test4 = new UIElement()
                .width(250)
                .height(0.05F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test5 = new UIElement()
                .width(300)
                .height(0.1F)
                .marginLeft(50)
                .marginTop(40);

        UIElement test6 = new UIElement()
                .width(300)
                .height(150)
                .marginLeft(50)
                .marginTop(40);

        //TODO something about margin right - it doesn't influence the width here with width auto. Check HTML test, do we even need to care?
        UIElement test7 = new UIElement()
                .width(1F)
                .heightAuto()
                .marginTop(40)
                .marginRight(50).marginLeft(100)
                .paddingLeft(100)
                .paddingRight(100);

        //test7.getTransformation().setWrap(false);

        UIElement test8 = new UIElement()
                .width(900)
                .height(50);

        UIElement test9 = new UIElement()
                .width(500)
                .height(125).heightAuto();

        UIElement test91 = new UIElement()
                .width(200)
                .height(25)
                .marginTop(150)
                .marginLeft(25);
        test9.addChildren(test91);

        UIElement test10 = new UIElement()
                .width(550)
                .height(150)
                .marginTop(200);

        test7.addChildren(test9, test10, test8, test6);

        wrapper.addChildren(test7);

        return wrapper;
    }
}
