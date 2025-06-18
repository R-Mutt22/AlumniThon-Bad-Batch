package com.bad.batch.Repository;

import com.bad.batch.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Puedes agregar métodos personalizados aquí si es necesario
}

