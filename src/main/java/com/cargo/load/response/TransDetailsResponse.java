package com.cargo.load.response;


import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TransDetailsResponse {

	private String id;
	private String scheduleType;
    private String numberOfLegs;
    private String cutOffDate;
    private String cargoReadyDate;
    
    private List<ScheduleLegsResponse> scheduleLegsResponseList;
}
