package net.thanhdevjava.to_do_list.service;

import net.thanhdevjava.to_do_list.dto.TaskDTO;
import net.thanhdevjava.to_do_list.entity.User;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getTasks();

    List<TaskDTO> getTasksByUserId(Long userId);

    TaskDTO getTaskById(Long id);

    TaskDTO createTask(TaskDTO taskDTO, User user);

    TaskDTO updateTask(TaskDTO taskDTO, User user);

    void deleteTask(Long id);
}
