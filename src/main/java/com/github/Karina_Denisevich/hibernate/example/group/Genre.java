package com.github.Karina_Denisevich.hibernate.example.group;

import javax.persistence.*;
import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "genre", catalog = "music", uniqueConstraints = {
        @UniqueConstraint(columnNames = "GENRE_NAME")})
public class Genre implements Serializable {

    private static long SERIAL_ID = 1;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "GENRE_ID", unique = true, nullable = false)
    private Integer genreId;

    @Column(name = "GENRE_NAME", unique = true, nullable = false, length = 30)
    private String genreName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "genres")
    private Set<Group> groups = new HashSet<Group>(0);

    public Genre() {
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Genre(String genreName, Set<Group> genres) {
        this.genreName = genreName;
        this.genres = genres;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public Set<Group> getGenres() {
        return genres;
    }

    public void setGenres(Set<Group> genres) {
        this.genres = genres;
    }
}
