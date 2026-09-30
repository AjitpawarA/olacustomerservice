package com.alpha.CustomerService.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.FairPriceAllVehicles;
import com.alpha.CustomerService.Dto.Fairprice;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Dto.RidefairDTO;
import com.alpha.CustomerService.Dto.SearchDestinationResponeDto;
import com.alpha.CustomerService.Dto.SelectRideDTO;
import com.alpha.CustomerService.Entity.Booking;
import com.alpha.CustomerService.Entity.Customer;
import com.alpha.CustomerService.Exception.CustomerNotExist;
import com.alpha.CustomerService.Repository.Cardinationrepository;
import com.alpha.CustomerService.Repository.CustomerRepositor;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepositor customerRepositor;
	@Autowired
	private Cardinationrepository cardinationrepository;
	@Autowired
	private RedisService redisserver;

	public Customer CreateCustomer(CustomerDto custDto) {
		Customer c = new Customer();
		c.setName(custDto.getName());
		c.setMobile(custDto.getMobile());
		c.setEmail(custDto.getEmail());
		c.setGender(custDto.getGender());
		Customer cust = customerRepositor.save(c);

		int custId = cust.getId();

		if (custId >= 1 && custId <= 9999) {
			String otp = String.format("%04d", custId);
			System.out.println(otp);
			c.setOtp(otp);
		} else if (custId >= 10000) {
			System.out.println(custId % 10000);
			c.setOtp(custId % 10000 + "");
		}
		customerRepositor.save(c);
		return c;
	}

	public ResponceStructure<String> DeleteCustomer(int custid) {
		Customer cust = customerRepositor.findById(custid).orElseThrow(() -> new CustomerNotExist());
		customerRepositor.deleteById(custid);

		ResponceStructure<String> rs = new ResponceStructure<String>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Delete Succesfully");
		rs.setData("Deleted record");
		return rs;
	}

	//returen responce structure
	public Customer findCustomer(int id) {
		Customer cust = customerRepositor.findById(id).orElseThrow(() -> new CustomerNotExist());
		return cust;
	}

	@Autowired
	private RestTemplate restTemplate;

	public ResponceStructure<List<SearchDestinationResponeDto>> searchdroplocation(String searchkey) {
		// TODO Auto-generated method stub
		String url = "https://us1.locationiq.com/v1/search?key=pk.ee69342003ac6bc7ebb859fb52baf933&q=" + searchkey
				+ "&format=json&";
		ArrayList<Object> list = restTemplate.getForObject(url, ArrayList.class);
//		Map<String, Object> map = (Map<String, Object>)list;
		List<SearchDestinationResponeDto> searchDestinationResponeDtos = new ArrayList<SearchDestinationResponeDto>();
		for (Object searchDestinationResponeDto : list) {
			Map<String, Object> map = (Map<String, Object>) searchDestinationResponeDto;
			SearchDestinationResponeDto searchDestinationResponeDto2 = new SearchDestinationResponeDto();
			searchDestinationResponeDto2.setAdderss((String) map.get("display_name"));
			searchDestinationResponeDto2.setLatitude((Double.parseDouble((String) map.get("lon"))));
			searchDestinationResponeDto2.setLongitude(Double.parseDouble((String) map.get("lat")));
			searchDestinationResponeDtos.add(searchDestinationResponeDto2);
		}
		ResponceStructure<List<SearchDestinationResponeDto>> response = new ResponceStructure<>();
		response.setStatusCode(HttpStatus.OK.value());
		response.setMessage("The matching addresses are:");
		response.setData(searchDestinationResponeDtos);

		return response;
	}
	public ResponceStructure<FairPriceAllVehicles> selectRide(SelectRideDTO selectRideDTO) {
		String url = "https://us1.locationiq.com/v1/directions/driving/"
				+ selectRideDTO.getSourcelocation().getLongitude() + ","
				+ selectRideDTO.getSourcelocation().getLatitude() + ";"
				+ selectRideDTO.getDestinationlocation().getLongitude() + ","
				+ selectRideDTO.getDestinationlocation().getLatitude()
				+ "?key=pk.ee69342003ac6bc7ebb859fb52baf933&steps=true&alternatives=true&geometries=polyline&overview=full&";
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);
		Customer c = customerRepositor.findById(selectRideDTO.getCustid()).orElseThrow(()-> new CustomerNotExist());
		// Extract routes from the api
		List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
		if (routes != null && !routes.isEmpty()) {
			Map<String, Object> firstRoute = routes.get(0);
			int custId = selectRideDTO.getCustid();
			double distance =(((Number) firstRoute.get("distance")).doubleValue())/1000;
			double duration=(((Number) firstRoute.get("duration")).doubleValue())/60;	
			double bikePrice = calculateFare(Fairprice.BIKE, distance, duration);
	        double autoPrice = calculateFare(Fairprice.AUTO, distance, duration);
	        double cabPrice = calculateFare(Fairprice.CAB, distance, duration);
	        String pickup =selectRideDTO.getSourcelocation().getLatitude()+ "," +selectRideDTO.getSourcelocation().getLongitude();
	        String destination =selectRideDTO.getDestinationlocation().getLatitude()+ "," +selectRideDTO.getDestinationlocation().getLongitude();
	        
	        redisserver.saveRideDetails(custId,pickup,destination,distance,duration,bikePrice,autoPrice,cabPrice);
	        ResponceStructure<FairPriceAllVehicles> rs = new ResponceStructure<FairPriceAllVehicles>();
	        FairPriceAllVehicles fp = new FairPriceAllVehicles();
	        fp.setDistance(distance);
	        fp.setDuration(duration);
	        fp.setBikePrice(bikePrice);
	        fp.setAutoPrice(autoPrice);
	        fp.setCarPrice(cabPrice);
	        fp.setPickupLocation(pickup);
	        fp.setDestinationLocation(destination);
	        
	        rs.setStatusCode(HttpStatus.FOUND.value());
	        rs.setMessage("Select the Vehicle According to you fare");
	        rs.setData(fp);
	        return rs;
			}
        ResponceStructure<FairPriceAllVehicles> rs = new ResponceStructure<FairPriceAllVehicles>();
        rs.setStatusCode(HttpStatus.NOT_FOUND.value());
		rs.setMessage("Route Not Found");
		rs.setData(null);
		return rs;
		}
	
	
	
		private double calculateFare(Fairprice vehicle,double distance,double duration) {
		    double baseFare = 0;
		    double pricePerKm = 0;
		    double pricePerMinute = 0;
		    switch (vehicle) {
		        case BIKE:
		            baseFare = 20;
		            pricePerKm = 8;
		            pricePerMinute = 1;
		            break;

		        case AUTO:
		            baseFare = 30;
		            pricePerKm = 12;
		            pricePerMinute = 1.5;
		            break;

		        case CAB:
		            baseFare = 50;
		            pricePerKm = 18;
		            pricePerMinute = 2;
		            break;
		    }

		    return baseFare+ (distance * pricePerKm)+ (duration * pricePerMinute);
		}

		public void bookRide(int custId, String vehicle) {
			Customer c = customerRepositor.findById(custId).orElseThrow(()-> new CustomerNotExist());
			Map<Object, Object> rideData =redisserver.getRideDetails(custId);
			Booking b = new Booking();
			String pickup = (String) rideData.get("pickupLocation");
			String destination = (String) rideData.get("destinationLocation");
//			double distance = Double.parseDouble((String) rideData.get("distance"));
//			double duration = Double.parseDouble((String) rideData.get("duration"));
//			double bikePrice = Double.parseDouble((String) rideData.get("bikePrice"));
//			double autoPrice = Double.parseDouble((String) rideData.get("autoPrice"));
//			double cabPrice = Double.parseDouble((String) rideData.get("cabPrice"));
			System.out.println(pickup);
			System.out.println(destination);
		}
		
		
		
}
