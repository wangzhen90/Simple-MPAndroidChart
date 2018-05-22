package com.wangzhen.simplechart;

import java.util.Random;

public class OuterClass {
    public static long OUTER_DATE = System.currentTimeMillis();
    public static int a = 1;
    static {
        System.out.println("外部类静态块加载时间：" + System.currentTimeMillis());
    }

    public OuterClass() {
        timeElapsed();
        System.out.println("外部类构造函数事件：" + System.currentTimeMillis());
    }

    static class InnerStaticClass {
        static {
            System.out.println("内部类静态块加载时间：" + System.currentTimeMillis());
        }
        public static long INNER_STATIC_DATE = System.currentTimeMillis();

    }

    class InnerClass {
        public long INNER_DATE = 0;
        public InnerClass() {
            timeElapsed();
            INNER_DATE = System.currentTimeMillis();
        }           
    }

    public static void Hello(){System.out.println("Hello");}

    public static void main(String[] args) {
        /**
         * 静态内部类只有在调用的时候才会被加载，然后加载其中的静态块和静态变量
         * 如果我们注释了下面这行，只会打印：
         *  外部类静态块加载时间：1526985550527
         *  外部类静态变量加载时间：1526985550527

         放开的话会打印：
         外部类静态块加载时间：1526985692850
         内部类静态块加载时间：1526985692859
         内部类静态变量加载时间：1526985692859
         外部类静态变量加载时间：1526985692850
         */
        System.out.println("内部类静态变量加载时间：" + InnerStaticClass.INNER_STATIC_DATE );
        System.out.println("外部类静态变量加载时间：" + OuterClass.OUTER_DATE );
    }

    //单纯的为了耗时而已
    private void timeElapsed() {
        for (int i = 0; i < 10000000; i++) {
            int a = new Random(100).nextInt(), b = new Random(100).nextInt();
            a = a + b;
        }
    }
}