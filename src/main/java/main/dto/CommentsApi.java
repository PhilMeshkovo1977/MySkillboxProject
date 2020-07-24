package main.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentsApi {

  private int id;
  private Long timestamp;
  private String text;
  private UserApi user;
}