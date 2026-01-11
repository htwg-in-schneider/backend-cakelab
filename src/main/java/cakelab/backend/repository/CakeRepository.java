package cakelab.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cakelab.backend.model.Category;
import cakelab.backend.model.Cake;

@Repository
public interface CakeRepository extends JpaRepository<Cake, Long> {
    List<Cake> findByNameContainingIgnoreCase(String name);
    List<Cake> findByNameContainingIgnoreCaseAndCategory(String name, Category category);
    List<Cake> findByCategory(Category category);
   
}