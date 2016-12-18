package edu.greenriver.it;

import edu.greenriver.it.consumers.LinksQueue;
import edu.greenriver.it.consumers.Parser;
import edu.greenriver.it.producers.Fetcher;
import edu.greenriver.it.producers.PageQueue;

import java.util.ArrayList;
import java.util.Scanner;

 /**
 * @author Jeremy Belt
 * @version 1.0
 * 11/21/2016
 * Driver.java
 * The Driver class contains the menu and output of the web crawler
 */
public class Driver {

/**
 *  main method that starts the web crawler
 */
    public static void main(String[] args) {

        ArrayList<String> keywordList = new ArrayList<>();

        //Start the program with 1 consumer and producer
        new Fetcher().start();
        new Parser(keywordList).start();

        Scanner scanner = new Scanner(System.in);

        menu:
        while (true) {

            System.out.println(
                    "1. Add seed url\n" +
                    "2. Add consumer\n" +
                    "3. Add producer\n" +
                    "4. Add keyword search\n" +
                    "5. Print stats"
            );

            String userChoice = scanner.nextLine();
            switch (userChoice) {

                case "1":
                    System.out.println("Please enter a url");
                    String seedUrl = scanner.nextLine();
                    LinksQueue.addLink(seedUrl);
                    break;

                case "2":
                    System.out.println("Starting a new Consumer thread");
                    new Parser(keywordList).start();
                    break;

                case "3":
                    System.out.println("Starting a new Producer thread");
                    new Fetcher().start();
                    break;

                case "4":
                    System.out.println("Add a keyword to search for.");
                    String newKeyword = scanner.nextLine();
                    keywordList.add(newKeyword);

                    System.out.println("Here's a list of current keywords");
                    for (String word: keywordList) {
                        System.out.println(word);
                    }
                    scanner.nextLine();
                    break;

                case "5":
                    System.out.println(
                            "Keywords found: " + Parser.getTotalCount() + "\n" +
                            "Links found: " + LinksQueue.getLinksFound() + "\n" +
                            "Pages found: " + PageQueue.getPagesDownloaded() + "\n" +
                            "Failed downloads: " + Fetcher.getErrorCounter() + "\n" +
                            "Producers: " + Fetcher.getFetcherThreadCounter() + "\n" +
                            "Consumers: " + Parser.getParserThreadCounter()
                    );
                    scanner.nextLine();
            }
        }
    }
}
