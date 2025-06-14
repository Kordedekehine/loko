package com.prcp.prcp.entity;

import com.prcp.prcp.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Approval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private FileRecord file;

    @Enumerated(EnumType.STRING)
    private Roles approverRoles;

    private boolean approved;
    private String comment;

    private String approver;
    private LocalDateTime approvedAt;
}
