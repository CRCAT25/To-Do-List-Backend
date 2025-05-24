package net.thanhdevjava.to_do_list.controller;

import net.thanhdevjava.to_do_list.dto.ResponseDTO;
import net.thanhdevjava.to_do_list.dto.TaskDTO;
import net.thanhdevjava.to_do_list.dto.UserDTO;
import net.thanhdevjava.to_do_list.entity.User;
import net.thanhdevjava.to_do_list.mapper.UserMapper;
import net.thanhdevjava.to_do_list.service.TaskService;
import net.thanhdevjava.to_do_list.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")  // Optional: Enable CORS
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    // READ (Get all tasks)
    @GetMapping
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getAllTasks() {
        try{
            return ResponseEntity
                    .ok(ResponseDTO.success("Fetched successfully", taskService.getTasks()));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }

    // Get tasks by id user
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<List<TaskDTO>>> getTaskByUserId(@PathVariable Long id) {
        try{
            return ResponseEntity
                    .ok(ResponseDTO.success("Fetched successfully", taskService.getTasksByUserId(id)));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }

    // Create task
    @PostMapping
    public ResponseEntity<ResponseDTO<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
        UserDTO userDTO = userService.getUserById(taskDTO.getUserId());
        User user = UserMapper.toEntity(userDTO);
        try{
            return ResponseEntity
                    .ok(ResponseDTO.success("Created successfully", taskService.createTask(taskDTO, user)));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }

    // Update task
    @PutMapping
    public ResponseEntity<ResponseDTO<TaskDTO>> updateTask(@RequestBody TaskDTO taskDTO) {
        UserDTO userDTO = userService.getUserById(taskDTO.getUserId());
        User user = UserMapper.toEntity(userDTO);

        try{
            TaskDTO task = taskService.getTaskById(taskDTO.getId());

            if (task == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseDTO.error("Task not found", "TASK_NOT_FOUND"));
            }

            return ResponseEntity
                    .ok(ResponseDTO.success("Updated successfully", taskService.updateTask(taskDTO, user)));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }

    // Delete task
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteTask(@PathVariable Long id) {
        try{
            TaskDTO task = taskService.getTaskById(id);
            if( task == null){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseDTO.error("Task not found", "TASK_NOT_FOUND"));
            }

            taskService.deleteTask(id);

            return ResponseEntity.ok(ResponseDTO.success("Deleted successfully", null));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }
}
