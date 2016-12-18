package edu.greenriver.it.consumers;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * @author Jeremy Belt
 * @version 1.0
 * 11/21/2016
 * LinksQueue.java
 * The The Links Queue holds the links scrapped out of a web page
 * The Fetcher then takes a link off the queue and get the html from that site
 */
public class LinksQueue {

    private static final int MAX_SIZE = 50000;

    private static LinkedList<String> linksLinkedList = new LinkedList<>();
    private static HashSet<String> linksSet = new HashSet<>();

    /**
     * adds a link to the links queue if the set containing links does not already have it
     * add wake of the thread if it was sleeping
     */
    public static void addLink(String url) {

        synchronized (linksLinkedList) {

            if (!linksSet.contains(url)) {

                while (linksLinkedList.size() >= MAX_SIZE) {
                    try {
                        linksLinkedList.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(url);
                linksSet.add(url);
                linksLinkedList.addFirst(url);
                linksLinkedList.notify();
            }
        }
    }


    /**
     * @return String
     * get the link from the queue or wait if the queue is empty
    */
    public static String getNextLink() {
        synchronized (linksLinkedList) {

            while (linksLinkedList.size() == 0) {
                try {
                    linksLinkedList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String removed = linksLinkedList.removeLast();
            linksLinkedList.notify();
            return removed;
        }
    }

    /**
     * @return int
     * get the number of unique links found by the fetcher
     */
    public static int getLinksFound(){
        return linksSet.size();
    }
}

