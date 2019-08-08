/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fsproj;

import static java.awt.SystemColor.menu;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class records {

    static String CustomerName, destination, CustomerID, phone, keyf;
}

class node {

    int[] a = new int[5];
    int size;
    node[] next = new node[4];
    node parent;

    node() {
        for (int i = 0; i < 4; i++) {
            next[i] = null;
        }
        parent = null;
        size = 0;
    }
}

class btree {

    node root = new node();
    File fle = new File("display.txt");

    public node findLeaf(int key, int level) {
        node ptr = root;
        node prevptr = null;
        level = 0;
        int i;
        while (ptr != null) {
            i = 0;
            level++;
            while (i < ptr.size - 1 && key > ptr.a[i]) {
                i++;
            }
            prevptr = ptr;
            ptr = ptr.next[i];
        }
        return prevptr;
    }

    public void updateKey(node parent, node child, int newkey) {
        if (parent == null) {
            return;
        }
        if (parent.size == 0) {
            return;
        }
        int oldkey = child.a[child.size - 2];

        for (int i = 0; i < parent.size; i++) {
            if (parent.a[i] == oldkey) {
                parent.a[i] = newkey;
                parent.next[i] = child;
            }
        }
    }

    public void search(int key) {
        if (root == null) {

            System.out.print("The tree Does not exist");
            System.out.print("\n");
            return;
        }
        int level = 0;
        node leaf = findLeaf(key, level);
        int flg = 0;
        for (int i = 0; i < leaf.size; i++) {
            if (leaf.a[i] == key) {
                flg = 1;
                System.out.print("The Order ID ");
                System.out.print(key);
                System.out.print(" Exists in the B-Tree at the level ");
                System.out.print(level);
                System.out.print("\n");
            }
        }
        if (flg == 0) {
            System.out.print("The ID Searched for was not found");
            System.out.print("\n");
        }
    }

    public void insert(int key) throws IOException {
        FileWriter fw = new FileWriter("display.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        if (root == null) {
            root = new node();
            root.a[root.size] = key;
            root.size++;
            return;
        }
        int level = 0;
        node leaf = findLeaf(key, level);
        int i;
        for (i = 0; i <= leaf.size; i++) {
            if (leaf.a[i] == key) {
                System.out.print("The Record to be inserted already exists");
                System.out.print("\n");
                return;
            }
        }
        promote(leaf, key, null);
        System.out.print("---------------\n");
        fw.write("---------------\n");
        //bw.newLine();
        traverse(root);
        fw.write("---------------\n");
        //bw.newLine();
        System.out.print("----------------\n");
        //bw.close();
        fw.close();
    }

    void insertIntoNode(node n, int key, node address) {
        int i;
        if (n == null) {
            return;
        }
        for (i = 0; i < n.size; i++) {
            if (n.a[i] == key) {
                return;
            }
        }
        i = n.size - 1;
        while (i >= 0 && n.a[i] > key) {
            n.a[i + 1] = n.a[i];
            n.next[i + 1] = n.next[i];
            i--;
        }
        i++;
        n.a[i] = key;
        n.next[i] = address;
        n.size++;
        if (i == n.size - 1) {
            updateKey(n.parent, n, key);
        }
    }

    void promote(node n, int key, node address) {
        if (n == null) {
            return;
        }
        if (n.size < 4) {
            insertIntoNode(n, key, address);
            return;
        }
        if (n == root) {
            root = new node();
            n.parent = root;
        }
        node newptr = split(n);
        node t;
        if (key < n.a[0]) {
            t = newptr;
        } else {
            t = n;
        }
        insertIntoNode(t, key, address);
        promote(n.parent, n.a[n.size - 1], n);
        promote(newptr.parent, newptr.a[newptr.size - 1], newptr);
    }

    node split(node n) {
        int midpoint = (n.size + 1) / 2;
        int newsize = n.size - midpoint;
        node newptr = new node();
        node child;
        newptr.parent = n.parent;
        int i;
        for (i = 0; i < midpoint; i++) {
            newptr.a[i] = n.a[i];
            newptr.next[i] = n.next[i];
            n.a[i] = n.a[i + midpoint];
            n.next[i] = n.next[i + midpoint];
        }
        n.size = midpoint;
        newptr.size = newsize;
        for (i = 0; i < n.size; i++) {
            child = n.next[i];
            if (child != null) {
                child.parent = n;
            }
        }
        for (i = 0; i < newptr.size; i++) {
            child = newptr.next[i];
            if (child != null) {
                child.parent = newptr;
            }
        }
        return newptr;
    }

    void traverse(node ptr) throws IOException {
        FileWriter fw = new FileWriter("display.txt", true);
         BufferedWriter bw = new BufferedWriter(fw);
        if (ptr == null) {
            return;
        }
        //fw.write("---------------");
        //bw.newLine();
        
        for (int i = 0; i < ptr.size; i++) {
            System.out.print(ptr.a[i] + "  ");
            int source = ptr.a[i];
            fw.write(source+"  ");
        }
        System.out.println();
        bw.newLine();
        bw.close();
        for (int i = 0; i < ptr.size; i++) {
            traverse(ptr.next[i]);
        }
        fw.close();
    }

    btree() {
        root = null;
    }
}

public class Fs {

    static int countno() throws FileNotFoundException, IOException {
        int no1;
        no1 = 0;
        try {
            File fle = new File("ms.txt");
            if (fle.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fw = new FileWriter(fle, true);
        } catch (IOException ioe) {
            System.out.println("Exception occurred:");
        }
        BufferedReader reader = new BufferedReader(new FileReader("ms.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            no1++;
        }
        no1 = no1 - 1;
        reader.close();
        return no1;
    }

    static String CustomerName, destination, CustomerID, ph, count;
    static records[] recs = new records[100];

    public static void main(String[] args) throws IOException {
        new loginform().setVisible(true);

    }
}
