package com.peter.study.audio_video;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    public static void writePCM(byte[] frme) {
        try {
            String filePath = Environment.getExternalStorageDirectory() + "/_test.pcm";
            File file = new File(filePath);
            FileOutputStream fos = null;
            if (!file.exists()) {
                file.createNewFile();//如果文件不存在，就创建该文件
                fos = new FileOutputStream(file);//首次写入获取
            } else {
                //如果文件已存在，那么就在文件末尾追加写入
                fos = new FileOutputStream(file, true);//这里构造方法多了一个参数true,表示在文件末尾追加写入
            }
            fos.write(frme);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] arr) {
        Child.setAge(10);
        Child child = new Child();
        Child_2 child_2 = new Child_2();
        Child_2 child_21 = new Child_2();

        System.out.println("child = [" + Child.getAge() + "]");
        System.out.println("child_2 = [" + Child_2.getAge() + "]");
    }
    public static class Child extends Parent {

        static {
            System.out.println("childC");
        }

        public Child(){
            System.out.println("childD");
        }
    }
    public static class Child_2 extends Parent {
        static {
            System.out.println("childC_1");
        }
        public Child_2(){
            System.out.println("childD_2");
        }
    }
    public static class Parent {
        static {
            System.out.println("parentA");
        }
        private static int mAge;
        public Parent(){
            System.out.println("parentB");
        }
        public static int getAge(){
            return mAge;
        }
        public static void setAge(int age){
            mAge = age;
        }
    }
}
