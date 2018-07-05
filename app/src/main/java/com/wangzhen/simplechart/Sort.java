package com.wangzhen.simplechart;

/**
 * Created by wangzhen on 2018/6/11.
 */

public class Sort {

    //1.选择排序
    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (less(arr[j], arr[min])) {
                    min = j;
                }
            }
            exch(arr, i, min);
        }
    }

    //2.冒泡排序
    public static void bubbleSort(int[] arr){
        for(int i = 0; i < arr.length; i++){
           for(int j = i+1; j < arr.length; j++){
               if(less(arr[j],arr[i])){
                   exch(arr,i,j);
               }
           }

        }
    }

    //3.插入排序
    public static void insertSort(int[] arr){

        int len = arr.length;

        for(int i = 1; i < len; i++){

            for(int j = i; j > 0 && less(arr[j],arr[j-1]); j--){
                exch(arr,j,j-1);
            }
        }
    }




    private static boolean less(int a, int b) {
        return a < b;
    }

    private static void exch(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void show(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

    }
}
