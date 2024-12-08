package john.project.controllers;

import java.util.HashMap;

import john.project.models.ClientForm;
import john.project.models.OrderForm;

public class CacheManager {
	private HashMap<String, OrderForm> cacheOrder = new HashMap<>();
    private HashMap<String, ClientForm> cacheCli = new HashMap<>();
    
	public HashMap<String, OrderForm> getCache() { return this.cacheOrder; }
	public HashMap<String, ClientForm> getCli() { return this.cacheCli; }
	public void save(String key, OrderForm order) {	cacheOrder.put(key, order);	}
	public OrderForm get(String key) { return this.cacheOrder.get(key);	}
	public void deleteOrder(String string) { this.cacheOrder.remove(string); }
	public void save(String key, ClientForm client) { cacheCli.put(key, client); }
	public ClientForm getLast(String key) {	return this.cacheCli.get(key); }
	public void deleteClient(String string) { this.cacheCli.remove(string); }
	

}
