package com.storeagent.repository;

import com.storeagent.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByBrandIgnoreCaseAndColorIgnoreCaseAndSize(String brand, String color, Integer size);

    List<Product> findTop5ByOrderByRatingDesc();

    List<Product> findTop5ByOrderByTrendingScoreDesc();

    List<Product> findByBrandIgnoreCase(String brand);

    List<Product> findByCategoryIgnoreCase(String category);

    @Query("SELECT DISTINCT p.brand FROM Product p ORDER BY p.brand")
    List<String> findDistinctBrands();

    @Query("SELECT DISTINCT p.color FROM Product p ORDER BY p.color")
    List<String> findDistinctColors();

    @Query("SELECT DISTINCT p.size FROM Product p ORDER BY p.size")
    List<Integer> findDistinctSizes();
}
