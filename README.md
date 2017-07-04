# Table of Contents
1. [To Run](README.md#to-run)
2. [Repo Directory Structure](README.md#repo-directory-structure)
3. [Data Structure](README.md#data-structure)
4. [Dataset](README.md#dataset)
5. [Algorithm](README.md#algorithm)

# To Run

In macOS/Linux/Unix, cd to this repo directory "/data-science", enter the following command in the terminal:
```
./run.sh
```
When it's finished, you will see it printing "Done! See the flagged_purchases.json file in ./log_output!" and output file "flagged_purchases" in the directory "./log_output".

This code starts at the main() in ProcessData class.

### Repo Directory Structure

├── README.md
├── run.sh
├── src
│   └── com
│       └── insightintodatascience
|           └── Person.java
|           └── ProcessData.java
|           └── Purchase.java
├── log_input
│   └── batch_log.json
│   └── stream_log.json
├── log_output
|   └── flagged_purchases.json
├── com
    └── insightintodatascience
        └── Person.class
        └── ProcessData.class
        └── Purchase.class

# Data Structure
### Person
Each person has id, and a set of friends, which stores his/her friends id.
```
public class Person {
	private int id;
	private Set<Integer> friends;

	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public Set<Integer> getFriends() { return friends; }
	public void setFriends(Set<Integer> friends) { this.friends = friends; }

	public Person(int id) {
		this.setId(id);
		Set<Integer> friends = new HashSet<Integer>();
		this.setFriends(friends);
	}
}
```
### Purchase
```
public class Purchase {

	public Purchase(String timestamp, int id, double amount) {
		this.setTimestamp(timestamp);
		this.setId(id);
		this.setAmount(amount);
	}

	private int id;
	private String timestamp;	//should be Date, for the simplicity of this
	private double amount;
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	public String getTimestamp() { return timestamp; }
	public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
	public double getAmount() { return amount; }
	public void setAmount(double amount) { this.amount = amount; }

}
```
### ProcessData
```
public class ProcessData {

	public static void main(String[] args) {...}

  // get network within D degree for a person of id in pch
	private static Set<Integer> getNetwork(Purchase pch, Map<Integer, Person> allPeopleMap, int D) {...}

	// get network recursively
	private static Set<Integer> getNetwork(Integer id, Map<Integer, Person> allPeopleMap, Set<Integer> checkedID, int D) { ... }

}
```
In the ProcessData class, both parameters of 'D' and 'T' are set in this class;


'Map<Integer, Queue<Purchase>> purMap = new HashMap<Integer, Queue<Purchase>>();' : to store each user's purchases, keys are customer's ids while the corresponding values are a queue of purchases a customer purchase. This will be constantly updated when the program is processing the stream of the input log data. Note that the maximum size of the queue is set to be T. The reason behind this is that the size of tracked purchases history within a user's network is no greater than T, so it's guaranteed that each person's tracked purchases size will not exceeds T. Whenever the size of the queue exceeds T, the queue will poll out the earliest purchase and insert the latest one on a First-In-First-Serve order.


'List<Purchase> purchaseStream = new ArrayList<Purchase>();' : to store the purchase from the streaming of incoming data. As discussed above, the number of each person's tracked purchases will not exceed T, therefore the maximum size of purchaseStream is (the number of all Person * T). When the size of purchaseStream exceeds this number, it will recycle the list, removing items from the beginning when a new purchase is added based on (FIFS).


'Map<Integer, Person> allPeopleMap = new HashMap<Integer, Person>();' : to store id -> Person. The reason to use this is that, since a person's data is stored as a Person object, once created it's hard to check again. Therefore using a map of this can easily get each person's id, which is useful for 'befriend' and 'unfriend'.

# Dataset
Input data are in the directory of "./log_input". After this program is finished, the output file flagged_purchases.json should be created in the "./log_output" folder.

# Algorithm
Briefly speaking, this program first takes into 'batch_log.json' as input, build the data structure for Person, Purchase, Map<Integer, Queue<Purchase>> purMap and List<Purchase> purchaseStream. Then it read the file 'stream_log.json', whenever it read a new record of purchase, it extracts the record, checking if this record is "purchase", "befriend" or "unfriend".


### Purchase
When the program detects a record from the streaming is "purchase", it will first save it as a whole into 'purchaseStream', and update 'purMap'. To update 'purMap', if the map does not contain the customer's id, it will create a queue adding this purchase, and put id -> queue into this map; otherwise if the map contains the customer's id, it will insert this new purchase into his/her corresponding purchase queue directly. Note that the queue's size will not exceed T.


### Befriend
When the program detects a record from the streaming is "befriend", it will first check if 'allPeopleMap' save both of the customers. If not, both will be put into 'allPeopleMap' with 'id -> person'. After that each one will be added into the other person's 'Set<Integer> friends'.


### Unfriend
When the program detects a record from the streaming is "unfriend", it will first check if 'allPeopleMap' save both of the customers. If not, both will be put into 'allPeopleMap' with 'id -> person'. After that each one will be removed from the other person's 'Set<Integer> friends'.

### private static Set<Integer> getNetwork(Purchase pch, Map<Integer, Person> allPeopleMap, int D)
### private static Set<Integer> getNetwork(Integer id, Map<Integer, Person> allPeopleMap, Set<Integer> checkedID, int D)
This method will return a set of friends' ids within 'D' degree from a person whose id is 'id', and call 'private static Set<Integer> getNetwork(Integer id, Map<Integer, Person> allPeopleMap, Set<Integer> checkedID, int D)' recursively to add friends with the degree of 'D - 1', until 'D == 0'. A set of 'checkedID' is used to keep track of traversed customers. In each round, for example a customer with 'id', at first this id and all his/her friends' ids will be added into 'checkedID'. 'int[] currentFriends = new int[myNetwork.size()];' is used to store current customer's friends. If the current value of 'D' is greater than 0, it will go through the array of 'currentFriends', 'getNetwork(currentFriends[i], allPeopleMap, checkedID, D - 1)' and add all the elements returned from this method recursively.


### Calculations
When we updated the models after receiving a record from stream log, we check if the size of network of the current customer. If this number is no greater than 2, we do nothing; Otherwise we calculate 'mean' and 'sd', and append this purchase into the output file if the condition of (amount > mean + 3 * sd) is true.
