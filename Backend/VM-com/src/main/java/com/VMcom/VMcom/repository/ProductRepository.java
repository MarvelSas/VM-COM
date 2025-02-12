package com.VMcom.VMcom.repository;

import com.VMcom.VMcom.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long >, PagingAndSortingRepository<Product, Long> {

    @Query(value = "select u from Product u where u.productCategory.id=?1")
    List<Product> findByCategory(Long categoryId);

    @Query(value = "select u from Product u where u.productCategory.id=?1 and u.price>?2 and u.price<?3 and u.amount>0 and LOWER(u.name) like CONCAT('%',LOWER(?4),'%') " )
    Page<Product> findByCategoryAndPriceBetweenMinAndMaxValueAndByNameAndIfItIsOnStock(Long categoryId, Double minPrice, Double maxPrice, String name, Pageable pageable);
    @Query(value = "select u from Product u where u.productCategory.id=?1 and u.price>?2 and u.price<?3 and LOWER(u.name) like CONCAT('%',LOWER(?4),'%')")
    Page<Product> findByCategoryAndPriceBetweenMinAndMaxValueAndByName(Long categoryId,Double minPrice,Double maxPrice,String name,Pageable pageable);

    @Query(value = "select u from Product u where u.price>?1 and u.price<?2 and u.amount>0 and LOWER(u.name) like CONCAT('%',LOWER(?3),'%')")
    Page<Product> findByPriceBetweenMinAndMaxValueAndByNameIfItIsOnStock(Double minPrice,Double maxPrice,String name,Pageable pageable);

    @Query(value = "select u from Product u where u.price>?1 and u.price<?2 and u.amount>0 and LOWER(u.name) like CONCAT('%',LOWER(?3),'%')")
    Page<Product> findByPriceBetweenMinAndMaxValueAndByName(Double minPrice,Double maxPrice,String name,Pageable pageable);


}
