package com.alpha.CustomerService.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alpha.CustomerService.Dto.CustomerDto;
import com.alpha.CustomerService.Dto.ResponceStructure;
import com.alpha.CustomerService.Dto.RidefairDTO;
import com.alpha.CustomerService.Dto.SearchDestinationResponeDto;
import com.alpha.CustomerService.Dto.SelectRideDTO;
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
	public Customer CreateCustomer(CustomerDto custDto) {
		Customer c = new Customer();
		c.setName(custDto.getName());
		c.setMobile(custDto.getMobile());
		c.setEmail(custDto.getEmail());
		c.setGender(custDto.getGender());
		Customer cust =customerRepositor.save(c);
		
		int custId=cust.getId();

		if(custId>=1 && custId <=9999) {
			String otp = String.format("%04d", custId);
			System.out.println(otp);
			c.setOtp(otp);
		}else if(custId>=10000){
			System.out.println(custId%10000);
			c.setOtp(custId%10000+"");
		}
		customerRepositor.save(c);
		return c;
	}

	public ResponceStructure<String> DeleteCustomer(int custid) {
		Customer cust=customerRepositor.findById(custid).orElseThrow(()-> new CustomerNotExist());
		customerRepositor.deleteById(custid);
		
		ResponceStructure<String> rs = new ResponceStructure<String>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Delete Succesfully");
		rs.setData("Deleted record");
		return rs;
	}

	public Customer findCustomer(int id) {
		Customer cust=customerRepositor.findById(id).orElseThrow(()-> new CustomerNotExist());
		return cust;
	}
	@Autowired
	private RestTemplate restTemplate;
	public ResponceStructure<List<SearchDestinationResponeDto>> searchdroplocation(String searchkey){
		// TODO Auto-generated method stub
		String url = "https://us1.locationiq.com/v1/search?key=pk.9cae04b25eb1f3eef542e54e8ba4f653&q=" + searchkey
				+ "&format=json&";
		ArrayList<Object> list = restTemplate.getForObject(url, ArrayList.class);
//		Map<String, Object> map = (Map<String, Object>)list;
		List<SearchDestinationResponeDto> searchDestinationResponeDtos = new ArrayList<SearchDestinationResponeDto>();
		for (Object searchDestinationResponeDto : list) {
			Map<String, Object> map = (Map<String, Object>)searchDestinationResponeDto;
			SearchDestinationResponeDto searchDestinationResponeDto2 = new SearchDestinationResponeDto();
			searchDestinationResponeDto2.setAdderss((String)map.get("display_name"));
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
	
	public void selectRide(SelectRideDTO selectRideDTO) {
		String url = "https://us1.locationiq.com/v1/directions/driving/"+selectRideDTO.getSourcelocation().getLongitude()+","+selectRideDTO.getSourcelocation().getLatitude()+";"+selectRideDTO.getDestinationlocation().getLongitude()+","+selectRideDTO.getDestinationlocation().getLatitude()+"?key=pk.9cae04b25eb1f3eef542e54e8ba4f653&steps=true&alternatives=true&geometries=polyline&overview=full&";
		System.out.println(url);

	    Map<String, Object> response = restTemplate.getForObject(url, Map.class);

	    // Extract routes
	    List<Map<String, Object>> routes = (List<Map<String, Object>>) response.get("routes");
	    if (routes != null && !routes.isEmpty()) {
	        Map<String, Object> firstRoute = routes.get(0);
	        RidefairDTO ridefairDTO = new RidefairDTO();
	        ridefairDTO.setDistance(Double.parseDouble((String)firstRoute.get("distance")));
	        ridefairDTO.setDuration(Double.parseDouble((String)firstRoute.get("duration")));
	        Object distance = firstRoute.get("distance");
	        Object duration = firstRoute.get("duration");
	        System.out.println("Distance: " + distance);
	        System.out.println("Duration: " + duration);
	    }
		
		
	}
	

}
