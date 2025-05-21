package net.thanhdevjava.to_do_list.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private Long status;

    private String statusName;

    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
