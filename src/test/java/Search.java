import org.junit.Test;

import java.util.*;

public class Search {
    public Integer[] data = new Integer[]{1,2,3,4,5,6,7,8,9,10,77,99};
    public Integer[] data2 = new Integer[]{8,4,2,3,9,6,7,5,5,10,9,12,19,16,15};
    public Integer[] data3 = new Integer[]{2,1,4,2,2,2,2,1,3};
    public Random r = new Random();

    //@Test
    public void maxProfit() throws Exception {
        int[] nums = new int[]{-1,0,1,2,-1,-4};
        int k = 3;
        String s = "tree";
        char[] chars = s.toCharArray();
        Map map = new HashMap();
        for(char i:chars){
            int o = map.get(i)==null?1: (int) map.get(i);
            o++;
            map.put(i,o);
        }
        map.values().toArray();
        Arrays.sort(nums);
        System.out.println(String.valueOf(1));
    }

    @Test
    public void quicksortSimple(){
        Integer[] randBigData = getRandBigData();
        //quickSort(randBigData,0,randBigData.length-1);
        quickSortComparable(randBigData,0,randBigData.length-1);

//        for (Integer row=0;row<randBigData.length;row++){
//            System.out.print(row+",");
//        }
    }

    @Test
    public void quicksortRand(){
        Integer[] randBigData2 = (Integer[])getRandBigData();
        quickSortByRand(randBigData2,0,randBigData2.length-1);
    }

    @Test
    public void quicksortMiddle(){
        Integer[] randBigData = getRandBigData();
        quickSortByMid(randBigData,0,randBigData.length-1);
    }

    @Test
    public void DualPivotQuicksort(){
        Integer[] randBigData = getRandBigData();
        Arrays.sort(randBigData);
    }

    public int binarysearch(Integer[] data,int begin,int last,int value){
        while (begin<=last){
            int mid = (last+begin)/2;
            if(data[mid]>value){
                last = mid-1;
            }
            if(data[mid]<value){
                begin = mid+1;
            }
            if(data[mid]==value)
                return mid;
        }
        return -1;
    }

