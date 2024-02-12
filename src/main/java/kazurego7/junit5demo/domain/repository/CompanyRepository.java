package kazurego7.junit5demo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import kazurego7.junit5demo.domain.entity.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
}
