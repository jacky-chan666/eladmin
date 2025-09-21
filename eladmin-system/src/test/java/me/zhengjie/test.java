package me.zhengjie;

public class test {
    public static void main(String[] args) {
        interfaceAImpl interfaceA = new interfaceAImpl();
        testOne(interfaceA);
        testOne(new interfaceBImpl());

    }

    public static void testOne(baseInterface baseInterface){

        System.out.println(baseInterface.get());
    }
}