    public void quickSort(Integer[] arr, int low, int high) {
        // low,high 为每次处理数组时的首、尾元素索引

        //当low==high是表示该序列只有一个元素，不必排序了
        if (low >= high) {
            return;
        }
        // 选出哨兵元素和基准元素。这里左边的哨兵元素为第2个元素（第一个为基准元素）
        //int i = low+1, j = high,base = arr[low];
        int i = low, j = high, base = arr[low];
        while (i < j) {
            //2,1,4,1,3;
            //右边哨兵从后向前找
            while (arr[j] >= base && i < j) {
                j--;
            }
            //左边哨兵从前向后找
            while (arr[i] <= base && i < j) {
                i++;
            }
            //swap(arr,i,j);  //交换元素
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        int tmp = arr[low];
        arr[low] = arr[j];
        arr[j] = tmp;
        //swap(arr,low,j);  //基准元素与右哨兵交换
        //递归调用，排序左子集合和右子集合
        quickSort(arr,low,i-1);
        quickSort(arr,i+1,high);
    }

    public void quickSortByRand(Integer[] arr, int low, int high) {
        // low,high 为每次处理数组时的首、尾元素索引

        //当low==high是表示该序列只有一个元素，不必排序了
        if (low >= high) {
            return;
        }
        // 选出哨兵元素和基准元素。这里左边的哨兵元素为第2个元素（第一个为基准元素）
        //int i = low+1, j = high,base = arr[low];
        int rand = r.nextInt(high) % (high - low + 1) + low;
        swap(arr,rand,low);
        int i = low, j = high, base = arr[low];
        while (i < j) {
            //2,1,4,1,3;
            //右边哨兵从后向前找
            while (arr[j] >= base && i < j) {
                j--;
            }
            //左边哨兵从前向后找
            while (arr[i] <= base && i < j) {
                i++;
            }
            //swap(arr,i,j);  //交换元素
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        int tmp = arr[low];
        arr[low] = arr[j];
        arr[j] = tmp;
        //swap(arr,low,j);  //基准元素与右哨兵交换
        //递归调用，排序左子集合和右子集合
        quickSortByRand(arr,low,j-1);
        quickSortByRand(arr,j+1,high);
    }

    public void quickSortByMid(Integer[] arr, int low, int high) {
        // low,high 为每次处理数组时的首、尾元素索引

        //当low==high是表示该序列只有一个元素，不必排序了
        if (low >= high) {
            return;
        }
        // 选出哨兵元素和基准元素。这里左边的哨兵元素为第2个元素（第一个为基准元素）
        //int i = low+1, j = high,base = arr[low];
        dealPivot(arr,0,data.length-1);
        int i = low, j = high, base = arr[low];
        while (i < j) {
            //2,1,4,1,3;
            //右边哨兵从后向前找
            while (arr[j] >= base && i < j) {
                j--;
            }
            //左边哨兵从前向后找
            while (arr[i] <= base && i < j) {
                i++;
            }
            //swap(arr,i,j);  //交换元素
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        int tmp = arr[low];
        arr[low] = arr[j];
        arr[j] = tmp;
        //swap(arr,low,j);  //基准元素与右哨兵交换
        //递归调用，排序左子集合和右子集合
        quickSortByRand(arr,low,j-1);
        quickSortByRand(arr,j+1,high);
    }

    public void swap(Object[]arr,int i,int j){
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public void mybinarysearch(Integer[] arr,int low,int hight){
        if (low >= hight) {
            return;
        }
        int i = low+1;int j = hight;int base = arr[low];
        while(i<j){
            while (arr[j]>=base&&i<j){
                j--;
            }
            while (arr[i]<=base&&i<j){
                i++;
            }
            swap(arr,i,j);
        }
        swap(arr,low,j);
        mybinarysearch(arr,low,j-1);
        mybinarysearch(arr,j+1,hight);
    }

    public Integer[] getRandBigData(){
        List<Integer> l = new ArrayList<>();
        for(int i=0;i<500;i++){
            l.add(r.nextInt(1000000));
        }
        return l.toArray(new Integer[0]);
    }

    public int getMid(Integer[] a) {
        int left = 0;
        int right = a.length - 1;
        int mid = a.length / 2;
        if((a[left]>a[right]||a[left]>a[mid])&&(a[left]<a[right]||a[left]<a[mid]))
            return left;
        int max = ((max=(left>right)?left:right)>mid?max:mid);
        return max;
    }

    public void dealPivot(Integer[] arr, int left, int right) {
        int mid = (left + right) / 2;
        if (arr[left] > arr[mid]) {
            swap(arr, left, mid);
        }
        if (arr[left] > arr[right]) {
            swap(arr, left, right);
        }
        if (arr[right] < arr[mid]) {
            swap(arr, right, mid);
        }
        swap(arr, left, mid);
    }

    private <AnyType extends Comparable<? super AnyType>> void quickSortComparable(AnyType[] arr, int low, int high) {
        // low,high 为每次处理数组时的首、尾元素索引

        //当low==high是表示该序列只有一个元素，不必排序了
        if (low >= high) {
            return;
        }
        // 选出哨兵元素和基准元素。这里左边的哨兵元素为第2个元素（第一个为基准元素）
        //int i = low+1, j = high,base = arr[low];
        int i = low, j = high;
        AnyType base = arr[low];
        while (i < j) {
            //2,1,4,1,3;
            //右边哨兵从后向前找
            while (arr[j].compareTo(base)>=0 && i < j) {
                j--;
            }
            //左边哨兵从前向后找
            while (arr[i].compareTo(base)<=0 && i < j) {
                i++;
            }
            swap(arr,i,j);  //交换元素
        }
        swap(arr,low,j);
        //swap(arr,low,j);  //基准元素与右哨兵交换
        //递归调用，排序左子集合和右子集合
        quickSortComparable(arr,low,i-1);
        quickSortComparable(arr,i+1,high);
    }

    @Test
    public void tst1(){
        for (int i=0;i<1000000;i++){
            int a = 8;
            int b = a/8;
        }
    }

    @Test
    public void tst2(){
//        for (int i=0;i<1000000;i++){
//            int a = 8;
//            int b = a >> 3;
//        }

        int left = 0;
        int right = 8;

        System.out.println((left+right)>>>1);
        System.out.println((left+right)/2);
    }
}
