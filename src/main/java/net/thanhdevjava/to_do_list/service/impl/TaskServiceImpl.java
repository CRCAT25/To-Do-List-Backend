package net.thanhdevjava.to_do_list.service.impl;

import net.thanhdevjava.to_do_list.dto.TaskDTO;
import net.thanhdevjava.to_do_list.entity.Task;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.mapper.TaskMapper;
import net.thanhdevjava.to_do_list.repository.TaskRepository;
import net.thanhdevjava.to_do_list.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    private TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<TaskDTO> getTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    @Override
    public List<TaskDTO> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId)
                .stream()
                .map(TaskMapper::toDTO)
                .toList();
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        return task.map(TaskMapper::toDTO).orElse(null);
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, User user) {
        Task task = TaskMapper.toEntity(taskDTO, user);
        taskRepository.save(task);
        return TaskMapper.toDTO(task);
    }

    @Override
    public TaskDTO updateTask(TaskDTO taskDTO, User user) {
        Task foundTask = taskRepository.findById(taskDTO.getId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        foundTask.setUser(user);
        foundTask.setStatus(taskDTO.getStatus());
        foundTask.setStatusName(taskDTO.getStatusName());
        foundTask.setTitle(taskDTO.getTitle());
        taskRepository.save(foundTask);

        return TaskMapper.toDTO(foundTask);
    }

    @Override
    public void deleteTask(Long id) {
        Task foundTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        taskRepository.delete(foundTask);
    }
}
