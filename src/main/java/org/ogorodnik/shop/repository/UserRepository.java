package org.ogorodnik.shop.repository;

import org.ogorodnik.shop.entity.Credentials;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Credentials, Long> {

    public Optional<Credentials> findByLoginIgnoreCase(String login);
}
