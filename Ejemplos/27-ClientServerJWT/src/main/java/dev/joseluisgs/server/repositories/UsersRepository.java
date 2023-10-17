package dev.joseluisgs.server.repositories;

import dev.joseluisgs.common.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.Optional;

public class UsersRepository {
    private static UsersRepository INSTANCE = null;
    private final List<User> users = List.of(
            new User(
                    1,
                    "pepe",
                    BCrypt.hashpw("pepe1234", BCrypt.gensalt(12)),
                    User.Role.ADMIN
            ),
            new User(
                    2,
                    "ana",
                    BCrypt.hashpw("ana1234", BCrypt.gensalt(12)),
                    User.Role.USER
            )
    );

    private UsersRepository() {
    }

    public synchronized static UsersRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepository();
        }
        return INSTANCE;
    }

    public Optional<User> findByByUsername(String username) {
        return users.stream()
                .filter(user -> user.username().equals(username))
                .findFirst();
    }

    public Optional<User> findByById(int id) {
        return users.stream()
                .filter(user -> user.id() == id)
                .findFirst();
    }
}
