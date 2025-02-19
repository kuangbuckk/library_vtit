package com.project.library.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "functions")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Function {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @Column(name = "function_name", nullable = false, length = 60, unique = true)
    private String functionName;

    @Column(name = "description", length = 120)
    private String description;

    @ManyToMany(mappedBy = "functions", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonBackReference //bị sở hữu
    private List<RoleGroup> roleGroups;
}
