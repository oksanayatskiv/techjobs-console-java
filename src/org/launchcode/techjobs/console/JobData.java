package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        Collections.sort(values);
        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param columnName   Column that should be searched.
     * @param searchRequest Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String columnName, String searchRequest) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobsResultList = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String columnValue = row.get(columnName);

            if ((columnValue.toLowerCase()).contains(searchRequest.toLowerCase())) {
                jobsResultList.add(row);
            }
        }

        return jobsResultList;
    }

    public static ArrayList<HashMap<String, String>> findByValue(String expectedValue) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobsResult = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            for (Map.Entry<String, String> pair : row.entrySet()){
                if ((pair.getValue().toLowerCase()).contains(expectedValue.toLowerCase())){
                    jobsResult.add(row);
                    break;
                }
            }
        }

        return jobsResult;
    }

//    public static ArrayList<HashMap<String, String>> findByValue(String value) {
//
//        // load data, if not already loaded
//        loadData();
//
//        ArrayList<HashMap<String, String>> jobsResults = new ArrayList<>();
//
//        for (HashMap<String, String> listElement : allJobs) {
//
//            for (String key : listElement.keySet()) {
//                String aValue = listElement.get(key);
//
//                boolean containsValue = (aValue.toLowerCase()).contains(value.toLowerCase());
//
//                if (containsValue) {
//                    jobsResults.add(listElement);
//
//                    // Finding one field in a job that matches is sufficient
//                    break;
//                }
//            }
//        }
//
//        return jobsResults;
//    }



    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
