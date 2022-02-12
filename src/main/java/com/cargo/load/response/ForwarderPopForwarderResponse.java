package com.cargo.load.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ForwarderPopForwarderResponse {

	private List<ForwarderDetailsResponse> forwarderDetailsResponseList;
	private List<PopularForwarderResponse> popularForwarderResponseList; 
}
