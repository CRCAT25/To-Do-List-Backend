package net.thanhdevjava.to_do_list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private Long userId;
    private String username;
    private Long status;
    private String statusName;
    private Date deadline;
}
