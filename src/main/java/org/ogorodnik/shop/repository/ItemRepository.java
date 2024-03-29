package org.ogorodnik.shop.repository;

import org.ogorodnik.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Iterable<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchName, String SearchDescription);

}
