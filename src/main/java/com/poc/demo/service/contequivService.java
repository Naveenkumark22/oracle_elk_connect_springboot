package com.poc.demo.service;

import com.poc.demo.dao.*;

import com.poc.demo.model.contequiv;
import com.poc.demo.repository.contequivRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class contequivService {

    @Autowired
    private contequivRepo repo1;
    @Autowired
    private ElkSearchDao dao;
    @Autowired
    private ElasticsearchWebClient dao2;

    @Autowired
    private ElkScroll dao3;
    @Autowired
    private newElk dao7;
    @Autowired
    private ElasticsearchScrollExample dao8;
    public List<contequiv> getallcontequiv() {
        // checkResultsFromQuery();
         List<contequiv> l1=new ArrayList<>();
        List<contequiv> l2=new ArrayList<>();
       l1= repo1.getkeys();
l2=repo1.getkeys1();
l1.addAll(l2);
return l1;
//
//        int pageNumber = 0; // Page numbers start from 0
//        int pageSize = 1000; // Fetch 1000 records at a time
//        List<contequiv> allData = new ArrayList<>();
//        while (true) {
//            Page<contequiv> page = repo1.findAll11(PageRequest.of(pageNumber, pageSize));
//            List<contequiv> data = page.getContent();
//            if (data.isEmpty()) {
//                break; // No more data to fetch
//            }
//            allData.addAll(data);
//            pageNumber++;
//            System.out.println("-----------"+pageNumber+"-----------");
//        }
//        return allData;



//        int pageNumber = 0; // Page numbers start from 0
//            int pageSize = 20000; // Fetch 1000 records at a time
//         List<contequiv> allData = new ArrayList<>();
//        while (allData.size() < 20000) { // Stop when 10 records are fetched
//            Page<contequiv> page = repo1.findAll11(PageRequest.of(pageNumber, pageSize));
//            List<contequiv> data = page.getContent();
//            if (data.isEmpty()) {
//                break; // No more data to fetch
//            }
//            allData.addAll(data);
//            pageNumber++;
//        }
//
//        //return allData.subList(0, Math.min(allData.size(), 10000)); // Return up to 10 records
//return allData;

    }
                           //multithreadning
//    10k-40 sec
//1k-1min
 // 10k * 5 =4.16 min
    //
    public List<String> fetchDataConcurrently() throws ExecutionException, InterruptedException, IOException {
        int batchSize = 15000; // Number of records to fetch per batch
         int numBatches = 13; // Total number of batches (1 lakh records / 10,000 records)
//        long count= repo1.getCount();
//        int numBatches= (int) (count/batchSize)+1;
        ExecutorService executor = Executors.newFixedThreadPool(numBatches);
        List<Future<List<contequiv>>> futures = new ArrayList<>();
        for (int i = 0; i < numBatches; i++) {
            int offset = i * batchSize;
            int endrow = (i + 1) * batchSize;
          System.out.println(offset + "-----"+ endrow);
             System.out.println(endrow);
            Future<List<contequiv>> future = executor.submit(() ->
                    repo1.findWithCustomQuery(offset,endrow));
            futures.add(future);
        }
        List<String> tableadminclientids = new ArrayList<>();

        for (Future<List<contequiv>> future : futures) {
            List<contequiv> contequivList = future.get();
            for (contequiv c : contequivList) {
                tableadminclientids.add(c.getAdminclientid());
            }
        }
        System.out.println(tableadminclientids);
//        List<contequiv> result = new ArrayList<>();
//        for (Future<List<contequiv>> future : futures) {
//            try {
//                result.addAll(future.get());
//            } catch (Exception e) {
//                // Handle exception
//            }
//        }

        executor.shutdown();

        List<String> elkids= (List<String>) fetchelkdata();
        List<String> mismatchids=compareclientids(tableadminclientids,elkids);
return mismatchids;
       // return dao3.fetch(ids);
    }

    private List<String> compareclientids(List<String> tableadminclientids, List<String> elkids) {
//List<String> mismatchids=new ArrayList<>(tableadminclientids);

         tableadminclientids.removeAll(elkids);
         return  tableadminclientids;

    }


    public Object fetchelkdata() throws IOException {

String q="{\"size\":\"1000\",\"from\":\"0\",\"query\":{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"term\":{\"id\":{\"boost\":\"1\",\"value\":\"1057431638 \"}}}]}}]},\"_source\":[\"id\"]}";
String q1="{\"size\":\"1000\",\"from\":\"0\",\"query\":{\"bool\":{\"must\":[{\"bool\":{\"must\":[{\"term\":{\"id\":{\"boost\":\"1\",\"value\":\"1057431656\"}}}]}}]}}}\n";
//all rec
String q3="{\"size\":\"10000\",\"from\":\"0\",\"query\":{\"match_all\":{}}}\n";
//using scroll
String query = "{\"scroll\": \"1m\", \"scroll_id\": \"a1\", \"query\": {\"match_all\": {}}, \"_source\": [\"id\", \"keyType\", \"gkDomainAttribute\"]}";


        String q5 = "{\"size\":10000,\"from\":0,\"_source\":[\"id\"],\"query\":{\"match_all\":{}}}\n";
        String q6 = "{\"size\":10000,\"from\":0,\"_source\":[\"id\",\"_scroll_id\"],\"query\":{\"match_all\":{}}}\n";
        String q7 = "{\"size\": 10000, \"query\": {\"match_all\": {}}, \"_source\": [\"id\"]}";

       // return dao.querySource(q5);
      //  System.out.println(q1);
 //return dao8.elkcall();

        List<List<Map<String, Object>>> hitslist=dao8.elkcall();
List<String> idlist=new ArrayList<>();

        for (List<Map<String, Object>> innerList : hitslist) {
            for (Map<String, Object> innerMap : innerList) {
                Object fieldValue = innerMap.get("_id");
                if (fieldValue != null) {
                    idlist.add((String) fieldValue);
                }
            }
        }
return idlist;

    }

    public List<String> fetchDataConcurrently1() throws ExecutionException, InterruptedException, IOException {
        int batchSize = 10000; // Number of records to fetch per batch
        int numBatches = 30; // Total number of batches (1 lakh records / 10,000 records)
//        long count= repo1.getCount();
//        int numBatches= (int) (count/batchSize)+1;
        ExecutorService executor = Executors.newFixedThreadPool(numBatches);
        List<Future<List<contequiv>>> futures = new ArrayList<>();
        for (int i = 0; i < numBatches; i++) {
            int offset = i * batchSize;
            int endrow = (i + 1) * batchSize;
            System.out.println(offset + "-----" + endrow);
            System.out.println(endrow);
            Future<List<contequiv>> future = executor.submit(() ->
                    repo1.findWithCustomQuery(offset, endrow));
            futures.add(future);
        }
        List<String> tableadminclientids = new ArrayList<>();
        for (Future<List<contequiv>> future : futures) {
            List<contequiv> contequivList = future.get();
            for (contequiv c : contequivList) {
                tableadminclientids.add(c.getAdminclientid());
            }
        }
        return  tableadminclientids;
    }




}
