package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class BookingCountByStatusResponse {
	
   private long requestCount;
   private long acceptedCount;
   private long cancelledCount;
   private long rejectedCount;
   
   
   public BookingCountByStatusResponse(long requestCount, long acceptedCount, long cancelledCount, long rejectedCount) {
	  super();
	  this.requestCount = requestCount;
	  this.acceptedCount = acceptedCount;
	  this.cancelledCount = cancelledCount;
	  this.rejectedCount = rejectedCount;
   }


   public BookingCountByStatusResponse() {
	 super();
   }
   
   
}
