#Hibernate-Example

##Getting started

###Dependencies

To get started it is necessary to add MySQL and Hibernate dependencies [pom.xml](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/pom.xml#L20-L38).

```xml
     <!-- MySQL database driver -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.15</version>
        </dependency>

        <!--Hibernate dependency-->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>3.6.3.Final</version>
        </dependency>

        <dependency>
            <groupId>javassist</groupId>
            <artifactId>javassist</artifactId>
            <version>3.12.1.GA</version>
     </dependency>
```

Javassist (JAVA programming ASSISTant) makes Java bytecode manipulation simple. It is a class library for editing bytecodes in Java.
MySQL table scripts
___

###Database

####Creating
[creating.sql](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/resources/creating.sql).

```sql
CREATE DATABASE IF NOT EXISTS music DEFAULT CHARSET=utf8;
USE music;
CREATE TABLE IF NOT EXISTS `group`(`GROUP_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,`GROUP_NAME` VARCHAR(50) NOT NULL,PRIMARY KEY(`GROUP_ID`) USING BTREE) DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `genre`(`GENRE_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,`GENRE_NAME` VARCHAR(30) NOT NULL,PRIMARY KEY(`GENRE_ID`) USING BTREE) DEFAULT CHARSET=utf8;
CREATE TABLE IF NOT EXISTS `group_genre`(`GROUP_GENRE_ID` INT(10) UNSIGNED NOT NULL AUTO_INCREMENT, PRIMARY KEY(`GROUP_GENRE_ID`),`GROUP_ID` INT(10) UNSIGNED NOT NULL,`GENRE_ID` INT(10) UNSIGNED NOT NULL, CONSTRAINT `FK_GROUP_ID` FOREIGN KEY(`GROUP_ID`) REFERENCES `group`(`GROUP_ID`) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `FK_GENRE_ID` FOREIGN KEY(`GENRE_ID`) REFERENCES `genre`(`GENRE_ID`) ON DELETE CASCADE ON UPDATE CASCADE) DEFAULT CHARSET=utf8;
```

B-tree - is the index grouped by the leaves of a binary tree. Suitable for large indexes, in fact it is an index of indexes. For example, indices with the value of 1 to 10 are stored in one branch, between 11 and 20 in the other, etc., when a request comes from an index number 35, come to the third branch, and there find the 5th element.
```sql
CONSTRAINT `FK_GROUP_ID` FOREIGN KEY(`GROUP_ID`) REFERENCES `group`(`GROUP_ID`) ON DELETE CASCADE ON UPDATE CASCADE
```
Ensure the referential integrity of the data in one table to match values in another table.<br />
A FOREIGN KEY in one table points to a PRIMARY KEY in another table. The FOREIGN KEY constraint is used to prevent actions that would destroy links between tables.<br />
Cascade will work when you delete something on table Genre of Group. Any record on table Group_Genre that has reference to table Genre or Group will also be deleted.<br />

####Filling
[data.sql](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/resources/data.sql).

```sql
USE music;
insert into `group` (GROUP_NAME) values('Metallica'), ('Rihanna');
insert into `genre` (GENRE_NAME) values('rock'), ('pop'), ('metal');
insert into `group_genre` (GROUP_ID, GENRE_ID) values('1', '1'), ('1', '3'), ('2', '2');
```
___

###Hibernate model class

Define new annotation code inside [Genre.java](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/java/com/github/Karina_Denisevich/hibernate/example/group/Genre.java).
```java
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
    
    //getter, sette and constuctors
}
```
`uniqueConstraints = { @UniqueConstraint(columnNames = "GENRE_NAME")}` it means, filed GENRE_NAME must be unique.<br />
The `@Id` annotation indicating the member field below is the primary key of current entity.<br />
The `@GeneratedValue` annotation is used to specify how the primary key should be generated.<br />
`(strategy = IDENTITY)` indicates that the persistence provider must assign primary keys for the entity using a database identity column.<br /> 
`fetch = FetchType.LAZY` groups will be loaded only when they will be called.<br />
` mappedBy = "genres"` Genre is not an owner of the relationships.<br />


Adn define new annotation code inside [Group.java](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/java/com/github/Karina_Denisevich/hibernate/example/group/Group.java).
```java
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
    
    //getters, setters and constructors
}
```
The `inverseJoinColumns` attribute is responsible for the columns mapping of the inverse side.
___

###Hibernate configuration file
[hibernate.cfg.xml](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/resources/group/hibernate.cfg.xml).
```xml
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
    <session-factory>
        <property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
        <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/?</property>
        <property name="hibernate.connection.username">root</property>
        <property name="hibernate.connection.password">root</property>
        <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
        <property name="hbm2ddl.auto">create-drop</property>
        <property name="hibernate.hbm2ddl.import_files">creating.sql, data.sql</property>
        <mapping class="com.github.Karina_Denisevich.hibernate.example.group.Genre" />
        <mapping class="com.github.Karina_Denisevich.hibernate.example.group.Group" />
    </session-factory>
</hibernate-configuration>
```
___

###Run project
[App.java](https://github.com/Karina-Denisevich/Hibernate-Example/blob/master/src/main/java/com/github/Karina_Denisevich/hibernate/example/App.java).
```java
public class App {

    public static void main(String[] args) {

        System.out.println("Hibernate many to many (XML Mapping)");
        Session session = HibernateUtil.getSessionFactory().openSession();

        session.beginTransaction();

        List<Genre> list = session.createCriteria(Genre.class).list();

        System.out.println("First genre by id:");
        System.out.println(list.get(0).getGenreName());

        session.getTransaction().commit();

        HibernateUtil.shutdown();
    }
}
```
