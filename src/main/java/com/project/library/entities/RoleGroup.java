package com.project.library.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "role_groups")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID code;

    @Column(name = "role_group_name", nullable = false, length = 30)
    private String roleGroupName;

    @Column(name = "description", length = 50)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_groups_functions",
            joinColumns = @JoinColumn(name = "role_group_code"),
            inverseJoinColumns = @JoinColumn(name = "function_code")
    )
    @JsonManagedReference
    private List<Function> functions;

    @ManyToMany(mappedBy = "roleGroups")
    @JsonBackReference
    private List<User> users;
}
