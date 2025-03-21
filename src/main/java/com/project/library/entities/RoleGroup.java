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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_group_name", nullable = false, length = 30)
    private String roleGroupName;

    @Column(name = "description", length = 50)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_groups_functions",
            joinColumns = @JoinColumn(name = "role_group_id"),
            inverseJoinColumns = @JoinColumn(name = "function_id")
    )
    private List<Function> functions;

    @ManyToMany(mappedBy = "roleGroups")
    private List<User> users;

    public static String ADMIN = "ADMIN";
    public static String MANAGER = "MANAGER";
    public static String LIBRARIAN = "LIBRARIAN";
    public static String USER = "USER";
}
