package com.itanoji.time.cutter.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    @Column(unique=true)
    private String login;
    private String password;
    @Column(unique=true)
    private String email;
    @ElementCollection
    @CollectionTable(name = "user_files", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "file_name")
    private Collection<String> diagrams;

}
