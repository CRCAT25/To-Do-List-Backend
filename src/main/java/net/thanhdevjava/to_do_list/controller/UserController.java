package net.thanhdevjava.to_do_list.controller;

import net.thanhdevjava.to_do_list.dto.ResponseDTO;
import net.thanhdevjava.to_do_list.dto.UserDTO;
import net.thanhdevjava.to_do_list.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")  // Optional: Enable CORS
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // READ (Get all users)
    @GetMapping
    public ResponseEntity<ResponseDTO<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getUsers();
            return ResponseEntity.ok(ResponseDTO.success("Fetched successfully", users));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }


    // READ (Get user by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = userService.getUserById(id);

            if (userDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseDTO.error("User not found", "USER_NOT_FOUND"));
            }

            return ResponseEntity.ok(ResponseDTO.success("Fetched successfully", userDTO));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }


    // UPDATE
    @PutMapping()
    public ResponseEntity<ResponseDTO<UserDTO>> updateUser(@RequestBody UserDTO userDTO) {
        try {
            if (userDTO.getId() == null) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(ResponseDTO.error("Invalid request", "INVALID_REQUEST"));
            }

            return ResponseEntity.ok(ResponseDTO.success("Updated successfully", userService.updateUser(userDTO)));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable Long id) {
        try{
            UserDTO userDTO = userService.getUserById(id);

            if (userDTO == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ResponseDTO.error("User not found", "USER_NOT_FOUND"));
            }

            return ResponseEntity.ok(ResponseDTO.success("Deleted successfully", null));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseDTO.error("Internal server error", "INTERNAL_ERROR"));
        }
    }
}
