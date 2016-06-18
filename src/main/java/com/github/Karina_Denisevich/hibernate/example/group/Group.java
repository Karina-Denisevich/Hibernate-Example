package com.github.Karina_Denisevich.hibernate.example.group;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "group", catalog = "music")
public class Group implements java.io.Serializable {

    private static long SERIAL_ID = 1;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "GROUP_ID", unique = true, nullable = false)
    private Integer groupId;

    @Column(name = "GROUP_NAME", nullable = false, length = 50)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "group_genre", catalog = "music", joinColumns = {
            @JoinColumn(name = "GROUP_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "GENRE_ID",
                    nullable = false, updatable = false)})
    private Set<Genre> genres = new HashSet<Genre>(0);

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(String name, Set<Genre> genres) {
        this.name = name;
        this.genres = genres;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }
}
