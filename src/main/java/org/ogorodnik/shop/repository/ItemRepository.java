package org.ogorodnik.shop.repository;

import org.ogorodnik.shop.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    public Iterable<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String searchName, String SearchDescription);

}
