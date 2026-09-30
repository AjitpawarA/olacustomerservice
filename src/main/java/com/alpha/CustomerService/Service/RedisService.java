package com.alpha.CustomerService.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate redisTemplate;

	public void saveRideDetails(long custId, String pickupLocation, String destinationLocation, double distance,
			double duration, double bikePrice, double autoPrice, double carPrice) {
		String key = "CustId_" + custId;
		redisTemplate.opsForHash().put(key, "pickupLocation", pickupLocation);
		redisTemplate.opsForHash().put(key, "destinationLocation", destinationLocation);
		redisTemplate.opsForHash().put(key, "distance", String.valueOf(distance));
		redisTemplate.opsForHash().put(key, "duration", String.valueOf(duration));
		redisTemplate.opsForHash().put(key, "bikePrice", String.valueOf(bikePrice));
		redisTemplate.opsForHash().put(key, "autoPrice", String.valueOf(autoPrice));
		redisTemplate.opsForHash().put(key, "carPrice", String.valueOf(carPrice));
	}
	
	public Map<Object, Object> getRideDetails(long rideId) {

	    String key = "ride:" + rideId;

	    return redisTemplate.opsForHash().entries(key);
	}
	
	
}
