/*
 * Author: github.com/BryanBo-Cao
 */
package com.insightintodatascience;

import java.io.*;
import java.util.*;

public class ProcessData {

	public static void main(String[] args) {
		
		// specify your parameters here
		int D = 0, T = 0;
		
//		String batch_logAddress = "./src/com/insightintodatascience/log_input/batch_log.json",
//			stream_logAddress = "./src/com/insightintodatascience/log_input/stream_log.json",
//			flagged_logAddress = "./src/com/insightintodatascience/log_output/flagged_purchases.json";
		String batch_logAddress = "./log_input/batch_log.json",
				stream_logAddress = "./log_input/stream_log.json",
				flagged_logAddress = "./log_output/flagged_purchases.json";
			
		File outputFile = new File(flagged_logAddress);
		PrintWriter writer = null;
		if (!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			writer = new PrintWriter(new FileWriter(flagged_logAddress));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// record purchases, keys are user ids, values Purchases that the user made 
		Map<Integer, Queue<Purchase>> purMap = new HashMap<Integer, Queue<Purchase>>();
		
		// streaming of purchases
		List<Purchase> purchaseStream = new ArrayList<Purchase>();
		
		Map<Integer, Person> allPeopleMap = new HashMap<Integer, Person>(); // store id -> Person
		
		// Read batch_log
		File jsonInputFile = new File(batch_logAddress);
		InputStreamReader isr;
		try {
			isr = new InputStreamReader(new FileInputStream(jsonInputFile));
			BufferedReader br = new BufferedReader(isr);
		    String line;
		    boolean needDT = true;
		    int startIndex = -1, endIndex = -1;
		    while ((line = br.readLine()) != null) {
		    	if (needDT) {
		    	 	// set "D" and "T"
			    	startIndex = line.indexOf("D") + 4;
			    	endIndex = line.indexOf(",", startIndex) - 1;
			    	D = Integer.valueOf(line.substring(startIndex , endIndex));
			    	startIndex = line.indexOf("T") + 4;
			    	endIndex = line.indexOf("}", startIndex) - 1;
			    	T = Integer.valueOf(line.substring(startIndex , endIndex));
			    	needDT = false;
		    	} else {
		    		if (line.contains("purchase")) {
		    			
		    			// set id
		    			startIndex = line.indexOf("id") + 6;
				    	endIndex = line.indexOf(",", startIndex) - 1;
				    	int id = Integer.valueOf(line.substring(startIndex , endIndex));
				    	
				    	// set timestamp
		    			startIndex = line.indexOf("timestamp") + 12;
				    	endIndex = line.indexOf(",", startIndex) - 1;
				    	//@SuppressWarnings("deprecation")
						//Date timestamp = new Date(line.substring(startIndex , endIndex));
				    	String timestamp = line.substring(startIndex , endIndex);
				    	
				    	// set amount
		    			startIndex = line.indexOf("amount") + 10;
				    	endIndex = line.indexOf("}", startIndex) - 1;
				    	double amount = Double.valueOf(line.substring(startIndex , endIndex));
				    	Purchase pch = new Purchase(timestamp, id, amount);
				    	
				    	if (purchaseStream.size() > allPeopleMap.size() * T) purchaseStream.remove(0);
				    	purchaseStream.add(pch);
				    	
		    			if (purMap.containsKey(id)) {
		    				Queue<Purchase> q = purMap.get(id);
		    				if (q.size() > T) q.poll();
		    				q.offer(pch);
	    					purMap.put(id, q);
		    			} else {
		    				Queue<Purchase> q = new LinkedList<Purchase>();
		    				q.offer(pch);
		    				purMap.put(id, q);
		    			}
		    			
		    			if (!allPeopleMap.containsKey(id)) allPeopleMap.put(id, new Person(id));
		    			
		    		} else {
		    			// set ids
		    			startIndex = line.indexOf("id1") + 7;
				    	endIndex = line.indexOf(",", startIndex) - 1;
				    	int id1 = Integer.valueOf(line.substring(startIndex , endIndex));
				    	
				    	startIndex = line.indexOf("id2") + 7;
				    	endIndex = line.indexOf("}", startIndex) - 1;
				    	int id2 = Integer.valueOf(line.substring(startIndex , endIndex));
		    			
				    	if (!allPeopleMap.containsKey(id1)) allPeopleMap.put(id1, new Person(id1));
				    	if (!allPeopleMap.containsKey(id2)) allPeopleMap.put(id2, new Person(id2));
				    	
		    			if (line.contains("befriend")) {
					    	
					    	allPeopleMap.get(id1).getFriends().add(id2);
					    	allPeopleMap.get(id2).getFriends().add(id1);
//			    			System.out.println("befriend");
			    			
			    		} else if (line.contains("unfriend")) {
			    			
			    			if (allPeopleMap.get(id1).getFriends().contains(id2))
			    				allPeopleMap.get(id1).getFriends().remove(id2);
			    			if (allPeopleMap.get(id2).getFriends().contains(id1))
			    				allPeopleMap.get(id2).getFriends().remove(id1);
//			    			System.out.println("unfriend");
			    		}
		    				
		    		}
		    	}
		   
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	           
		//// Read batch_log
		jsonInputFile = new File(stream_logAddress);
		try {
			isr = new InputStreamReader(new FileInputStream(jsonInputFile));
			BufferedReader br = new BufferedReader(isr);
		    String line;
		    int startIndex = -1, endIndex = -1;
		    while ((line = br.readLine()) != null) {
//		    	System.out.println(line);
		    	if (line.contains("purchase")) {
	    			
	    			// set id
	    			startIndex = line.indexOf("id") + 6;
			    	endIndex = line.indexOf(",", startIndex) - 1;
			    	int id = Integer.valueOf(line.substring(startIndex , endIndex));
			    	
			    	// set timestamp
	    			startIndex = line.indexOf("timestamp") + 12;
			    	endIndex = line.indexOf(",", startIndex) - 1;
			    	//@SuppressWarnings("deprecation")
					//Date timestamp = new Date(line.substring(startIndex , endIndex));
			    	String timestamp = line.substring(startIndex , endIndex);
			    	
			    	// set amount
	    			startIndex = line.indexOf("amount") + 10;
			    	endIndex = line.indexOf("}", startIndex) - 1;
			    	double amount = Double.valueOf(line.substring(startIndex , endIndex));
			    	Purchase pch = new Purchase(timestamp, id, amount);
			    	
			    	// detect if this purchase is anomaly
			    	// element is user id
			    	Set<Integer> network = getNetwork(pch, allPeopleMap, D);
			    	
			    	// proceed the calculations
			    	if (network.size() >= 2) {
			    		
			    		// get last T purchases in this network
			    		List<Purchase> lastTPurchases = new ArrayList<Purchase>();
			    		
			    		int cnt = T;
			    		double sumAmount = 0;
			    		for (int i = purchaseStream.size() - 1; i >= 0 && cnt > 0; i--) {
			    			Purchase pchInStream = purchaseStream.get(i);
			    			if (network.contains(pchInStream.getId())) {
			    				sumAmount += pchInStream.getAmount();
			    				lastTPurchases.add(pchInStream);
			    				cnt--;
			    			}
			    		}
			    		
			    		// mean
			    		double mean = sumAmount / (T - cnt);
			    		
			    		// sd
			    		double sumAmountDiff2 = 0;
			    		for (int i = 0; i < lastTPurchases.size(); i++) {
			    			Purchase phcInLastTPurchases = lastTPurchases.get(i);
			    			double diff = phcInLastTPurchases.getAmount() - mean;
		    				sumAmountDiff2 += diff * diff ;
		    				
			    		}
			    		double sd = Math.sqrt(sumAmountDiff2 / (T - cnt));
			    		
			    		if (amount > mean + 3 * sd)
			    			writer.append(line + "\n");
			    	}
			    	
	    			if (purMap.containsKey(id)) {
	    				Queue<Purchase> q = purMap.get(id);
	    				
	    				if (q.size() > T) q.poll();
	    				q.offer(pch);
    					purMap.put(id, q);
	    			} else {
	    				Queue<Purchase> q = new LinkedList<Purchase>();
	    				q.offer(pch);
	    				purMap.put(id, q);
	    			}
	    			
	    			if (!allPeopleMap.containsKey(id)) allPeopleMap.put(id, new Person(id));
	    			
	    			if (purchaseStream.size() > allPeopleMap.size() * T) purchaseStream.remove(0);
			    	purchaseStream.add(pch);
	    			
//	    			System.out.println(id + " " + timestamp + " " + amount);
	    			
	    		} else if (line.length() > 0){
	    			// set ids
	    			startIndex = line.indexOf("id1") + 7;
			    	endIndex = line.indexOf(",", startIndex) - 1;
			    	int id1 = Integer.valueOf(line.substring(startIndex , endIndex));
//			    	System.out.println(id1);
			    	
			    	startIndex = line.indexOf("id2") + 7;
			    	endIndex = line.indexOf("}", startIndex) - 1;
			    	int id2 = Integer.valueOf(line.substring(startIndex , endIndex));
//			    	System.out.println(id2);
	    			
			    	if (!allPeopleMap.containsKey(id1)) allPeopleMap.put(id1, new Person(id1));
			    	if (!allPeopleMap.containsKey(id2)) allPeopleMap.put(id2, new Person(id2));
			    	
	    			if (line.contains("befriend")) {
				    	
				    	allPeopleMap.get(id1).getFriends().add(id2);
				    	allPeopleMap.get(id2).getFriends().add(id1);
//		    			System.out.println("befriend");
		    			
		    		} else if (line.contains("unfriend")) {
		    			
		    			if (allPeopleMap.get(id1).getFriends().contains(id2))
		    				allPeopleMap.get(id1).getFriends().remove(id2);
		    			if (allPeopleMap.get(id2).getFriends().contains(id1))
		    				allPeopleMap.get(id2).getFriends().remove(id1);
//		    			System.out.println("unfriend");
		    			
		    		}
	    				
	    		}
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		writer.flush();
		writer.close();
		System.out.println("Done! See the flagged_purchases.json file in ./log_output!");
	}
	
	// get network within D degree for a person of id in pch
	private static Set<Integer> getNetwork(Purchase pch, Map<Integer, Person> allPeopleMap, int D) {
		
		int id = pch.getId();
		Set<Integer> checkedID = new HashSet<Integer>();
		return getNetwork(id, allPeopleMap, checkedID, D);
		
	}
	
	// get network recursively
	private static Set<Integer> getNetwork(Integer id, Map<Integer, Person> allPeopleMap, 
			Set<Integer> checkedID, int D) {
		checkedID.add(id);
		Set<Integer> myNetwork = allPeopleMap.get(id).getFriends();
		Iterator<Integer> it = myNetwork.iterator();

		int[] currentFriends = new int[myNetwork.size()];
		int i = 0;
		while (it.hasNext()) {
			int next = it.next();
			if (!checkedID.contains(next)) {
				checkedID.add(next);
				currentFriends[i] = next;
			}
		}
		
		if (D > 0) {
			for (int ii = 0; ii < currentFriends.length; ii++) {
				if (!checkedID.contains(currentFriends[i]))
					myNetwork.addAll(getNetwork(currentFriends[i], allPeopleMap, checkedID, D - 1));
			}
		}
			
		return myNetwork;
	}

}
