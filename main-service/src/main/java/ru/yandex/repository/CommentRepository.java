package ru.yandex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.model.comment.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select c from Comment c where c.event.id = ?1")
    List<Comment> findByEventId(Integer id);

    @Query("select c from Comment c where c.event.id = ?1 order by c.author.name DESC")
    List<Comment> findByEventIdOrderByAuthorNameDesc(Integer id);

    @Query("select c from Comment c where c.event.id = ?1 order by c.createdAt DESC")
    List<Comment> findByEventIdOrderByCreatedAtDesc(Integer id);

    @Query("select c from Comment c where c.event.id = ?1 order by c.createdAt DESC, c.author.name DESC")
    List<Comment> findByEventIdOrderByCreatedAtDescAuthorNameDesc(Integer id);


}
