package at.spengergasse.repository;

import at.spengergasse.domain.InfoRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoRequestRepository extends JpaRepository<InfoRequest, Long> {
}
