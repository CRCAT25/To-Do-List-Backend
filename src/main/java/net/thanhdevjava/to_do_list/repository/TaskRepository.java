package net.thanhdevjava.to_do_list.repository;

import net.thanhdevjava.to_do_list.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);

    Optional<Task> findById(Long id);
}
