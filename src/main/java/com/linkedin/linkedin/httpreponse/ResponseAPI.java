package com.linkedin.linkedin.httpreponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseAPI<T> {
   private String message;
   private HttpStatus status;
   private T payload;
   @Builder.Default
   private LocalDateTime timestamp = LocalDateTime.now();

}
