package edu.greenriver.it.producers;

import edu.greenriver.it.consumers.LinksQueue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Jeremy Belt
 * @version 1.0
 * 11/21/2016
 * Fetcher.java
 * The Fetcher takes a link from a website
 * then it will download the html from the link
 * and will convert that html into a single string
 * and add that string to the page queue
 */
public class Fetcher extends Thread{

    private static int fetcherThreadCounter;
    private static int errorCounter;


    /**
     * The constructor increments a fetcher thread counter and prints out the number
     * of total fetcher threads that have been created
     */
    public Fetcher() {
        fetcherThreadCounter++;
        System.out.println("Current Number of Fetcher Threads: " + fetcherThreadCounter);
    }


    /**
     * While the Links queue isn't empty
     * grab a link and open an http connection
     * read the page line by line and append each line to
     * a String Buffer object
     * then add that string to the page queue
     */
    @Override
    public void run() {
        while (true) {

            try {
                String page = LinksQueue.getNextLink();

                URL url = new URL(page);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader download = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String inputLine;
                StringBuffer fullPage = new StringBuffer();

                while ((inputLine = download.readLine()) != null) {
                    fullPage.append(inputLine);
                }
                download.close();
                PageQueue.addPage(fullPage.toString());

            } catch (Exception e) {
                errorCounter++;
            }
        }
    }

    /**
     * @return int
     * get the number of fetcher threads that have been created
     */
    public static int getFetcherThreadCounter() {
        return fetcherThreadCounter;
    }

    /**
     * @return int
     * get the number of times there was some sort of error
     * when a website's html couldn't be accessed by the fetcher
     */
    public static int getErrorCounter() {
        return errorCounter;
    }
}
