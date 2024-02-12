package kazurego7.junit5demo.domain.entity;

import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(schema = "demo", name = "company")
public class CompanyEntity {
    @Id
    @UuidGenerator
    private String companyId;
    private String companyName;
    private String address;
}
