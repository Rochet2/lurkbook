package projekti;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUsername(String username);
    List<Account> findByNicknameContainingIgnoreCase(String username, Pageable pageable);
    Account findByProfileurl(String profileurl);
}
