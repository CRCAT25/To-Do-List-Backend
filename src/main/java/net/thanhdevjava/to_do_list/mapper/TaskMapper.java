package net.thanhdevjava.to_do_list.mapper;

import net.thanhdevjava.to_do_list.dto.TaskDTO;
import net.thanhdevjava.to_do_list.entity.Task;
import net.thanhdevjava.to_do_list.entity.User;

public class TaskMapper {
    public static TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setUserId(task.getUser().getId());
        dto.setUsername(task.getUser().getUsername()); // if needed
        dto.setStatus(task.getStatus());
        dto.setDeadline(task.getDeadline());
        dto.setFullName(task.getUser().getFullName());
        return dto;
    }

    public static Task toEntity(TaskDTO dto, User user) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setUser(user); // You must find the User entity by ID beforehand
        task.setStatus(dto.getStatus());
        task.setDeadline(dto.getDeadline());
        return task;
    }
}
