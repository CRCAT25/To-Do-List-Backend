package net.thanhdevjava.to_do_list.controller;

import net.thanhdevjava.to_do_list.dto.TaskDTO;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")  // Optional: Enable CORS
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // READ (Get all tasks)
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getTasks());
    }

    // Get tasks by id user
    @GetMapping("/{id}")
    public ResponseEntity<List<TaskDTO>> getTaskByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTasksByUserId(id));
    }

    // Create task
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO, User user) {
        return ResponseEntity.ok(taskService.createTask(taskDTO, user));
    }

    // Update task
    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO, User user) {
        return ResponseEntity.ok(taskService.updateTask(taskDTO, user));
    }

    // Delete task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
