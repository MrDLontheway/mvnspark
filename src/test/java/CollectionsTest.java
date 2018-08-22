import org.junit.Test;

import java.util.*;

public class CollectionsTest {

    @Test
    public void linkedlist(){
        /**
         * LinkedList 是线程不安全的，允许元素为null的双向链表。  没有实现RandomAccess所以其以下标，随机访问元素速度较慢。
         * 因其底层数据结构是链表，所以可想而知，它的增删只需要移动指针即可，故时间效率较高。不需要批量扩容，也不需要预留空间，所以空间效率比ArrayList高。
         缺点就是需要随机访问元素时，时间效率很低，虽然底层在根据下标查询Node的时候，会根据index判断目标Node在前半段还是后半段，然后决定是顺序还是逆序查询，以提升时间效率。不过随着n的增大，总体时间效率依然很低。
         当每次增、删时，都会修改modCount。
         */
        LinkedList l = new LinkedList();
        l.add(1);
        l.add(2);
        l.push(3);
        l.add(2,4);

        System.out.println(l);
    }

    @Test
    public void arraylist(){
        ArrayList arr = new ArrayList();
        arr.add(1,1);
        arr.get(0);
    }

    @Test
    public void map(){
        //hashmap 底层是entry的一个数组 也就是entry单向链表的实现 每个entry 存储 k v hash值
        //因为插入的位置是 hash值与table长度取模 所以无序
        HashMap hm = new HashMap();
        hm.get(1);
        Stu s1 = new Stu("daile",1);
        Stu s2 = new Stu("daile",1);
        Stu s3 = new Stu("daile",1);
        System.out.println(s1.hashCode());
        System.out.println(s2.hashCode());
        System.out.println(s3.hashCode());
        hm.put(s1,1);
        hm.put(s2,1);

        Object r1 = hm.get(s1);
        Object r2 = hm.get(s2);
        //因为linedhashmap 内置是一个 entry 的双向链表所以是有序的
        LinkedHashMap a = new LinkedHashMap();
        a.put("a",1);
        a.get(1);
        //
        TreeMap treeMap  = new TreeMap();
        treeMap.put(1,1);
        System.out.println(3&10);
        hm.put("1",1);
    }


    @Test
    public void set(){
        /**
         * 底层是hashmap的实现  因为hashmap的key唯一特点 所以set是不重复的集合 为了符合
         * 为了符合map k v 的特点 add 将element 加入key value添加一个任意object假值
         * 因为hashmap 无序的特性所以 hashset也是无序的
         */
        HashSet<Stu> s = new HashSet();
        /**
         * LinkedHashSet继承自HashSet，源码更少、更简单，唯一的区别是LinkedHashSet内部使用的是LinkHashMap。'
         * 这样做的意义或者好处就是LinkedHashSet中的元素顺序是可以保证的，也就是说遍历序和插入序是一致的。
         */
        LinkedHashSet s1 = new LinkedHashSet();

        TreeSet treeSet = new TreeSet();
        treeSet.add(1);
        s1.add(1);
    }

    /**
     * 红黑树自适应调整函数
     */
    /** From CLR */
/*    private void fixAfterInsertion(TreeMap.Entry<K,V> x) {
        x.color = RED;  //新加入节点必须为红色

        while (x != null && x != root && x.parent.color == RED) {
            if (parentOf(x) == leftOf(parentOf(parentOf(x)))) { //若它的父节点为左节点
                TreeMap.Entry<K,V> y = rightOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {  //若它叔父节点为红色 将它叔父全部设置为黑色
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {// 若它的叔父不为
                    if (x == rightOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateLeft(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateRight(parentOf(parentOf(x)));
                }
            } else {
                TreeMap.Entry<K,V> y = leftOf(parentOf(parentOf(x)));
                if (colorOf(y) == RED) {
                    setColor(parentOf(x), BLACK);
                    setColor(y, BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    x = parentOf(parentOf(x));
                } else {
                    if (x == leftOf(parentOf(x))) {
                        x = parentOf(x);
                        rotateRight(x);
                    }
                    setColor(parentOf(x), BLACK);
                    setColor(parentOf(parentOf(x)), RED);
                    rotateLeft(parentOf(parentOf(x)));
                }
            }
        }
        root.color = BLACK;
    }*/

}
