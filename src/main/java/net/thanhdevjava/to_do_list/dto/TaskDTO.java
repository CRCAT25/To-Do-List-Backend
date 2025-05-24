package net.thanhdevjava.to_do_list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thanhdevjava.to_do_list.enums.TaskStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private Long userId;
    private String username;
    private TaskStatus status;
    private LocalDateTime deadline;
}
