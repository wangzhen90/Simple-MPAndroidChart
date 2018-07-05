package com.wangzhen.simplechart;

import java.util.NoSuchElementException;

public class DoubleNodeList<E> {
    //transient只能修饰成员变量，代表该成员不需要序列化
    transient Node<E> first;
    transient Node<E> last;
    int size = 0;

    //Node类
    public static final class Node<ET> {
        ET item;
        Node previous, next;

        public Node(ET o, Node<ET> p, Node<ET> n) {
            this.item = o;
            this.previous = p;
            this.next = n;
        }

    }

    public DoubleNodeList() {

    }

    public void addFirst(E object) {
        //1.获取当前的first结点
        Node<E> oldFirst = first;
        //2.创建一个新的结点，并将新结点的next置为oldFirst
        Node<E> newNode = new Node<E>(object, null, oldFirst);
        //3.当前的first指向新结点
        first = newNode;
        //4.判断链表为null的情况
        if (first == null) {
            //链表为null，first = last；
            last = newNode;
        } else {
            //5.当前first的next指向上一个first结点
            first.next = oldFirst;
        }

        size++;

    }

    public void addLast(E object) {
        //1.获取当前的last结点
        Node<E> oldLast = last;
        //2.创建一个新的结点，并将新结点的previous置为oldLast
        Node<E> newNode = new Node<E>(object, oldLast, null);
        //3.当前的last指向新结点
        last = newNode;

        //4.判断链表为null的情况
        if (oldLast == null) {
            //链表为null，first = last；
            first = last;
        } else {
            //5.上一个last结点的next指向新结点
            oldLast.next = newNode;
        }
        size++;
    }


    public E removeFirst() {
        Node<E> currentFirst = first;
        if (currentFirst == null) {
            throw new NoSuchElementException();
        } else {
            //1.取出要返回的currentFirst中的数据
            E item = currentFirst.item;
            //2.更换first
            Node<E> currFirstNext = currentFirst.next;
            //currentFirst 已经是个游离对象了，内部成员置为null
            currentFirst.item = null;
            currentFirst.next = null;

            first = currFirstNext;
            //removeFirst后为null了，将last也置为null
            if (currFirstNext == null) {
                last = null;
            } else {
                //currFirstNext 已经是first了，previous应该是null
                currFirstNext.previous = null;
            }

            --size;
            return item;
        }
    }

    public E removeLast() {

        Node<E> currentLast = last;

        if (last == null) {
            throw new NoSuchElementException();
        } else {
            E item = currentLast.item;

            Node currLastPre = last.previous;
            last = currLastPre;

            currentLast.item = null;
            currentLast.previous = null;

            if (currLastPre == null) {
                first = null;
            } else {
                last.next = null;
            }

            --size;
            return item;
        }
    }


    public E get(int location) {

        Node<E> result = getNode(location);
        if (result != null) {
            return result.item;
        } else {
            return null;
        }
    }

    public Node getNode(int location) {
        Node<E> result = null;
        if (location >= 0 && location < size) {
            //如果位于左区间就正向遍历，否则逆向遍历
            if (location < (size / 2)) {
                result = first;
                for (int i = 0; i <= location; i++) {
                    result = result.next;
                }
            } else {
                result = last;
                for (int i = size; i > location; i--) {
                    result = result.previous;
                }
            }
            return result;
        }
        throw new IndexOutOfBoundsException();
    }


    private void addBefore(E object, Node<E> currLocationNode) {

        Node<E> preNode = currLocationNode.previous;
        Node<E> newNode = new Node<E>(object, preNode, currLocationNode);

        currLocationNode.previous = newNode;

        if (preNode == null) {
            first = newNode;
        } else {
            preNode.next = newNode;
        }
        size++;
    }

    public void addBefore(E object, int location) {

        if (location == size) {
            addLast(object);
        } else {
            this.addBefore(object, getNode(location));
        }
    }

    /**
     * 删除某个特定结点
     * @param o
     * @return
     */
    public boolean remove(E o) {
        if (o == null) {
            //遍历比对，找出相应的节点，断开与链表的连接
            for (Node<E> x = first; x != null; x = x.next) {
                if (x.item == null) {
                    unlink(x);
                    return true;
                }
            }
        } else {
            //遍历比对，找出相应的节点，断开与链表的连接
            for (Node<E> x = first; x != null; x = x.next) {
                if (o.equals(x.item)) {
                    unlink(x);
                    return true;
                }
            }
        }

        return false;

    }

    /**
     * 断开结点node
     *
     * @param node
     * @return
     */
    E unlink(Node<E> node) {

        E item = node.item;

        Node<E> n = node.next;
        Node<E> pre = node.previous;
        //如果是头结点
        if (pre == null) {
            first = n;
        } else {
            pre.next = n;
            node.previous = null;
        }


        if (n == null) {
            last = node.previous;
        } else {
            n.previous = pre;
            node.next = null;
        }

        node.item = null;

        size--;

        return item;

    }


    @Override
    public String toString() {

        StringBuffer stringBuffer = new StringBuffer();
        int location = 1;
        for (Node<E> x = first; x != null; x = x.next) {
            if (x != null) {
                stringBuffer.append("location" + location + ":" + x.item);
                stringBuffer.append("\n");
                location++;
            }

        }

        return stringBuffer.toString();
    }
}