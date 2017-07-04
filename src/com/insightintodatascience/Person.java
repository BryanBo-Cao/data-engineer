package com.insightintodatascience;

import java.util.HashSet;
import java.util.Set;

public class Person {
	private int id;
	private Set<Integer> friends;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Set<Integer> getFriends() {
		return friends;
	}
	public void setFriends(Set<Integer> friends) {
		this.friends = friends;
	}
	
	public Person(int id) {
		this.setId(id);
		Set<Integer> friends = new HashSet<Integer>();
		this.setFriends(friends);
	}
}
